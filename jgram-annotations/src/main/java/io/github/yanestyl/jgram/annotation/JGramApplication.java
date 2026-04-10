package io.github.yanestyl.jgram.annotation;

import java.lang.annotation.*;

/**
 * Marks the main application class.
 * JGram will scan for @BotController classes
 * starting from this class's package automatically.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JGramApplication {
}
