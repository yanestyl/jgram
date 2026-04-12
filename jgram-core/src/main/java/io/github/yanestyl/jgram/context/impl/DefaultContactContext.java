package io.github.yanestyl.jgram.context.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.context.ContactContext;
import io.github.yanestyl.jgram.fsm.Session;

public class DefaultContactContext extends ContactContext {

    private final Contact contact;
    private final TelegramBot bot;

    public DefaultContactContext(Message message, TelegramBot bot, Session session) {
        super(message.chat().id(), message.from(), session);
        this.contact = message.contact();
        this.bot = bot;
    }

    @Override public String phoneNumber()   { return contact.phoneNumber(); }
    @Override public String firstName()     { return contact.firstName(); }
    @Override public String lastName()      { return contact.lastName(); }
    @Override public Long contactUserId()   { return contact.userId() != null ? contact.userId().longValue() : null; }

    @Override
    public void reply(String text) {
        bot.execute(new SendMessage(chatId, text));
    }

    @Override
    public void replyMarkdown(String text) {
        bot.execute(new SendMessage(chatId, text).parseMode(ParseMode.Markdown));
    }
}
