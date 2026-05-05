package alchemy;

/**
 * Cooling box class for a machine that cools down ingredients in the laboratory
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class CoolingBox extends Device {
    // =================================================================================
    // Fields
    // =================================================================================
    private Temperature temperature;

    // =================================================================================
    // Constructor
    // =================================================================================

    public CoolingBox() {}

    // =================================================================================
    // Temperature
    // =================================================================================

    /**
     * Returns the temperature of the cooling box
     *
     * @return Temperature of the cooling box
     */
    public Temperature getTemperature() {
        return temperature;
    }


    /**
     * Set the temperature of the cooling box
     *
     * @param temperature Given temperature to which the CoolingBox's temperature will be set
     */
    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    // =================================================================================
    // Other Methods
    // =================================================================================

    /**
     * Adds the item from the ingredient container to the cooling box
     *
     * @param container IngredientContainer to extract from
     *
     * @throws IllegalStateException if ingredient container holds more than one item
     */
    @Override
    public void add(IngredientContainer container) throws IllegalStateException {
        if (internalIngredients.size() >= 1) {
            throw new IllegalStateException("Coolingox can only hold one ingredient.");
        }
        super.add(container);
    }

    /**
     *
     * @note throw exception if it holds more than one ingredient? Because add method already does this and is called
     *       before the run method.
     */
    @Override
    public void run() {
        if (internalIngredients.isEmpty()) {
            throw new IllegalStateException("The storage of the CoolingBox is empty.");
        }

        Ingredient ingredient = internalIngredients.getFirst();

        if (ingredient.getTemperature().lessThan(this.temperature)) {
            result = ingredient;
        } else {
            result = new Ingredient(
                    ingredient.getIngredientType(),
                    this.temperature,
                    ingredient.getState(),
                    ingredient.getQuantity()
            );
    }

        // clear internal ingredients
        internalIngredients.clear();
    }
}
