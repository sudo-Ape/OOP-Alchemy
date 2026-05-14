package alchemy;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * Cooling box class for a machine that cools down ingredients in the laboratory.
 * A cooling box can hold at most one ingredient at a time and cools it to the cooling box's set temperature.
 * If the cooling box's temperature is higher than the ingredient's current temperature, the ingredient is left unchanged.
 * Otherwise, the result temperature is the cooling box's temperature.
 *
 * @invar The temperature of this cooling box must always be non-null
 *       | getTemperature() != null
 *
 * @invar The cooling box holds at most one ingredient at a time
 *       | internalIngredients.size() <= 1
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class CoolingBox extends Device {
    // =================================================================================
    // Fields
    // =================================================================================

    /**
     * The temperature at which this cooling box operates
     */
    private Temperature temperature;

    // =================================================================================
    // Constructor
    // =================================================================================

    /**
     * Create a new cooling box with given temperature
     *
     * @param temperature The given temperature for this cooling box
     *
     * @effect Temperature is set to the given temperature
     *      | setTemperature(temperature)
     */
    public CoolingBox(String temperature) {
        this.setTemperature(temperature);
    }


    // =================================================================================
    // Temperature
    // =================================================================================

    /**
     * Returns the temperature of the cooling box
     *
     * @return Temperature of the cooling box
     *         | result == temperature
     */
    @Basic
    public Temperature getTemperature() throws IllegalStateException {
        return temperature;
    }


    /**
     * Set the temperature of the cooling box
     *
     * @param temperature Given temperature to which the CoolingBox's temperature will be set
     *
     * @post Temperature is set to given temperature
     *      | new.getTemperature() == temperature
     *
     * @throws IllegalStateException If cooling box has been terminated
     *      | isTerminated()
     */
    public void setTemperature(String temperature) throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This cooling box has been terminated.");
        }
        this.temperature = new Temperature(temperature);
    }

    // =================================================================================
    // Other Methods
    // =================================================================================

    /**
     * Adds the ingredient from given container to this cooling box
     *
     * @param container IngredientContainer to extract from
     *
     * @post The ingredient from the container is added to this cooling box's internal ingredients
     *       | new.internalIngredients.contains(container.getContents())
     *
     * @throws IllegalStateException If cooling box already holds an ingredient
     *       | !internalIngredients.isEmpty()
     *
     * @throws IllegalStateException If cooling box is not in a (valid) laboratory
     *       | getLocation() == null
     *
     * @throws IllegalStateException If cooling box has been terminated
     *       | isTerminated()
     *
     * @throws IllegalArgumentException If container is null or empty
     *       | container == null || container.isEmpty()
     */
    @Override
    public void add(IngredientContainer container) throws IllegalStateException {
        if (!internalIngredients.isEmpty()) {
            throw new IllegalStateException("Cooling box can only hold one ingredient.");
        }
        super.add(container);
    }

    /**
     * Cools down the ingredient to this cooling box's temperature
     *
     * @post If the cooling box's temperature is higher than the ingredient's temperature, the result is the unchanged ingredient
     *      | if internalIngredients.getFirst().getTemperature().lessThan(getTemperature()):
     *      |   result == internalIngredients.getFirst()
     *
     * @post If the cooling box's temperature is at least as low as the ingredient's temperature, the result is a new ingredient
     *       with the same type, state, and quantity, and a temperature equal to the cooling box's temperature.
     *       | if !internalIngredients.getFirst().getTemperature().lessThan(getTemperature()):
     *       |   result.getIngredientType() == internalIngredients.getFirst().getIngredientType()
     *       |   && result.getState() == internalIngredients.getFirst().getState()
     *       |   && result.getQuantity() == internalIngredients.getFirst().getQuantity()
     *       |   && result.getTemperature().getValue() == getTemperature().getValue()
     *
     * @post Internal ingredients list is emptied after the run
     *       | new.internalIngredients.isEmpty()
     *
     * @throws IllegalStateException If cooling box is not in a (valid) laboratory
     *      | getLocation() == null
     *
     * @throws IllegalStateException If this cooling box is empty
     *         | internalIngredients.isEmpty()
     *
     * @throws IllegalStateException If this cooling box has been terminated
     *         | isTerminated()
     */
    @Override
    public void run() throws IllegalStateException {
        super.run(); // Do repetitive checks here!

        Ingredient ingredient = internalIngredients.getFirst();

        if (ingredient.getTemperature().lessThan(this.getTemperature())) {
            result = new Ingredient( // Create a copy!
                    ingredient.getIngredientType(),
                    ingredient.getTemperature(),
                    ingredient.getState(),
                    ingredient.getQuantity()
            );
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

    /**
     * Terminate this cooling box
     *
     * @post Cooling box is terminated
     *      | new.isTerminated()
     *
     * @post Location of cooling box is set to null
     *      | new.getLocation() == null
     *
     * @post If location of cooling box was non-null, remove the cooling box from that location.
     *      | if getLocation() != null:
     *      |   (new) getLocation().getCoolingBox() == null
     */
    @Override @Raw
    public void terminate() {
        if (getLocation() != null) {
            getLocation().setCoolingBox(null);
        }
        super.terminate();
    }
}
