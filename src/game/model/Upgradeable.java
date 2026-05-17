/**
 * Interface representing entities that can be upgraded.
 *
 * Implemented by both Building and Inhabitant.
 *
 * Provides a unified mechanism for:
 * - Checking upgrade eligibility
 * - Executing upgrade logic
 * - Retrieving upgrade costs
 */
package game.model;

import game.exceptions.MaxLevelReachedException;
import game.exceptions.NotEnoughResourcesException;

public interface Upgradeable {
    boolean canUpgrade();
    void upgrade(ResourceStorage storage) throws MaxLevelReachedException, NotEnoughResourcesException;
    int getUpgradeCost(ResourceType type);
    int getLevel();
}