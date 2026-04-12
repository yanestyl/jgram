package io.github.yanestyl.jgram.fsm;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores arbitrary key-value data for a user session.
 */
public class Session {

    private final Map<String, String> data = new HashMap<>();

    public void put(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public String getOrDefault(String key, String defaultValue) {
        return data.getOrDefault(key, defaultValue);
    }

    public boolean has(String key) {
        return data.containsKey(key);
    }

    public void remove(String key) {
        data.remove(key);
    }

    public void clear() {
        data.clear();
    }
}
