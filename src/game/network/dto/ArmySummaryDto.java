/**
 * Serializable DTO describing a generated detached army.
 */
package game.network.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArmySummaryDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int unitCount;
    private final int attackScore;
    private final List<String> units;

    public ArmySummaryDto(int unitCount, int attackScore, List<String> units) {
        this.unitCount = unitCount;
        this.attackScore = attackScore;
        this.units = new ArrayList<>(units);
    }

    public int getUnitCount() {
        return unitCount;
    }

    public int getAttackScore() {
        return attackScore;
    }

    public List<String> getUnits() {
        return units;
    }
}
