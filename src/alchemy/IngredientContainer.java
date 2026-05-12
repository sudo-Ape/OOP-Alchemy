package alchemy;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.List;

/**
 * Ingredient container class to hold a single ingredient of a given unit capacity.
 * A container can be empty or hold an ingredient whose quantity does not exceed the container's capacity.
 * No physical container exists for the smallest or largest units of each state.
 *
 * @invar The capacity of this container must always be a valid unit, or this container is terminated
 *      | canHaveAsCapacity(capacity) || isTerminated()
 *
 * @invar The quantity of the contents must not exceed the capacity, or this container is terminated or empty
 *      | getContents() == null || getContents().getQuantity().lessThan(getCapacity()) || isTerminated()
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class IngredientContainer {
    // =================================================================================
    // Fields
    // =================================================================================

    /**
     * The unit capacity of this ingredient container
     */
    private Unit capacity;

    /**
     * The ingredient currently stored in this container, or null if the container is empty
     */
    private Ingredient contents = null;

    /**
     * Boolean indicating whether this container has been terminated
     */
    private boolean terminated;

    // =================================================================================
    // Constructors
    // =================================================================================

    /**
     * Create a new ingredient container of given unit capacity and with given contents
     *
     * @param capacity Given unit capacity
     * @param contents Given contents
     *
     * @effect Capacity is set to given capacity
     *      | setCapacity(capacity)
     *
     * @effect Given contents are added to this container
     *      | add(contents)
     *
     * @throws IllegalArgumentException If capacity is null or an invalid unit
     *      | capacity == null
     *
     * @throws IllegalArgumentException If contents is null or exceeds the capacity
     *      | contents == null || !contents.getQuantity().lessThan(capacity)
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
     *
     * @effect Capacity is set to given capacity
     *      | setCapacity(capacity)
     *
     * @post Contents are set to null
     *      | new.getContents() == null
     *
     * @throws IllegalArgumentException If capacity is null or an invalid unit
     *      | capacity == null
     */
    public IngredientContainer(Unit capacity) {
        this.setCapacity(capacity);
        this.setContents(null);
    }

    /**
     * Create a new ingredient container with given contents and a minimum fitting capacity
     *
     * @param contents Given contents
     *
     * @effect Capacity is set to the smallest unit that fits the contents
     *      | setCapacity(Quantity.selectAppropriateUnit(contents))
     *
     * @effect Contents are set to given contents
     *      | setContents(contents)
     *
     * @throws IllegalArgumentException If contents is null
     *      | contents == null
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
     * @post Contents are set to given contents
     *      | new.getContents() == contents
     *
     * @throws IllegalStateException If container is terminated
     *      | isTerminated()
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
     *
     * @post The capacity of the container is set to the given unit
     *      | new.getCapacity() == capacity
     *
     * @throws IllegalArgumentException If the given capacity is null
     *      | capacity == null
     *
     * @throws IllegalArgumentException If the given capacity is a unit for which no container exists
     *      | for some state in State.values():
     *      |   capacity == state.getAllowedUnits().getFirst() || capacity == state.getAllowedUnits().getLast()
     *
     * @throws IllegalStateException If container is terminated
     *      | isTerminated()
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
     *      | result == contents
     *
     * @throws IllegalStateException If container is terminated
     *      | isTerminated()
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
     *      | result == capacity
     *
     * @throws IllegalStateException If container is terminated
     *      | isTerminated()
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
     *
     * @post If the container was empty, its contents are set to the given ingredient
     *      | if getContents() == null:
     *      |   new.getContents() == ingredient
     *
     * @post If the container already held an ingredient of the same type, its quantity is updated to the combined quantity
     *      | if getContents() != null && getContents().equals(ingredient):
     *      |   new.getContents().getQuantity() == getContents().getQuantity().plus(ingredient.getQuantity())
     *
     * @throws IllegalStateException If container is terminated
     *      | isTerminated()
     *
     * @throws IllegalArgumentException If the given ingredient is null
     *      | ingredient == null
     *
     * @throws IllegalArgumentException If the container is not empty and the ingredient is of a different type
     *      | getContents() != null && !getContents().equals(ingredient)
     *
     * @throws IllegalArgumentException If adding the ingredient would exceed this container's capacity
     *      | !ingredient.getQuantity().lessThan(getCapacity())
     *      | || getContents() != null && !getContents().getQuantity().plus(ingredient.getQuantity()).lessThan(getCapacity())
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
     * Empties this container and terminates its contents
     *
     * @effect Contents of this container is set to null
     *      | setContents(null)
     *
     * @throws IllegalStateException If container is terminated
     *      | isTerminated()
     */
    public void empty() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This container has been terminated.");
        }

        // Terminate contents and empty
        getContents().terminate();
        setContents(null);
    }

    /**
     * Check whether this container is empty
     *
     * @return Whether this container is empty
     *      | result == (getContents() == null)
     *
     * @throws IllegalStateException If container is terminated
     *      | isTerminated()
     */
    public boolean isEmpty() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This container has been terminated.");
        }

        return this.getContents() == null;
    }

    /**
     * Check whether this container has been terminated
     *
     * @return Whether this container has been terminated
     *      | result == terminated
     */
    public boolean isTerminated() {
        return terminated;
    }

    /**
     * Terminate this container and its contents if any exist
     *
     * @post This container is terminated
     *      | new.isTerminated()
     *
     * @post If this container held an ingredient, that ingredient is terminated
     *      | if contents != null:
     *      |   contents.isTerminated()
     @note Ingredient container has no bidirectional associations. It holds an undirectional reference to Ingredient,
     *     but Ingredient has no back-reference to any container. Laboratory and Device both use containers as method
     *     parameters/return values only - again no back reference.
     */
    public void terminate() {
        if (contents != null) {
            contents.terminate();
        }
        terminated = true;
    }
}

