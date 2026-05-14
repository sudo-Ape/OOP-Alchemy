package alchemy;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.List;

/**
 * Ingredient container class to hold a single ingredient with quantity up to a given unit capacity.
 * A container can be empty or hold an ingredient whose quantity does not exceed the container's capacity.
 * No physical container exists for the smallest or largest units of each state.
 *
 * @invar The capacity of this container must always be a valid unit, or this container is terminated
 *      | canHaveAsCapacity(capacity) || isTerminated()
 *
 * @invar The content quantity must not exceed the capacity, or this container is terminated, or it is empty
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
    private Unit capacity = null;

    /**
     * The ingredient currently stored in this container, or null if the container is empty
     */
    private Ingredient contents = null;

    /**
     * Boolean indicating whether this container has been terminated
     */
    private boolean terminated = false;

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
     */
    public IngredientContainer(Unit capacity, Ingredient contents) {
        this.setCapacity(capacity);
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
     * @effect Given contents are added to this container
     *      | add(contents)
     */
    public IngredientContainer(Ingredient contents) {
        this.setCapacity(Quantity.selectAppropriateUnit(contents));
        this.add(contents);
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
     * @throws IllegalArgumentException If the given capacity is not allowed
     *      | !canHaveAsCapacity(capacity)
     *
     * @throws IllegalStateException If container is terminated
     *      | isTerminated()
     */
    private void setCapacity(Unit capacity) throws IllegalArgumentException, IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This container has been terminated.");
        }

        if (!canHaveAsCapacity(capacity)) {
            throw new IllegalArgumentException("Given capacity is invalid.");
        }

        this.capacity = capacity;
    }

    /**
     * Check whether the given unit capacity can be used
     *
     * @param capacity Given unit capacity
     *
     * @return True if the given capacity is not null and is not any state's smallest or largest allowed unit; false otherwise
     *      | for state in State.values():
     *      |   if capacity == state.getAllowedUnits().getFirst() || capacity == state.getAllowedUnits().getLast():
     *      |       result == false
     *      | result == (capacity != null)
     */
    public static boolean canHaveAsCapacity(Unit capacity) {
        for (State state : State.values()) {
            if (capacity == state.getAllowedUnits().getFirst() || capacity == state.getAllowedUnits().getLast()){
                return false;
            }
        }

        return capacity != null;
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
     * @post If container contents have been terminated, remove the association
     *      | if contents.isTerminated():
     *      |   contents = null
     *
     * @throws IllegalStateException If container is terminated
     *      | isTerminated()
     */
    @Basic
    public Ingredient getContents() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This container has been terminated.");
        }

        if (contents != null && contents.isTerminated()) { // Safe-check if this container's contents have been terminated at some point
            contents = null;
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
    @Basic
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
     * @post If old container contents were terminated, their association is removed
     *      | if contents.isTerminated():
     *      |   contents = null
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
     *      | !ingredient.getQuantity().lessThan(getCapacity()) || getContents() != null && !getContents().getQuantity().plus(ingredient.getQuantity()).lessThan(getCapacity())
     *
     * @throws IllegalArgumentException If ingredient's state does not allow container capacity as a unit
     *      | !ingredient.getState().getAllowedUnits().contains(getCapacity())
     */
    public void add(Ingredient ingredient) throws IllegalArgumentException, IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This container has been terminated.");
        }

        if (!ingredient.getState().getAllowedUnits().contains(getCapacity())) {
            throw new IllegalArgumentException("This container cannot hold an ingredient of that state.");
        }

        if (contents != null && contents.isTerminated()) { // Safe-check if this container's contents have been terminated at some point
            contents = null;
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
     * @post Old contents are terminated
     *      | getContents().isTerminated()
     *
     * @throws IllegalStateException If container is terminated
     *      | isTerminated()
     */
    public void empty() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This container has been terminated.");
        }

        // Terminate contents and empty (if not empty already)
        if (getContents() != null) {
            getContents().terminate();
            setContents(null);
        }
    }

    /**
     * Check whether this container is empty
     *
     * @post If old container contents were terminated, their association is removed
     *      | if contents.isTerminated():
     *      |   contents = null
     *
     * @return Whether this container is empty or the contents' quantity is zero
     *      | result == (getContents() == null || getContents().getQuantity().isZero())
     */
    public boolean isEmpty() throws IllegalStateException {
        if (contents != null && contents.isTerminated()) { // Safe-check if this container's contents have been terminated at some point
            contents = null;
        }

        return this.getContents() == null || this.getContents().getQuantity().isZero();
    }

    /**
     * Check whether this container has been terminated
     *
     * @return Whether this container has been terminated
     *      | result == terminated
     */
    @Basic
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
     */
    @Raw
    public void terminate() {
        if (contents != null) {
            contents.terminate();
        }
        terminated = true;
    }
}

