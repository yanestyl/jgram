package io.github.yanestyl.jgram.context.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Sticker;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.context.StickerContext;
import io.github.yanestyl.jgram.fsm.Session;

public class DefaultStickerContext extends StickerContext {

    private final Sticker sticker;
    private final TelegramBot bot;

    public DefaultStickerContext(Message message, TelegramBot bot, Session session) {
        super(message.chat().id(), message.from(), session);
        this.sticker = message.sticker();
        this.bot = bot;
    }

    @Override public String fileId()      { return sticker.fileId(); }
    @Override public String emoji()       { return sticker.emoji(); }
    @Override public String setName()     { return sticker.setName(); }
    @Override public boolean isAnimated() { return Boolean.TRUE.equals(sticker.isAnimated()); }
    @Override public boolean isVideo()    { return Boolean.TRUE.equals(sticker.isVideo()); }

    @Override
    public void reply(String text) {
        bot.execute(new SendMessage(chatId, text));
    }

    @Override
    public void replyMarkdown(String text) {
        bot.execute(new SendMessage(chatId, text).parseMode(ParseMode.Markdown));
    }
}
