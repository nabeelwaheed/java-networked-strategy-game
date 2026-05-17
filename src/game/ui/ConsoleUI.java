/**
 * Console-based user interface for the Village War Strategy Game.
 *
 * This class handles all interaction with the player through standard input
 * and output. It displays menus, collects user choices, and delegates game
 * actions to the GameController.
 *
 * ConsoleUI is responsible only for presentation and input handling, while
 * the controller and engine manage game rules and persistence behavior.
 */
package game.ui;

import game.buildings.Building;
import game.buildings.BuildingType;
import game.combat.AttackResult;
import game.controller.GameController;
import game.exceptions.GameException;
import game.inhabitants.Inhabitant;
import game.inhabitants.InhabitantType;
import game.model.Village;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final GameController controller;
    private final Scanner scanner;

    public ConsoleUI(GameController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the main user interaction loop.
     *
     * This method first shows the persistence menu, then repeatedly displays
     * the game menu, reads the user's input, and delegates actions until exit.
     */
    public void start() {
        if (!showStartupMenu()) {
            System.out.println("Goodbye.");
            return;
        }

        boolean running = true;
        while (running) {
            printPendingEvents();
            printMenu();
            String input = scanner.nextLine().trim();
            try {
                switch (input) {
                    case "1" -> showVillageStatus();
                    case "2" -> buildMenu();
                    case "3" -> trainMenu();
                    case "4" -> upgradeMenu();
                    case "5" -> exploreAndAttack();
                    case "6" -> advanceTimeMenu();
                    case "7" -> saveNow();
                    case "8" -> loadGame();
                    case "9" -> newGame();
                    case "0" -> running = false;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (GameException e) {
                System.out.println("Action failed: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Persistence failed: " + e.getMessage());
            }
        }
        try {
            controller.saveGame();
            System.out.println("Game saved.");
        } catch (IOException e) {
            System.out.println("Could not save game: " + e.getMessage());
        }
        System.out.println("Goodbye.");
    }

    private void printMenu() {
        System.out.println("\n=== Village War Menu ===");
        System.out.println("Time: " + controller.getTime() + " hours");
        System.out.println("1. Show village status");
        System.out.println("2. Build");
        System.out.println("3. Train inhabitant/unit");
        System.out.println("4. Upgrade");
        System.out.println("5. Explore villages and attack");
        System.out.println("6. Advance time");
        System.out.println("7. Save now");
        System.out.println("8. Load game");
        System.out.println("9. New game");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    /**
     * Displays the persistence options shown before the main game starts.
     *
     * The player may load a previous session, begin a new game,
     * or exit the application.
     *
     * @return true if gameplay should continue, otherwise false
     */
    private boolean showStartupMenu() {
        while (true) {
            System.out.println("\n=== Persistence Menu ===");
            System.out.println("1. Load game");
            System.out.println("2. New game");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String input = scanner.nextLine().trim();
            try {
                switch (input) {
                    case "1" -> {
                        boolean loaded = controller.loadGame();
                        if (loaded) {
                            System.out.println("Saved game loaded.");
                            return true;
                        }
                        System.out.println("No save file found. Start a new game instead.");
                    }
                    case "2" -> {
                        controller.newGame();
                        System.out.println("New game started.");
                        return true;
                    }
                    case "0" -> {
                        return false;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (IOException e) {
                System.out.println("Persistence failed: " + e.getMessage());
            }
        }
    }

    /**
     * Displays the current state of the player's village, including resources,
     * buildings, inhabitants, army size, and combat scores.
     */
    private void showVillageStatus() {
        Village village = controller.getPlayer().getVillage();
        System.out.println("\nVillage: " + village.getName());
        System.out.println("Trophies: " + village.getTrophies());
        System.out.println("Population: " + village.getCurrentPopulation() + "/" + village.getPopulationCap());
        System.out.println("Guard: " + (village.isGuardActive() ? village.getGuardRemainingHours() + " hours remaining" : "inactive"));
        System.out.println("Resources: " + village.getResources().snapshot());

        System.out.println("Buildings:");
        List<Building> buildings = village.getBuildings();
        for (int i = 0; i < buildings.size(); i++) {
            System.out.println("  [" + i + "] " + buildings.get(i));
        }

        System.out.println("Inhabitants:");
        List<Inhabitant> inhabitants = village.getInhabitants();
        for (int i = 0; i < inhabitants.size(); i++) {
            System.out.println("  [" + i + "] " + inhabitants.get(i));
        }

        System.out.println("Army size: " + village.getArmy().size());
        System.out.println("Army attack score: " + village.getArmy().calculateAttackScore());
        System.out.println("Village defense score: " + village.calculateDefenseScore());
    }

    /**
     * Displays available building options and processes the player's
     * building choice through the GameEngine.
     *
     * @throws GameException if the selected building action fails
     */
    private void buildMenu() throws GameException {
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
        switch (input) {
            case "1" -> controller.build(BuildingType.FARM);
            case "2" -> controller.build(BuildingType.GOLD_MINE);
            case "3" -> controller.build(BuildingType.IRON_MINE);
            case "4" -> controller.build(BuildingType.LUMBER_MILL);
            case "5" -> controller.build(BuildingType.GOLD_STORAGE);
            case "6" -> controller.build(BuildingType.IRON_STORAGE);
            case "7" -> controller.build(BuildingType.LUMBER_STORAGE);
            case "8" -> controller.build(BuildingType.ARCHER_TOWER);
            case "9" -> controller.build(BuildingType.CANNON);
            default -> System.out.println("Invalid build choice.");
        }
    }

    /**
     * Displays available training options and processes the player's
     * choice to train inhabitants or army units.
     *
     * @throws GameException if the training action fails
     */
    private void trainMenu() throws GameException {
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
        switch (input) {
            case "1" -> controller.train(InhabitantType.WORKER);
            case "2" -> controller.train(InhabitantType.MINER);
            case "3" -> controller.train(InhabitantType.COLLECTOR);
            case "4" -> controller.train(InhabitantType.SOLDIER);
            case "5" -> controller.train(InhabitantType.ARCHER);
            case "6" -> controller.train(InhabitantType.KNIGHT);
            case "7" -> controller.train(InhabitantType.CATAPULT);
            default -> System.out.println("Invalid training choice.");
        }
    }

    /**
     * Allows the player to choose whether to upgrade a building
     * or an inhabitant, then delegates the selected upgrade action
     * to the GameEngine.
     *
     * @throws GameException if the upgrade action fails
     */
    private void upgradeMenu() throws GameException {
        System.out.println("Upgrade (1) Building or (2) Inhabitant?");
        String choice = scanner.nextLine().trim();
        if ("1".equals(choice)) {
            showVillageStatus();
            System.out.print("Building index: ");
            int index = Integer.parseInt(scanner.nextLine().trim());
            controller.upgradeBuilding(index);
        } else if ("2".equals(choice)) {
            showVillageStatus();
            System.out.print("Inhabitant index: ");
            int index = Integer.parseInt(scanner.nextLine().trim());
            controller.upgradeInhabitant(index);
        } else {
            System.out.println("Invalid option.");
        }
    }

    /**
     * Displays generated enemy villages, allows the player to choose
     * one to attack, and prints the resulting combat outcome.
     */
    private void exploreAndAttack() throws GameException {
        List<Village> options = controller.exploreVillages();
        System.out.println("\nAvailable villages to attack:");
        for (int i = 0; i < options.size(); i++) {
            Village village = options.get(i);
            System.out.println("[" + i + "] " + village.getName()
                    + " | Defense=" + village.calculateDefenseScore()
                    + " | Army=" + village.getArmy().calculateAttackScore()
                    + " | Resources=" + village.getResources().snapshot()
                    + (village.isGuardActive() ? " | Guard Active" : ""));
        }
        System.out.print("Choose village index (-1 to cancel): ");
        int index = Integer.parseInt(scanner.nextLine().trim());
        if (index >= 0 && index < options.size()) {
            AttackResult result = controller.attack(options.get(index));
            System.out.println(result);
        }
    }

    private void advanceTimeMenu() {
        System.out.print("Advance how many hours? ");
        int hours = Integer.parseInt(scanner.nextLine().trim());
        controller.advanceTime(hours);
    }

    private void printPendingEvents() {
        List<String> events = controller.consumeEvents();
        for (String event : events) {
            System.out.println("[Event] " + event);
        }
    }

    /**
     * Saves the current game session immediately.
     *
     * @throws IOException if the save file cannot be written
     */
    private void saveNow() throws IOException {
        controller.saveGame();
        System.out.println("Game saved.");
    }

    /**
     * Loads the saved game session and replaces the current in-memory session.
     *
     * @throws IOException if the save file cannot be read
     */
    private void loadGame() throws IOException {
        boolean loaded = controller.loadGame();
        if (loaded) {
            System.out.println("Saved game loaded.");
        } else {
            System.out.println("No save file found. Current session unchanged.");
        }
    }

    /**
     * Starts a fresh in-memory game session after asking for confirmation.
     */
    private void newGame() {
        System.out.print("Start a new game and replace the current in-memory session? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        if ("y".equals(input) || "yes".equals(input)) {
            controller.newGame();
            System.out.println("New game started.");
        } else {
            System.out.println("New game cancelled.");
        }
    }
}
