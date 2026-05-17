/**
 * Basic combat unit.
 *
 * Provides balanced damage and survivability.
 */
package game.inhabitants;

public class Soldier extends ArmyUnit {
    public Soldier(int id) {
        super(id, "Soldier", 20, 80, 1);
    }
}