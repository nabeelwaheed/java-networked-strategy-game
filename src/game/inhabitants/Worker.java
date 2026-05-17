/**
 * Represents a worker inhabitant.
 *
 * Workers can assist in construction and may be
 * assigned to tasks. Upgrades improve efficiency.
 */
package game.inhabitants;

public class Worker extends Inhabitant {
    private boolean available;
    private int efficiency;

    public Worker(int id) {
        super(id, "Worker", 1, 5);
        this.available = true;
        this.efficiency = 10;
    }

    public boolean isAvailable() {
        return available;
    }

    /**
     * Marks the worker as currently assigned to a task.
     */
    public void assignWork() {
        available = false;
    }

    /**
     * Marks the worker as available after completing assigned work.
     */
    public void finishWork() {
        available = true;
    }

    public int getEfficiency() {
        return efficiency;
    }

    @Override
    protected void onUpgrade() {
        efficiency += 3;
    }
}