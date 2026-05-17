package game.buildings;

public enum BuildingType {
    VILLAGE_HALL,
    FARM,
    GOLD_MINE,
    IRON_MINE,
    LUMBER_MILL,
    GOLD_STORAGE,
    IRON_STORAGE,
    LUMBER_STORAGE,
    ARCHER_TOWER,
    CANNON;

    public static BuildingType fromBuilding(Building building) {
        return switch (building.getName()) {
            case "Village Hall" -> VILLAGE_HALL;
            case "Farm" -> FARM;
            case "Gold Mine" -> GOLD_MINE;
            case "Iron Mine" -> IRON_MINE;
            case "Lumber Mill" -> LUMBER_MILL;
            case "Gold Storage" -> GOLD_STORAGE;
            case "Iron Storage" -> IRON_STORAGE;
            case "Lumber Storage" -> LUMBER_STORAGE;
            case "Archer Tower" -> ARCHER_TOWER;
            case "Cannon" -> CANNON;
            default -> throw new IllegalArgumentException("Unknown building: " + building.getName());
        };
    }
}
