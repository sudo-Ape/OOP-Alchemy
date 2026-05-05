package alchemy;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * Enum to represent an object's measurement unit
 */
public enum Unit {
    // =================================================================================
    // Enum definitions
    // =================================================================================
    DROP((float) 1/8,"drop"),
    SPOON(1,"spoon"),
    VIAL(5,"vial"),
    BOTTLE(15,"bottle"),
    JUG(105,"jug"),
    BARREL(1260,"barrel"),
    STOREROOM(6300,"storeroom"),

    PINCH((float) 1/6,"pinch"),
    SACHET(7,"sachet"),
    BOX(42,"box"),
    SACK(126,"sack"),
    CHEST(1260,"chest");

    // =================================================================================
    // Fields
    // =================================================================================
    /**
     * Amount of spoons this unit is equal to
     */
    private final double spoons;

    /**
     * Display name string for this unit
     */
    private final String displayName;

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
    Unit(double spoons, String displayName) {
        this.spoons = spoons;
        this.displayName = displayName;
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

    /**
     * Get the display name for this unit
     *
     * @return Display name for this unit
     */
    public String getDisplayName() {
        return displayName;
    }
}
