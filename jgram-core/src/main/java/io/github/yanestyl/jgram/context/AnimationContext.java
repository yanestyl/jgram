package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;
import io.github.yanestyl.jgram.fsm.Session;

public abstract class AnimationContext extends BotContext {

    protected AnimationContext(long chatId, User user, Session session) {
        super(chatId, user, session);
    }

    public abstract String fileId();
    public abstract int duration();
    public abstract int width();
    public abstract int height();
    public abstract String fileName();
    public abstract String mimeType();
    public abstract long fileSize();
    public abstract String caption();
}
