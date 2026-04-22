package alchemy;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

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
     */
    private void setCapacity(Unit capacity) {
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
        // You can only add to an empty container, not add stuff to stuff (this would require hard checks or mixing)
        if (getContents() == null && ingredient.getQuantity().lessThan(getCapacity())) {
            setContents(ingredient);
        }

        // If we got here, something failed
        throw new IllegalArgumentException("The given ingredient cannot be added to this container!");
    }
}
