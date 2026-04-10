package io.github.yanestyl.jgram.handler;

/**
 * Result of applying filters to an update.
 */
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

    public boolean isPassed() {
        return passed;
    }

    public String getFallback() {
        return fallback;
    }
}
