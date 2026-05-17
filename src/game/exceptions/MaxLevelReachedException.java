/**
 * Thrown when an upgrade is attempted on an entity
 * that has already reached its maximum level.
 */
package game.exceptions;

public class MaxLevelReachedException extends GameException {
    public MaxLevelReachedException(String message) {
        super(message);
    }
}