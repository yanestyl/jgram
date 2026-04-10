package io.github.yanestyl.jgram.annotation;

import java.lang.annotation.*;

/**
 * Handles incoming location messages.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnLocation {
}
