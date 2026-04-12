package io.github.yanestyl.jgram.core;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.message.MaybeInaccessibleMessage;
import io.github.yanestyl.jgram.model.UpdateContext;

public class UpdateContextMapper {

    public static UpdateContext map(Update update) {
        if (update.message() != null) {
            return fromMessage(update.message());
        } else if (update.callbackQuery() != null) {
            return fromCallback(update.callbackQuery());
        }
        return UpdateContext.builder()
                .updateType(UpdateContext.UpdateType.UNKNOWN)
                .build();
    }

    private static UpdateContext fromMessage(Message msg) {
        UpdateContext.UpdateType type = detectType(msg);

        return UpdateContext.builder()
                .chatId(msg.chat().id())
                .userId(msg.from().id())
                .firstName(msg.from().firstName())
                .username(msg.from().username())
                .text(msg.text())
                .chatType(parseChatType(msg.chat().type(), msg.chat()))
                .updateType(type)
                .build();
    }

    private static UpdateContext fromCallback(CallbackQuery cb) {
        MaybeInaccessibleMessage msg = cb.maybeInaccessibleMessage();
        return UpdateContext.builder()
                .chatId(msg.chat().id())
                .userId(cb.from().id())
                .firstName(cb.from().firstName())
                .username(cb.from().username())
                .text(cb.data())
                .chatType(parseChatType(msg.chat().type(), msg.chat()))
                .updateType(UpdateContext.UpdateType.CALLBACK_QUERY)
                .build();
    }

    private static UpdateContext.ChatType parseChatType(Chat.Type type, Chat chat) {
        if (type == null) return UpdateContext.ChatType.UNKNOWN;
        if (Boolean.TRUE.equals(chat.isDirectMessages())) {
            return UpdateContext.ChatType.DIRECT_MESSAGES;
        }
        return switch (type) {
            case Private    -> UpdateContext.ChatType.PRIVATE;
            case group      -> UpdateContext.ChatType.GROUP;
            case supergroup -> UpdateContext.ChatType.SUPERGROUP;
            case channel    -> UpdateContext.ChatType.CHANNEL;
        };
    }

    private static UpdateContext.UpdateType detectType(Message msg) {
        if (msg.animation() != null) return UpdateContext.UpdateType.ANIMATION;
        if (msg.photo() != null)     return UpdateContext.UpdateType.PHOTO;
        if (msg.sticker() != null)   return UpdateContext.UpdateType.STICKER;
        if (msg.video() != null)     return UpdateContext.UpdateType.VIDEO;
        if (msg.document() != null)  return UpdateContext.UpdateType.DOCUMENT;
        if (msg.audio() != null)     return UpdateContext.UpdateType.AUDIO;
        if (msg.voice() != null)     return UpdateContext.UpdateType.VOICE;
        if (msg.contact() != null)   return UpdateContext.UpdateType.CONTACT;
        if (msg.location() != null)  return UpdateContext.UpdateType.LOCATION;
        return UpdateContext.UpdateType.MESSAGE;
    }
}
