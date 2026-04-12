package io.github.yanestyl.example.filter;

import io.github.yanestyl.example.filter.filter.PremiumFilter;
import io.github.yanestyl.jgram.annotation.*;
import io.github.yanestyl.jgram.annotation.filter.ChatType;
import io.github.yanestyl.jgram.annotation.filter.OnlyMention;
import io.github.yanestyl.jgram.annotation.filter.UseFilter;
import io.github.yanestyl.jgram.annotation.content.OnCommand;
import io.github.yanestyl.jgram.annotation.content.OnMessage;
import io.github.yanestyl.jgram.context.MessageContext;

@BotController
public class FilterBotController {

    @OnCommand("/start")
    public String start(MessageContext ctx) {
        return "Привет, " + ctx.user().firstName() + "! \uD83D\uDC4B\n\n" +
                "Доступные команды:\n" +
                "/private - только в личке\n" +
                "/group - только в группе\n" +
                "/premium - только для премиум\n" +
                "/mention - только с упоминанием бота";
    }

    // -----------------------------------------------------------
    // @ChatType примеры
    // -----------------------------------------------------------

    @OnCommand("/private")
    @ChatType(value = ChatType.Type.PRIVATE,
            fallback = "\uD83D\uDD12 Эта команда работает только в личных сообщениях!")
    public String privateOnly(MessageContext ctx) {
        return "✅ Привет из личного чата, " + ctx.user().firstName() + "!";
    }

    @OnCommand("/group")
    @ChatType(value = ChatType.Type.GROUP,
            fallback = "\uD83D\uDC65 Эта команда работает только в группах!")
    public String groupOnly(MessageContext ctx) {
        return "✅ Привет из группы, " + ctx.user().firstName() + "!";
    }

    // -----------------------------------------------------------
    // @ChatType примеры
    // -----------------------------------------------------------

    @OnCommand("/premium")
    @UseFilter(PremiumFilter.class)
    public String premiumOnly(MessageContext ctx) {
        return "⭐ Добро пожаловать в премиум, " + ctx.user().firstName() + "!";
    }

    // -----------------------------------------------------------
    // @OnlyMention примеры
    // -----------------------------------------------------------
    @OnMessage
    @OnlyMention(fallback = "Упомяни меня чтобы я ответил!")
    public String onMention(MessageContext ctx) {
        return "Ты меня упомянул! \uD83D\uDC4B";
    }
}
