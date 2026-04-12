package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;
import io.github.yanestyl.jgram.fsm.Session;

public abstract class LocationContext extends BotContext {

    protected LocationContext(long chatId, User user, Session session) {
        super(chatId, user, session);
    }

    public abstract float latitude();
    public abstract float longitude();
}
