package io.github.yanestyl.example.keyboard;

import io.github.yanestyl.jgram.annotation.BotController;
import io.github.yanestyl.jgram.annotation.OnCommand;
import io.github.yanestyl.jgram.annotation.OnMessage;
import io.github.yanestyl.jgram.context.MessageContext;
import io.github.yanestyl.jgram.response.BotResponse;

@BotController
public class KeyboardBotController {

    // -----------------------------------------------------------
    // Старт - показываем reply клавиатуру
    // -----------------------------------------------------------

    @OnCommand("/start")
    public BotResponse start(MessageContext ctx) {
        return BotResponse.text("Привет");
    }

    // -----------------------------------------------------------
    // Reply кнопки
    // -----------------------------------------------------------

    @OnMessage(contains = "Каталог")
    public BotResponse catalog() {
        return BotResponse.text("Каталог");
    }

    @OnMessage(contains = "Избранное")
    public BotResponse favorites() {
        return BotResponse.text("Избранное");
    }

    @OnMessage(contains = "Корзина")
    public BotResponse cart() {
        return BotResponse.text("Корзина");
    }

    @OnMessage(contains = "Настройки")
    public BotResponse settings() {
        return BotResponse.text("Настройки");
    }

    @OnMessage(contains = "Помощь")
    public BotResponse help() {
        return BotResponse.text("Помощь");
    }
}
