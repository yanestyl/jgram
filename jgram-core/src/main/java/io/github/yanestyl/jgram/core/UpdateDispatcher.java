package io.github.yanestyl.jgram.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.annotation.content.OnMessage;
import io.github.yanestyl.jgram.config.model.JGramConfig;
import io.github.yanestyl.jgram.context.BotContext;
import io.github.yanestyl.jgram.context.CallbackContext;
import io.github.yanestyl.jgram.context.MessageContext;
import io.github.yanestyl.jgram.context.impl.*;
import io.github.yanestyl.jgram.fsm.Session;
import io.github.yanestyl.jgram.fsm.SessionManager;
import io.github.yanestyl.jgram.fsm.StateManager;
import io.github.yanestyl.jgram.handler.FilterResult;
import io.github.yanestyl.jgram.handler.HandlerMethod;
import io.github.yanestyl.jgram.model.UpdateContext;
import io.github.yanestyl.jgram.response.BotResponse;
import io.github.yanestyl.jgram.response.BotResponseSender;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class UpdateDispatcher {

    private final HandlerRegistry registry;
    private final TelegramBot bot;
    private final StateManager stateManager;
    private final SessionManager sessionManager;
    private final JGramConfig config;

    public UpdateDispatcher(HandlerRegistry registry, TelegramBot bot, JGramConfig config) {
        this.registry = registry;
        this.bot = bot;
        this.stateManager = new StateManager();
        this.sessionManager = new SessionManager();
        this.config = config;
    }

    public void dispatch(Update update) {
        try {
            UpdateContext updateCtx = UpdateContextMapper.map(update);

            if (config.getLogging().isHandlers()) {
                log.debug("Dispatching update type: {}", updateCtx.updateType());
            }

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
        long chatId = message.chat().id();
        Session session = sessionManager.getSession(userId);

        // анимация — проверяем до фото т.к. GIF может прийти как документ
        if (message.animation() != null) {
            handle(registry.findAnimationHandler(),
                    new DefaultAnimationContext(message, bot, session),
                    null, chatId, updateCtx, userId);
            return;
        }

        // фото
        if (message.photo() != null) {
            handle(registry.findPhotoHandler(),
                    new DefaultPhotoContext(message, bot, session),
                    null, chatId, updateCtx, userId);
            return;
        }

        // стикер
        if (message.sticker() != null) {
            handle(registry.findStickerHandler(),
                    new DefaultStickerContext(message, bot, session),
                    null, chatId, updateCtx, userId);
            return;
        }

        // видео
        if (message.video() != null) {
            handle(registry.findVideoHandler(),
                    new DefaultVideoContext(message, bot, session),
                    null, chatId, updateCtx, userId);
            return;
        }

        // документ
        if (message.document() != null) {
            handle(registry.findDocumentHandler(),
                    new DefaultDocumentContext(message, bot, session),
                    null, chatId, updateCtx, userId);
            return;
        }

        // аудио
        if (message.audio() != null) {
            handle(registry.findAudioHandler(),
                    new DefaultAudioContext(message, bot, session),
                    null, chatId, updateCtx, userId);
            return;
        }

        // голосовое
        if (message.voice() != null) {
            handle(registry.findVoiceHandler(),
                    new DefaultVoiceContext(message, bot, session),
                    null, chatId, updateCtx, userId);
            return;
        }

        // контакт
        if (message.contact() != null) {
            handle(registry.findContactHandler(),
                    new DefaultContactContext(message, bot, session),
                    null, chatId, updateCtx, userId);
            return;
        }

        // геолокация
        if (message.location() != null) {
            handle(registry.findLocationHandler(),
                    new DefaultLocationContext(message, bot, session),
                    null, chatId, updateCtx, userId);
            return;
        }

        // текст
        String text = message.text();
        if (text == null) return;

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
                handle(handler, ctx, text, chatId, updateCtx, userId);
                return;
            }
        }

        // @OnMessage с @ClearsState — проверяем ДО состояния
        for (HandlerMethod handler : registry.getMessageHandlers()) {
            if (handler.hasClearsState()) {
                OnMessage annotation = handler.getMethod().getAnnotation(OnMessage.class);
                if (matches(annotation, text)) {
                    stateManager.clearState(userId);
                    sessionManager.clearSession(userId);
                    handle(handler, ctx, text, chatId, updateCtx, userId);
                    return;
                }
            }
        }

        // пользователь в состоянии?
        if (stateManager.hasState(userId)) {
            String currentState = stateManager.getState(userId);
            HandlerMethod handler = registry.findStateHandler(currentState);
            if (handler != null) {
                handle(handler, ctx, text, chatId, updateCtx, userId);
                return;
            }
        }

        // обычные @OnMessage
        for (HandlerMethod handler : registry.getMessageHandlers()) {
            if (!handler.hasClearsState()) {
                OnMessage annotation = handler.getMethod().getAnnotation(OnMessage.class);
                if (matches(annotation, text)) {
                    handle(handler, ctx, text, chatId, updateCtx, userId);
                    return;
                }
            }
        }
    }

    // -----------------------------------------------------------
    // Callback routing
    // -----------------------------------------------------------

    private void handleCallback(CallbackQuery callback, UpdateContext updateCtx) throws Exception {
        String data = callback.data();
        long chatId = callback.maybeInaccessibleMessage().chat().id();
        long userId = callback.from().id();
        Session session = sessionManager.getSession(userId);
        CallbackContext ctx = new DefaultCallbackContext(callback, bot, session);

        HandlerMethod handler = registry.findCallbackHandler(data);
        if (handler != null) {
            handle(handler, ctx, data, chatId, updateCtx, userId);
        }
    }

    // -----------------------------------------------------------
    // Unified invocation
    // -----------------------------------------------------------

    private void handle(
            HandlerMethod handler,
            BotContext ctx,
            String text,
            long chatId,
            UpdateContext updateCtx,
            long userId) throws Exception {

        if (handler == null) return;

        // фильтры
        FilterResult result = handler.applyFilters(updateCtx);
        if (!result.isPassed()) {
            if (result.getFallback() != null) {
                bot.execute(new SendMessage(chatId, result.getFallback()));
            }
            return;
        }

        // вызов хендлера
        Method method = handler.getMethod();
        Object response;

        if (method.getParameterCount() == 0) {
            response = handler.invoke();
        } else {
            Class<?> paramType = method.getParameterTypes()[0];
            if (BotContext.class.isAssignableFrom(paramType)) {
                response = handler.invoke(ctx);
            } else if (paramType == String.class) {
                response = handler.invoke(text);
            } else {
                log.warn("Unsupported parameter type: {}", paramType.getName());
                response = handler.invoke();
            }
        }

        // отправляем ответ
        if (response instanceof String reply) {
            bot.execute(new SendMessage(chatId, reply));
        } else if (response instanceof BotResponse botResponse) {
            new BotResponseSender(bot).send(chatId, botResponse);
        }

        // FSM переходы
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
    // Helpers
    // -----------------------------------------------------------

    private boolean matches(OnMessage annotation, String text) {
        if (!annotation.contains().isEmpty() && !text.contains(annotation.contains())) return false;
        if (!annotation.startsWith().isEmpty() && !text.startsWith(annotation.startsWith())) return false;
        return annotation.regex().isEmpty() || text.matches(annotation.regex());
    }
}
