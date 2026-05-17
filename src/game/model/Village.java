/**
 * Represents a player's village.
 *
 * Village maintains:
 * - Buildings
 * - Inhabitants
 * - Army
 * - Resource storage
 * - Trophy ranking
 *
 * It encapsulates population capacity, defense calculation,
 * and resource collection logic.
 */
package game.model;

import game.buildings.Building;
import game.buildings.DefenseBuilding;
import game.buildings.Farm;
import game.buildings.GoldMine;
import game.buildings.IronMine;
import game.buildings.LumberMill;
import game.buildings.ProductionBuilding;
import game.buildings.StorageBuilding;
import game.inhabitants.Inhabitant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Village {
    private static final int MAX_BUILDINGS = 20;
    private final int villageId;
    private final String name;
    private final List<Building> buildings;
    private final List<Inhabitant> inhabitants;
    private final Army army;
    private final ResourceStorage resources;
    private int trophies;
    private boolean guardActive;
    private int guardRemainingHours;

    public Village(int villageId, String name) {
        this.villageId = villageId;
        this.name = name;
        this.buildings = new ArrayList<>();
        this.inhabitants = new ArrayList<>();
        this.army = new Army();
        this.resources = new ResourceStorage(500, 500, 500);
        this.trophies = 0;
        this.guardActive = false;
    }

    public int getVillageId() {
        return villageId;
    }

    public String getName() {
        return name;
    }

    public List<Building> getBuildings() {
        return Collections.unmodifiableList(buildings);
    }

    public List<Inhabitant> getInhabitants() {
        return Collections.unmodifiableList(inhabitants);
    }

    public Army getArmy() {
        return army;
    }

    public ResourceStorage getResources() {
        return resources;
    }

    public int getTrophies() {
        return trophies;
    }

    public void setTrophies(int trophies) {
        this.trophies = trophies;
    }

    public void addTrophies(int value) {
        trophies += value;
    }

    /**
     * Indicates whether the village is currently under guard protection.
     *
     * @return true if guard mode is active
     */
    public boolean isGuardActive() {
        return guardActive;
    }

    public void setGuardActive(boolean guardActive) {
        this.guardActive = guardActive;
        if (!guardActive) {
            guardRemainingHours = 0;
        }
    }

    public int getGuardRemainingHours() {
        return guardRemainingHours;
    }

    public void activateGuard(int hours) {
        guardActive = hours > 0;
        guardRemainingHours = Math.max(0, hours);
    }

    public void advanceGuardTime(int hours) {
        if (!guardActive || hours <= 0) {
            return;
        }
        guardRemainingHours = Math.max(0, guardRemainingHours - hours);
        if (guardRemainingHours == 0) {
            guardActive = false;
        }
    }

    /**
     * Adds a building to the village and updates resource capacity
     * if the building is a storage structure.
     *
     * @param building the building to add
     */
    public void addBuilding(Building building) {
        buildings.add(building);
        if (building instanceof StorageBuilding storageBuilding) {
            int updatedCapacity = resources.getCapacity(storageBuilding.getResourceType()) + storageBuilding.getCapacity();
            resources.setCapacity(storageBuilding.getResourceType(), updatedCapacity);
        }
    }

    public boolean canBuildMore() {
        return buildings.size() < MAX_BUILDINGS;
    }

    /**
     * Adds an inhabitant to the village population.
     *
     * @param inhabitant the inhabitant to add
     */
    public void addInhabitant(Inhabitant inhabitant) {
        inhabitants.add(inhabitant);
    }

    /**
     * Calculates the maximum supported population of the village
     * based on the total capacity provided by all Farm buildings.
     *
     * @return the village population capacity
     */
    public int getPopulationCap() {
        return buildings.stream()
                .filter(Farm.class::isInstance)
                .map(Farm.class::cast)
                .mapToInt(Farm::getPopulationSupported)
                .sum();
    }

    public int getCurrentPopulation() {
        return inhabitants.size();
    }

    public boolean hasPopulationSpace() {
        return getCurrentPopulation() < getPopulationCap();
    }

    /**
     * Calculates the village defense score.
     *
     * The defense score is based on defensive buildings as well as
     * supporting contribution from inhabitants.
     *
     * @return the total defense score of the village
     */
    public int calculateDefenseScore() {
        int buildingDefense = buildings.stream()
                .filter(DefenseBuilding.class::isInstance)
                .map(DefenseBuilding.class::cast)
                .mapToInt(d -> d.getDamage() * d.getLevel())
                .sum();

        int supportDefense = inhabitants.stream()
                .mapToInt(i -> i.getLevel() * 2)
                .sum();

        int villageSizeBonus = buildings.stream()
                .mapToInt(Building::getLevel)
                .sum();

        return buildingDefense + supportDefense + villageSizeBonus;
    }

    /**
     * Collects resources produced by the village's production buildings
     * and adds them to the village resource storage.
     *
     * Resource generation is calculated using the production rates of
     * gold mines, iron mines, and lumber mills.
     */
    public void collectResources() {
        int goldGain = buildings.stream()
                .filter(GoldMine.class::isInstance)
                .map(ProductionBuilding.class::cast)
                .mapToInt(ProductionBuilding::getProductionRate)
                .sum();

        int ironGain = buildings.stream()
                .filter(IronMine.class::isInstance)
                .map(ProductionBuilding.class::cast)
                .mapToInt(ProductionBuilding::getProductionRate)
                .sum();

        int woodGain = buildings.stream()
                .filter(LumberMill.class::isInstance)
                .map(ProductionBuilding.class::cast)
                .mapToInt(ProductionBuilding::getProductionRate)
                .sum();

        resources.add(ResourceType.GOLD, goldGain);
        resources.add(ResourceType.IRON, ironGain);
        resources.add(ResourceType.WOOD, woodGain);
    }
}
