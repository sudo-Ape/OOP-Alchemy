package alchemy;

import java.util.Map;

public class Transmogrifier extends Device {
    // =================================================================================
    // Methods
    // =================================================================================

    /**
     * WIP...
     *
     * @effect State is set to new State
     *         | setState(newState)
     *
     * @effect Quantity is set to new Quantity
     *         | setQuantity(newQuantity)
     *
     * @note setState (Ingredient) was made package private to easily here.
     *       This follows the same structure setQuantity already had.
     */
    @Override
    public void run() {
        if (internalIngredients.isEmpty()) {
            throw new IllegalStateException("The storage of the transmogrifier is empty.");
        }

        Ingredient ingredient = internalIngredients.getFirst();

        // Get number of spoons
        double spoons = ingredient.getQuantity().getSpoons();

        // Toggle State
        State newState = ingredient.getState() == State.LIQUID ? State.POWDER : State.LIQUID;

        // Build a bridge Quantity
        Quantity newQuantity = new Quantity(newState, Map.of(Unit.SPOON,(int) spoons));

        // Update the ingredient
        ingredient.setState(newState);
        ingredient.setQuantity(newQuantity);

        // Pack into larger units
        newQuantity.simplifyUnit();

        result = ingredient;
    }
}

