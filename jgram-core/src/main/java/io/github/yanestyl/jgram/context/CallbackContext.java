package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;
import io.github.yanestyl.jgram.fsm.Session;

public abstract class CallbackContext extends BotContext {

    protected CallbackContext(long chatId, User user, Session session) {
        super(chatId, user, session);
    }

    public abstract String data();
    public abstract void answer();
    public abstract void answer(String text);
    public abstract void answerAlert(String text);
}
