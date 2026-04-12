package io.github.yanestyl.jgram.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.github.yanestyl.jgram.annotation.BotController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BotApplication {

    public static void run(String token, String basePackage) {
        log.info("Starting JGram...");

        TelegramBot bot = new TelegramBot(token);
        HandlerRegistry registry = new HandlerRegistry();

        // сканируем @BotController классы
        try (ScanResult result = new ClassGraph()
                .enableAnnotationInfo()
                .acceptPackages(basePackage)
                .scan()) {

            result.getClassesWithAnnotation(BotController.class)
                    .forEach(classInfo -> {
                        try {
                            Object instance = classInfo.loadClass().getDeclaredConstructor().newInstance();
                            registry.register(instance);
                            log.info("Registered controller: {}", classInfo.getSimpleName());
                        } catch (Exception e) {
                            log.error("Failed to instantiate controller: {}", classInfo.getSimpleName(), e);
                        }
                    });
        }

        UpdateDispatcher dispatcher = new UpdateDispatcher(registry, bot);

        // запуск long polling
        bot.setUpdatesListener(updates -> {
            updates.forEach(dispatcher::dispatch);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        log.info("JGram is running!");
    }
}
