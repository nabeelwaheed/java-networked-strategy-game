/**
 * Defensive structure that deals ranged damage to attacking armies.
 *
 * Contributes to village defense score.
 */
package game.buildings;

import game.model.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class ArcherTower extends DefenseBuilding {
    public ArcherTower() {
        super("Archer Tower", 1, 180, 5, 20, 4);
    }

    @Override
    public Map<ResourceType, Integer> getBuildCost() {
        Map<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.WOOD, 40);
        cost.put(ResourceType.IRON, 15);
        return cost;
    }
}