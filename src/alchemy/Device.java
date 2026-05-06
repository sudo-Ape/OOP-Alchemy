package alchemy;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Device class for machines in a laboratory
 *
 * @invar List of internal ingredient must always be valid, or device is terminated
 *      | canHaveAsInternalIngredients(internalIngredients) || isTerminated()
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public abstract class Device {
    // =================================================================================
    // Fields
    // =================================================================================
    /**
     * List of ingredients currently inside the device
     *
     * @invar internalIngredients must be an effective list
     *      | internalIngredients != null
     *
     * @invar Each Ingredient in internalIngredients must be an effective Ingredient
     *      | for ingredient in internalIngredients:
     *      |   ingredient != null
     */
    protected List<Ingredient> internalIngredients = new ArrayList<>();

    /**
     * Boolean indicating whether this device has been terminated
     */
    private boolean terminated = false;

    /**
     * Result of the alchemic operation formed by Device's subclass implementations
     */
    protected Ingredient result = null;

    /**
     * Current location of the device: either a Laboratory or null if placed nowhere. A device can only be used if it is inside a Laboratory.
     */
    private Laboratory location = null;

    // =================================================================================
    // Constructor
    // =================================================================================

    /**
     * Create a new device
     */
    public Device() {}

    // =================================================================================
    // Location
    // =================================================================================

    /**
     * Get the location of this device
     *
     * @return Current location of this device
     *
     * @throws IllegalStateException If device has been terminated
     *      | isTerminated()
     */
    public Laboratory getLocation() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This device has been terminated.");
        }
        return location;
    }

    /**
     * Set the location for this device
     *
     * @param location New location for this device
     *
     * @post Location is given location
     *      | new.getLocation() == location
     *
     * @throws IllegalStateException If device has been terminated
     *      | isTerminated()
     *
     * @note This method is package-private: only laboratories can call this method when you add a device to them (bidirectional association).
     */
    void setLocation(Laboratory location) throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This device has been terminated.");
        }
        this.location = location;
    }

    // =================================================================================
    // Termination (destructors)
    // =================================================================================

    /**
     * Terminate this device
     *
     * @post Device is terminated
     *      | isTerminated()
     */
    public void terminate() {
        terminated = true;
    }

    /**
     * Check whether this device has been terminated
     *
     * @return Whether this device has been terminated
     */
    public boolean isTerminated() {
        return terminated;
    }

    // =================================================================================
    // Methods
    // =================================================================================
    /**
     * Adds contents of given ingredient container to the device
     *
     * @param container IngredientContainer to extract from
     *
     * @throws IllegalStateException If device is terminated
     *      | isTerminated()
     *
     * @throws IllegalArgumentException If container is null or empty
     *      | container == null || container.isEmpty()
     *
     * @throws IllegalStateException If device is not in a laboratory
     *      | getLocation() == null
     *
     * @post Given container is emptied
     *      | WIP
     *
     * @post Given container is terminated
     *      | container.isTerminated()
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
        // WIP need to terminate the container still, after terminate() is made, also is emptying even necessary then? (if not remove post too)
    }


    /**
     * Collects the result of the alchemical transformation into a fitting container, to be called after run() has been completed
     *
     * @return IngredientContainer containing the result ingredient
     *      | result == new IngredientContainer(Quantity.selectAppropriateUnit(result),result)
     *
     * @post Result is removed from device
     *      | new.result == null
     *
     * @throws IllegalStateException If device is terminated
     *      | isTerminated()
     *
     * @throws IllegalStateException If device contains no result
     *      | result == null
     *
     * @throws IllegalStateException If device is not in a laboratory
     *      | getLocation() == null
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
     * Check whether given list of internal ingredients is valid
     *
     * @param internalIngredients Given list of internal ingredients
     *
     * @return True if list is non-null and all its contents are non-null; false otherwise
     *      | if internalIngredients == null: result == false
     *      | for ingredient in internalIngredients:
     *      |   if ingredient == null: result == false
     *      | result == true
     */
    public static boolean canHaveAsInternalIngredients(List<Ingredient> internalIngredients) {
        if (internalIngredients == null) {
            return false;
        }

        for (Ingredient ingredient : internalIngredients) {
            if (ingredient == null) {
                return false;
            }
        }

        return true;
    }
}
