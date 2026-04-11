package io.github.yanestyl.jgram.core;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import io.github.yanestyl.jgram.model.UpdateContext;

public class UpdateContextMapper {

    public static UpdateContext map(Update update) {
        UpdateContext.Builder builder = UpdateContext.builder();

        if (update.message() != null) {
            Message msg = update.message();
            builder.chatId(msg.chat().id())
                    .chatType(msg.chat().type().name())
                    .userId(msg.from().id())
                    .firstName(msg.from().firstName())
                    .username(msg.from().username());

            if (msg.photo() != null) {
                builder.updateType(UpdateContext.UpdateType.PHOTO);
            } else if (msg.location() != null) {
                builder.updateType(UpdateContext.UpdateType.LOCATION);
            } else {
                builder.updateType(UpdateContext.UpdateType.MESSAGE)
                        .text(msg.text());
            }
        } else if (update.callbackQuery() != null) {
            CallbackQuery cb = update.callbackQuery();
            builder.chatId(cb.maybeInaccessibleMessage().chat().id())
                    .chatType(cb.maybeInaccessibleMessage().chat().type().name())
                    .userId(cb.from().id())
                    .username(cb.from().username())
                    .text(cb.data())
                    .updateType(UpdateContext.UpdateType.CALLBACK_QUERY);
        }

        return builder.build();
    }
}
