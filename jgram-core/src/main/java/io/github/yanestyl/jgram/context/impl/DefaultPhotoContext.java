package io.github.yanestyl.jgram.context.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.context.PhotoContext;

public class DefaultPhotoContext extends PhotoContext {

    private final Message message;
    private final TelegramBot bot;

    public DefaultPhotoContext(Message message, TelegramBot bot) {
        super(message.chat().id(), message.from());
        this.message = message;
        this.bot = bot;
    }

    @Override
    public String fileId() {
        PhotoSize[] photos = message.photo();
        // берем максимальное качество
        return photos[photos.length - 1].fileId();
    }

    @Override
    public String caption() {
        return message.caption() != null ? message.caption() : "";
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
