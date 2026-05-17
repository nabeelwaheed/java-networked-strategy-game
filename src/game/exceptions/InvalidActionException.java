/**
 * Thrown when an invalid or unsupported action
 * is requested in the game.
 */
package game.exceptions;

public class InvalidActionException extends GameException {
    public InvalidActionException(String message) {
        super(message);
    }
}