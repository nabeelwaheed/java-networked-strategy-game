/**
 * Client-side network helper used to communicate with the server.
 *
 * The client exchanges serialized request and response objects over a TCP socket.
 */
package game.network.client;

import game.network.protocol.GameRequest;
import game.network.protocol.GameResponse;
import game.network.protocol.RequestType;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NetworkGameClient implements Closeable {
    private final Socket socket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;

    public NetworkGameClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Sends the initial authentication request to the server.
     *
     * @param username the username entered by the player
     * @param password the password entered by the player
     * @return the server authentication response
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if the response cannot be decoded
     */
    public GameResponse authenticate(String username, String password) throws IOException, ClassNotFoundException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);
        return send(new GameRequest(RequestType.AUTHENTICATE, parameters));
    }

    /**
     * Sends a request that does not need parameters.
     *
     * @param type the request type
     * @return the server response
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if the response cannot be decoded
     */
    public GameResponse send(RequestType type) throws IOException, ClassNotFoundException {
        return send(new GameRequest(type));
    }

    /**
     * Sends a request with string parameters.
     *
     * @param type the request type
     * @param parameters the request parameters
     * @return the server response
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if the response cannot be decoded
     */
    public GameResponse send(RequestType type, Map<String, String> parameters) throws IOException, ClassNotFoundException {
        return send(new GameRequest(type, parameters));
    }

    /**
     * Writes one request to the server and reads back one response.
     *
     * @param request the request to send
     * @return the server response
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if the response cannot be decoded
     */
    private GameResponse send(GameRequest request) throws IOException, ClassNotFoundException {
        output.writeObject(request);
        output.flush();
        return (GameResponse) input.readObject();
    }

    /**
     * Closes the socket connection to the server.
     *
     * @throws IOException if the socket cannot be closed
     */
    @Override
    public void close() throws IOException {
        socket.close();
    }
}
