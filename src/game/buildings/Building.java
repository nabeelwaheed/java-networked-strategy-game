/**
 * Abstract base class for all building types.
 *
 * Defines common attributes such as:
 * - Level
 * - Hit points
 * - Maximum level
 *
 * Implements the Upgradeable interface.
 * Specialized building types extend this class.
 */
package game.buildings;

import game.exceptions.MaxLevelReachedException;
import game.exceptions.NotEnoughResourcesException;
import game.model.ResourceStorage;
import game.model.ResourceType;
import game.model.Upgradeable;

import java.util.EnumMap;
import java.util.Map;

public abstract class Building implements Upgradeable {
    private final String name;
    private int level;
    private int hitPoints;
    private final int maxLevel;

    protected Building(String name, int level, int hitPoints, int maxLevel) {
        this.name = name;
        this.level = level;
        this.hitPoints = hitPoints;
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getLevel() {
        return level;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * Reduces the building's hit points by the specified damage amount.
     *
     * Hit points will not drop below zero.
     *
     * @param damage the damage dealt to the building
     */
    public void takeDamage(int damage) {
        hitPoints = Math.max(0, hitPoints - damage);
    }

    /**
     * Determines whether the building can still be upgraded.
     *
     * @return true if the building level is below its maximum level
     */
    @Override
    public boolean canUpgrade() {
        return level < maxLevel;
    }

    /**
     * Upgrades the building if it has not reached its maximum level and
     * sufficient resources are available.
     *
     * The upgrade increases building level and hit points and may trigger
     * subclass-specific upgrade behavior.
     *
     * @param storage the village resource storage used to pay upgrade costs
     * @throws MaxLevelReachedException if the building is already at max level
     * @throws NotEnoughResourcesException if required resources are unavailable
     */
    @Override
    public void upgrade(ResourceStorage storage) throws MaxLevelReachedException, NotEnoughResourcesException {
        if (!canUpgrade()) {
            throw new MaxLevelReachedException(name + " is already at max level.");
        }
        Map<ResourceType, Integer> cost = getUpgradeCostMap();
        storage.spendAll(cost);
        level++;
        hitPoints += 25;
        onUpgrade();
    }

    /**
     * Restores the building state from persisted data without charging costs.
     *
     * @param restoredLevel the persisted level
     * @param restoredHitPoints the persisted hit points
     */
    public void restoreState(int restoredLevel, int restoredHitPoints) {
        if (restoredLevel < 1 || restoredLevel > maxLevel) {
            throw new IllegalArgumentException("Invalid restored level for " + name + ".");
        }
        while (level < restoredLevel) {
            level++;
            onUpgrade();
        }
        hitPoints = restoredHitPoints;
    }


    protected void onUpgrade() {
        // optional override
    }

    public abstract Map<ResourceType, Integer> getBuildCost();

    protected Map<ResourceType, Integer> getUpgradeCostMap() {
        Map<ResourceType, Integer> map = new EnumMap<>(ResourceType.class);
        map.put(ResourceType.GOLD, getUpgradeCost(ResourceType.GOLD));
        map.put(ResourceType.IRON, getUpgradeCost(ResourceType.IRON));
        map.put(ResourceType.WOOD, getUpgradeCost(ResourceType.WOOD));
        return map;
    }

    @Override
    public int getUpgradeCost(ResourceType type) {
        int base = 20 * (level + 1);
        return switch (type) {
            case GOLD -> base;
            case IRON -> base / 2;
            case WOOD -> base / 2;
        };
    }

    @Override
    public String toString() {
        return name + " (Lv." + level + ", HP=" + hitPoints + ")";
    }
}
