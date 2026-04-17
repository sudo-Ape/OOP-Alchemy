package alchemy;

public enum Unit {
    DROP("drop"),
    SPOON("spoon"),         // 6 drops (L), 6 pinches (P)
    VIAL("vial"),           // 5 spoons (L)
    BOTTLE("bottle"),       // 3 vials (L)
    JUG("jug"),             // 7 bottles (L)
    BARREL("barrel"),       // 12 jugs (L)
    STOREROOM("storeroom"), // 5 barrels (L), 5 chests (P)

    PINCH("pinch"),
    SACHET("sachet"),       // 7 spoons (P)
    BOX("box"),             // 6 sachets (P)
    SACK("sack"),           // 3 boxes
    CHEST("chest");         // 10 sacks

    private String unit = null;

    /**
     * Initialize a new ingredient with given unit
     *
     * @param unit
     *        The unit of the ingredient
     *
     * @note The unit will depend on the state of the ingredient (L or P)
     */
    protected Unit(String unit) {
        this.unit = unit;
    }

    /**
     * Returns the unit of the ingredient
     *
     * @return the unit if the ingredient
     */
    @Basic @Raw
    public String getUnit() {
        return unit;
    }
}
