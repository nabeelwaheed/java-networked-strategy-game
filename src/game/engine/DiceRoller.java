/**
 * Utility class responsible for generating randomness in the game.
 *
 * DiceRoller provides methods for:
 * - Random integer generation within a range
 * - Probability-based success evaluation
 *
 * It is used during combat resolution and village generation
 * to introduce non-deterministic behavior.
 */
package game.engine;

import java.util.Random;

public class DiceRoller {
    private final Random random = new Random();

    /**
     * Returns a random integer within the given inclusive range.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return a random integer between min and max
     */
    public int roll(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Evaluates whether a probability-based event succeeds.
     *
     * @param probability the probability of success, typically between 0.0 and 1.0
     * @return true if the event succeeds, otherwise false
     */
    public boolean chance(double probability) {
        return random.nextDouble() < probability;
    }
}