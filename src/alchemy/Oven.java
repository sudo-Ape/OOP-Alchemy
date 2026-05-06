package alchemy;
import java.util.Random;

/**
 * Oven class for a machine that heats up ingredients in the laboratory
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class Oven extends Device {
    // =================================================================================
    // Fields
    // =================================================================================
    private Temperature temperature;
    private final Random rand = new Random();

    // =================================================================================
    // Constructor
    // =================================================================================

    public Oven(Temperature temperature) {
        this.setTemperature(temperature);
    }

    // =================================================================================
    // Temperature
    // =================================================================================

    /**
     * Returns the temperature of the Oven
     *
     * @return Temperature of the Oven
     *
     * @throws IllegalStateException If oven has been terminated
     *      | isTerminated()
     */
    public Temperature getTemperature() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This oven has been terminated.");
        }
        return temperature;
    }

    /**
     * Set the temperature of the Oven
     *
     * @param temperature Given temperature to which the Oven's temperature will be set
     *
     * @throws IllegalStateException If oven has been terminated
     *      | isTerminated()
     */
    public void setTemperature(Temperature temperature) throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This oven has been terminated.");
        }
        this.temperature = temperature;
    }

    // =================================================================================
    // Other Methods
    // =================================================================================

    @Override
    public void add(IngredientContainer container) throws IllegalStateException {
        if (!internalIngredients.isEmpty()) {
            throw new IllegalStateException("Oven can only hold one ingredient.");
        }
        super.add(container);
    }

    @Override
    public void run() throws IllegalStateException {
        super.run(); // Do repetitive checks here!

        Ingredient ingredient = internalIngredients.getFirst();

        if (this.getTemperature().lessThan(ingredient.getTemperature())) {
            result = ingredient;
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
        clearInternalIngredients();
    }

    @Override
    public void terminate() {
        if (getLocation() != null) {
            getLocation().setOven(null);
        }
        super.terminate();
    }
}
