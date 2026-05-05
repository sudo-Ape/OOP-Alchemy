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

    public Oven() {}

    // =================================================================================
    // Temperature
    // =================================================================================

    /**
     * Returns the temperature of the Oven
     *
     * @return temperature of the Oven
     */
    public Temperature getTemperature() {
        return temperature;
    }

    /**
     * Set the temperature of the Oven
     *
     * @param temperature Given temperature to which the Oven's temperature will be set
     */
    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    // =================================================================================
    // Other Methods
    // =================================================================================

    @Override
    public void add(IngredientContainer container) throws IllegalStateException {
        if (internalIngredients.size() >= 1) {
            throw new IllegalStateException("Oven can only hold one ingredient.");
        }
        super.add(container);
    }

    @Override
    public void run() {
        if (internalIngredients.isEmpty()) {
            throw new IllegalStateException("The storage of the Oven is empty.");
        }

        Ingredient ingredient = internalIngredients.getFirst();

        if (this.temperature.lessThan(ingredient.getTemperature())) {
            result = ingredient;
        } else {
            int deviation = rand.nextInt(11) - 5; // gives [-5, 5]
            int finalValue = Math.max(-10000, Math.min(10000, this.temperature.getValue() + deviation));
            result = new Ingredient(
                    ingredient.getIngredientType(),
                    new Temperature(finalValue),
                    ingredient.getState(),
                    ingredient.getQuantity()
            );
        }

        internalIngredients.clear();
    }
}
