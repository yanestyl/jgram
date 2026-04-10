package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;

public abstract class MessageContext extends BotContext {

    protected MessageContext(long chatId, User user) {
        super(chatId, user);
    }

    public abstract String text();
    public abstract Message message();
}
