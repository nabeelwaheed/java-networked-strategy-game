/**
 * Console-based client UI
 *
 * This class authenticates with the server, sends request messages
 * and renders the returned DTOs for the player
 */
package game.network.client;

import game.network.dto.ArmySummaryDto;
import game.network.dto.AttackResultDto;
import game.network.dto.VillageDefenseTestResultDto;
import game.network.dto.VillageOptionDto;
import game.network.dto.VillageStatusDto;
import game.network.protocol.GameResponse;
import game.network.protocol.RequestType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NetworkConsoleUI {
    private final NetworkGameClient client;
    private final Scanner scanner;

    public NetworkConsoleUI(NetworkGameClient client) {
        this.client = client;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the networked console interface after successful authentication.
     *
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if a server response cannot be decoded
     */
    public void start() throws IOException, ClassNotFoundException {
        if (!authenticate()) {
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> showVillageStatus();
                case "2" -> buildMenu();
                case "3" -> trainMenu();
                case "4" -> upgradeMenu();
                case "5" -> exploreAndAttack();
                case "6" -> advanceTimeMenu();
                case "7" -> printResponse(client.send(RequestType.SAVE_GAME));
                case "8" -> printResponse(client.send(RequestType.LOAD_GAME));
                case "9" -> printResponse(client.send(RequestType.NEW_GAME));
                case "10" -> generateTestingArmy();
                case "11" -> testVillage();
                case "0" -> {
                    printResponse(client.send(RequestType.DISCONNECT));
                    running = false;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Prompts for credentials and sends them to the server.
     *
     * @return true if the server accepts the login
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if the response cannot be decoded
     */
    private boolean authenticate() throws IOException, ClassNotFoundException {
        System.out.println("=== Client Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        GameResponse response = client.authenticate(username, password);
        printResponse(response);
        return response.isSuccess();
    }

    /**
     * Prints the available network game commands.
     */
    private void printMenu() {
        System.out.println("\n=== Network Village War Menu ===");
        System.out.println("1. Show village status");
        System.out.println("2. Build");
        System.out.println("3. Train inhabitant/unit");
        System.out.println("4. Upgrade");
        System.out.println("5. Explore villages and attack");
        System.out.println("6. Advance time");
        System.out.println("7. Save now");
        System.out.println("8. Load game");
        System.out.println("9. New game");
        System.out.println("10. Generate testing army");
        System.out.println("11. Test village base");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    /**
     * Requests and displays the current village status from the server.
     *
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if the response cannot be decoded
     */
    private void showVillageStatus() throws IOException, ClassNotFoundException {
        GameResponse response = client.send(RequestType.SHOW_STATUS);
        printResponse(response);
        if (response.isSuccess()) {
            VillageStatusDto status = (VillageStatusDto) response.getPayload();
            System.out.println("\nVillage: " + status.getVillageName());
            System.out.println("Time: " + status.getTimeHours() + " hours");
            System.out.println("Trophies: " + status.getTrophies());
            System.out.println("Population: " + status.getCurrentPopulation() + "/" + status.getPopulationCap());
            System.out.println("Guard: " + (status.isGuardActive() ? status.getGuardRemainingHours() + " hours remaining" : "inactive"));
            System.out.println("Resources: " + status.getResources());
            System.out.println("Buildings:");
            status.getBuildings().forEach(line -> System.out.println("  " + line));
            System.out.println("Inhabitants:");
            status.getInhabitants().forEach(line -> System.out.println("  " + line));
            System.out.println("Army size: " + status.getArmySize());
            System.out.println("Army attack score: " + status.getArmyAttackScore());
            System.out.println("Village defense score: " + status.getVillageDefenseScore());
        }
    }

    /**
     * Displays building choices and sends the selected build request.
     *
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if the response cannot be decoded
     */
    private void buildMenu() throws IOException, ClassNotFoundException {
        System.out.println("\nBuild options:");
        System.out.println("1. Farm");
        System.out.println("2. Gold Mine");
        System.out.println("3. Iron Mine");
        System.out.println("4. Lumber Mill");
        System.out.println("5. Gold Storage");
        System.out.println("6. Iron Storage");
        System.out.println("7. Lumber Storage");
        System.out.println("8. Archer Tower");
        System.out.println("9. Cannon");
        System.out.print("Choose: ");
        String input = scanner.nextLine().trim();

        String type = switch (input) {
            case "1" -> "FARM";
            case "2" -> "GOLD_MINE";
            case "3" -> "IRON_MINE";
            case "4" -> "LUMBER_MILL";
            case "5" -> "GOLD_STORAGE";
            case "6" -> "IRON_STORAGE";
            case "7" -> "LUMBER_STORAGE";
            case "8" -> "ARCHER_TOWER";
            case "9" -> "CANNON";
            default -> null;
        };

        if (type == null) {
            System.out.println("Invalid build choice.");
            return;
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("buildingType", type);
        printResponse(client.send(RequestType.BUILD, parameters));
    }

    /**
     * Displays training choices and sends the selected training request.
     *
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if the response cannot be decoded
     */
    private void trainMenu() throws IOException, ClassNotFoundException {
        System.out.println("\nTrain options:");
        System.out.println("1. Worker");
        System.out.println("2. Miner");
        System.out.println("3. Collector");
        System.out.println("4. Soldier");
        System.out.println("5. Archer");
        System.out.println("6. Knight");
        System.out.println("7. Catapult");
        System.out.print("Choose: ");
        String input = scanner.nextLine().trim();

        String type = switch (input) {
            case "1" -> "WORKER";
            case "2" -> "MINER";
            case "3" -> "COLLECTOR";
            case "4" -> "SOLDIER";
            case "5" -> "ARCHER";
            case "6" -> "KNIGHT";
            case "7" -> "CATAPULT";
            default -> null;
        };

        if (type == null) {
            System.out.println("Invalid training choice.");
            return;
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("inhabitantType", type);
        printResponse(client.send(RequestType.TRAIN, parameters));
    }

    /**
     * Sends an upgrade request for either a building or inhabitant.
     *
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if the response cannot be decoded
     */
    private void upgradeMenu() throws IOException, ClassNotFoundException {
        System.out.println("Upgrade (1) Building or (2) Inhabitant?");
        String choice = scanner.nextLine().trim();
        Map<String, String> parameters = new HashMap<>();

        if ("1".equals(choice)) {
            System.out.print("Building index: ");
            parameters.put("index", scanner.nextLine().trim());
            printResponse(client.send(RequestType.UPGRADE_BUILDING, parameters));
        } else if ("2".equals(choice)) {
            System.out.print("Inhabitant index: ");
            parameters.put("index", scanner.nextLine().trim());
            printResponse(client.send(RequestType.UPGRADE_INHABITANT, parameters));
        } else {
            System.out.println("Invalid option.");
        }
    }

    /**
     * Requests generated villages and optionally attacks one of them.
     *
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if the response cannot be decoded
     */
    private void exploreAndAttack() throws IOException, ClassNotFoundException {
        GameResponse response = client.send(RequestType.EXPLORE_VILLAGES);
        printResponse(response);
        if (!response.isSuccess()) {
            return;
        }

        @SuppressWarnings("unchecked")
        List<VillageOptionDto> options = (List<VillageOptionDto>) response.getPayload();
        System.out.println("\nAvailable villages to attack:");
        for (VillageOptionDto option : options) {
            System.out.println("[" + option.getIndex() + "] " + option.getName()
                    + " | Defense=" + option.getDefenseScore()
                    + " | Army=" + option.getArmyScore()
                    + " | Resources=" + option.getResources()
                    + (option.isGuardActive() ? " | Guard Active" : ""));
        }

        System.out.print("Choose village index (-1 to cancel): ");
        int index = Integer.parseInt(scanner.nextLine().trim());
        if (index < 0) {
            return;
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("index", String.valueOf(index));
        GameResponse attackResponse = client.send(RequestType.ATTACK_EXPLORED_VILLAGE, parameters);
        printResponse(attackResponse);
        if (attackResponse.isSuccess()) {
            AttackResultDto result = (AttackResultDto) attackResponse.getPayload();
            System.out.println("Attack " + (result.isSuccess() ? "won" : "lost")
                    + " | Loot: gold=" + result.getLootGold()
                    + ", iron=" + result.getLootIron()
                    + ", wood=" + result.getLootWood()
                    + " | Trophies: " + result.getTrophiesChange());
        }
    }

    /**
     * Sends a request to advance server-side game time.
     *
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if the response cannot be decoded
     */
    private void advanceTimeMenu() throws IOException, ClassNotFoundException {
        System.out.print("Advance how many hours? ");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("hours", scanner.nextLine().trim());
        printResponse(client.send(RequestType.ADVANCE_TIME, parameters));
    }

    /**
     * Requests a detached generated army for village testing.
     *
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if the response cannot be decoded
     */
    private void generateTestingArmy() throws IOException, ClassNotFoundException {
        GameResponse response = client.send(RequestType.GENERATE_TEST_ARMY);
        printResponse(response);
        if (response.isSuccess()) {
            ArmySummaryDto army = (ArmySummaryDto) response.getPayload();
            System.out.println("Generated army: units=" + army.getUnitCount() + ", attack score=" + army.getAttackScore());
            army.getUnits().forEach(line -> System.out.println("  " + line));
        }
    }

    /**
     * Requests a village defense test against generated armies.
     *
     * @throws IOException if socket communication fails
     * @throws ClassNotFoundException if the response cannot be decoded
     */
    private void testVillage() throws IOException, ClassNotFoundException {
        System.out.print("How many generated armies should test the village? ");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("rounds", scanner.nextLine().trim());
        GameResponse response = client.send(RequestType.TEST_VILLAGE, parameters);
        printResponse(response);
        if (response.isSuccess()) {
            VillageDefenseTestResultDto result = (VillageDefenseTestResultDto) response.getPayload();
            System.out.println("Village defense test score: "
                    + result.getSuccessfulDefenses() + "/" + result.getTotalTests()
                    + " defended (" + String.format("%.1f", result.getDefenseSuccessRate()) + "%)");
            result.getRoundSummaries().forEach(line -> System.out.println("  " + line));
        }
    }

    /**
     * Prints a server response message and any server-side game events.
     *
     * @param response the server response to display
     */
    private void printResponse(GameResponse response) {
        if (response.getMessage() != null && !response.getMessage().isBlank()) {
            System.out.println(response.getMessage());
        }
        response.getEvents().forEach(event -> System.out.println("[Event] " + event));
    }
}
