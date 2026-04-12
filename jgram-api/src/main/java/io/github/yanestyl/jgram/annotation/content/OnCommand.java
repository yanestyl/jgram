package io.github.yanestyl.jgram.annotation.content;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Handles one or more bot commands.
 * <p>
 * Single command:
 * <pre>
 * {@literal @}OnCommand("/start")
 * public String start() { }
 * </pre>
 * <p>
 * Multiple commands:
 * <pre>
 * {@literal @}OnCommand({"/help", "/h", "/?"})
 * public String help() { }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnCommand {

    /**
     * One or more commands to handle, including slash.
     */
    String[] value();
}
