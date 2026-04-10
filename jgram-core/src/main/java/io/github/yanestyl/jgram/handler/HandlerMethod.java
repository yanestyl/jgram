package io.github.yanestyl.jgram.handler;

import com.pengrad.telegrambot.model.Update;
import io.github.yanestyl.jgram.annotation.ChatType;
import io.github.yanestyl.jgram.annotation.OnlyMention;
import io.github.yanestyl.jgram.annotation.UseFilter;
import io.github.yanestyl.jgram.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Represents a single registered handler method.
 */
public class HandlerMethod {

    private static final Logger log = LoggerFactory.getLogger(HandlerMethod.class);

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
    public FilterResult applyFilters(Update update) {
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
            public boolean test(Update update) {
                if (update.message() == null) return false;
                String type = update.message().chat().type().name();
                return switch (annotation.value()) {
                    case PRIVATE -> "Private".equals(type);
                    case GROUP -> "group".equals(type);
                    case SUPERGROUP -> "supergroup".equals(type);
                    case CHANNEL -> "channel".equals(type);
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
            public boolean test(Update update) {
                if (update.message() == null) return false;
                String text = update.message().text();
                if (text == null) return false;
                // проверяем наличие @username упоминания в тексте
                var entities = update.message().entities();
                if (entities == null) return false;
                for (var entity : entities) {
                    if ("mention".equalsIgnoreCase(entity.type().name())) return true;
                }
                return false;
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
}
