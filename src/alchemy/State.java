package alchemy;

import java.util.Arrays;
import java.util.List;

public enum State {
    // IMPORTANT: ordered from smallest to largest!
    LIQUID(Arrays.asList(Unit.DROP,Unit.SPOON,Unit.VIAL,Unit.BOTTLE,Unit.JUG,Unit.BARREL,Unit.STOREROOM)),
    POWDER(Arrays.asList(Unit.PINCH,Unit.SPOON,Unit.SACHET,Unit.BOX,Unit.SACK,Unit.CHEST,Unit.STOREROOM));

    // field
    private List<Unit> allowedUnits;

    // constructor
    State(List<Unit> allowedUnits) {
        this.allowedUnits = allowedUnits;
    }

    public List<Unit> getAllowedUnits() {
        return allowedUnits;
    }

    public void setAllowedUnits(List<Unit> allowedUnits) {
        this.allowedUnits = allowedUnits;
    }
}
