package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;

/**
 * Base context available in all handlers.
 */
public abstract class BotContext {

    protected final long chatId;
    protected final User user;

    protected BotContext(long chatId, User user) {
        this.chatId = chatId;
        this.user = user;
    }

    public long chatId() {
        return chatId;
    }

    public User user() {
        return user;
    }

    public abstract void reply(String text);
    public abstract void replyMarkdown(String text);
}
