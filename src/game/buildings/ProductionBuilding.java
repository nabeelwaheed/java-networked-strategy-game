/**
 * Abstract subclass of Building representing resource-producing structures.
 *
 * Adds production rate behavior and upgrades increase production output.
 */
package game.buildings;

public abstract class ProductionBuilding extends Building {
    private int productionRate;

    protected ProductionBuilding(String name, int level, int hitPoints, int maxLevel, int productionRate) {
        super(name, level, hitPoints, maxLevel);
        this.productionRate = productionRate;
    }

    /**
     * Returns the resource production rate of the building.
     *
     * @return the production rate
     */
    public int getProductionRate() {
        return productionRate;
    }

    @Override
    protected void onUpgrade() {
        productionRate += 5;
    }
}