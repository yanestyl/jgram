package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;

public abstract class CallbackContext extends BotContext {

    protected CallbackContext(long chatId, User user) {
        super(chatId, user);
    }

    public abstract String data();
    public abstract void answer();
    public abstract void answer(String text);
    public abstract void answerAlert(String text);
}
