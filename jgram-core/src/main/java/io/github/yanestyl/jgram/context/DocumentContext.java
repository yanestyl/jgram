package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;
import io.github.yanestyl.jgram.fsm.Session;

public abstract class DocumentContext extends BotContext {

    protected DocumentContext(long chatId, User user, Session session) {
        super(chatId, user, session);
    }

    public abstract String fileId();
    public abstract String fileName();      // оригинальное имя файла
    public abstract String mimeType();      // тип файла
    public abstract long fileSize();
    public abstract String caption();
}
