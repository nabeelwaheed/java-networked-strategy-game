/**
 * Serializable DTO describing the current state of a player's village.
 */
package game.network.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VillageStatusDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String villageName;
    private final int trophies;
    private final int currentPopulation;
    private final int populationCap;
    private final boolean guardActive;
    private final int guardRemainingHours;
    private final Map<String, Integer> resources;
    private final List<String> buildings;
    private final List<String> inhabitants;
    private final int armySize;
    private final int armyAttackScore;
    private final int villageDefenseScore;
    private final int timeHours;

    public VillageStatusDto(String villageName,
                            int trophies,
                            int currentPopulation,
                            int populationCap,
                            boolean guardActive,
                            int guardRemainingHours,
                            Map<String, Integer> resources,
                            List<String> buildings,
                            List<String> inhabitants,
                            int armySize,
                            int armyAttackScore,
                            int villageDefenseScore,
                            int timeHours) {
        this.villageName = villageName;
        this.trophies = trophies;
        this.currentPopulation = currentPopulation;
        this.populationCap = populationCap;
        this.guardActive = guardActive;
        this.guardRemainingHours = guardRemainingHours;
        this.resources = new LinkedHashMap<>(resources);
        this.buildings = new ArrayList<>(buildings);
        this.inhabitants = new ArrayList<>(inhabitants);
        this.armySize = armySize;
        this.armyAttackScore = armyAttackScore;
        this.villageDefenseScore = villageDefenseScore;
        this.timeHours = timeHours;
    }

    public String getVillageName() {
        return villageName;
    }

    public int getTrophies() {
        return trophies;
    }

    public int getCurrentPopulation() {
        return currentPopulation;
    }

    public int getPopulationCap() {
        return populationCap;
    }

    public boolean isGuardActive() {
        return guardActive;
    }

    public int getGuardRemainingHours() {
        return guardRemainingHours;
    }

    public Map<String, Integer> getResources() {
        return resources;
    }

    public List<String> getBuildings() {
        return buildings;
    }

    public List<String> getInhabitants() {
        return inhabitants;
    }

    public int getArmySize() {
        return armySize;
    }

    public int getArmyAttackScore() {
        return armyAttackScore;
    }

    public int getVillageDefenseScore() {
        return villageDefenseScore;
    }

    public int getTimeHours() {
        return timeHours;
    }
}
