package alchemy;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.List;

/**
 * Ingredient container class for OGP alchemy
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class IngredientContainer {
    // =================================================================================
    // Fields
    // =================================================================================
    private Unit capacity;
    private Ingredient contents = null;

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

    // =================================================================================
    // Setters
    // =================================================================================

    /**
     * Set the ingredient contents of this ingredient container
     *
     * @param contents Ingredient contents of this ingredient container
     */
    private void setContents(Ingredient contents) {
        this.contents = contents;
    }

    /**
     * Set the unit capacity for this ingredient container
     *
     * @param capacity Unit capacity for this ingredient container
     * @throws IllegalArgumentException - If the given capacity is null
     *
     *                                  - If the given capacity is a unit for which no container exists,
     *                                  specifically DROP, PINCH and STOREROOM
     *
     *                                  - If the current container currently holds an ingredient and the new capacity is not compatible with
     *                                  the ingredient's current state
     *
     *                                  - If the container currently holds an ingredient whose quantity exceeds the given capacity
     * @post The capacity of the container is set to the given unit
     * @note I think the capacity should always be initialized before the content to avoid the last 2 throws WIP?
     */
    private void setCapacity(Unit capacity) throws IllegalArgumentException {
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
     */
    public Ingredient getContents() {
        return contents;
    }


    /**
     * Get the unit capacity for this ingredient container
     *
     * @return Unit capacity for this ingredient container
     */
    public Unit getCapacity() {
        return capacity;
    }

    // =================================================================================
    // Methods
    // =================================================================================

    /**
     * Add given ingredient to this container
     *
     * @param ingredient Given ingredient to add
     * @throws IllegalArgumentException - If the given ingredient is null
     *                                  - If the container is not empty and the ingredient is of a different type
     *                                  - If adding the ingredient would exceed this container's capacity
     */
    public void add(Ingredient ingredient) throws IllegalArgumentException {
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
     */
    public void empty(){
        setContents(null);
    }

    public boolean isEmpty(){
        return this.getContents() == null;
    }
}

