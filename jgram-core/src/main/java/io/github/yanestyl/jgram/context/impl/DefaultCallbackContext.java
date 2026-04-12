package io.github.yanestyl.jgram.context.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import io.github.yanestyl.jgram.context.CallbackContext;
import io.github.yanestyl.jgram.fsm.Session;

public class DefaultCallbackContext extends CallbackContext {

    private final CallbackQuery callbackQuery;
    private final TelegramBot bot;

    public DefaultCallbackContext(CallbackQuery callbackQuery, TelegramBot bot, Session session) {
        super(callbackQuery.maybeInaccessibleMessage().chat().id(), callbackQuery.from(), session);
        this.callbackQuery = callbackQuery;
        this.bot = bot;
    }

    @Override
    public String data() {
        return callbackQuery.data();
    }

    @Override
    public void answer() {
        bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
    }

    @Override
    public void answer(String text) {
        bot.execute(new AnswerCallbackQuery(callbackQuery.id()).text(text));
    }

    @Override
    public void answerAlert(String text) {
        bot.execute(new AnswerCallbackQuery(callbackQuery.id())
                .text(text).showAlert(true));
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
