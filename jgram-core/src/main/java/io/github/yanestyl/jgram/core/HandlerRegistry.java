package io.github.yanestyl.jgram.core;

import io.github.yanestyl.jgram.annotation.fsm.OnState;
import io.github.yanestyl.jgram.annotation.content.*;
import io.github.yanestyl.jgram.handler.HandlerMethod;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HandlerRegistry {

    private final Map<String, HandlerMethod> commandHandlers  = new HashMap<>();
    @Getter
    private final List<HandlerMethod>        messageHandlers  = new ArrayList<>();
    private final Map<String, HandlerMethod> callbackHandlers = new HashMap<>();
    private final Map<String, HandlerMethod> stateHandlers    = new HashMap<>();

    private HandlerMethod anyCallbackHandler;
    private HandlerMethod photoHandler;
    private HandlerMethod locationHandler;
    private HandlerMethod stickerHandler;
    private HandlerMethod videoHandler;
    private HandlerMethod documentHandler;
    private HandlerMethod audioHandler;
    private HandlerMethod voiceHandler;
    private HandlerMethod contactHandler;
    private HandlerMethod animationHandler;

    public void register(Object controllerInstance) {
        Class<?> clazz = controllerInstance.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            method.setAccessible(true);

            if (method.isAnnotationPresent(OnCommand.class)) {
                String[] commands = method.getAnnotation(OnCommand.class).value();
                HandlerMethod handler = new HandlerMethod(controllerInstance, method);
                for (String command : commands) {
                    commandHandlers.put(command, handler);
                    log.debug("Registered command handler: {}", command);
                }
            }

            if (method.isAnnotationPresent(OnMessage.class)) {
                messageHandlers.add(new HandlerMethod(controllerInstance, method));
            }

            if (method.isAnnotationPresent(OnState.class)) {
                String state = method.getAnnotation(OnState.class).value();
                stateHandlers.put(state, new HandlerMethod(controllerInstance, method));
            }

            if (method.isAnnotationPresent(OnCallbackQuery.class)) {
                String data = method.getAnnotation(OnCallbackQuery.class).value();
                HandlerMethod handler = new HandlerMethod(controllerInstance, method);
                if (data.isEmpty()) {
                    anyCallbackHandler = handler;
                } else {
                    callbackHandlers.put(data, handler);
                }
            }

            if (method.isAnnotationPresent(OnPhoto.class))
                photoHandler = new HandlerMethod(controllerInstance, method);

            if (method.isAnnotationPresent(OnLocation.class))
                locationHandler = new HandlerMethod(controllerInstance, method);

            if (method.isAnnotationPresent(OnSticker.class))
                stickerHandler = new HandlerMethod(controllerInstance, method);

            if (method.isAnnotationPresent(OnVideo.class))
                videoHandler = new HandlerMethod(controllerInstance, method);

            if (method.isAnnotationPresent(OnDocument.class))
                documentHandler = new HandlerMethod(controllerInstance, method);

            if (method.isAnnotationPresent(OnAudio.class))
                audioHandler = new HandlerMethod(controllerInstance, method);

            if (method.isAnnotationPresent(OnVoice.class))
                voiceHandler = new HandlerMethod(controllerInstance, method);

            if (method.isAnnotationPresent(OnContact.class))
                contactHandler = new HandlerMethod(controllerInstance, method);

            if (method.isAnnotationPresent(OnAnimation.class))
                animationHandler = new HandlerMethod(controllerInstance, method);
        }
    }

    public HandlerMethod findCommandHandler(String command)  { return commandHandlers.get(command); }

    public HandlerMethod findStateHandler(String state)      { return stateHandlers.get(state); }
    public HandlerMethod findCallbackHandler(String data)    { return callbackHandlers.getOrDefault(data, anyCallbackHandler); }
    public HandlerMethod findPhotoHandler()                  { return photoHandler; }
    public HandlerMethod findLocationHandler()               { return locationHandler; }
    public HandlerMethod findStickerHandler()                { return stickerHandler; }
    public HandlerMethod findVideoHandler()                  { return videoHandler; }
    public HandlerMethod findDocumentHandler()               { return documentHandler; }
    public HandlerMethod findAudioHandler()                  { return audioHandler; }
    public HandlerMethod findVoiceHandler()                  { return voiceHandler; }
    public HandlerMethod findContactHandler()                { return contactHandler; }
    public HandlerMethod findAnimationHandler()              { return animationHandler; }
}
