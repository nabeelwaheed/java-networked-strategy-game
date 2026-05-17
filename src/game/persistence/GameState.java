/**
 * State of the current game session.
 *
 * GameState groups together the values needed to save and restore
 * the player's progress, including village data, time, and ID counters.
 */
package game.persistence;

import game.model.Village;

public class GameState {
    private final String username;
    private final Village village;
    private final int timeHours;
    private final int nextInhabitantId;

    public GameState(String username, Village village, int timeHours, int nextInhabitantId) {
        this.username = username;
        this.village = village;
        this.timeHours = timeHours;
        this.nextInhabitantId = nextInhabitantId;
    }

    public String getUsername() {
        return username;
    }

    public Village getVillage() {
        return village;
    }

    public int getTimeHours() {
        return timeHours;
    }

    public int getNextInhabitantId() {
        return nextInhabitantId;
    }
}
