package io.github.yanestyl.jgram.annotation.filter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChatType {

    Type value();

    /**
     * Message to send if filter fails. Empty = ignore silently.
     */
    String fallback() default "";

    enum Type {
        PRIVATE, GROUP, SUPERGROUP, CHANNEL
    }
}
