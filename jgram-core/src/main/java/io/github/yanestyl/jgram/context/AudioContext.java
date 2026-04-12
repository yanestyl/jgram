package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;
import io.github.yanestyl.jgram.fsm.Session;

public abstract class AudioContext extends BotContext {

    protected AudioContext(long chatId, User user, Session session) {
        super(chatId, user, session);
    }

    public abstract String fileId();
    public abstract int duration();
    public abstract String title();         // название трека
    public abstract String performer();     // исполнитель
    public abstract String mimeType();
    public abstract long fileSize();
    public abstract String caption();
}
