/**
 * Serializable DTO describing a generated village available for attack.
 */
package game.network.dto;

import java.io.Serial;
import java.io.Serializable;

public class VillageOptionDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int index;
    private final String name;
    private final int defenseScore;
    private final int armyScore;
    private final String resources;
    private final boolean guardActive;

    public VillageOptionDto(int index, String name, int defenseScore, int armyScore, String resources, boolean guardActive) {
        this.index = index;
        this.name = name;
        this.defenseScore = defenseScore;
        this.armyScore = armyScore;
        this.resources = resources;
        this.guardActive = guardActive;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getDefenseScore() {
        return defenseScore;
    }

    public int getArmyScore() {
        return armyScore;
    }

    public String getResources() {
        return resources;
    }

    public boolean isGuardActive() {
        return guardActive;
    }
}
