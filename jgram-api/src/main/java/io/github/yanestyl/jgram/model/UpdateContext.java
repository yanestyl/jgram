package io.github.yanestyl.jgram.model;

/**
 * Transport-agnostic representation of an incoming update.
 * Abstracts away the underlying API library (e.g. pengrad).
 */
public class UpdateContext {

    public enum ChatType {PRIVATE, GROUP, SUPERGROUP, CHANNEL, UNKNOWN}

    public enum UpdateType {MESSAGE, CALLBACK_QUERY, PHOTO, LOCATION, UNKNOWN}

    private final long chatId;
    private final long userId;
    private final String firstName;
    private final String username;
    private final String text;
    private final String chatType;
    private final UpdateType updateType;

    private UpdateContext(Builder builder) {
        this.chatId = builder.chatId;
        this.userId = builder.userId;
        this.firstName = builder.firstName;
        this.username = builder.username;
        this.text = builder.text;
        this.chatType = builder.chatType;
        this.updateType = builder.updateType;
    }

    public long chatId() {
        return chatId;
    }

    public long userId() {
        return userId;
    }

    public String firstName() {
        return firstName;
    }

    public String username() {
        return username;
    }

    public String text() {
        return text;
    }

    public String chatType() {
        return chatType;
    }

    public UpdateType updateType() {
        return updateType;
    }

    // -----------------------------------------------------------
    // Builder
    // -----------------------------------------------------------

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long chatId;
        private long userId;
        private String firstName;
        private String username;
        private String text;
        private String chatType;
        private UpdateType updateType = UpdateType.UNKNOWN;

        public Builder chatId(long chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder userId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder chatType(String chatType) {
            this.chatType = chatType;
            return this;
        }

        public Builder updateType(UpdateType updateType) {
            this.updateType = updateType;
            return this;
        }

        public UpdateContext build() {
            return new UpdateContext(this);
        }
    }
}
