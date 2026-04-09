package io.github.yanestyl.jgram.handler;

import java.lang.reflect.Method;

/**
 * Represents a single registered handler method.
 */
public class HandlerMethod {

    private final Object instance;
    private final Method method;

    public HandlerMethod(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public Object getInstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }

    /**
     * Invokes the handler and returns result (String or null).
     */
    public Object invoke(Object... args) throws Exception {
        return method.invoke(instance, args);
    }
}
