/**
 * Abstract subclass of Building representing resource storage structures.
 *
 * Stores capacity information for a specific ResourceType.
 * Upgrades increase storage capacity.
 */
package game.buildings;

import game.model.ResourceType;

public abstract class StorageBuilding extends Building {
    private final ResourceType resourceType;
    private int capacity;

    protected StorageBuilding(String name, int level, int hitPoints, int maxLevel, ResourceType resourceType, int capacity) {
        super(name, level, hitPoints, maxLevel);
        this.resourceType = resourceType;
        this.capacity = capacity;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    protected void onUpgrade() {
        capacity += 100;
    }
}