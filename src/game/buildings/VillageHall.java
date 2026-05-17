/**
 * Represents the central building of a village.
 *
 * The VillageHall may determine upgrade limits
 * and progression mechanics in future extensions.
 */
package game.buildings;

import game.model.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class VillageHall extends Building {
    public VillageHall() {
        super("Village Hall", 1, 300, 5);
    }

    @Override
    public Map<ResourceType, Integer> getBuildCost() {
        return new EnumMap<>(ResourceType.class);
    }
}