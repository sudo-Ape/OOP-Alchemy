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
    public IngredientContainer(Unit capacity, Ingredient contents) {
        // CAPACITY CANNOT BE SMALLEST OR LARGEST! WIP
        this.setCapacity(capacity);
        this.contents = contents;
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
     *                                  <p>
     *                                  - If the given capacity is a unit for which no container exists,
     *                                  specifically DROP, PINCH and STOREROOM
     *                                  <p>
     *                                  - If the current container currently holds an ingredient and the new capacity is not compatible with
     *                                  the ingredient's current state
     *                                  <p>
     *                                  - If the container currently holds an ingredient whose quantity exceeds the given capacity
     * @post The capacity of the container is set to the given unit
     * @note I think the capacity should always be initialized before the content to avoid the last 2 throws WIP?
     */
    private void setCapacity(Unit capacity) throws IllegalArgumentException {
        // 1. Basic check
        if (capacity == null) throw new IllegalArgumentException("Null capacity");

        // 2. Unit restriction: No containers for smallest/largest units
        if (capacity == Unit.DROP || capacity == Unit.PINCH || capacity == Unit.STOREROOM) {
            throw new IllegalArgumentException("No physical container exists for this unit.");
        }

        // 3. Content checks (only if container is NOT empty)
        if (this.contents != null) {
            // State Check: Must match Liquid/Powder units
            if (!this.contents.getState().getAllowedUnits().contains(capacity)) {
                throw new IllegalArgumentException("Incompatible state for this unit.");
            }

            // Volume Check: Cannot be smaller than current amount
            if (this.contents.getQuantity().getSpoons() > capacity.getSpoons()) {
                throw new IllegalArgumentException("New capacity is too small for current contents.");
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
     *
     * @throws IllegalArgumentException If the given ingredient cannot be added to this container
     */
    public void add(Ingredient ingredient) throws IllegalArgumentException {
        if (ingredient == null)
            throw new IllegalArgumentException("Cannot add null ingredient.");

        // Container is empty
        if (getContents() == null) {
            if (ingredient.getQuantity().getSpoons() > getCapacity().getSpoons())
                throw new IllegalArgumentException("Ingredient quantity exceeds container capacity.");
            setContents(ingredient);

        } else {
            // Check if the container allows the ingredient
            if (!getContents().getIngredientType().equals(ingredient.getIngredientType()))
                throw new IllegalArgumentException("Ingredient type does not match container contents.");

            // Check if sum of both ingredients exceeds the capacity
            double combinedSpoons = getContents().getQuantity().getSpoons()
                    + ingredient.getQuantity().getSpoons();
            if (combinedSpoons > getCapacity().getSpoons())
                throw new IllegalArgumentException("Combined quantity exceeds container capacity.");


            Quantity merged = getContents().getQuantity().plus(ingredient.getQuantity());
            setContents(new Ingredient(
                    getContents().getIngredientType(),
                    getContents().getTemperature(),
                    getContents().getState(),
                    merged
            ));
        }
    }

    /**
     * Adds a given quantity of the ingredient to the container in which it is already in
     *
     * @param quantity Given quantity to add
     *
     * @throws IllegalStateException    If this container is empty (no ingredient to add to)
     *
     * @throws IllegalArgumentException If the given quantity's state does not match the container contents
     *                                  If adding the quantity would exceed this container's capacity
     */
    public void addQuantity(Quantity quantity) throws IllegalStateException, IllegalArgumentException {
        if (getContents() == null)
            throw new IllegalStateException("Cannot add quantity to an empty container.");

        if (!quantity.getState().equals(getContents().getState()))
            throw new IllegalArgumentException("Quantity state does not match container contents.");

        double combinedSpoons = getContents().getQuantity().getSpoons() + quantity.getSpoons();
        if (combinedSpoons > getCapacity().getSpoons())
            throw new IllegalArgumentException("Combined quantity exceeds container capacity.");

        Quantity merged = getContents().getQuantity().plus(quantity);
        setContents(new Ingredient(
                getContents().getIngredientType(),
                getContents().getTemperature(),
                getContents().getState(),
                merged
        ));
    }
}

