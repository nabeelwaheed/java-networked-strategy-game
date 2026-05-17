/**
 * Default implementation of the inhabitant factory.
 *
 * This class creates the different inhabitant and army unit
 * objects needed by the engine and persistence layer.
 */
package game.factory;

import game.inhabitants.Archer;
import game.inhabitants.Catapult;
import game.inhabitants.Collector;
import game.inhabitants.Inhabitant;
import game.inhabitants.InhabitantType;
import game.inhabitants.Knight;
import game.inhabitants.Miner;
import game.inhabitants.Soldier;
import game.inhabitants.Worker;

public class DefaultInhabitantFactory implements InhabitantFactory {
    @Override
    public Inhabitant create(InhabitantType type, int id) {
        return switch (type) {
            case WORKER -> new Worker(id);
            case MINER -> new Miner(id);
            case COLLECTOR -> new Collector(id);
            case SOLDIER -> new Soldier(id);
            case ARCHER -> new Archer(id);
            case KNIGHT -> new Knight(id);
            case CATAPULT -> new Catapult(id);
        };
    }
}
