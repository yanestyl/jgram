package io.github.yanestyl.jgram.context.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.context.DocumentContext;
import io.github.yanestyl.jgram.fsm.Session;

public class DefaultDocumentContext extends DocumentContext {

    private final Document document;
    private final Message message;
    private final TelegramBot bot;

    public DefaultDocumentContext(Message message, TelegramBot bot, Session session) {
        super(message.chat().id(), message.from(), session);
        this.document = message.document();
        this.message = message;
        this.bot = bot;
    }

    @Override public String fileId()    { return document.fileId(); }
    @Override public String fileName()  { return document.fileName() != null ? document.fileName() : ""; }
    @Override public String mimeType()  { return document.mimeType() != null ? document.mimeType() : ""; }
    @Override public long fileSize()    { return document.fileSize(); }
    @Override public String caption()   { return message.caption() != null ? message.caption() : ""; }

    @Override
    public void reply(String text) {
        bot.execute(new SendMessage(chatId, text));
    }

    @Override
    public void replyMarkdown(String text) {
        bot.execute(new SendMessage(chatId, text).parseMode(ParseMode.Markdown));
    }
}
