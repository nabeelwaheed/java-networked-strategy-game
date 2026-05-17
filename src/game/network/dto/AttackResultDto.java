/**
 * Serializable DTO for sending attack outcomes over the network.
 */
package game.network.dto;

import java.io.Serial;
import java.io.Serializable;

public class AttackResultDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final int lootGold;
    private final int lootIron;
    private final int lootWood;
    private final int trophiesChange;

    public AttackResultDto(boolean success, int lootGold, int lootIron, int lootWood, int trophiesChange) {
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
}
