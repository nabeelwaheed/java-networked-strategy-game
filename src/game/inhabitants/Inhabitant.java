/**
 * Abstract base class for all inhabitants in the village.
 *
 * Contains shared attributes such as:
 * - ID
 * - Level
 * - Maximum level
 *
 * Implements the Upgradeable interface.
 */
package game.inhabitants;

import game.exceptions.MaxLevelReachedException;
import game.exceptions.NotEnoughResourcesException;
import game.model.ResourceStorage;
import game.model.ResourceType;
import game.model.Upgradeable;

public abstract class Inhabitant implements Upgradeable {
    private final int id;
    private final String name;
    private int level;
    private final int maxLevel;

    protected Inhabitant(int id, String name, int level, int maxLevel) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.maxLevel = maxLevel;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getLevel() {
        return level;
    }

    /**
     * Determines whether the inhabitant can be upgraded further.
     *
     * @return true if the level is below the maximum level
     */
    @Override
    public boolean canUpgrade() {
        return level < maxLevel;
    }

    /**
     * Upgrades the inhabitant if sufficient resources are available
     * and the maximum level has not been reached.
     *
     * @param storage the village resource storage used to pay upgrade costs
     * @throws MaxLevelReachedException if the inhabitant is already at max level
     * @throws NotEnoughResourcesException if required resources are unavailable
     */
    @Override
    public void upgrade(ResourceStorage storage) throws MaxLevelReachedException, NotEnoughResourcesException {
        if (!canUpgrade()) {
            throw new MaxLevelReachedException(name + " is already max level.");
        }
        storage.spend(ResourceType.GOLD, getUpgradeCost(ResourceType.GOLD));
        storage.spend(ResourceType.IRON, getUpgradeCost(ResourceType.IRON));
        storage.spend(ResourceType.WOOD, getUpgradeCost(ResourceType.WOOD));
        level++;
        onUpgrade();
    }

    /**
     * Restores the inhabitant state from persisted data without charging costs.
     *
     * @param restoredLevel the persisted level
     */
    public void restoreState(int restoredLevel) {
        if (restoredLevel < 1 || restoredLevel > maxLevel) {
            throw new IllegalArgumentException("Invalid restored level for " + name + ".");
        }
        while (level < restoredLevel) {
            level++;
            onUpgrade();
        }
    }

    protected void onUpgrade() {
        // optional override
    }

    @Override
    public int getUpgradeCost(ResourceType type) {
        int base = 10 * (level + 1);
        return switch (type) {
            case GOLD -> base;
            case IRON -> base / 2;
            case WOOD -> base / 2;
        };
    }

    @Override
    public String toString() {
        return name + "#" + id + " (Lv." + level + ")";
    }
}
