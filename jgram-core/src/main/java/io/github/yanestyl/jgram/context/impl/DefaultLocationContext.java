package io.github.yanestyl.jgram.context.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.context.LocationContext;
import io.github.yanestyl.jgram.fsm.Session;

public class DefaultLocationContext extends LocationContext {
    private final Message message;
    private final TelegramBot bot;

    public DefaultLocationContext(Message message, TelegramBot bot, Session session) {
        super(message.chat().id(), message.from(), session);
        this.message = message;
        this.bot = bot;
    }

    @Override
    public float latitude() {
        return message.location().latitude();
    }

    @Override
    public float longitude() {
        return message.location().longitude();
    }

    @Override
    public void reply(String text) {
        bot.execute(new SendMessage(chatId, text));
    }

    @Override
    public void replyMarkdown(String text) {
        bot.execute(new SendMessage(chatId, text).parseMode(ParseMode.Markdown));
    }
}
