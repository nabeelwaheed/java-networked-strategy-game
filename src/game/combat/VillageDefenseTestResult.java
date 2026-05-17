/**
 * Represents the outcome of testing a village base against generated armies.
 *
 * It records how many generated attacks were defended successfully and
 * calculates a final defense success percentage for the player.
 */
package game.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VillageDefenseTestResult {
    private final int totalTests;
    private final int successfulDefenses;
    private final int failedDefenses;
    private final List<String> roundSummaries;

    public VillageDefenseTestResult(int totalTests, int successfulDefenses, int failedDefenses, List<String> roundSummaries) {
        this.totalTests = totalTests;
        this.successfulDefenses = successfulDefenses;
        this.failedDefenses = failedDefenses;
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
        if (totalTests == 0) {
            return 0.0;
        }
        return (successfulDefenses * 100.0) / totalTests;
    }

    public List<String> getRoundSummaries() {
        return Collections.unmodifiableList(roundSummaries);
    }

    @Override
    public String toString() {
        return "Village defense test: " + successfulDefenses + "/" + totalTests
                + " defended successfully (" + String.format("%.1f", getDefenseSuccessRate()) + "%)";
    }
}
