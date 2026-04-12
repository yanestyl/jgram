package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;
import io.github.yanestyl.jgram.fsm.Session;

/**
 * Base context available in all handlers.
 */
public abstract class BotContext {

    protected final long chatId;
    protected final User user;
    protected final Session session;

    protected BotContext(long chatId, User user, Session session) {
        this.chatId = chatId;
        this.user = user;
        this.session = session;
    }

    public long chatId() {
        return chatId;
    }

    public User user() {
        return user;
    }

    public Session session() {
        return session;
    }

    public abstract void reply(String text);
    public abstract void replyMarkdown(String text);
}
