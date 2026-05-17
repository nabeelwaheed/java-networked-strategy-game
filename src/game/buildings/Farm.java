/**
 * Represents a farm building.
 *
 * Farms increase population capacity.
 * Upgrading a farm increases the number of inhabitants supported.
 */
package game.buildings;

import game.model.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class Farm extends Building {
    private int populationSupported;

    public Farm() {
        super("Farm", 1, 120, 5);
        this.populationSupported = 5;
    }

    /**
     * Returns the amount of population this farm can support.
     *
     * @return the supported population capacity
     */
    public int getPopulationSupported() {
        return populationSupported;
    }

    @Override
    protected void onUpgrade() {
        populationSupported += 3;
    }

    @Override
    public Map<ResourceType, Integer> getBuildCost() {
        Map<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.WOOD, 25);
        return cost;
    }
}