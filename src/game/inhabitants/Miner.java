/**
 * Represents a miner inhabitant.
 *
 * Miners contribute to resource production
 * and have a defined production capacity.
 */
package game.inhabitants;

public class Miner extends Inhabitant {
    private final int capacity;

    public Miner(int id) {
        super(id, "Miner", 1, 5);
        this.capacity = 12;
    }

    public int getCapacity() {
        return capacity;
    }
}