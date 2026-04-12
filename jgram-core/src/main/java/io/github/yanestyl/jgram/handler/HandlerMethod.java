package io.github.yanestyl.jgram.handler;

import io.github.yanestyl.jgram.annotation.filter.ChatType;
import io.github.yanestyl.jgram.annotation.filter.OnlyMention;
import io.github.yanestyl.jgram.annotation.filter.UseFilter;
import io.github.yanestyl.jgram.annotation.fsm.ClearsState;
import io.github.yanestyl.jgram.annotation.fsm.EnterState;
import io.github.yanestyl.jgram.annotation.fsm.ExitState;
import io.github.yanestyl.jgram.annotation.fsm.NextState;
import io.github.yanestyl.jgram.annotation.fsm.OnState;
import io.github.yanestyl.jgram.filter.Filter;
import io.github.yanestyl.jgram.model.UpdateContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * Represents a single registered handler method.
 */
@Slf4j
public class HandlerMethod {

    private final Object instance;
    private final Method method;

    // кэшируем фильтры при регистрации
    private final Filter chatTypeFilter;
    private final Filter mentionFilter;
    private final Filter customFilter;

    public HandlerMethod(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        this.chatTypeFilter = buildChatTypeFilter(method);
        this.mentionFilter  = buildMentionFilter(method);
        this.customFilter   = buildCustomFilter(method);
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

    /**
     * Checks all filters against the update.
     * Returns null if passed, or a fallback message if failed.
     */
    public FilterResult applyFilters(UpdateContext update) {
        for (Filter filter : new Filter[]{chatTypeFilter, mentionFilter, customFilter}) {
            if (filter == null) continue;
            if (!filter.test(update)) {
                return FilterResult.fail(filter.onFail());
            }
        }
        return FilterResult.pass();
    }

    // -----------------------------------------------------------
    // Filter builders
    // -----------------------------------------------------------

    private Filter buildChatTypeFilter(Method method) {
        if (!method.isAnnotationPresent(ChatType.class)) return null;
        ChatType annotation = method.getAnnotation(ChatType.class);

        return new Filter() {
            @Override
            public boolean test(UpdateContext ctx) {
                return switch (annotation.value()) {
                    case PRIVATE    -> ctx.getChatType() == UpdateContext.ChatType.PRIVATE;
                    case GROUP      -> ctx.getChatType() == UpdateContext.ChatType.GROUP;
                    case SUPERGROUP -> ctx.getChatType() == UpdateContext.ChatType.SUPERGROUP;
                    case CHANNEL    -> ctx.getChatType() == UpdateContext.ChatType.CHANNEL;
                };
            }

            @Override
            public String onFail() {
                return annotation.fallback().isEmpty() ? null : annotation.fallback();
            }
        };
    }

    private Filter buildMentionFilter(Method method) {
        if (!method.isAnnotationPresent(OnlyMention.class)) return null;
        OnlyMention annotation = method.getAnnotation(OnlyMention.class);

        return new Filter() {
            @Override
            public boolean test(UpdateContext ctx) {
                String text = ctx.getText();
                if (text == null) return false;
                return text.contains("@");
            }

            @Override
            public String onFail() {
                return annotation.fallback().isEmpty() ? null : annotation.fallback();
            }
        };
    }

    private Filter buildCustomFilter(Method method) {
        if (!method.isAnnotationPresent(UseFilter.class)) return null;
        Class<? extends Filter> filterClass = method.getAnnotation(UseFilter.class).value();

        try {
            return filterClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("Failed to instantiate filter: {}", filterClass.getSimpleName(), e);
            return null;
        }
    }

    // геттеры для FSM аннотаций
    public boolean hasEnterState() {
        return method.isAnnotationPresent(EnterState.class);
    }

    public String getEnterState() {
        return method.getAnnotation(EnterState.class).value();
    }

    public boolean hasOnState() {
        return method.isAnnotationPresent(OnState.class);
    }

    public String getOnState() {
        return method.getAnnotation(OnState.class).value();
    }

    public boolean hasNextState() {
        return method.isAnnotationPresent(NextState.class);
    }

    public String getNextState() {
        return method.getAnnotation(NextState.class).value();
    }

    public boolean hasExitState() {
        return method.isAnnotationPresent(ExitState.class);
    }

    public boolean hasClearsState() {
        return method.isAnnotationPresent(ClearsState.class);
    }
}
