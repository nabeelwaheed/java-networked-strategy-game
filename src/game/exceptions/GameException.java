/**
 * Base exception class for all custom game-related exceptions.
 *
 * Provides a common superclass for domain-specific error handling.
 */
package game.exceptions;

public class GameException extends Exception {
  public GameException(String message) {
    super(message);
  }
}