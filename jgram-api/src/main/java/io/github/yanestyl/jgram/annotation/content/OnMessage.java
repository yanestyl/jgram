package io.github.yanestyl.jgram.annotation.content;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Handles incoming text messages.
 * Without parameters - catches all text messages.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnMessage {

    /**
     * Message must contain this substring. Empty = any message.
     */
    String contains() default "";

    /**
     * Message must start with this string. Empty = ignored.
     */
    String startsWith() default "";

    /**
     * Message must match this regex. Empty = ignored.
     */
    String regex() default "";
}
