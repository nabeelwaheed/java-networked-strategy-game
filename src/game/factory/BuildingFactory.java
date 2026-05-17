/**
 * Factory interface for creating building objects.
 *
 * This abstraction hides direct building instantiation from
 * the rest of the game and supports the Assignment 3 factory design.
 */
package game.factory;

import game.buildings.Building;
import game.buildings.BuildingType;

public interface BuildingFactory {
    /**
     * Creates a building matching the requested type.
     *
     * @param type the building type to instantiate
     * @return the created building object
     */
    Building create(BuildingType type);
}
