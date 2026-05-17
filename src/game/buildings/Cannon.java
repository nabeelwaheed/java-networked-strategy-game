/**
 * Defensive structure that deals high damage to attacking armies.
 *
 * Contributes significantly to village defense score.
 */
package game.buildings;

import game.model.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class Cannon extends DefenseBuilding {
    public Cannon() {
        super("Cannon", 1, 200, 5, 30, 3);
    }

    @Override
    public Map<ResourceType, Integer> getBuildCost() {
        Map<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.WOOD, 35);
        cost.put(ResourceType.IRON, 25);
        return cost;
    }
}