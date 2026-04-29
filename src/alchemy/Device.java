package alchemy;

import java.util.ArrayList;
import java.util.List;

public abstract class Device {

    // =================================================================================
    // Fields
    // =================================================================================

    protected List<Ingredient> InternalIngredients = new ArrayList<>();
    private boolean isTerminated = false;



    // =================================================================================
    // Methods
    // =================================================================================


    /**
     * Adds a given amount of a certain ingredient to the device
     * The container is emptied after extraction (destructive operation)
     *
     * @param container Ingredientcontainer to extract from
     * @throws IllegalStateException if device is terminated
     * @throws IllegalStateException if container is null or emoty
     *
     */
    public void add(IngredientContainer container) throws IllegalStateException{
        if (this.isTerminated){
            throw new IllegalStateException("Device is terminated.");
        }
        if (container == null || container.isEmpty()) {
            throw new IllegalStateException("Container is empty or null");
        }

        // Retrieve the ingredient from the container
        Ingredient toAdd = container.getContents();

        // Add the ingredients to device's internal list
        this.InternalIngredients.add(toAdd);

        // Empty the container
        container.empty();
    }


    /**
     * Collects the result of the alchemical transformation
     * Must be called after run() has been completed
     * Returns a new IngredientContainer with the result
     *
     * @return IngredientContainer containing the result ingredient
     * @throws IllegalStateException if device is terminated
     * @throws IllegalStateException if device is empty
     */
    public IngredientContainer collect() throws IllegalStateException{
        if (this.isTerminated){
            throw new IllegalStateException("Device is terminated.");
        }
        if (InternalIngredients.isEmpty()) {
            throw new IllegalStateException("Device is either empty or does not exist.");
        }

        // Retrieve the single processed result ingredient
        Ingredient resultIngredient = InternalIngredients.getFirst();

        // Choose an appropriate container unit for the result
        Unit chosenUnit = selectAppropriateUnit(resultIngredient);

        // Construct a new IngredientContainer with the chosen unit and result ingredient
        IngredientContainer result = new IngredientContainer(chosenUnit, resultIngredient);

        // Clear InternalIngredients so the device is empty after collection
        InternalIngredients.empty();

        return result;
    }

    /**
     * Selects the most appropriate Unit for storing the given ingredient.
     *
     * 1. Get all allowed units for the ingredient's state (LIQUID or POWDER)
     * 2. Loop from the largest to smallest unit (excluding extremes: PINCH/DROP and STOREROOM)
     * 3. Return the first (largest) unit that can hold the ingredient's quantity
     *
     * @param ingredient The ingredient to select a unit for
     * @return The most appropriate Unit
     */
    private Unit selectAppropriateUnit(Ingredient ingredient){
        List<Unit> allowed = ingredient.getState().getAllowedUnits();

        // Loop backwards from second-to-last to second-first
        for (int i = allowed.size() - 2; i >= 1; i--) {
            Unit unit = allowed.get(i);

            // Check if the ingredient's quantity fits in this unit
            if (ingredient.getQuantity().lessThan(unit)){
                return unit;
            }
        }

        // Fallback: return the second-to last unit
        return allowed.get(allowed.size() - 2);
    }


    /**
     * Abstract method to be implemented by device subclasses.
     * This is where the actual alchemical transformation happens.
     *
     * Subclasses should override this to:
     * - Modify the ingredient(s) in InternalIngredients
     * - Create new ingredient(s) as needed
     * - Replace the original ingredients with the result(s)
     */
    public abstract void run();
}
