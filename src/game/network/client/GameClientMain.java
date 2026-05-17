/**
 * Entry point for the Assignment 4 game client.
 *
 * The client connects to the server over TCP and authenticates the player
 * and then exposes the networked console interface.
 */
package game.network.client;

import java.io.IOException;

public class GameClientMain {
    private static final int DEFAULT_PORT = 5050;

    /**
     * Starts the client and connects it to the configured server.
     *
     * @param args optional host and port arguments
     */
    public static void main(String... args) {
        String host = args.length > 0 ? args[0] : "127.0.0.1"; //local host
        int port = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;

        try (NetworkGameClient client = new NetworkGameClient(host, port)) {
            NetworkConsoleUI ui = new NetworkConsoleUI(client);
            ui.start();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Unable to start client: " + e.getMessage());
        }
    }
}
