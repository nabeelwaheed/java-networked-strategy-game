/**
 * Default implementation of the building factory.
 *
 * This class centralizes the creation of all supported building
 * types so that clients do not instantiate concrete classes directly.
 */
package game.factory;

import game.buildings.ArcherTower;
import game.buildings.Building;
import game.buildings.BuildingType;
import game.buildings.Cannon;
import game.buildings.Farm;
import game.buildings.GoldMine;
import game.buildings.GoldStorage;
import game.buildings.IronMine;
import game.buildings.IronStorage;
import game.buildings.LumberMill;
import game.buildings.LumberStorage;
import game.buildings.VillageHall;

public class DefaultBuildingFactory implements BuildingFactory {
    @Override
    public Building create(BuildingType type) {
        return switch (type) {
            case VILLAGE_HALL -> new VillageHall();
            case FARM -> new Farm();
            case GOLD_MINE -> new GoldMine();
            case IRON_MINE -> new IronMine();
            case LUMBER_MILL -> new LumberMill();
            case GOLD_STORAGE -> new GoldStorage();
            case IRON_STORAGE -> new IronStorage();
            case LUMBER_STORAGE -> new LumberStorage();
            case ARCHER_TOWER -> new ArcherTower();
            case CANNON -> new Cannon();
        };
    }
}
