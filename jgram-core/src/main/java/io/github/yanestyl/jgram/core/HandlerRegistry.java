package io.github.yanestyl.jgram.core;

import io.github.yanestyl.jgram.annotation.fsm.OnState;
import io.github.yanestyl.jgram.annotation.content.*;
import io.github.yanestyl.jgram.handler.HandlerMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerRegistry {

    private final Map<String, HandlerMethod>    commandHandlers = new HashMap<>();
    private final List<HandlerMethod>           messageHandlers = new ArrayList<>();
    private final Map<String, HandlerMethod>    callbackHandlers = new HashMap<>();
    private final Map<String, HandlerMethod>    stateHandlers = new HashMap<>();
    private HandlerMethod anyCallbackHandler;
    private HandlerMethod photoHandler;
    private HandlerMethod locationHandler;

    public void register(Object controllerInstance) {
        Class<?> clazz = controllerInstance.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            method.setAccessible(true);

            if (method.isAnnotationPresent(OnState.class)) {
                String state = method.getAnnotation(OnState.class).value();
                stateHandlers.put(state, new HandlerMethod(controllerInstance, method));
            }

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

            if (method.isAnnotationPresent(OnPhoto.class)) {
                photoHandler = new HandlerMethod(controllerInstance, method);
            }

            if (method.isAnnotationPresent(OnLocation.class)) {
                locationHandler = new HandlerMethod(controllerInstance, method);
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

    public HandlerMethod findPhotoHandler() {
        return photoHandler;
    }

    public HandlerMethod findLocationHandler() {
        return locationHandler;
    }

    public HandlerMethod findStateHandler(String state) {
        return stateHandlers.get(state);
    }
}
