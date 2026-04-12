package io.github.yanestyl.jgram.context.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.context.MessageContext;
import io.github.yanestyl.jgram.fsm.Session;

public class DefaultMessageContext extends MessageContext {

    private final Message message;
    private final TelegramBot bot;

    public DefaultMessageContext(Message message, TelegramBot bot, Session session) {
        super(message.chat().id(), message.from(), session);
        this.message = message;
        this.bot = bot;
    }

    @Override
    public String text() {
        return message.text();
    }

    @Override
    public Message message() {
        return message;
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
