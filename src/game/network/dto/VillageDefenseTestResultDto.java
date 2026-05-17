/**
 * Serializable DTO describing the result of testing a village base.
 */
package game.network.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VillageDefenseTestResultDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int totalTests;
    private final int successfulDefenses;
    private final int failedDefenses;
    private final double defenseSuccessRate;
    private final List<String> roundSummaries;

    public VillageDefenseTestResultDto(int totalTests, int successfulDefenses, int failedDefenses,
                                       double defenseSuccessRate, List<String> roundSummaries) {
        this.totalTests = totalTests;
        this.successfulDefenses = successfulDefenses;
        this.failedDefenses = failedDefenses;
        this.defenseSuccessRate = defenseSuccessRate;
        this.roundSummaries = new ArrayList<>(roundSummaries);
    }

    public int getTotalTests() {
        return totalTests;
    }

    public int getSuccessfulDefenses() {
        return successfulDefenses;
    }

    public int getFailedDefenses() {
        return failedDefenses;
    }

    public double getDefenseSuccessRate() {
        return defenseSuccessRate;
    }

    public List<String> getRoundSummaries() {
        return roundSummaries;
    }
}
