package io.github.yanestyl.jgram.response;

import java.util.Arrays;
import java.util.List;

/**
 * Universal response object returned from handler methods.
 *
 * <p>
 * Examples:
 * <pre>
 *     return BotResponse.text("Hello!")
 *          .inlineKeyboard(
 *              Row.of(Button.callback("Click me", "click"))
 *          );
 *
 *      return BotResponse.text("Choose:")
 *          .replyKeyboard(
 *              Row.of("Option 1", "Option 2")
 *          ).resizable();
 * </pre>
 */
public class BotResponse {

    public enum KeyboardType { INLINE, REPLY, REMOVE, NONE }

    private final String text;
    private List<Row> rows;
    private KeyboardType keyboardType = KeyboardType.NONE;
    private boolean resizable = false;
    private boolean oneTime = false;

    private BotResponse(String text) {
        this.text = text;
    }

    // -----------------------------------------------------------
    // Factory
    // -----------------------------------------------------------

    public static BotResponse text(String text) {
        return new BotResponse(text);
    }

    // -----------------------------------------------------------
    // Keyboards
    // -----------------------------------------------------------

    public BotResponse inlineKeyboard(Row... rows) {
        this.rows = Arrays.asList(rows);
        this.keyboardType = KeyboardType.INLINE;
        return this;
    }

    public BotResponse replyKeyboard(Row... rows) {
        this.rows = Arrays.asList(rows);
        this.keyboardType = KeyboardType.REPLY;
        return this;
    }

    public BotResponse removeKeyboard() {
        this.keyboardType = KeyboardType.REMOVE;
        return this;
    }

    // -----------------------------------------------------------
    // Reply keyboard options
    // -----------------------------------------------------------

    /** Makes reply keyboard resize to fit buttons. */
    public BotResponse resizable() {
        this.resizable = true;
        return this;
    }

    /** Hides reply keyboard after one press. */
    public BotResponse oneTime() {
        this.oneTime = true;
        return this;
    }

    // -----------------------------------------------------------
    // Getters
    // -----------------------------------------------------------

    public String getText() {
        return text;
    }

    public List<Row> getRows() {
        return rows;
    }

    public KeyboardType getKeyboardType() {
        return keyboardType;
    }

    public boolean isResizable() {
        return resizable;
    }

    public boolean isOneTime() {
        return oneTime;
    }
}
