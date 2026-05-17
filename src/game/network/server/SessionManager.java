/**
 * Manages authenticated player sessions on the server.
 *
 * Sessions remain available even when clients disconnect so that the
 * server can continue advancing their game state over time.
 */
package game.network.server;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private final ConcurrentHashMap<String, PlayerSession> sessions;
    private final Path saveDirectory;

    public SessionManager(Path saveDirectory) {
        this.sessions = new ConcurrentHashMap<>();
        this.saveDirectory = saveDirectory;
    }

    /**
     * Returns an existing player session or creates one for a new login.
     *
     * @param username the authenticated username
     * @return the player's server-side session
     * @throws IOException if the session save file cannot be loaded
     */
    public PlayerSession getOrCreateSession(String username) throws IOException {
        PlayerSession existing = sessions.get(username);
        if (existing != null) {
            return existing;
        }

        PlayerSession created = new PlayerSession(username, saveDirectory.resolve(username + "-village-state.xml"));
        PlayerSession raced = sessions.putIfAbsent(username, created);
        return raced != null ? raced : created;
    }

    /**
     * Returns all active sessions for server-side ticking.
     *
     * @return the active player sessions
     */
    public Collection<PlayerSession> getAllSessions() {
        return sessions.values();
    }
}
