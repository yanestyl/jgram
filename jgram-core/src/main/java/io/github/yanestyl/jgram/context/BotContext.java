package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;
import io.github.yanestyl.jgram.fsm.Session;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Base context available in all handlers.
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class BotContext {

    protected final long chatId;
    protected final User user;
    protected final Session session;

    public abstract void reply(String text);
    public abstract void replyMarkdown(String text);
}
