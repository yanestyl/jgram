package io.github.yanestyl.jgram.context.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Voice;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.context.VoiceContext;
import io.github.yanestyl.jgram.fsm.Session;

public class DefaultVoiceContext extends VoiceContext {

    private final Voice voice;
    private final TelegramBot bot;

    public DefaultVoiceContext(Message message, TelegramBot bot, Session session) {
        super(message.chat().id(), message.from(), session);
        this.voice = message.voice();
        this.bot = bot;
    }

    @Override public String fileId()   { return voice.fileId(); }
    @Override public int duration()    { return voice.duration(); }
    @Override public String mimeType() { return voice.mimeType() != null ? voice.mimeType() : ""; }
    @Override public long fileSize()   { return voice.fileSize(); }

    @Override
    public void reply(String text) {
        bot.execute(new SendMessage(chatId, text));
    }

    @Override
    public void replyMarkdown(String text) {
        bot.execute(new SendMessage(chatId, text).parseMode(ParseMode.Markdown));
    }
}
