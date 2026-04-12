package io.github.yanestyl.jgram.core;

import io.github.yanestyl.jgram.annotation.JGramApplication;
import io.github.yanestyl.jgram.config.ConfigLoader;
import io.github.yanestyl.jgram.config.model.JGramConfig;

public final class JGram {

    private String configPath;

    private JGram() {}

    public static JGram create() {
        return new JGram();
    }

    public JGram config(String path) {
        this.configPath = path;
        return this;
    }

    public void run(Class<?> appClass, String[] args) {
        if (!appClass.isAnnotationPresent(JGramApplication.class)) {
            throw new IllegalArgumentException(
                    appClass.getSimpleName() + " must be annotated with @JGramApplication"
            );
        }

        JGramConfig config = ConfigLoader.load(configPath);
        String basePackage = appClass.getPackageName();
        BotApplication.run(config, basePackage);
    }

    // shortcut — самый простой способ запуска
    public static void run(Class<?> appClass, String[] args) {
        new JGram().run(appClass, args);
    }
}
