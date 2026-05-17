/**
 * Abstraction for external or internal attack-resolution systems.
 *
 * This interface allows the game engine to request the result of
 * an attack without depending directly on a specific API or library.
 */
package game.adapter;

import game.model.Village;

public interface AttackOutcomeCalculator {
    /**
     * Resolves an attack between two villages.
     *
     * @param attacker the attacking village
     * @param defender the defending village
     * @return the translated attack outcome
     */
    AttackResolution resolve(Village attacker, Village defender);
}
