package alchemy;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.List;

/**
 * Ingredient container class to hold ingredients
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class IngredientContainer {
    // =================================================================================
    // Fields
    // =================================================================================
    private Unit capacity;
    private Ingredient contents = null;
    private boolean terminated;

    // =================================================================================
    // Constructors
    // =================================================================================

    /**
     * Create a new ingredient container of given unit capacity and with given contents
     *
     * @param capacity Given unit capacity
     * @param contents Given contents
     */
    public IngredientContainer(Unit capacity, Ingredient contents) {
        this.setCapacity(capacity);
        this.setContents(null);
        this.add(contents);
    }

    /**
     * Create a new empty ingredient container of given unit capacity
     *
     * @param capacity Given unit capacity
     */
    public IngredientContainer(Unit capacity) {
        this.setCapacity(capacity);
        this.setContents(null);
    }

    /**
     * Create a new ingredient container with given contents and corresponding minimum capacity
     *
     * @param contents Given contents
     */
    public IngredientContainer(Ingredient contents) {
        this.setCapacity(Quantity.selectAppropriateUnit(contents));
        this.setContents(contents);
    }

    // =================================================================================
    // Setters
    // =================================================================================

    /**
     * Set the ingredient contents of this ingredient container
     *
     * @param contents Ingredient contents of this ingredient container
     *
     * @throws IllegalStateException If container is terminated
     */
    private void setContents(Ingredient contents) throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This container has been terminated.");
        }
        this.contents = contents;
    }

    /**
     * Set the unit capacity for this ingredient container
     *
     * @param capacity Unit capacity for this ingredient container
     * @throws IllegalArgumentException If the given capacity is null
     * @throws IllegalArgumentException If the given capacity is a unit for which no container exists, specifically DROP, PINCH and STOREROOM
     *
     * @post The capacity of the container is set to the given unit
     *
     * @throws IllegalStateException If container is terminated
     */
    private void setCapacity(Unit capacity) throws IllegalArgumentException, IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This container has been terminated.");
        }

        // 1. Basic check
        if (capacity == null) throw new IllegalArgumentException("Null capacity");

        // 2. Unit restriction: No containers for smallest/largest units
        for (State state : State.values()) {
            if (capacity == state.getAllowedUnits().getFirst() || capacity == state.getAllowedUnits().getLast()){
                throw new IllegalArgumentException("No physical container exists for this unit.");
            }
        }

        this.capacity = capacity;
    }

    // =================================================================================
    // Getters
    // =================================================================================

    /**
     * Get the ingredient contents of this ingredient container
     *
     * @return Ingredient contents of this ingredient container
     *
     * @throws IllegalStateException If container is terminated
     */
    public Ingredient getContents() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This container has been terminated.");
        }
        return contents;
    }


    /**
     * Get the unit capacity for this ingredient container
     *
     * @return Unit capacity for this ingredient container
     *
     * @throws IllegalStateException If container is terminated
     */
    public Unit getCapacity() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This container has been terminated.");
        }

        return capacity;
    }

    // =================================================================================
    // Methods
    // =================================================================================

    /**
     * Add given ingredient to this container
     *
     * @param ingredient Given ingredient to add
     * @throws IllegalArgumentException If the given ingredient is null
     * @throws IllegalArgumentException If the container is not empty and the ingredient is of a different type
     * @throws IllegalArgumentException If adding the ingredient would exceed this container's capacity
     *
     * @throws IllegalStateException If container is terminated
     */
    public void add(Ingredient ingredient) throws IllegalArgumentException, IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This container has been terminated.");
        }

        if (ingredient == null)
            throw new IllegalArgumentException("Cannot add null ingredient.");

        if (getContents() == null) { // Container is empty
            if (!ingredient.getQuantity().lessThan(getCapacity())) {
                throw new IllegalArgumentException("Ingredient quantity exceeds container capacity.");
            }
            setContents(ingredient);
        } else { // Container already has contents
            // Check if the container allows the ingredient
            if (!getContents().equals(ingredient)) {
                throw new IllegalArgumentException("Ingredient type does not match container contents.");
            }

            // Check if sum of both ingredients exceeds the capacity
            Quantity combinedSpoons = getContents().getQuantity().plus(ingredient.getQuantity());
            if (!combinedSpoons.lessThan(getCapacity())) {
                throw new IllegalArgumentException("Combined quantity exceeds container capacity.");
            }

            // Nothing went wrong, update the quantity
            getContents().setQuantity(combinedSpoons);
        }
    }

    /**
     * Empties the given container
     *
     * @effect Contents of the given container is set to null
     *      | setContents(null)
     *
     * @throws IllegalStateException If container is terminated
     */
    public void empty() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This container has been terminated.");
        }

        setContents(null);
    }

    /**
     * Check whether this container is empty
     *
     * @return Whether this container is empty
     *
     * @throws IllegalStateException If container is terminated
     *
     * WIP this should also be true if there is an ingredient in there with quantity=0 !!!
     */
    public boolean isEmpty() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This container has been terminated.");
        }

        return this.getContents() == null;
    }

    /**
     * Check whether this device has been terminated
     *
     * @return Whether this device has been terminated
     */
    public boolean isTerminated() {
        return terminated;
    }
}

