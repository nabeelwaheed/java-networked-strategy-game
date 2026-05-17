/**
 * Thrown when a player attempts to perform an action
 * without sufficient resources.
 */
package game.exceptions;

public class NotEnoughResourcesException extends GameException {
  public NotEnoughResourcesException(String message) {
    super(message);
  }
}