package io.github.yanestyl.example;

import io.github.yanestyl.jgram.annotation.BotController;
import io.github.yanestyl.jgram.annotation.OnCommand;
import io.github.yanestyl.jgram.annotation.OnMessage;
import io.github.yanestyl.jgram.context.MessageContext;

@BotController
public class EchoBotController {

    @OnCommand("/start")
    public String start(MessageContext ctx) {
        return "Привет, " + ctx.user().firstName() + "! \uD83D\uDC4B";
    }

    @OnCommand("/help")
    public String help() {
        return "Просто напиши любое сообщение - я повторю его!";
    }

    @OnMessage
    public String echo(String text) {
        return "Ты написал: " + text;
    }
}
