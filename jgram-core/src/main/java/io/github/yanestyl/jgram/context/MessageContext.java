package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import io.github.yanestyl.jgram.fsm.Session;

public abstract class MessageContext extends BotContext {

    protected MessageContext(long chatId, User user, Session session) {
        super(chatId, user, session);
    }

    public abstract String text();
    public abstract Message message();
}
