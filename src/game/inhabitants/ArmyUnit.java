/**
 * Abstract subclass of Inhabitant representing combat units.
 *
 * Defines combat-related attributes such as:
 * - Attack damage
 * - Hit points
 * - Attack range
 *
 * Specialized combat units extend this class.
 */
package game.inhabitants;

public abstract class ArmyUnit extends Inhabitant {
    private int attackDamage;
    private int hitPoints;
    private int range;

    protected ArmyUnit(int id, String name, int attackDamage, int hitPoints, int range) {
        super(id, name, 1, 5);
        this.attackDamage = attackDamage;
        this.hitPoints = hitPoints;
        this.range = range;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getRange() {
        return range;
    }

    /**
     * Reduces the unit's hit points by the specified damage amount.
     *
     * Hit points will not drop below zero.
     *
     * @param damage the damage dealt to the unit
     */
    public void takeDamage(int damage) {
        hitPoints = Math.max(0, hitPoints - damage);
    }

    /**
     * Checks whether the unit is still alive.
     *
     * @return true if hit points are greater than zero
     */
    public boolean isAlive() {
        return hitPoints > 0;
    }

    public void restoreState(int restoredLevel, int restoredHitPoints) {
        super.restoreState(restoredLevel);
        this.hitPoints = restoredHitPoints;
    }

    @Override
    protected void onUpgrade() {
        attackDamage += 5;
        hitPoints += 10;
    }
}
