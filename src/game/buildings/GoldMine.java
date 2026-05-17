/**
 * Production building that generates GOLD resources.
 *
 * Upgrades increase gold production rate.
 */
package game.buildings;

import game.model.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class GoldMine extends ProductionBuilding {
    public GoldMine() {
        super("Gold Mine", 1, 130, 5, 15);
    }

    @Override
    public Map<ResourceType, Integer> getBuildCost() {
        Map<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.WOOD, 30);
        cost.put(ResourceType.IRON, 20);
        return cost;
    }
}