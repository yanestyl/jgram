package io.github.yanestyl.jgram.fsm;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages per-user sessions.
 * Thread-safe in-memory implementation.
 */
public class SessionManager {

    private final ConcurrentHashMap<Long, Session> sessions = new ConcurrentHashMap<>();

    /**
     * Returns session for user, creates new one if not exists.
     */
    public Session getSession(long userId) {
        return sessions.computeIfAbsent(userId, id -> new Session());
    }

    public void clearSession(long userId) {
        sessions.remove(userId);
    }
}
