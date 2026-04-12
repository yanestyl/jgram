package io.github.yanestyl.jgram.annotation.filter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnlyMention {

    /**
     * Message to send if bot is not mentioned. Empty = ignore silently.
     */
    String fallback() default "";
}
