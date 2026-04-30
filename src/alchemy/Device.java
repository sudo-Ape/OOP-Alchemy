package alchemy;

import java.util.ArrayList;
import java.util.List;

public abstract class Device {

    // =================================================================================
    // Fields
    // =================================================================================

    protected List<Ingredient> internalIngredients = new ArrayList<>();
    private boolean terminated = false;
    protected Ingredient result = null;

    // =================================================================================
    // Constructor
    // =================================================================================
    public Device() {}

    // =================================================================================
    // Methods
    // =================================================================================
    /**
     * Adds a given amount of a certain ingredient to the device
     * The container is emptied after extraction (destructive operation)
     *
     * @param container IngredientContainer to extract from
     * @throws IllegalStateException If device is terminated
     * @throws IllegalStateException If container is null or empty
     *
     * @post Given container is emptied
     *      | WIP
     *
     * @post Given container is terminated
     *      | WIP
     */
    public void add(IngredientContainer container) throws IllegalStateException {
        if (isTerminated()){
            throw new IllegalStateException("Device is terminated.");
        }
        if (container == null || container.isEmpty()) {
            throw new IllegalStateException("Container is empty or null");
        }

        // Retrieve the ingredient from the container
        Ingredient toAdd = container.getContents();

        // Add the ingredients to device's internal list
        this.internalIngredients.add(toAdd);

        // Empty the container
        container.empty();
    }


    /**
     * Collects the result of the alchemical transformation
     * Must be called after run() has been completed
     * Returns a new IngredientContainer with the result
     *
     * @return IngredientContainer containing the result ingredient
     * @throws IllegalStateException If device is terminated
     * @throws IllegalStateException If device contains no result
     */
    public IngredientContainer collect() throws IllegalStateException {
        if (isTerminated()){
            throw new IllegalStateException("Device is terminated.");
        }
        if (result == null) {
            throw new IllegalStateException("No result exists for this device.");
        }

        // Choose an appropriate container unit for the result
        Unit chosenUnit = selectAppropriateUnit(result);

        // Construct a new IngredientContainer with the chosen unit and result ingredient
        IngredientContainer resultContainer = new IngredientContainer(chosenUnit, result);

        // Clear result
        result = null;

        return resultContainer;
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
     *
     * @throws IllegalArgumentException If the given ingredient does not fit into a container.
     *      | WIP
     */
    private static Unit selectAppropriateUnit(Ingredient ingredient) throws IllegalArgumentException {
        List<Unit> allowed = ingredient.getState().getAllowedUnits();

        // Loop forwards from second to second-to-last
        for (int i = 1; i < allowed.size() - 1; i++) {
            Unit unit = allowed.get(i);

            // Check if the ingredient's quantity fits in this unit
            if (ingredient.getQuantity().lessThan(unit)){
                return unit;
            }
        }

        throw new IllegalArgumentException("The given ingredient does not fit into a container.");
    }

    /**
     * Run the device on the set ingredient contents
     *
     * @post Result is set to device's function result
     *
     * @post Internal ingredients list is emptied
     *      | WIP
     */
    public abstract void run();

    /**
     * Check whether this device has been terminated
     *
     * @return Whether this device has been terminated
     */
    public boolean isTerminated() {
        return terminated;
    }
}
