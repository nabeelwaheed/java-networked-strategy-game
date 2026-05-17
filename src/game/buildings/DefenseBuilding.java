/**
 * Abstract subclass of Building representing defensive structures.
 *
 * Adds combat-related properties such as:
 * - Damage
 * - Attack range
 *
 * Used in village defense score calculation.
 */
package game.buildings;

public abstract class DefenseBuilding extends Building {
    private int damage;
    private int range;

    protected DefenseBuilding(String name, int level, int hitPoints, int maxLevel, int damage, int range) {
        super(name, level, hitPoints, maxLevel);
        this.damage = damage;
        this.range = range;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    @Override
    protected void onUpgrade() {
        damage += 5;
    }
}