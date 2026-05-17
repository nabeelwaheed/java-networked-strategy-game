/**
 * Serializable request sent by the client to the server.
 *
 * Each request contains a request type and a small map of string parameters.
 */
package game.network.protocol;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final RequestType type;
    private final Map<String, String> parameters;

    public GameRequest(RequestType type) {
        this(type, new HashMap<>());
    }

    public GameRequest(RequestType type, Map<String, String> parameters) {
        this.type = type;
        this.parameters = new HashMap<>(parameters);
    }

    public RequestType getType() {
        return type;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }
}
