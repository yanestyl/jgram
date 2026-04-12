package io.github.yanestyl.jgram.handler;

import lombok.Getter;

/**
 * Result of applying filters to an update.
 */
@Getter
public class FilterResult {

    private final boolean passed;
    private final String fallback; // null = молчать

    public FilterResult(boolean passed, String fallback) {
        this.passed = passed;
        this.fallback = fallback;
    }

    public static FilterResult pass() {
        return new FilterResult(true, null);
    }

    public static FilterResult fail(String fallback) {
        return new FilterResult(false, fallback);
    }

}
