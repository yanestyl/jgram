package io.github.yanestyl.jgram.model;

import lombok.Builder;
import lombok.Getter;

/**
 * Transport-agnostic representation of an incoming update.
 * Abstracts away the underlying API library (e.g. pengrad).
 */
@Getter
@Builder
public class UpdateContext {

    public enum ChatType {
        PRIVATE, GROUP, SUPERGROUP, CHANNEL, DIRECT_MESSAGES, UNKNOWN
    }

    public enum UpdateType {
        MESSAGE, CALLBACK_QUERY, PHOTO, LOCATION,
        STICKER, VIDEO, DOCUMENT, AUDIO, VOICE,
        CONTACT, ANIMATION, UNKNOWN
    }

    private final long chatId;
    private final long userId;
    private final String firstName;
    private final String username;
    private final String text;
    private final ChatType chatType;
    private final UpdateType updateType;
}
