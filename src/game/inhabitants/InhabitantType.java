/**
 * Enumeration of the supported inhabitant categories in the game.
 *
 * It is used by the factory and persistence code to identify
 * which inhabitant class should be created or restored.
 */
package game.inhabitants;

public enum InhabitantType {
    WORKER,
    MINER,
    COLLECTOR,
    SOLDIER,
    ARCHER,
    KNIGHT,
    CATAPULT;

    public static InhabitantType fromInhabitant(Inhabitant inhabitant) {
        return switch (inhabitant.getName()) {
            case "Worker" -> WORKER;
            case "Miner" -> MINER;
            case "Collector" -> COLLECTOR;
            case "Soldier" -> SOLDIER;
            case "Archer" -> ARCHER;
            case "Knight" -> KNIGHT;
            case "Catapult" -> CATAPULT;
            default -> throw new IllegalArgumentException("Unknown inhabitant: " + inhabitant.getName());
        };
    }
}
