package io.github.yanestyl.jgram.context;

import com.pengrad.telegrambot.model.User;
import io.github.yanestyl.jgram.fsm.Session;

public abstract class ContactContext extends BotContext {

    protected ContactContext(long chatId, User user, Session session) {
        super(chatId, user, session);
    }

    public abstract String phoneNumber();
    public abstract String firstName();
    public abstract String lastName();      // может быть null
    public abstract Long contactUserId();   // Telegram ID если есть
}
