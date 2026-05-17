/**
 * Thrown when attempting to train an inhabitant
 * beyond the village population capacity.
 */
package game.exceptions;

public class PopulationLimitExceededException extends GameException {
    public PopulationLimitExceededException(String message) {
        super(message);
    }
}