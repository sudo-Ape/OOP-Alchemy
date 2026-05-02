package alchemy;

import java.util.ArrayList;
import java.util.List;

public abstract class Device {
    // WIP: all devices' run methods should throw exceptions if the device is not in a laboratory (getLocation() == null)
    // WIP: proper termination (terminate() method and checks in every other method), for all classes?

    // =================================================================================
    // Fields
    // =================================================================================

    protected List<Ingredient> internalIngredients = new ArrayList<>();
    private boolean terminated = false;
    protected Ingredient result = null;
    private Laboratory location = null;

    // =================================================================================
    // Constructor
    // =================================================================================
    public Device() {}

    // =================================================================================
    // Location
    // =================================================================================

    /**
     * Get the location of this device
     *
     * @return Current location of this device
     */
    public Laboratory getLocation() {
        return location;
    }

    /**
     * Set the location for this device
     *
     * @param location New location for this device
     *
     * @note This method is package-private: only laboratories can call this method when you add a device to them (bidirectional association).
     */
    void setLocation(Laboratory location) {
        this.location = location;
    }


    // =================================================================================
    // Methods
    // =================================================================================
    /**
     * Adds a given amount of a certain ingredient to the device
     * The container is emptied after extraction (destructive operation)
     *
     * @param container IngredientContainer to extract from
     * @throws IllegalStateException If device is terminated
     * @throws IllegalArgumentException If container is null or empty
     * @throws IllegalStateException If device is not in a laboratory
     *
     * @post Given container is emptied
     *      | WIP
     *
     * @post Given container is terminated
     *      | WIP
     */
    public void add(IngredientContainer container) throws IllegalStateException, IllegalArgumentException {
        if (getLocation() == null) {
            throw new IllegalStateException("Device is not in a (valid) laboratory.");
        }
        if (isTerminated()){
            throw new IllegalStateException("Device is terminated.");
        }
        if (container == null || container.isEmpty()) {
            throw new IllegalArgumentException("Container is empty or null");
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
     * @throws IllegalStateException If device is not in a laboratory
     */
    public IngredientContainer collect() throws IllegalStateException {
        if (getLocation() == null) {
            throw new IllegalStateException("Device is not in a (valid) laboratory.");
        }
        if (isTerminated()){
            throw new IllegalStateException("Device is terminated.");
        }
        if (result == null) {
            throw new IllegalStateException("No result exists for this device.");
        }

        // Choose an appropriate container unit for the result
        Unit chosenUnit = Quantity.selectAppropriateUnit(result);

        // Construct a new IngredientContainer with the chosen unit and result ingredient
        IngredientContainer resultContainer = new IngredientContainer(chosenUnit, result);

        // Clear result
        result = null;

        return resultContainer;
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
