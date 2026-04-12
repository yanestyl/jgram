package io.github.yanestyl.example.context;

import io.github.yanestyl.jgram.annotation.*;
import io.github.yanestyl.jgram.annotation.content.OnCommand;
import io.github.yanestyl.jgram.annotation.content.OnLocation;
import io.github.yanestyl.jgram.annotation.content.OnMessage;
import io.github.yanestyl.jgram.annotation.content.OnPhoto;
import io.github.yanestyl.jgram.context.LocationContext;
import io.github.yanestyl.jgram.context.MessageContext;
import io.github.yanestyl.jgram.context.PhotoContext;

@BotController
public class ContextBotController {

    @OnCommand("/start")
    public String start(MessageContext ctx) {
        return "Привет, " + ctx.user().firstName() + "! \uD83D\uDC4B\n" +
                "Твой chat id: " + ctx.chatId();
    }

    @OnCommand("/me")
    public String me(MessageContext ctx) {
        var user = ctx.user();
        return "Ты: " + user.firstName() + " " +
                (user.lastName() != null ? user.lastName() : "") + "\n" +
                "Username: @" + (user.username() != null ? user.username() : "нет") + "\n" +
                "ID: " + user.id();
    }

    @OnMessage
    public String echo(MessageContext ctx) {
        return "Ты написал: «" + ctx.text() + "»\n" +
                "Длина: " + ctx.text().length() + " символов";
    }

    @OnPhoto
    public void photo(PhotoContext ctx) {
        String caption = ctx.caption().isEmpty() ? "без подписи" : ctx.caption();
        ctx.reply("\uD83D\uDCF8 Фото получено!\nПодпись: " + caption);
    }

    @OnLocation
    public void location(LocationContext ctx) {
        ctx.reply("\uD83D\uDCCD Твоя геолокация:\n" +
                "Широта: " + ctx.latitude() + "\n" +
                "Долгота: " + ctx.longitude());
    }
}
