package io.github.yanestyl.jgram.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.annotation.OnMessage;
import io.github.yanestyl.jgram.handler.HandlerMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private void handleMessage(Message message) throws Exception {
        String text = message.text();
        if (text == null) return;

        long chatId = message.chat().id();

        if (text.startsWith("/")) {
            String command = text.split(" ")[0]; // "/start args" -> "/start"
            HandlerMethod handler = registry.findCommandHandler(command);
            if (handler != null) {
                invokeAndReply(handler, chatId);
                return;
            }
        }

        List<HandlerMethod> handlers = registry.getMessageHandlers();
        for (HandlerMethod handler : handlers) {
            OnMessage annotation = handler.getMethod().getAnnotation(OnMessage.class);
            if (matches(annotation, text)) {
                invokeAndReply(handler, chatId);
                return;
            }
        }
    }

    private void handleCallback(CallbackQuery callback) throws Exception {
        String data = callback.data();
        long chatId = callback.maybeInaccessibleMessage().chat().id();
        HandlerMethod handler = registry.findCallbackHandler(data);
        if (handler != null) {
            invokeAndReply(handler, chatId);
        }
    }

    private void invokeAndReply(HandlerMethod handler, long chatId) throws Exception {
        Object result = handler.invoke();
        if (result instanceof String text) {
            bot.execute(new SendMessage(chatId, text));
        }
    }

    private boolean matches(OnMessage annotation, String text) {
        if (!annotation.contains().isEmpty() && !text.contains(annotation.contains())) return false;
        if (!annotation.startsWith().isEmpty() && !text.startsWith(annotation.startsWith())) return false;
        if (!annotation.regex().isEmpty() && !text.matches(annotation.regex())) return false;
        return true;
    }
}
