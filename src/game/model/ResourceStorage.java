/**
 * Manages village resource quantities and capacities.
 *
 * Resources are stored using a Map<ResourceType, Integer>
 * to ensure type safety and extensibility.
 *
 * This class enforces capacity limits and validates spending logic.
 */
package game.model;

import game.exceptions.NotEnoughResourcesException;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class ResourceStorage {
    private final Map<ResourceType, Integer> resources;
    private final Map<ResourceType, Integer> capacities;

    public ResourceStorage(int goldCap, int ironCap, int woodCap) {
        resources = new EnumMap<>(ResourceType.class);
        capacities = new EnumMap<>(ResourceType.class);
        for (ResourceType type : ResourceType.values()) {
            resources.put(type, 0);
        }
        capacities.put(ResourceType.GOLD, goldCap);
        capacities.put(ResourceType.IRON, ironCap);
        capacities.put(ResourceType.WOOD, woodCap);
    }

    public int get(ResourceType type) {
        return resources.getOrDefault(type, 0);
    }

    public int getCapacity(ResourceType type) {
        return capacities.getOrDefault(type, Integer.MAX_VALUE);
    }

    /**
     * Adds a quantity of a resource to storage without exceeding capacity.
     *
     * @param type the resource type to add
     * @param amount the amount to add
     */
    public void add(ResourceType type, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        int current = get(type);
        int capacity = capacities.getOrDefault(type, Integer.MAX_VALUE);
        resources.put(type, Math.min(current + amount, capacity));
    }

    /**
     * Spends a quantity of a resource from storage.
     *
     * @param type the resource type to spend
     * @param amount the amount to spend
     * @throws NotEnoughResourcesException if insufficient resources are available
     */
    public void spend(ResourceType type, int amount) throws NotEnoughResourcesException {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        int current = get(type);
        if (current < amount) {
            throw new NotEnoughResourcesException("Not enough " + type + ". Needed: " + amount + ", available: " + current);
        }
        resources.put(type, current - amount);
    }

    /**
     * Deducts all resource costs required for an action after first verifying
     * that the village has sufficient amounts of every required resource.
     *
     * @param cost a map of resource costs
     * @throws NotEnoughResourcesException if any required resource is insufficient
     */
    public void spendAll(Map<ResourceType, Integer> cost) throws NotEnoughResourcesException {
        for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
            if (get(entry.getKey()) < entry.getValue()) {
                throw new NotEnoughResourcesException("Not enough resources for purchase.");
            }
        }
        for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
            spend(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Sets the maximum storage capacity for a given resource type.
     *
     * If the current stored amount exceeds the new capacity, it is reduced
     * to match the allowed maximum.
     *
     * @param type the resource type
     * @param capacity the new maximum capacity
     */
    public void setCapacity(ResourceType type, int capacity) {
        capacities.put(type, capacity);
        resources.put(type, Math.min(resources.get(type), capacity));
    }

    public void set(ResourceType type, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        resources.put(type, Math.min(amount, getCapacity(type)));
    }

    public Map<ResourceType, Integer> snapshot() {
        return Collections.unmodifiableMap(resources);
    }
}
