package io.github.yanestyl.jgram.core;

import io.github.yanestyl.jgram.annotation.OnCallbackQuery;
import io.github.yanestyl.jgram.annotation.OnCommand;
import io.github.yanestyl.jgram.annotation.OnMessage;
import io.github.yanestyl.jgram.handler.HandlerMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerRegistry {

    // "/start" -> HandlerMethod
    private final Map<String, HandlerMethod> commandHandlers = new HashMap<>();

    // список хендлеров для текстовых сообщений
    private final List<HandlerMethod> messageHandlers = new ArrayList<>();

    // "callback_data" -> HandlerMethod
    private final Map<String, HandlerMethod> callbackHandlers = new HashMap<>();

    // catch-all для callback
    private HandlerMethod anyCallbackHandler;

    public void register(Object controllerInstance) {
        Class<?> clazz = controllerInstance.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            method.setAccessible(true);

            if (method.isAnnotationPresent(OnCommand.class)) {
                String command = method.getAnnotation(OnCommand.class).value();
                commandHandlers.put(command, new HandlerMethod(controllerInstance, method));
            }

            if (method.isAnnotationPresent(OnMessage.class)) {
                messageHandlers.add(new HandlerMethod(controllerInstance, method));
            }

            if (method.isAnnotationPresent(OnCallbackQuery.class)) {
                String data = method.getAnnotation(OnCallbackQuery.class).value();
                if (data.isEmpty()) {
                    anyCallbackHandler = new HandlerMethod(controllerInstance, method);
                } else {
                    callbackHandlers.put(data, new HandlerMethod(controllerInstance, method));
                }
            }
        }
    }

    public HandlerMethod findCommandHandler(String command) {
        return commandHandlers.get(command);
    }

    public List<HandlerMethod> getMessageHandlers() {
        return messageHandlers;
    }

    public HandlerMethod findCallbackHandler(String data) {
        return callbackHandlers.getOrDefault(data, anyCallbackHandler);
    }
}
