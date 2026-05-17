/**
 * Storage building for GOLD resources.
 *
 * Upgrades increase maximum gold storage capacity.
 */
package game.buildings;

import game.model.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class GoldStorage extends StorageBuilding {
    public GoldStorage() {
        super("Gold Storage", 1, 150, 5, ResourceType.GOLD, 500);
    }

    @Override
    public Map<ResourceType, Integer> getBuildCost() {
        Map<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.WOOD, 30);
        return cost;
    }
}