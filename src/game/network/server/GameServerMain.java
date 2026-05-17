/**
 * Entry point for the game server.
 *
 * The server accepts multiple TCP clients, authenticates them, keeps
 * their game sessions running over time and forward requests to a
 * worker thread pool for simulation-related tasks.
 */
package game.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameServerMain {
    private static final int DEFAULT_PORT = 5050;

    /**
     * Starts the TCP server and initializes the server-side thread pools.
     *
     * @param args optional first argument for the server port
     */
    public static void main(String... args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        Path serverDataDir = Path.of("server-data");

        try {
            UserDatabase userDatabase = new FileUserDatabase(serverDataDir.resolve("users.db"));
            SessionManager sessionManager = new SessionManager(serverDataDir.resolve("saves"));
            ExecutorService clientPool = Executors.newCachedThreadPool();
            ExecutorService simulationPool = Executors.newFixedThreadPool(
                    Math.max(4, Runtime.getRuntime().availableProcessors())
            );
            ScheduledExecutorService ticker = Executors.newSingleThreadScheduledExecutor();

            ticker.scheduleAtFixedRate(() -> sessionManager.getAllSessions()
                    .forEach(PlayerSession::advanceOneServerTick), 5, 5, TimeUnit.SECONDS);

            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Game server listening on port " + port + "...");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    clientPool.submit(new ClientHandler(clientSocket, userDatabase, sessionManager, simulationPool));
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to start server: " + e.getMessage());
        }
    }
}
