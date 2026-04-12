package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;
import io.github.yanestyl.jgram.fsm.Session;

public abstract class VideoContext extends BotContext {

    protected VideoContext(long chatId, User user, Session session) {
        super(chatId, user, session);
    }

    public abstract String fileId();
    public abstract int duration();         // длительность в секундах
    public abstract int width();
    public abstract int height();
    public abstract long fileSize();
    public abstract String caption();
}
