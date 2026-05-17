/**
 * Simple value object representing the result of an adapted attack.
 *
 * It stores whether the attack succeeded and the amounts of loot
 * returned after the external combat API finishes its calculation.
 */
package game.adapter;

public class AttackResolution {
    private final boolean success;
    private final int lootGold;
    private final int lootIron;
    private final int lootWood;

    public AttackResolution(boolean success, int lootGold, int lootIron, int lootWood) {
        this.success = success;
        this.lootGold = lootGold;
        this.lootIron = lootIron;
        this.lootWood = lootWood;
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
}
