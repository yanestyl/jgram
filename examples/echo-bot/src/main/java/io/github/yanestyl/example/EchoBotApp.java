package io.github.yanestyl.example;

import io.github.yanestyl.jgram.core.BotApplication;

public class EchoBotApp {

    public static void main(String[] args) {
        String token = System.getenv("BOT_TOKEN");

        if (token == null || token.isBlank()) {
            throw new IllegalStateException("BOT_TOKEN environment variable is not set!");
        }

        BotApplication.run(token, "io.github.yanestyl.example");
    }
}
