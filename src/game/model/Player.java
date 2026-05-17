/**
 * Represents a game player.
 *
 * A Player owns exactly one Village and maintains
 * identifying information such as username.
 *
 * This abstraction allows future support for multiplayer extensions.
 */
package game.model;

public class Player {
    private final String username;
    private final Village village;

    public Player(String username, Village village) {
        this.username = username;
        this.village = village;
    }

    public String getUsername() {
        return username;
    }

    public Village getVillage() {
        return village;
    }
}