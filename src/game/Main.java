/**
 * Muhammad Nabeel Waheed
 * Kev Akpinar
 */
/**
 * Entry point of the Village War Strategy Game.
 *
 * This class initializes the Assignment 3 controller and launches
 * the console user interface. It also defines the XML save file
 * used by the persistence layer.
 */
package game;

import game.controller.GameController;
import game.ui.ConsoleUI;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    /**
     * Starts the application by creating the controller and launching
     * the console-based user interface.
     *
     */
    public static void main(String... args) {
        Path savePath = Path.of("game-data", "village-state.xml");
        try {
            GameController controller = GameController.create(savePath, "PlayerOne");
            ConsoleUI ui = new ConsoleUI(controller);
            ui.start();
        } catch (IOException e) {
            System.out.println("Unable to start the game: " + e.getMessage());
        }
    }
}
