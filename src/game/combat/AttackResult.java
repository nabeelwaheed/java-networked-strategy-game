/**
 * Represents the outcome of an attack.
 *
 * Stores:
 * - Whether the attack succeeded
 * - Loot obtained (gold, iron, wood)
 * - Trophy changes
 *
 * Returned by GameEngine after combat simulation.
 */
package game.combat;

public class AttackResult {
    private final boolean success;
    private final int lootGold;
    private final int lootIron;
    private final int lootWood;
    private final int trophiesChange;

    public AttackResult(boolean success, int lootGold, int lootIron, int lootWood, int trophiesChange) {
        this.success = success;
        this.lootGold = lootGold;
        this.lootIron = lootIron;
        this.lootWood = lootWood;
        this.trophiesChange = trophiesChange;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getLootGold() {
        return lootGold;
    }

    public int getLootIron() {
        return lootIron;
    }

    public int getLootWood() {
        return lootWood;
    }

    public int getTrophiesChange() {
        return trophiesChange;
    }

    @Override
    public String toString() {
        return "Attack " + (success ? "won" : "lost")
                + " | Loot: gold=" + lootGold
                + ", iron=" + lootIron
                + ", wood=" + lootWood
                + " | Trophies: " + trophiesChange;
    }
}
