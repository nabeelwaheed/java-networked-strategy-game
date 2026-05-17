/**
 * Adapter that connects the game engine to the provided ChallengeDecision API.
 *
 * This class translates the current game's villages, army units,
 * defensive structures, and resources into the format expected by
 * the external ChallengeDecision classes. It then converts the
 * returned result back into the game's internal attack format.
 */
package game.adapter;

import ChallengeDecision.Arbitrer;
import ChallengeDecision.ChallengeAttack;
import ChallengeDecision.ChallengeDefense;
import ChallengeDecision.ChallengeEntitySet;
import ChallengeDecision.ChallengeResource;
import ChallengeDecision.ChallengeResult;
import game.buildings.Building;
import game.buildings.DefenseBuilding;
import game.inhabitants.ArmyUnit;
import game.inhabitants.Inhabitant;
import game.model.ResourceType;
import game.model.Village;

public class ChallengeDecisionAttackAdapter implements AttackOutcomeCalculator {
    /**
     * Converts the attacker and defender villages into the external API
     * structures and delegates the final combat decision to Arbitrer.
     *
     * @param attacker the attacking village
     * @param defender the defending village
     * @return the translated outcome of the attack
     */
    @Override
    public AttackResolution resolve(Village attacker, Village defender) {
        ChallengeEntitySet<Double, Double> challenger = new ChallengeEntitySet<>();
        ChallengeEntitySet<Double, Double> challengee = new ChallengeEntitySet<>();

        for (ArmyUnit unit : attacker.getArmy().getUnits()) {
            if (unit.isAlive()) {
                challenger.getEntityAttackList().add(new ChallengeAttack<>((double) (unit.getAttackDamage() * unit.getLevel()), (double) unit.getHitPoints())
                );
            }
        }

        for (Building building : defender.getBuildings()) {
            if (building instanceof DefenseBuilding defenseBuilding) {
                challengee.getEntityDefenseList().add(
                        new ChallengeDefense<>((double) (defenseBuilding.getDamage() * defenseBuilding.getLevel()),
                                (double) defenseBuilding.getHitPoints())
                );
            }
        }

        int supportDefense = defender.getInhabitants().stream()
                .mapToInt(Inhabitant::getLevel)
                .sum();
        int supportHitPoints = defender.getBuildings().stream()
                .mapToInt(Building::getHitPoints)
                .sum();
        if (supportDefense > 0 || supportHitPoints > 0) {
            challengee.getEntityDefenseList().add(new ChallengeDefense<>((double) Math.max(1, supportDefense), (double) Math.max(1, supportHitPoints)));
        }

        challengee.getEntityResourceList().add(new ChallengeResource<>((double) defender.getResources().get(ResourceType.GOLD), 0.0));
        challengee.getEntityResourceList().add(new ChallengeResource<>((double) defender.getResources().get(ResourceType.IRON), 0.0));
        challengee.getEntityResourceList().add(new ChallengeResource<>((double) defender.getResources().get(ResourceType.WOOD), 0.0));

        ChallengeResult result = Arbitrer.challengeDecide(challenger, challengee);
        if (!result.getChallengeWon()) {
            return new AttackResolution(false, 0, 0, 0);
        }

        int lootGold = 0;
        int lootIron = 0;
        int lootWood = 0;
        if (result.getLoot().size() >= 3) {
            lootGold = Math.max(0, result.getLoot().get(0).getProperty().intValue());
            lootIron = Math.max(0, result.getLoot().get(1).getProperty().intValue());
            lootWood = Math.max(0, result.getLoot().get(2).getProperty().intValue());
        }

        return new AttackResolution(true, lootGold, lootIron, lootWood);
    }
}
