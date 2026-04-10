package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;

public abstract class LocationContext extends BotContext {

    protected LocationContext(long chatId, User user) {
        super(chatId, user);
    }

    public abstract float latitude();
    public abstract float longitude();
}
