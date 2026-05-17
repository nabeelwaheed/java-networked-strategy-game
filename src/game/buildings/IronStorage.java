/**
 * Storage building for IRON resources.
 *
 * Upgrades increase maximum iron storage capacity.
 */
package game.buildings;

import game.model.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class IronStorage extends StorageBuilding {
    public IronStorage() {
        super("Iron Storage", 1, 150, 5, ResourceType.IRON, 500);
    }

    @Override
    public Map<ResourceType, Integer> getBuildCost() {
        Map<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.WOOD, 30);
        return cost;
    }
}