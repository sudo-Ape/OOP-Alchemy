package alchemy;

import java.util.List;

public class Kettle extends Device {
    // =================================================================================
    // Mixing methods
    // =================================================================================
    @Override
    public void run() {
        result = mix(internalIngredients);
        internalIngredients.clear();
    }

    /**
     * Static method to mix a given list of ingredients
     *
     * @param ingredients Given list of ingredients to mix
     *
     * @return New ingredient as mix of given ingredients
     *
     * @note A static method is required here for later use in Laboratory.
     */
    public static Ingredient mix(List<Ingredient> ingredients) {
        // Mixing logic here...

        return null;
    }
}
