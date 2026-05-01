package alchemy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Kettle extends Device {
    // =================================================================================
    // Fields
    // =================================================================================
    private static final Temperature stateDiffTemperature = new Temperature(20);
    private static final State priorityState = State.LIQUID;

    // =================================================================================
    // Mixing methods
    // =================================================================================
    @Override
    public void run() {
        result = mix(internalIngredients);
        internalIngredients.clear();
    }

    /**
     * Static method to mix a given list of ingredients.
     *
     * - Name:                 Union of all basicIngredients sets; forms name automatically.
     * - State:                State of the ingredient whose standard temperature is closest
     *                         to stateDiffTemperature (default 20); LIQUID beats POWDER on a tie.
     * - Standard state:       Equal to resolved state.
     * - Standard temperature: Standard temperature of the closest ingredient; hottest wins on a tie.
     * - Quantity:             Sum of all quantities converted to the resolved state.
     * - Temperature:          Weighted average of current temperatures, weighted by spoons.
     *
     * @param ingredients Given list of ingredients to mix
     * @return New ingredient as mix of given ingredients, or null if the list is null/empty
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
            if (dist < nearestToStateDiff) {
                nearestToStateDiff = dist;
                resultState = ingredient.getState();
                resultStdTemp = type.getStandardTemperature();
            } else if (dist == nearestToStateDiff) {
                if (ingredient.getState() == priorityState) {
                    resultState = priorityState;
                }
                if (resultStdTemp == null || resultStdTemp.lessThan(type.getStandardTemperature())) {
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

        int resultTempValue = totalSpoons > 0
                ? (int) Math.round(weightedTempSum / totalSpoons)
                : stateDiffTemperature.getValue();

        IngredientType resultType = new IngredientType(resultStdTemp, resultState, totalBasicIngredients);
        return new Ingredient(resultType, new Temperature(resultTempValue), resultState, resultQuantity);
    }
}