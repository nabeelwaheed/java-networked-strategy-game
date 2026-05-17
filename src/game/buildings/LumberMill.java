/**
 * Production building that generates WOOD resources.
 *
 * Upgrades increase wood production rate.
 */
package game.buildings;

import game.model.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class LumberMill extends ProductionBuilding {
    public LumberMill() {
        super("Lumber Mill", 1, 130, 5, 12);
    }

    @Override
    public Map<ResourceType, Integer> getBuildCost() {
        Map<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.WOOD, 15);
        return cost;
    }
}