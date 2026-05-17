/**
 * Abstraction for saving and loading the state of the game.
 *
 * Different repository implementations may persist the game
 * using formats such as XML.
 */
package game.persistence;

import java.io.IOException;
import java.util.Optional;

public interface GameStateRepository {
    /**
     * Loads a previously saved game state if one exists.
     *
     * @return an optional containing the saved game state
     * @throws IOException if the saved state cannot be read
     */
    Optional<GameState> load() throws IOException;

    /**
     * Saves the current game state to persistent storage.
     *
     * @param state the current game state snapshot
     * @throws IOException if the state cannot be written
     */
    void save(GameState state) throws IOException;
}
