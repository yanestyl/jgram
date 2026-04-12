package io.github.yanestyl.jgram.response;

import lombok.Getter;

/**
 * Represents a single keyboard button.
 */
@Getter
public class Button {

    public enum Type { CALLBACK, URL, REPLY }

    private final String text;
    private final String data; // callback data or url
    private final Type type;

    private Button(String text, String data, Type type) {
        this.text = text;
        this.data = data;
        this.type = type;
    }

    // Inline кнопки
    public static Button callback(String text, String callbackData) {
        return new Button(text, callbackData, Type.CALLBACK);
    }

    public static Button url(String text, String url) {
        return new Button(text, url, Type.URL);
    }

    // Reply кнопка
    public static Button reply(String text) {
        return new Button(text, null, Type.REPLY);
    }

}
