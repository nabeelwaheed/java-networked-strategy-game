/**
 * Serializable response returned by the server to the client.
 *
 * A response includes whether the request succeeded, a user-facing message,
 * optional payload data and any pending engine event messages.
 */
package game.network.protocol;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;
    private final Serializable payload;
    private final List<String> events;

    public GameResponse(boolean success, String message, Serializable payload, List<String> events) {
        this.success = success;
        this.message = message;
        this.payload = payload;
        this.events = new ArrayList<>(events);
    }

    public static GameResponse success(String message, Serializable payload, List<String> events) {
        return new GameResponse(true, message, payload, events);
    }

    public static GameResponse failure(String message, List<String> events) {
        return new GameResponse(false, message, null, events);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Serializable getPayload() {
        return payload;
    }

    public List<String> getEvents() {
        return events;
    }
}
