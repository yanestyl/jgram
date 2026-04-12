package io.github.yanestyl.jgram.annotation.handler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Handles a specific bot command (e.g. "/start", "/help").
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnCommand {

    /**
     * The command to handle, including slash. Example: "/start"
     */
    String value();
}
