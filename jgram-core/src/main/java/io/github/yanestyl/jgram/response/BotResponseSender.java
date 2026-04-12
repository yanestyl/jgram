package io.github.yanestyl.jgram.response;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.List;

public class BotResponseSender {

    private final TelegramBot bot;

    public BotResponseSender(TelegramBot bot) {
        this.bot = bot;
    }

    public void send(long chatId, BotResponse response) {
        SendMessage request = new SendMessage(chatId, response.getText());

        switch (response.getKeyboardType()) {
            case INLINE -> request.replyMarkup(buildInline(response.getRows()));
            case REPLY -> request.replyMarkup(buildReply(response));
            case REMOVE -> request.replyMarkup(new ReplyKeyboardRemove());
            case NONE -> {
            }
        }

        bot.execute(request);
    }

    // -----------------------------------------------------------
    // Builders
    // -----------------------------------------------------------

    private InlineKeyboardMarkup buildInline(List<Row> rows) {
        InlineKeyboardButton[][] keyboard = rows.stream()
                .map(row -> row.getButtons().stream()
                        .map(this::toInlineButton)
                        .toArray(InlineKeyboardButton[]::new))
                .toArray(InlineKeyboardButton[][]::new);

        return new InlineKeyboardMarkup(keyboard);
    }

    private InlineKeyboardButton toInlineButton(Button button) {
        return switch (button.getType()) {
            case CALLBACK -> new InlineKeyboardButton(button.getText())
                    .callbackData(button.getData());
            case URL -> new InlineKeyboardButton(button.getText())
                    .url(button.getData());
            default -> throw new IllegalArgumentException(
                    "Unsupported button type for inline keyboard: " + button.getType());
        };
    }

    private ReplyKeyboardMarkup buildReply(BotResponse response) {
        String[][] keyboard = response.getRows().stream()
                .map(row -> row.getButtons().stream()
                        .map(Button::getText)
                        .toArray(String[]::new))
                .toArray(String[][]::new);

        return new ReplyKeyboardMarkup(keyboard)
                .resizeKeyboard(response.isResizable())
                .oneTimeKeyboard(response.isOneTime());
    }
}
