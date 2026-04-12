package io.github.yanestyl.jgram.fsm;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages per-user FSM states.
 * Thread-safe in-memory implementation.
 */
public class StateManager {

    private final ConcurrentHashMap<Long, String> states = new ConcurrentHashMap<>();

    /**
     * Returns current state for user, or null if no state.
     */
    public String getState(long userId) {
        return states.get(userId);
    }

    public void setState(long userId, String state) {
        states.put(userId, state);
    }

    public void clearState(long userId) {
        states.remove(userId);
    }

    public boolean hasState(long userId) {
        return states.containsKey(userId);
    }

    public boolean isInState(long userId, String state) {
        return state.equals(states.get(userId));
    }
}
