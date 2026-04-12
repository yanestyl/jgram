package io.github.yanestyl.jgram.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.GetUpdates;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.github.yanestyl.jgram.annotation.BotController;
import io.github.yanestyl.jgram.config.model.JGramConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BotApplication {

    public static void run(JGramConfig config, String basePackage) {
        log.info("Starting JGram...");

        TelegramBot bot = new TelegramBot(config.getToken());
        HandlerRegistry registry = new HandlerRegistry();

        // сканируем @BotController классы
        try (ScanResult result = new ClassGraph()
                .enableAnnotationInfo()
                .acceptPackages(basePackage)
                .scan()) {

            result.getClassesWithAnnotation(BotController.class)
                    .forEach(classInfo -> {
                        try {
                            Object instance = classInfo.loadClass()
                                    .getDeclaredConstructor().newInstance();
                            registry.register(instance);
                            log.info("Registered controller: {}", classInfo.getSimpleName());
                        } catch (Exception e) {
                            log.error("Failed to instantiate controller: {}",
                                    classInfo.getSimpleName(), e);
                        }
                    });
        }

        UpdateDispatcher dispatcher = new UpdateDispatcher(registry, bot, config);

        // настройки поллинга из конфига
        GetUpdates getUpdates = new GetUpdates()
                .timeout(config.getPolling().getTimeout())
                .limit(config.getPolling().getLimit());

        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                if (config.getLogging().isUpdates()) {
                    log.debug("Received update: {}", update.updateId());
                }
                dispatcher.dispatch(update);
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, getUpdates);

        // graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Stopping JGram...");
            bot.removeGetUpdatesListener();
            log.info("JGram stopped.");
        }));

        log.info("JGram is running!");
    }
}
