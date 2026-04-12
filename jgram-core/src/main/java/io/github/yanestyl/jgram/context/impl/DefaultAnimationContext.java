package io.github.yanestyl.jgram.context.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Animation;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.context.AnimationContext;
import io.github.yanestyl.jgram.fsm.Session;

public class DefaultAnimationContext extends AnimationContext {

    private final Animation animation;
    private final Message message;
    private final TelegramBot bot;

    public DefaultAnimationContext(Message message, TelegramBot bot, Session session) {
        super(message.chat().id(), message.from(), session);
        this.animation = message.animation();
        this.message = message;
        this.bot = bot;
    }

    @Override public String fileId()   { return animation.fileId(); }
    @Override public int duration()    { return animation.duration(); }
    @Override public int width()       { return animation.width(); }
    @Override public int height()      { return animation.height(); }
    @Override public String fileName() { return animation.fileName() != null ? animation.fileName() : ""; }
    @Override public String mimeType() { return animation.mimeType() != null ? animation.mimeType() : ""; }
    @Override public long fileSize()   { return animation.fileSize(); }
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
