package io.github.yanestyl.jgram.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.annotation.handler.OnMessage;
import io.github.yanestyl.jgram.context.BotContext;
import io.github.yanestyl.jgram.context.CallbackContext;
import io.github.yanestyl.jgram.context.LocationContext;
import io.github.yanestyl.jgram.context.MessageContext;
import io.github.yanestyl.jgram.context.PhotoContext;
import io.github.yanestyl.jgram.context.impl.DefaultCallbackContext;
import io.github.yanestyl.jgram.context.impl.DefaultLocationContext;
import io.github.yanestyl.jgram.context.impl.DefaultMessageContext;
import io.github.yanestyl.jgram.context.impl.DefaultPhotoContext;
import io.github.yanestyl.jgram.fsm.Session;
import io.github.yanestyl.jgram.fsm.SessionManager;
import io.github.yanestyl.jgram.fsm.StateManager;
import io.github.yanestyl.jgram.handler.FilterResult;
import io.github.yanestyl.jgram.handler.HandlerMethod;
import io.github.yanestyl.jgram.model.UpdateContext;
import io.github.yanestyl.jgram.response.BotResponse;
import io.github.yanestyl.jgram.response.BotResponseSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class UpdateDispatcher {

    private static final Logger log = LoggerFactory.getLogger(UpdateDispatcher.class);

    private final HandlerRegistry registry;
    private final TelegramBot bot;
    private final StateManager stateManager;
    private final SessionManager sessionManager;

    public UpdateDispatcher(HandlerRegistry registry, TelegramBot bot) {
        this.registry = registry;
        this.bot = bot;
        this.stateManager   = new StateManager();
        this.sessionManager = new SessionManager();
    }

    public void dispatch(Update update) {
        try {
            UpdateContext updateCtx = UpdateContextMapper.map(update);

            if (update.message() != null) {
                handleMessage(update.message(), updateCtx);
            } else if (update.callbackQuery() != null) {
                handleCallback(update.callbackQuery(), updateCtx);
            }
        } catch (Exception e) {
            log.error("Error dispatching update", e);
        }
    }

    // -----------------------------------------------------------
    // Message routing
    // -----------------------------------------------------------

    private void handleMessage(Message message, UpdateContext updateCtx) throws Exception {
        long userId = message.from().id();
        Session session = sessionManager.getSession(userId);

        // фото
        if (message.photo() != null) {
            HandlerMethod handler = registry.findPhotoHandler();
            if (handler != null) {
                PhotoContext ctx = new DefaultPhotoContext(message, bot, session);
                invokeAndReply(handler, ctx, null, message.chat().id(), updateCtx, userId);
            }
            return;
        }

        // геолокация
        if (message.location() != null) {
            HandlerMethod handler = registry.findLocationHandler();
            if (handler != null) {
                LocationContext ctx = new DefaultLocationContext(message, bot, session);
                invokeAndReply(handler, ctx, null, message.chat().id(), updateCtx, userId);
            }
            return;
        }

        // текст
        String text = message.text();
        if (text == null) return;

        long chatId = message.chat().id();

        MessageContext ctx = new DefaultMessageContext(message, bot, session);

        // команда?
        if (text.startsWith("/")) {
            String command = text.split(" ")[0];
            HandlerMethod handler = registry.findCommandHandler(command);
            if (handler != null) {
                if (handler.hasClearsState()) {
                    stateManager.clearState(userId);
                    sessionManager.clearSession(userId);
                }
                invokeAndReply(handler, ctx, text, chatId, updateCtx, userId);
                return;
            }
        }

        if (stateManager.hasState(userId)) {
            String currentState = stateManager.getState(userId);
            HandlerMethod handler = registry.findStateHandler(currentState);
            if (handler != null) {
                invokeAndReply(handler, ctx, text, chatId, updateCtx, userId);
                return;
            }
        }

        for (HandlerMethod handler : registry.getMessageHandlers()) {
            OnMessage annotation = handler.getMethod().getAnnotation(OnMessage.class);
            if (matches(annotation, text)) {
                invokeAndReply(handler, ctx, text, chatId, updateCtx, userId);
                return;
            }
        }
    }

    // -----------------------------------------------------------
    // Callback routing
    // -----------------------------------------------------------

    private void handleCallback(CallbackQuery callback, UpdateContext updateCtx) throws Exception {
        long userId = callback.from().id();
        Session session = sessionManager.getSession(userId);

        String data = callback.data();
        long chatId = callback.maybeInaccessibleMessage().chat().id();
        HandlerMethod handler = registry.findCallbackHandler(data);
        if (handler != null) {
            CallbackContext ctx = new DefaultCallbackContext(callback, bot, session);
            invokeAndReply(handler, ctx, data, chatId, updateCtx, userId);
        }
    }

    // -----------------------------------------------------------
    // Invocation - определяем что ждет метод и передаем нужное
    // -----------------------------------------------------------

    private void invokeAndReply(
            HandlerMethod handler,
            BotContext ctx,
            String text,
            long chatId,
            UpdateContext updateCtx,
            long userId) throws Exception {

        FilterResult result = handler.applyFilters(updateCtx);
        if (!result.isPassed()) {
            if (result.getFallback() != null) {
                bot.execute(new SendMessage(chatId, result.getFallback()));
            }
            return;
        }

        Method method = handler.getMethod();
        Object response;

        if (method.getParameterCount() == 0) {
            // @OnCommand("/start")
            // public String start() { }
            response = handler.invoke();
        } else {
            Class<?> paramType = method.getParameterTypes()[0];

            if (BotContext.class.isAssignableFrom(paramType)) {
                // @OnCommand("/start")
                // public String start(MessageContext ctx) { }
                response = handler.invoke(ctx);
            } else if (paramType == String.class) {
                // @OnMessage
                // public String echo(String text { }
                response = handler.invoke(text);
            } else {
                log.warn("Unsupported parameter type: {}", paramType.getName());
                response = handler.invoke();
            }
        }

        // если метод вернул строку - отправляем как ответ
        if (response instanceof String reply) {
            bot.execute(new SendMessage(chatId, reply));
        } else if (response instanceof BotResponse botResponse) {
            new BotResponseSender(bot).send(chatId, botResponse);
        }

        // FSM переходы - после вызова хендлера
        if (handler.hasExitState()) {
            stateManager.clearState(userId);
            sessionManager.clearSession(userId);
        } else if (handler.hasNextState()) {
            stateManager.setState(userId, handler.getNextState());
        } else if (handler.hasEnterState()) {
            stateManager.setState(userId, handler.getEnterState());
        }
    }

    // -----------------------------------------------------------
    // Filter matching
    // -----------------------------------------------------------

    private boolean matches(OnMessage annotation, String text) {
        if (!annotation.contains().isEmpty() && !text.contains(annotation.contains())) return false;
        if (!annotation.startsWith().isEmpty() && !text.startsWith(annotation.startsWith())) return false;
        if (!annotation.regex().isEmpty() && !text.matches(annotation.regex())) return false;
        return true;
    }
}
