package io.github.yanestyl.example.keyboard;

import io.github.yanestyl.jgram.annotation.BotController;
import io.github.yanestyl.jgram.annotation.content.OnCallbackQuery;
import io.github.yanestyl.jgram.annotation.content.OnCommand;
import io.github.yanestyl.jgram.annotation.content.OnMessage;
import io.github.yanestyl.jgram.context.CallbackContext;
import io.github.yanestyl.jgram.context.MessageContext;
import io.github.yanestyl.jgram.response.BotResponse;
import io.github.yanestyl.jgram.response.Button;
import io.github.yanestyl.jgram.response.Row;

@BotController
public class KeyboardBotController {

    // -----------------------------------------------------------
    // Старт - показываем reply клавиатуру
    // -----------------------------------------------------------

    @OnCommand("/start")
    public BotResponse start(MessageContext ctx) {
        return BotResponse.text("Привет, " + ctx.user().firstName() + "! \uD83D\uDC4B\n" +
                        "Выбери раздел:")
                .replyKeyboard(
                        Row.of("📦 Каталог", "⭐ Избранное"),
                        Row.of("🛒 Корзина", "⚙️ Настройки"),
                        Row.of("ℹ️ Помощь")
                ).resizable();
    }

    // -----------------------------------------------------------
    // Reply кнопки
    // -----------------------------------------------------------

    @OnMessage(contains = "📦 Каталог")
    public BotResponse catalog() {
        return BotResponse.text("\uD83D\uDCE6 Наш каталог:")
                .inlineKeyboard(
                        Row.of(
                                Button.callback("👟 Обувь", "cat_shoes"),
                                Button.callback("👕 Одежда", "cat_clothes")
                        ),
                        Row.of(
                                Button.callback("💻 Электроника", "cat_electronics")
                        ),
                        Row.of(
                                Button.url("🌐 Весь каталог на сайте", "https://example.com")
                        )
                );
    }

    @OnMessage(contains = "⭐ Избранное")
    public BotResponse favorites() {
        return BotResponse.text("⭐ Твоё избранное пусто!\n" +
                "Добавляй товары из каталога.");
    }

    @OnMessage(contains = "🛒 Корзина")
    public BotResponse cart() {
        return BotResponse.text("🛒 Твоя корзина пуста!")
                .inlineKeyboard(
                        Row.of(Button.callback("📦 Перейти в каталог", "go_catalog"))
                );
    }

    @OnMessage(contains = "⚙️ Настройки")
    public BotResponse settings() {
        return BotResponse.text("Настройки")
                .inlineKeyboard(
                        Row.of(Button.callback("🌍 Язык", "lang"),
                                Button.callback("🔔 Уведомления", "notifications")),
                        Row.of(Button.callback("🗑 Удалить аккаунт", "delete_account"))
                );
    }

    @OnMessage(contains = "ℹ️ Помощь")
    public BotResponse help() {
        return BotResponse.text("ℹ️ Помощь:\n\n" +
                "/start - главное меню\n" +
                "/cancel - убрать клавиатуру");
    }

    // -----------------------------------------------------------
    // Inline callbacks
    // -----------------------------------------------------------

    @OnCallbackQuery("cat_shoes")
    public void shoes(CallbackContext ctx) {
        ctx.answer();
        ctx.reply("👟 Раздел «Обувь» — скоро здесь будут товары!");
    }

    @OnCallbackQuery("cat_clothes")
    public void clothes(CallbackContext ctx) {
        ctx.answer();
        ctx.reply("👕 Раздел «Одежда» — скоро здесь будут товары!");
    }

    @OnCallbackQuery("cat_electronics")
    public void electronics(CallbackContext ctx) {
        ctx.answer();
        ctx.reply("💻 Раздел «Электроника» — скоро здесь будут товары!");
    }

    @OnCallbackQuery("go_catalog")
    public void goCatalog(CallbackContext ctx) {
        ctx.answer();
        ctx.reply("Переходим в каталог...");
    }

    @OnCallbackQuery("lang")
    public void language(CallbackContext ctx) {
        ctx.answerAlert("🌍 Смена языка пока недоступна!");
    }

    @OnCallbackQuery("notifications")
    public void notifications(CallbackContext ctx) {
        ctx.answer("🔔 Уведомления включены!");
    }

    @OnCallbackQuery("delete_account")
    public void deleteAccount(CallbackContext ctx) {
        ctx.answer("⚠️ Эта функция пока недоступна!");
    }

    // -----------------------------------------------------------
    // Убрать клавиатуру
    // -----------------------------------------------------------

    @OnCommand("/cancel")
    public BotResponse cancel() {
        return BotResponse.text("Клавиатура убрана ✅")
                .removeKeyboard();
    }
}
