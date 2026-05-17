/**
 * Responsible for generating enemy villages for attack exploration.
 *
 * VillageGenerator creates villages with randomized configurations
 * based on player level, including buildings, resources, and army units.
 *
 * This class separates village generation logic from GameEngine
 */
package game.engine;

import game.buildings.BuildingType;
import game.factory.BuildingFactory;
import game.factory.InhabitantFactory;
import game.inhabitants.ArmyUnit;
import game.inhabitants.Inhabitant;
import game.inhabitants.InhabitantType;
import game.model.Village;

import java.util.ArrayList;
import java.util.List;

public class VillageGenerator {
    private final DiceRoller diceRoller;
    private final BuildingFactory buildingFactory;
    private final InhabitantFactory inhabitantFactory;

    public VillageGenerator(DiceRoller diceRoller, BuildingFactory buildingFactory, InhabitantFactory inhabitantFactory) {
        this.diceRoller = diceRoller;
        this.buildingFactory = buildingFactory;
        this.inhabitantFactory = inhabitantFactory;
    }

    /**
     * Generates a list of enemy villages appropriate for the player's level.
     *
     * Each generated village may contain different buildings, resources,
     * and army units, allowing the player to explore attack options.
     *
     * @param playerLevel the current progression level of the player
     * @return a list of generated villages
     */
    public List<Village> generateVillages(int playerLevel) {
        List<Village> villages = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Village village = new Village(100 + i, "EnemyVillage" + i);
            village.addBuilding(buildingFactory.create(BuildingType.VILLAGE_HALL));
            village.addBuilding(buildingFactory.create(BuildingType.FARM));
            village.addBuilding(buildingFactory.create(BuildingType.GOLD_MINE));
            village.addBuilding(buildingFactory.create(BuildingType.IRON_MINE));
            village.addBuilding(buildingFactory.create(BuildingType.LUMBER_MILL));
            if (diceRoller.roll(0, 1) == 1) {
                village.addBuilding(buildingFactory.create(BuildingType.ARCHER_TOWER));
            }
            if (diceRoller.roll(0, 1) == 1) {
                village.addBuilding(buildingFactory.create(BuildingType.CANNON));
            }
            for (int j = 0; j < playerLevel + 1; j++) {
                addUnit(village, inhabitantFactory.create(InhabitantType.SOLDIER, j + 1));
            }
            if (playerLevel >= 2) {
                addUnit(village, inhabitantFactory.create(InhabitantType.ARCHER, 1000 + i));
            }
            if (playerLevel >= 3) {
                addUnit(village, inhabitantFactory.create(InhabitantType.KNIGHT, 2000 + i));
            }
            for (int j = 0; j < Math.max(1, playerLevel); j++) {
                village.addInhabitant(inhabitantFactory.create(InhabitantType.WORKER, 3000 + (i * 10) + j));
            }
            village.getResources().add(game.model.ResourceType.GOLD, 200);
            village.getResources().add(game.model.ResourceType.IRON, 150);
            village.getResources().add(game.model.ResourceType.WOOD, 180);
            villages.add(village);
        }
        return villages;
    }

    private void addUnit(Village village, Inhabitant inhabitant) {
        village.addInhabitant(inhabitant);
        if (inhabitant instanceof ArmyUnit armyUnit) {
            village.getArmy().addUnit(armyUnit);
        }
    }
}
