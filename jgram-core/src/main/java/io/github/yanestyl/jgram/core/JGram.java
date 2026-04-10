package io.github.yanestyl.jgram.core;

import io.github.yanestyl.jgram.annotation.JGramApplication;

public final class JGram {

    private JGram() {}

    public static void run(Class<?> appClass, String[] args) {
        if (!appClass.isAnnotationPresent(JGramApplication.class)) {
            throw new IllegalArgumentException(
                    appClass.getSimpleName() + " must be annotated with @JGramApplication"
            );
        }

        String token = System.getenv("BOT_TOKEN");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("BOT_TOKEN environment variable is not set!");
        }

        String basePackage = appClass.getPackageName();

        BotApplication.run(token, basePackage);
    }
}
