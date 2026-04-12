package io.github.yanestyl.jgram.context.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Audio;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.context.AudioContext;
import io.github.yanestyl.jgram.fsm.Session;

public class DefaultAudioContext extends AudioContext {

    private final Audio audio;
    private final Message message;
    private final TelegramBot bot;

    public DefaultAudioContext(Message message, TelegramBot bot, Session session) {
        super(message.chat().id(), message.from(), session);
        this.audio = message.audio();
        this.message = message;
        this.bot = bot;
    }

    @Override public String fileId()    { return audio.fileId(); }
    @Override public int duration()     { return audio.duration(); }
    @Override public String title()     { return audio.title() != null ? audio.title() : ""; }
    @Override public String performer() { return audio.performer() != null ? audio.performer() : ""; }
    @Override public String mimeType()  { return audio.mimeType() != null ? audio.mimeType() : ""; }
    @Override public long fileSize()    { return audio.fileSize(); }
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
