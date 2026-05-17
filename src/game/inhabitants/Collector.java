/**
 * Represents a collector inhabitant.
 *
 * Collectors assist in gathering or managing resources.
 */
package game.inhabitants;

public class Collector extends Inhabitant {
    private final int capacity;

    public Collector(int id) {
        super(id, "Collector", 1, 5);
        this.capacity = 12;
    }

    public int getCapacity() {
        return capacity;
    }
}