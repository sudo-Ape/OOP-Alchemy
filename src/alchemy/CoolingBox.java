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

    public CoolingBox(Temperature temperature) {
        this.setTemperature(temperature);
    }


    // =================================================================================
    // Temperature
    // =================================================================================

    /**
     * Returns the temperature of the cooling box
     *
     * @return Temperature of the cooling box
     *
     * @throws IllegalStateException If cooling box has been terminated
     *      | isTerminated()
     */
    public Temperature getTemperature() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This cooling box has been terminated.");
        }
        return temperature;
    }


    /**
     * Set the temperature of the cooling box
     *
     * @param temperature Given temperature to which the CoolingBox's temperature will be set
     *
     * @throws IllegalStateException If cooling box has been terminated
     *      | isTerminated()
     */
    public void setTemperature(Temperature temperature) throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This cooling box has been terminated.");
        }
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
     * @throws IllegalStateException If ingredient container holds more than one item
     */
    @Override
    public void add(IngredientContainer container) throws IllegalStateException {
        if (!internalIngredients.isEmpty()) {
            throw new IllegalStateException("Cooling box can only hold one ingredient.");
        }
        super.add(container);
    }

    @Override
    public void run() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This cooling box has been terminated.");
        }
        if (getLocation() == null) {
            throw new IllegalStateException("Cooling box is not in a (valid) laboratory.");
        }
        if (internalIngredients.isEmpty()) {
            throw new IllegalStateException("The storage of the cooling box is empty.");
        }

        Ingredient ingredient = internalIngredients.getFirst();

        if (ingredient.getTemperature().lessThan(this.getTemperature())) {
            result = ingredient;
        } else {
            result = new Ingredient(
                    ingredient.getIngredientType(),
                    this.getTemperature(),
                    ingredient.getState(),
                    ingredient.getQuantity()
            );
    }

        // Clear internal ingredients
        internalIngredients.clear();
    }
}
