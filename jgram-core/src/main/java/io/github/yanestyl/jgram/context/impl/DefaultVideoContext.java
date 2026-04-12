package io.github.yanestyl.jgram.context.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Video;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.context.VideoContext;
import io.github.yanestyl.jgram.fsm.Session;

public class DefaultVideoContext extends VideoContext {

    private final Video video;
    private final Message message;
    private final TelegramBot bot;

    public DefaultVideoContext(Message message, TelegramBot bot, Session session) {
        super(message.chat().id(), message.from(), session);
        this.video = message.video();
        this.message = message;
        this.bot = bot;
    }

    @Override public String fileId()   { return video.fileId(); }
    @Override public int duration()    { return video.duration(); }
    @Override public int width()       { return video.width(); }
    @Override public int height()      { return video.height(); }
    @Override public long fileSize()   { return video.fileSize(); }
    @Override public String caption()  { return message.caption() != null ? message.caption() : ""; }

    @Override
    public void reply(String text) {
        bot.execute(new SendMessage(chatId, text));
    }

    @Override
    public void replyMarkdown(String text) {
        bot.execute(new SendMessage(chatId, text).parseMode(ParseMode.Markdown));
    }
}
