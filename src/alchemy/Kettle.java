package alchemy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Kettle class for a machine that mixes ingredients in the laboratory
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class Kettle extends Device {
    // =================================================================================
    // Fields
    // =================================================================================
    /**
     * This temperature determines which (standard) state and standard temperature comes out of a mixture,
     * as this is chosen by the mix ingredient whose own standard temperature is closest to stateDiffTemperature.
     */
    private static final Temperature stateDiffTemperature = new Temperature(20);

    /**
     * This state wins ties if they come up when determining (standard) state, aka when two ingredients of different states are
     * both closest to stateDiffTemperature, the priorityState will win.
     */
    private static final State priorityState = State.LIQUID;

    // =================================================================================
    // Mixing methods
    // =================================================================================

    /**
     * Run the kettle on its internal ingredients
     *
     * @effect Does primary validity checks via abstract Device class
     *      | super.run()
     *
     * @post Device result is set to mixing result
     *      | result == Kettle.mix(internalIngredients)
     *
     * @post Internal ingredients list is emptied
     *      | new.internalIngredients.isEmpty()
     */
    @Override
    public void run() throws IllegalStateException {
        super.run(); // Do repetitive checks here!

        result = mix(internalIngredients);

        // Clear internal ingredients
        clearInternalIngredients();
    }

    /**
     * Static method to mix a given list of ingredients. Procedures per property are given as:
     * - Name:                 Union of all basicIngredients sets; forms name automatically.
     * - State:                State of the ingredient whose standard temperature is closest
     *                         to stateDiffTemperature (default 20); LIQUID beats POWDER on a tie.
     * - Standard state:       Equal to resolved state.
     * - Standard temperature: Standard temperature of the closest ingredient; hottest wins on a tie.
     * - Quantity:             Sum of all quantities converted to the resolved state.
     * - Temperature:          Weighted average of current temperatures, weighted by spoons.
     *
     * @param ingredients Given list of ingredients to mix
     *
     * @return New ingredient as mix of given ingredients, or null if the list is null/empty
     *      | Formal definition of this method is too complex.
     *
     * @note A static method is required here for later use in Laboratory.
     */
    public static Ingredient mix(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) return null;

        // --------- Name ---------
        Set<String> totalBasicIngredients = new HashSet<>();

        // --------- State & Standard Temperature ---------
        int nearestToStateDiff = Integer.MAX_VALUE;
        State resultState = priorityState;
        Temperature resultStdTemp = null;

        // --------- Quantity & Temperature ---------
        List<Quantity> quantities = new ArrayList<>();
        double totalSpoons = 0;
        double weightedTempSum = 0;

        for (Ingredient ingredient : ingredients) {
            IngredientType type = ingredient.getIngredientType();

            // Name
            totalBasicIngredients.addAll(type.getBasicIngredients());

            // State and standard temperature: track ingredient closest to stateDiffTemperature
            int dist = type.getStandardTemperature().difference(stateDiffTemperature);
            if (dist < nearestToStateDiff || resultStdTemp == null) {
                nearestToStateDiff = dist;
                resultState = ingredient.getState();
                resultStdTemp = type.getStandardTemperature();
            } else if (dist == nearestToStateDiff) {
                if (ingredient.getState() == priorityState) {
                    resultState = priorityState;
                }
                if (resultStdTemp.lessThan(type.getStandardTemperature())) {
                    resultStdTemp = type.getStandardTemperature();
                }
            }

            // Quantity
            quantities.add(ingredient.getQuantity());

            // Weighted temperature
            double spoons = ingredient.getQuantity().getSpoons();
            totalSpoons += spoons;
            weightedTempSum += spoons * ingredient.getTemperature().getValue();
        }

        // --------- Combine ---------
        Quantity resultQuantity = Quantity.sum(quantities, resultState);

        int resultTempValue = totalSpoons > 0 ? (int) Math.round(weightedTempSum / totalSpoons) : stateDiffTemperature.getValue();

        IngredientType resultType = new IngredientType(resultStdTemp, resultState, totalBasicIngredients);
        return new Ingredient(resultType, new Temperature(resultTempValue), resultState, resultQuantity);
    }

    /**
     * Terminate this kettle
     *
     * @post Kettle is untied from any laboratory it may have been in
     *      | if (old) getLocation() != null:
     *      |   getLocation() == null
     *
     * @post Any laboratory this kettle was tied to is updated
     *      | if (old) getLocation != null:
     *      |   getLocation().getCoolingBox() == null
     *
     * @effect Abstract superclass termination effects are performed
     *      | super.terminate()
     */
    @Override
    public void terminate() {
        if (getLocation() != null) {
            getLocation().setKettle(null);
        }
        super.terminate();
    }
}