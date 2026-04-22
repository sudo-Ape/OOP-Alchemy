package alchemy;

import java.util.Arrays;
import java.util.List;

public enum State {
    // =================================================================================
    // Enum definitions
    // =================================================================================
    // IMPORTANT: ordered from smallest to largest!
    LIQUID(Arrays.asList(Unit.DROP,Unit.SPOON,Unit.VIAL,Unit.BOTTLE,Unit.JUG,Unit.BARREL,Unit.STOREROOM)),
    POWDER(Arrays.asList(Unit.PINCH,Unit.SPOON,Unit.SACHET,Unit.BOX,Unit.SACK,Unit.CHEST,Unit.STOREROOM));

    // =================================================================================
    // Attributes
    // =================================================================================
    private List<Unit> allowedUnits;

    // =================================================================================
    // Constructor
    // =================================================================================
    State(List<Unit> allowedUnits) {
        this.allowedUnits = allowedUnits;
    }

    // =================================================================================
    // Getters
    // =================================================================================
    public List<Unit> getAllowedUnits() {
        return allowedUnits;
    }
}
