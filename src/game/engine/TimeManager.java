/**
 * Manages simulated game time progression.
 *
 * TimeManager tracks the total elapsed game hours and allows
 * time advancement when actions such as building, training,
 * upgrading, or attacking occur.
 *
 * This abstraction allows future extension to real-time or
 * scheduled task systems.
 */
package game.engine;

public class TimeManager {
    private int hours;

    public TimeManager() {
        this.hours = 0;
    }

    public TimeManager(int initialHours) {
        if (initialHours < 0) {
            throw new IllegalArgumentException("Time cannot be negative.");
        }
        this.hours = initialHours;
    }

    /**
     * Advances the simulated game clock by the specified number of hours.
     *
     * @param delta the number of hours to add
     * @throws IllegalArgumentException if delta is negative
     */
    public void advanceHours(int delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("Time cannot be negative.");
        }
        hours += delta;
    }

    public int getHours() {
        return hours;
    }
}
