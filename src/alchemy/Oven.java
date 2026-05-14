package alchemy;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.Random;


/**
 * Oven class for a machine that heats up ingredients in the laboratory.
 * An oven can hold at most one ingredient at a time and heats it to the oven's set temperature.
 * If the oven's temperature is lower than the ingredient's current temperature, the ingredient is left unchanged.
 * Otherwise, the result temperature is the oven's temperature with a random deviation in [-5, 5].
 *
 * @invar The temperature of this oven must always be non-null
 *      | getTemperature() != null
 *
 * @invar The oven holds at most one ingredient at a time
 *      | internalIngredients.size() <= 1
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class Oven extends Device {
    // =================================================================================
    // Fields
    // =================================================================================

    /**
     * The temperature at which this oven operates
     */
    private Temperature temperature = null;

    /**
     * Random number generator used to introduce a deviation in the result temperature
     */
    private final Random rand = new Random();

    // =================================================================================
    // Constructor
    // =================================================================================

    /**
     * Create a new oven with given temperature
     *
     * @param temperature The given temperature for this oven
     *
     * @effect Temperature is set to given temperature
     *         | setTemperature(temperature)
     */
    public Oven(String temperature) {
        this.setTemperature(temperature);
    }

    // =================================================================================
    // Temperature
    // =================================================================================

    /**
     * Returns the temperature of the Oven
     *
     * @return Temperature of the Oven
     *      | result == temperature
     */
    @Basic
    public Temperature getTemperature() {
        return temperature;
    }

    /**
     * Set the temperature of the Oven
     *
     * @param temperature Given temperature to which the Oven's temperature will be set
     *
     * @post Temperature is set to given temperature
     *      | new.getTemperature() == temperature
     *
     * @throws IllegalStateException If oven has been terminated
     *      | isTerminated()
     */
    public void setTemperature(String temperature) throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This oven has been terminated.");
        }
        this.temperature = new Temperature(temperature);
    }

    // =================================================================================
    // Other Methods
    // =================================================================================

    /**
     * Adds the ingredient from the given container to this oven
     *
     * @param container IngredientContainer to extract from
     *
     * @post The ingredient from the container is added to this oven's internal ingredients
     *      | new.internalIngredients.contains(container.getContents())
     *
     * @throws IllegalStateException If oven already holds an ingredient
     *      | !internalIngredients.isEmpty()
     *
     * @throws IllegalStateException If oven is not in a (valid) laboratory
     *      | getLocation() == null
     *
     * @throws IllegalStateException If oven has been terminated
     *      | isTerminated()
     *
     * @throws IllegalArgumentException If container is null or empty
     *      | container == null || container.isEmpty()
     */
    @Override
    public void add(IngredientContainer container) throws IllegalStateException {
        if (!internalIngredients.isEmpty()) {
            throw new IllegalStateException("Oven can only hold one ingredient.");
        }
        super.add(container);
    }

    /**
     * Heats up the ingredient to this oven's temperature
     *
     * @post If the oven's temperature is lower than the ingredient's temperature, the result is the unchanged ingredient
     *      | if getTemperature().lessThan(internalIngredients.getFirst().getTemperature()):
     *      |   result == internalIngredients.getFirst()
     *
     * @post If the oven's temperature is at least as high as the ingredient's temperature, the result is a new ingredient
     *       with the same type, state, and quantity, and a temperature equal to the oven's temperature
     *       plus a random deviation in [-5, 5]
     *      | if !getTemperature().lessThan(internalIngredients.getFirst().getTemperature()):
     *      |   result.getIngredientType() == internalIngredients.getFirst().getIngredientType()
     *      |   && result.getState() == internalIngredients.getFirst().getState()
     *      |   && result.getQuantity() == internalIngredients.getFirst().getQuantity()
     *      |   && result.getTemperature().getValue() >= getTemperature().getValue() - 5
     *      |   && result.getTemperature().getValue() <= getTemperature().getValue() + 5
     *
     * @post Internal ingredients list is emptied after the run
     *      | new.internalIngredients.isEmpty()
     *
     * @throws IllegalStateException If oven is not in a (valid) laboratory
     *      | getLocation() == null
     *
     * @throws IllegalStateException If this oven is empty
     *      | internalIngredients.isEmpty()
     *
     * @throws IllegalStateException If oven has been terminated
     *      | isTerminated()
     */
    @Override
    public void run() throws IllegalStateException {
        super.run(); // Do repetitive checks here!

        Ingredient ingredient = internalIngredients.getFirst();

        if (this.getTemperature().lessThan(ingredient.getTemperature())) {
            result = new Ingredient( // Create a copy!
                    ingredient.getIngredientType(),
                    ingredient.getTemperature(),
                    ingredient.getState(),
                    ingredient.getQuantity()
            );
        } else {
            int deviation = rand.nextInt(11) - 5; // Random integer in interval [-5, 5]
            result = new Ingredient(
                    ingredient.getIngredientType(),
                    new Temperature(getTemperature().getValue()+deviation),
                    ingredient.getState(),
                    ingredient.getQuantity()
            );
        }

        // Clear internal ingredients
        internalIngredients.clear();
    }

    /**
     * Terminate this oven
     *
     * @post Oven is terminated
     *      | new.isTerminated()
     *
     * @post Location of oven is set to null
     *      | new.getLocation() == null
     *
     * @post If location of oven was non-null, remove the oven from that location.
     *      | if getLocation() != null:
     *      |   (new) getLocation().getOven() == null
     */
    @Override @Raw
    public void terminate() {
        if (getLocation() != null) {
            getLocation().setOven(null);
        }
        super.terminate();
    }
}
