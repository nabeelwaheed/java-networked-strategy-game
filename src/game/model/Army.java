/**
 * Represents a collection of ArmyUnit objects.
 *
 * Army is responsible for:
 * - Managing army units
 * - Calculating total attack score
 * - Sorting units by combat strength
 *
 * It centralizes combat-related behavior.
 */
package game.model;

import game.inhabitants.ArmyUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Army {
    private final List<ArmyUnit> units;

    public Army() {
        this.units = new ArrayList<>();
    }

    /**
     * Adds an army unit to the army.
     *
     * @param unit the combat unit to add
     */
    public void addUnit(ArmyUnit unit) {
        units.add(unit);
    }

    public List<ArmyUnit> getUnits() {
        return Collections.unmodifiableList(units);
    }

    public int size() {
        return units.size();
    }

    /**
     * Calculates the total attack strength of the army.
     *
     * Only alive army units contribute to the score. The total is
     * determined using each unit's attack damage and level.
     *
     * @return the army's total attack score
     */
    public int calculateAttackScore() {
        return units.stream()
                .filter(ArmyUnit::isAlive)
                .mapToInt(u -> u.getAttackDamage() * u.getLevel())
                .sum();
    }

    /**
     * Sorts army units in descending order of attack damage.
     *
     * This method uses an anonymous Comparator class to demonstrate
     * custom ordering logic.
     */
    public void sortByDamageDescending() {
        Comparator<ArmyUnit> comparator = new Comparator<>() {
            @Override
            public int compare(ArmyUnit a, ArmyUnit b) {
                return Integer.compare(b.getAttackDamage(), a.getAttackDamage());
            }
        };
        units.sort(comparator);
    }
}