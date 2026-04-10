package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;

public abstract class PhotoContext extends BotContext {

    protected PhotoContext(long chatId, User user) {
        super(chatId, user);
    }

    public abstract String fileId();
    public abstract String caption();
}
