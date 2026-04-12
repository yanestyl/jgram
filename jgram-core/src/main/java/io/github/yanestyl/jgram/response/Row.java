package io.github.yanestyl.jgram.response;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a row of keyboard buttons.
 */
@Getter
public class Row {

    private final List<Button> buttons;

    private Row(List<Button> buttons) {
        this.buttons = buttons;
    }

    // Inline кнопки
    public static Row of(Button... buttons) {
        return new Row(Arrays.asList(buttons));
    }

    // Reply кнопки
    public static Row of(String... texts) {
        List<Button> buttons = Arrays.stream(texts)
                .map(Button::reply)
                .toList();
        return new Row(buttons);
    }

}
