/**
 * Storage building for WOOD resources.
 *
 * Upgrades increase maximum wood storage capacity.
 */
package game.buildings;

import game.model.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class LumberStorage extends StorageBuilding {
    public LumberStorage() {
        super("Lumber Storage", 1, 150, 5, ResourceType.WOOD, 500);
    }

    @Override
    public Map<ResourceType, Integer> getBuildCost() {
        Map<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.WOOD, 20);
        return cost;
    }
}