package alchemy;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * Represents an object's measurement unit
 */
public enum Unit {
    // =================================================================================
    // Enum definitions
    // =================================================================================
    DROP((float) 1 /8),
    SPOON(1),
    VIAL(5),
    BOTTLE(15),
    JUG(105),
    BARREL(1260),
    STOREROOM(6300),

    PINCH((float) 1 /6),
    SACHET(7),
    BOX(42),
    SACK(126),
    CHEST(1260);

    // =================================================================================
    // Fields
    // =================================================================================
    private double spoons;

    // =================================================================================
    // Constructor
    // =================================================================================
    /**
     * Initialize a new ingredient with given unit
     *
     * @param spoons The amount of spoons this unit is equal to
     *
     * @note The unit will depend on the state of the ingredient (L or P)
     */
    Unit(double spoons) {
        this.spoons = spoons;
    }

    // =================================================================================
    // Getters
    // =================================================================================
    /**
     * Get the number of spoons this unit is equal to
     *
     * @return Number of spoons this unit is equal to
     */
    public double getSpoons() {
        return spoons;
    }
}
