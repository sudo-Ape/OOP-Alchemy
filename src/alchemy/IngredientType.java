package alchemy;

import java.util.*;

/**
 * Ingredient type class for OGP alchemy
 *
 * @author Casper Vermeeren; LoÃ¯ck Sansen
 */
public class IngredientType {
    // =================================================================================
    // Fields
    // =================================================================================

    private Temperature standardTemperature;
    private State standardState;
    private Set<String> basicIngredients = new HashSet<>(); // Set to ensure no duplicate basic ingredients (possibly caused by mixing)

    // =================================================================================
    // Constructors
    // =================================================================================
    /**
     * Create a new ingredientType with given standard temperature, given standard state, and given list of basic ingredient names
     *
     * @param standardTemperature Given standard temperature
     * @param standardState Given standard state
     * @param basicIngredients Given basic ingredient names
     *
     * @note This constructor is package-private: the user cannot use it, only other code can!
     */
    IngredientType(Temperature standardTemperature, State standardState, Set<String> basicIngredients) {
        this.standardTemperature = standardTemperature;
        this.standardState = standardState;
        this.basicIngredients = basicIngredients;
    }

    // =================================================================================
    // Getters
    // =================================================================================
    /**
     * Get the standard temperature for this ingredient type
     *
     * @return Standard temperature for this ingredient type
     */
    public Temperature getStandardTemperature() {
        return standardTemperature;
    }

    /**
     * Get the standard temperature for this ingredient type, displayed in the user format
     *
     * @return Standard temperature for this ingredient type, displayed in the user format
     */
    public String getStandardTemperatureDisplay() {
        return standardTemperature.toUser();
    }

    /**
     * Get the standard state for this ingredient type
     *
     * @return Standard state for this ingredient type
     */
    public State getStandardState() {
        return standardState;
    }

    /**
     * Get the basic ingredients list for this ingredient type
     *
     * @return Basic ingredients list for this ingredient type
     */
    private Set<String> getBasicIngredients() {
        return basicIngredients;
    }

    // =================================================================================
    // Setters
    // =================================================================================

    /**
     * Set the basic ingredients for this ingredient type
     *
     * @throws IllegalArgumentException If the given set of basic ingredients is not allowed
     *      | !canHaveAsBasicIngredients(basicIngredients)
     *
     * @param basicIngredients Given set of basic ingredient names
     */
    public void setBasicIngredients(Set<String> basicIngredients) throws IllegalArgumentException {
        if (canHaveAsBasicIngredients(basicIngredients)) {
            this.basicIngredients = basicIngredients;
        } else {
            throw new IllegalArgumentException("At least one of the given basic ingredients is invalid!");
        }
    }

    /**
     * Sets the standard temperature of this ingredient type
     *
     * @param standardTemperature The given standard temperature
     */
    private void setStandardTemperature(Temperature standardTemperature) {
        this.standardTemperature = standardTemperature;
    }

    /**
     * Sets the standard state of this ingredient type
     *
     * @param standardState The given standard state
     */
    private void setStandardState(State standardState) {
        this.standardState = standardState;
    }

    // =================================================================================
    // Inspectors
    // =================================================================================

    /**
     * Check whether given list of basic ingredients is allowed
     *
     * @param basicIngredients Given list of basic ingredients to check
     *
     * @return Whether this list of basic ingredients is allowed
     */
    private boolean canHaveAsBasicIngredients(Set<String> basicIngredients) {
        for (String ingredient : basicIngredients) {
            if (!canHaveAsBasicIngredient(ingredient)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if given basic ingredient name is allowed name
     *
     * @param basicIngredient Basic ingredient name to check
     *
     * @return Whether this basic ingredient name is allowed
     *
     * @note Not implemented yet! Complex! (WIP)
     */
    private boolean canHaveAsBasicIngredient(String basicIngredient) {
        return true;
    }

    // =================================================================================
    // Simple name
    // =================================================================================
    /**
     * Get the simple name for this ingredient type
     *
     * @return Simple name for this ingredient type
     */
    public String getSimpleName() {
        String output = "";

        // Convert set to sorted list
        List<String> basicIngredients = new ArrayList<>(getBasicIngredients());
        basicIngredients.sort(Comparator.naturalOrder());

        int N = basicIngredients.size();
        for (int i = 0; i < N; i++) {
            output += basicIngredients.get(i);

            // Check if "mixed with" is necessary
            if (i == 0 && N > 1) {
                output.join(" mixed with ");
            }

            // Check if "and" or "," is necessary
            if (i > 0 && i < N-1) {
                if (i == N-2) {
                    output.join(" and ");
                } else {
                    output.join(", ");
                }
            }
        }

        return output;
    }
}
