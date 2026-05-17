/**
 * Handles one connected client on the Assignment 4 server.
 *
 * The handler performs an authentication handshake and then processes
 * serialized request/response messages until the client disconnects.
 */
package game.network.server;

import game.buildings.BuildingType;
import game.exceptions.GameException;
import game.inhabitants.InhabitantType;
import game.network.protocol.GameRequest;
import game.network.protocol.GameResponse;
import game.network.protocol.RequestType;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final UserDatabase userDatabase;
    private final SessionManager sessionManager;
    private final ExecutorService simulationExecutor;

    public ClientHandler(Socket socket,
                         UserDatabase userDatabase,
                         SessionManager sessionManager,
                         ExecutorService simulationExecutor) {
        this.socket = socket;
        this.userDatabase = userDatabase;
        this.sessionManager = sessionManager;
        this.simulationExecutor = simulationExecutor;
    }

    /**
     * Runs the client connection lifecycle.
     *
     * This method performs authentication first then processes
     * client requests until the client disconnects.
     */
    @Override
    public void run() {
        try (Socket clientSocket = socket;
             ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream())) {

            GameRequest authRequest = (GameRequest) input.readObject();
            if (authRequest.getType() != RequestType.AUTHENTICATE) {
                output.writeObject(GameResponse.failure("Authentication is required before any other request.", List.of()));
                output.flush();
                return;
            }

            String username = authRequest.getParameter("username");
            String password = authRequest.getParameter("password");
            if (!userDatabase.authenticate(username, password)) {
                output.writeObject(GameResponse.failure("Authentication failed.", List.of()));
                output.flush();
                return;
            }

            PlayerSession session = sessionManager.getOrCreateSession(username);
            output.writeObject(GameResponse.success("Authentication succeeded.", null, session.consumeEvents()));
            output.flush();

            while (true) {
                GameRequest request = (GameRequest) input.readObject();
                if (request.getType() == RequestType.DISCONNECT) {
                    session.saveGame();
                    output.writeObject(GameResponse.success("Disconnected from server.", null, session.consumeEvents()));
                    output.flush();
                    break;
                }

                GameResponse response = simulationExecutor.submit(() -> processRequest(session, request)).get();
                output.writeObject(response);
                output.flush();
            }
        } catch (EOFException ignored) {
            // client disconnected abruptly
        } catch (IOException | ClassNotFoundException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts exceptions from request processing into failure responses.
     *
     * @param session the authenticated player session
     * @param request the request sent by the client
     * @return a response to send back to the client
     */
    private GameResponse processRequest(PlayerSession session, GameRequest request) {
        try {
            return handleRequest(session, request);
        } catch (IOException | GameException e) {
            return GameResponse.failure(e.getMessage(), session.consumeEvents());
        }
    }

    /**
     * Executes a supported client request against the server-side session
     *
     * @param session the authenticated player session
     * @param request the request sent by the client
     * @return the response containing message, events and optional payload
     * @throws IOException if persistence operations fail
     * @throws GameException if a game action is invalid
     */
    private GameResponse handleRequest(PlayerSession session, GameRequest request) throws IOException, GameException {
        switch (request.getType()) {
            case SHOW_STATUS -> {
                return GameResponse.success("Village status updated.", session.getVillageStatus(), session.consumeEvents());
            }
            case BUILD -> {
                session.build(BuildingType.valueOf(request.getParameter("buildingType")));
                return GameResponse.success("Building completed.", null, session.consumeEvents());
            }
            case TRAIN -> {
                session.train(InhabitantType.valueOf(request.getParameter("inhabitantType")));
                return GameResponse.success("Training completed.", null, session.consumeEvents());
            }
            case UPGRADE_BUILDING -> {
                session.upgradeBuilding(Integer.parseInt(request.getParameter("index")));
                return GameResponse.success("Building upgraded.", null, session.consumeEvents());
            }
            case UPGRADE_INHABITANT -> {
                session.upgradeInhabitant(Integer.parseInt(request.getParameter("index")));
                return GameResponse.success("Inhabitant upgraded.", null, session.consumeEvents());
            }
            case EXPLORE_VILLAGES -> {
                return GameResponse.success("Villages generated.", (java.io.Serializable) session.exploreVillages(), session.consumeEvents());
            }
            case ATTACK_EXPLORED_VILLAGE -> {
                return GameResponse.success("Attack resolved.",
                        session.attackExploredVillage(Integer.parseInt(request.getParameter("index"))),
                        session.consumeEvents());
            }
            case ADVANCE_TIME -> {
                session.advanceTime(Integer.parseInt(request.getParameter("hours")));
                return GameResponse.success("Time advanced.", null, session.consumeEvents());
            }
            case SAVE_GAME -> {
                session.saveGame();
                return GameResponse.success("Game saved on server.", null, session.consumeEvents());
            }
            case LOAD_GAME -> {
                boolean loaded = session.loadGame();
                return GameResponse.success(loaded ? "Saved game loaded." : "No saved game found.", null, session.consumeEvents());
            }
            case NEW_GAME -> {
                session.newGame();
                return GameResponse.success("New game started.", null, session.consumeEvents());
            }
            case GENERATE_TEST_ARMY -> {
                return GameResponse.success("Testing army generated.", session.generateTestingArmy(), session.consumeEvents());
            }
            case TEST_VILLAGE -> {
                int rounds = Integer.parseInt(request.getParameter("rounds"));
                return GameResponse.success("Village defense test completed.", session.testVillage(rounds), session.consumeEvents());
            }
            default -> {
                return GameResponse.failure("Unsupported request: " + request.getType(), session.consumeEvents());
            }
        }
    }
}
