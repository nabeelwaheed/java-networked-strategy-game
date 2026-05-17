/**
 * Generates detached armies used for testing a player's village defense.
 *
 * The generated armies are based on the current village progression so that
 * defense tests remain compatible with the player's overall level.
 */
package game.engine;

import game.factory.InhabitantFactory;
import game.inhabitants.ArmyUnit;
import game.inhabitants.Inhabitant;
import game.inhabitants.InhabitantType;
import game.model.Army;

import java.util.ArrayList;
import java.util.List;

public class ArmyGenerator {
    private final DiceRoller diceRoller;
    private final InhabitantFactory inhabitantFactory;

    public ArmyGenerator(DiceRoller diceRoller, InhabitantFactory inhabitantFactory) {
        this.diceRoller = diceRoller;
        this.inhabitantFactory = inhabitantFactory;
    }

    /**
     * Generates a detached army with a strength compatible with the given level.
     *
     * @param playerLevel the current progression level of the defending village
     * @return a generated army for testing purposes
     */
    public Army generateArmy(int playerLevel) {
        Army army = new Army();
        int nextTempId = 10_000 + diceRoller.roll(1, 1_000);
        int soldiers = Math.max(1, playerLevel + diceRoller.roll(0, 2));
        for (int i = 0; i < soldiers; i++) {
            addUnit(army, inhabitantFactory.create(InhabitantType.SOLDIER, nextTempId++));
        }

        if (playerLevel >= 2) {
            int archers = diceRoller.roll(1, Math.max(1, playerLevel));
            for (int i = 0; i < archers; i++) {
                addUnit(army, inhabitantFactory.create(InhabitantType.ARCHER, nextTempId++));
            }
        }

        if (playerLevel >= 3) {
            int knights = diceRoller.roll(1, Math.max(1, playerLevel - 1));
            for (int i = 0; i < knights; i++) {
                addUnit(army, inhabitantFactory.create(InhabitantType.KNIGHT, nextTempId++));
            }
        }

        if (playerLevel >= 4 && diceRoller.roll(0, 1) == 1) {
            addUnit(army, inhabitantFactory.create(InhabitantType.CATAPULT, nextTempId));
        }

        return army;
    }

    /**
     * Generates a list of detached armies for repeated defense testing.
     *
     * @param playerLevel the current progression level of the defending village
     * @param count the number of armies to generate
     * @return the generated armies
     */
    public List<Army> generateArmies(int playerLevel, int count) {
        List<Army> armies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            armies.add(generateArmy(playerLevel));
        }
        return armies;
    }

    private void addUnit(Army army, Inhabitant inhabitant) {
        if (inhabitant instanceof ArmyUnit armyUnit) {
            army.addUnit(armyUnit);
        }
    }
}
