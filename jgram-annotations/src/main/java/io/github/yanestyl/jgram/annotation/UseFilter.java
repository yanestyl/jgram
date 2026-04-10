package io.github.yanestyl.jgram.annotation;

import io.github.yanestyl.jgram.filter.Filter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseFilter {

    /**
     * Filter class to instantiate and apply.
     */
    Class<? extends Filter> value();
}
