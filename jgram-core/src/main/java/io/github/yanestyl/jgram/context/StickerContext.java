package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;
import io.github.yanestyl.jgram.fsm.Session;

public abstract class StickerContext extends BotContext {

    protected StickerContext(long chatId, User user, Session session) {
        super(chatId, user, session);
    }

    public abstract String fileId();
    public abstract String emoji();          // эмодзи стикера
    public abstract String setName();        // название набора
    public abstract boolean isAnimated();    // анимированный?
    public abstract boolean isVideo();       // видео-стикер?
}
