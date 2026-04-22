package alchemy;

import java.util.Arrays;
import java.util.List;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * Represents the physical state of an object and its corresponding measurement unit
 */
public enum State {
    // =================================================================================
    // Enum definitions
    // =================================================================================

    /** State for liquids, orderred from drops to storerooms */
    LIQUID(Arrays.asList(Unit.DROP,Unit.SPOON,Unit.VIAL,Unit.BOTTLE,Unit.JUG,Unit.BARREL,Unit.STOREROOM)),

    /** State for powders, ordered fro pinches to storerooms */
    POWDER(Arrays.asList(Unit.PINCH,Unit.SPOON,Unit.SACHET,Unit.BOX,Unit.SACK,Unit.CHEST,Unit.STOREROOM));

    // =================================================================================
    // Fields
    // =================================================================================

    /** The list of units applicable to this state */
    private List<Unit> allowedUnits;

    // =================================================================================
    // Constructor
    // =================================================================================

    /**
     * Internal constructor to associate units with a state constant
     *
     * @param allowedUnits The allowed units for this state
     */
    State(List<Unit> allowedUnits) {
        this.allowedUnits = allowedUnits;
    }

    // =================================================================================
    // Getters
    // =================================================================================

    /**
     * Returns a list of allowed units for this ingredient
     *
     * @return List of allowed units for this ingredient
     */
    @Basic
    public List<Unit> getAllowedUnits() {
        return allowedUnits;
    }
}
