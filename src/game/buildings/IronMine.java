/**
 * Production building that generates IRON resources.
 *
 * Upgrades increase iron production rate.
 */
package game.buildings;

import game.model.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class IronMine extends ProductionBuilding {
    public IronMine() {
        super("Iron Mine", 1, 130, 5, 12);
    }

    @Override
    public Map<ResourceType, Integer> getBuildCost() {
        Map<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.WOOD, 25);
        return cost;
    }
}