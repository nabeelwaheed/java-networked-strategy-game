/**
 * Factory interface for creating inhabitant objects.
 *
 * It defines a common creation point for workers, non-combat
 * villagers, and army units used throughout the game.
 */
package game.factory;

import game.inhabitants.Inhabitant;
import game.inhabitants.InhabitantType;

public interface InhabitantFactory {
    /**
     * Creates an inhabitant of the requested type and ID.
     *
     * @param type the inhabitant type to instantiate
     * @param id the unique identifier assigned to the new inhabitant
     * @return the created inhabitant object
     */
    Inhabitant create(InhabitantType type, int id);
}
