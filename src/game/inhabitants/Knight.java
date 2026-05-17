/**
 * Heavy combat unit.
 *
 * Higher durability and stronger attack damage.
 */
package game.inhabitants;

public class Knight extends ArmyUnit {
    public Knight(int id) {
        super(id, "Knight", 30, 110, 1);
    }
}