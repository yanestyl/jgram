package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;
import io.github.yanestyl.jgram.fsm.Session;

public abstract class PhotoContext extends BotContext {

    protected PhotoContext(long chatId, User user, Session session) {
        super(chatId, user, session);
    }

    public abstract String fileId();
    public abstract String caption();
}
