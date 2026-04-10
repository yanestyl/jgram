package io.github.yanestyl.jgram.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.annotation.OnMessage;
import io.github.yanestyl.jgram.context.*;
import io.github.yanestyl.jgram.context.impl.DefaultCallbackContext;
import io.github.yanestyl.jgram.context.impl.DefaultLocationContext;
import io.github.yanestyl.jgram.context.impl.DefaultMessageContext;
import io.github.yanestyl.jgram.context.impl.DefaultPhotoContext;
import io.github.yanestyl.jgram.handler.HandlerMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

public class UpdateDispatcher {

    private static final Logger log = LoggerFactory.getLogger(UpdateDispatcher.class);

    private final HandlerRegistry registry;
    private final TelegramBot bot;

    public UpdateDispatcher(HandlerRegistry registry, TelegramBot bot) {
        this.registry = registry;
        this.bot = bot;
    }

    public void dispatch(Update update) {
        try {
            if (update.message() != null) {
                handleMessage(update.message());
            } else if (update.callbackQuery() != null) {
                handleCallback(update.callbackQuery());
            }
        } catch (Exception e) {
            log.error("Error dispatching update", e);
        }
    }

    // -----------------------------------------------------------
    // Message routing
    // -----------------------------------------------------------

    private void handleMessage(Message message) throws Exception {
        // фото
        if (message.photo() != null) {
            HandlerMethod handler = registry.findPhotoHandler();
            if (handler != null) {
                PhotoContext ctx = new DefaultPhotoContext(message, bot);
                invokeAndReply(handler, ctx, null, message.chat().id());
            }
            return;
        }

        // геолокация
        if (message.location() != null) {
            HandlerMethod handler = registry.findLocationHandler();
            if (handler != null) {
                LocationContext ctx = new DefaultLocationContext(message, bot);
                invokeAndReply(handler, ctx, null, message.chat().id());
            }
            return;
        }

        // текст
        String text = message.text();
        if (text == null) return;

        long chatId = message.chat().id();
        MessageContext ctx = new DefaultMessageContext(message, bot);

        // команда?
        if (text.startsWith("/")) {
            String command = text.split(" ")[0]; // "/start args" -> "/start"
            HandlerMethod handler = registry.findCommandHandler(command);
            if (handler != null) {
                invokeAndReply(handler, ctx, text, chatId);
                return;
            }
        }

        // текстовые хендлеры с фильтрами
        List<HandlerMethod> handlers = registry.getMessageHandlers();
        for (HandlerMethod handler : handlers) {
            OnMessage annotation = handler.getMethod().getAnnotation(OnMessage.class);
            if (matches(annotation, text)) {
                invokeAndReply(handler, ctx, text, chatId);
                return;
            }
        }
    }

    // -----------------------------------------------------------
    // Callback routing
    // -----------------------------------------------------------

    private void handleCallback(CallbackQuery callback) throws Exception {
        String data = callback.data();
        long chatId = callback.maybeInaccessibleMessage().chat().id();
        HandlerMethod handler = registry.findCallbackHandler(data);
        if (handler != null) {
            CallbackContext ctx = new DefaultCallbackContext(callback, bot);
            invokeAndReply(handler, ctx, data, chatId);
        }
    }

    // -----------------------------------------------------------
    // Invocation - определяем что ждет метод и передаем нужное
    // -----------------------------------------------------------

    private void invokeAndReply(
            HandlerMethod handler,
            BotContext ctx,
            String text,
            long chatId) throws Exception {

        Method method = handler.getMethod();
        Object result;

        if (method.getParameterCount() == 0) {
            // @OnCommand("/start")
            // public String start() { }
            result = handler.invoke();
        } else {
            Class<?> paramType = method.getParameterTypes()[0];

            if (BotContext.class.isAssignableFrom(paramType)) {
                // @OnCommand("/start")
                // public String start(MessageContext ctx) { }
                result = handler.invoke(ctx);
            } else if (paramType == String.class) {
                // @OnMessage
                // public String echo(String text { }
                result = handler.invoke(text);
            } else {
                log.warn("Unsupported parameter type: {}", paramType.getName());
                result = handler.invoke();
            }
        }

        // если метод вернул строку - отправляем как ответ
        if (result instanceof String reply) {
            bot.execute(new SendMessage(chatId, reply));
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
