/**
 * Ranged combat unit.
 *
 * Provides attack capability from a distance.
 */
package game.inhabitants;

public class Archer extends ArmyUnit {
    public Archer(int id) {
        super(id, "Archer", 18, 65, 3);
    }
}