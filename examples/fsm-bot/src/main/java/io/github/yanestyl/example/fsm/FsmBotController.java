package io.github.yanestyl.example.fsm;

import io.github.yanestyl.jgram.annotation.BotController;
import io.github.yanestyl.jgram.annotation.fsm.*;
import io.github.yanestyl.jgram.annotation.content.OnCommand;
import io.github.yanestyl.jgram.annotation.content.OnMessage;
import io.github.yanestyl.jgram.context.MessageContext;
import io.github.yanestyl.jgram.response.BotResponse;
import io.github.yanestyl.jgram.response.Button;
import io.github.yanestyl.jgram.response.Row;

@BotController
public class FsmBotController {

    // -----------------------------------------------------------
    // Старт
    // -----------------------------------------------------------

    @OnCommand("/start")
    public BotResponse start(MessageContext ctx) {
        return BotResponse.text("Привет, " + ctx.user().firstName() + "! 👋\n\n" +
                        "Доступные команды:\n" +
                        "/order — оформить заказ\n" +
                        "/feedback — оставить отзыв")
                .replyKeyboard(
                        Row.of("📦 Оформить заказ", "💬 Оставить отзыв")
                ).resizable();
    }

    // -----------------------------------------------------------
    // Сценарий 1 — Оформление заказа (3 шага)
    // -----------------------------------------------------------

    @OnCommand("/order")
    @EnterState("ORDER_WAITING_NAME")
    public BotResponse startOrder(MessageContext ctx) {
        return BotResponse.text("📦 Оформление заказа\n\n" +
                        "Шаг 1 из 3: Введите ваше имя:")
                .replyKeyboard(Row.of("❌ Отмена"))
                .resizable();
    }

    @OnMessage(contains = "Оформить заказ")
    @EnterState("ORDER_WAITING_NAME")
    public BotResponse startOrderButton(MessageContext ctx) {
        return BotResponse.text("📦 Оформление заказа\n\n" +
                        "Шаг 1 из 3: Введите ваше имя:")
                .replyKeyboard(Row.of("❌ Отмена"))
                .resizable();
    }

    @OnState("ORDER_WAITING_NAME")
    @NextState("ORDER_WAITING_PHONE")
    public BotResponse getOrderName(MessageContext ctx) {
        ctx.session().put("name", ctx.text());
        return BotResponse.text("✅ Имя сохранено: " + ctx.text() + "\n\n" +
                "Шаг 2 из 3: Введите номер телефона:");
    }

    @OnState("ORDER_WAITING_PHONE")
    @NextState("ORDER_WAITING_ADDRESS")
    public BotResponse getOrderPhone(MessageContext ctx) {
        ctx.session().put("phone", ctx.text());
        return BotResponse.text("✅ Телефон сохранён: " + ctx.text() + "\n\n" +
                "Шаг 3 из 3: Введите адрес доставки:");
    }

    @OnState("ORDER_WAITING_ADDRESS")
    @ExitState
    public BotResponse getOrderAddress(MessageContext ctx) {
        String name = ctx.session().get("name");
        String phone = ctx.session().get("phone");
        String address = ctx.text();

        return BotResponse.text("🎉 Заказ оформлен!\n\n" +
                        "👤 Имя: " + name + "\n" +
                        "📞 Телефон: " + phone + "\n" +
                        "📍 Адрес: " + address + "\n\n" +
                        "Мы свяжемся с вами в ближайшее время!")
                .replyKeyboard(
                        Row.of("📦 Оформить заказ", "💬 Оставить отзыв")
                ).resizable();
    }

    // -----------------------------------------------------------
    // Сценарий 2 — Отзыв (2 шага)
    // -----------------------------------------------------------

    @OnCommand("/feedback")
    @EnterState("FEEDBACK_WAITING_RATING")
    public BotResponse startFeedback() {
        return BotResponse.text("💬 Оставить отзыв\n\n" +
                        "Шаг 1 из 2: Оцените нас от 1 до 5:")
                .inlineKeyboard(
                        Row.of(
                                Button.callback("1 ⭐", "rating_1"),
                                Button.callback("2 ⭐", "rating_2"),
                                Button.callback("3 ⭐", "rating_3"),
                                Button.callback("4 ⭐", "rating_4"),
                                Button.callback("5 ⭐", "rating_5")
                        )
                );
    }

    @OnMessage(contains = "Оставить отзыв")
    @EnterState("FEEDBACK_WAITING_RATING")
    public BotResponse startFeedbackButton() {
        return BotResponse.text("💬 Оставить отзыв\n\n" +
                        "Шаг 1 из 2: Оцените нас от 1 до 5:")
                .inlineKeyboard(
                        Row.of(
                                Button.callback("1 ⭐", "rating_1"),
                                Button.callback("2 ⭐", "rating_2"),
                                Button.callback("3 ⭐", "rating_3"),
                                Button.callback("4 ⭐", "rating_4"),
                                Button.callback("5 ⭐", "rating_5")
                        )
                );
    }

    @OnState("FEEDBACK_WAITING_RATING")
    @NextState("FEEDBACK_WAITING_TEXT")
    public String getFeedbackRating(MessageContext ctx) {
        ctx.session().put("rating", ctx.text());
        return "✅ Оценка сохранена!\n\nШаг 2 из 2: Напишите ваш отзыв:";
    }

    @OnState("FEEDBACK_WAITING_TEXT")
    @ExitState
    public BotResponse getFeedbackText(MessageContext ctx) {
        String rating = ctx.session().get("rating");
        String text = ctx.text();

        return BotResponse.text("🙏 Спасибо за отзыв!\n\n" +
                        "⭐ Оценка: " + rating + "\n" +
                        "💬 Отзыв: " + text)
                .replyKeyboard(
                        Row.of("📦 Оформить заказ", "💬 Оставить отзыв")
                ).resizable();
    }

    // -----------------------------------------------------------
    // Отмена — работает из любого состояния
    // -----------------------------------------------------------

    @OnCommand("/cancel")
    @ClearsState
    public BotResponse cancel() {
        return BotResponse.text("❌ Действие отменено!")
                .replyKeyboard(
                        Row.of("📦 Оформить заказ", "💬 Оставить отзыв")
                ).resizable();
    }

    @OnMessage(contains = "Отмена")
    @ClearsState
    public BotResponse cancelButton() {
        return BotResponse.text("❌ Действие отменено!")
                .replyKeyboard(
                        Row.of("📦 Оформить заказ", "💬 Оставить отзыв")
                ).resizable();
    }
}
