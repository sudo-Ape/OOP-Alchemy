package alchemy;

import java.util.Objects;
import java.util.Set;

/**
 * Main alchemic ingredient class
 *
 * @invar Ingredient type must always be valid
 *      | canHaveAsIngredientType(getIngredientType())
 *
 * @invar Temperature must always be valid
 *      | canHaveAsTemperature(getTemperature())
 *
 * @invar Quantity must always be valid
 *      | canHaveAsQuantity(getQuantity())
 *
 * @invar Special name must always be valid
 *      | canHaveAsSpecialName(getSpecialName())
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class Ingredient {
    // =================================================================================
    // Fields
    // =================================================================================
    /**
     * Ingredient type associated with this ingredient
     *
     * @invar ingredientType references an effective object
     *      | ingredientType != null
     */
    private IngredientType ingredientType;

    /**
     * Temperature associated with this ingredient
     *
     * @invar temperature references an effective object
     *      | temperature != null
     */
    private Temperature temperature;

    /**
     * Variable referencing the state enum associated with this ingredient
     */
    private State state;

    /**
     * Quantity associated with this ingredient
     *
     * @invar quantity references an effective object
     *      | quantity != null
     */
    private Quantity quantity;

    /**
     * Special name string associated with this ingredient
     *
     * @invar specialName must be valid, akin to basic ingredient names in IngredientType
     *      | canHaveAsSpecialName(specialName)
     */
    private String specialName;

    /**
     * Static default ingredient type as total programming fall-back in case of invalid user input
     */
    private static final IngredientType defaultIngredientType = new IngredientType(new Temperature(20),State.LIQUID,Set.of("Water"));

    // =================================================================================
    // Constructors
    // =================================================================================
    /**
     * Create a new basic ingredient with given name, temperature, state, quantity, standard temperature, and standard state
     *
     * @param basicIngredient Given basic ingredient name
     * @param temperature Given temperature
     * @param state Given state
     * @param quantity Given quantity
     * @param standardState Given standard state
     * @param standardTemperature Given standard temperature
     *
     * @effect Ingredient type is set to a new ingredient type with standardState, standardTemperature, and a set containing only basicIngredient as basicIngredients
     *      | WIP
     *
     * @effect Quantity is set to given quantity
     *      | setQuantity(quantity)
     *
     * @effect Temperature is set to new object based on given temperature input
     *      | setTemperature(new Temperature(temperature))
     *
     * @effect State is set to given state
     *      | setState(state)
     */
    public Ingredient(String basicIngredient, String temperature, State state, Quantity quantity, State standardState, String standardTemperature) {
        // Create the necessary ingredient type
        IngredientType newIngredientType = new IngredientType(new Temperature(standardTemperature),standardState, Set.of(basicIngredient));

        // Set stuff
        setQuantity(quantity);
        setIngredientType(newIngredientType);
        setTemperature(new Temperature(temperature));
        setState(state);
    }

    /**
     * Create a new basic ingredient with given name, temperature, state, and quantity (all in standard)
     *
     * @param basicIngredient Given basic ingredient name
     * @param temperature Given temperature (equal standard temperature)
     * @param state Given state (equal standard state)
     * @param quantity Given quantity
     */
    public Ingredient(String basicIngredient, String temperature, State state, Quantity quantity) {
        this(basicIngredient,temperature,state,quantity,state,temperature);
    }

    /**
     * Package-private constructor to make non-basic ingredients with custom ingredientTypes (used by devices)
     *
     * @param ingredientType Given ingredient type
     * @param temperature Given temperature
     * @param state Given state
     * @param quantity Given quantity
     */
    Ingredient(IngredientType ingredientType, Temperature temperature, State state, Quantity quantity) {
        this.ingredientType = ingredientType;
        this.temperature = temperature;
        this.state = state;
        this.quantity = quantity;
    }

    // =================================================================================
    // Setters
    // =================================================================================

    /**
     * Set the ingredient type of this ingredient
     *
     * @param ingredientType Given ingredient type
     *
     * @post Ingredient type is given ingredient type if it is valid; ingredient type is defaultIngredientType otherwise
     *      | if (canHaveAsIngredientType(ingredientType)):
     *      |   new.getIngredientType() == ingredientType
     *      | else:
     *      |   new.getIngredientType() == defaultIngredientType
     */
    private void setIngredientType(IngredientType ingredientType) {
        if (canHaveAsIngredientType(ingredientType)) {
            this.ingredientType = ingredientType;
        } else {
            this.ingredientType = defaultIngredientType; // Total programming fall-back
        }
    }

    /**
     * Set the temperature of this ingredient
     *
     * @param temperature Given temperature
     *
     * @post Temperature is given temperature if it is valid; temperature is "(0,20)" otherwise
     *      | if (canHaveAsTemperature(temperature)):
     *      |   new.getTemperature() == temperature
     *      | else:
     *      |   new.getTemperature() == new Temperature(20)
     *
     * @note This method is package-private: the user should not randomly change the state, but specific in-package uses will!
     */
    void setTemperature(Temperature temperature) {
        if (canHaveAsTemperature(temperature)) {
            this.temperature = temperature;
        } else {
            this.temperature = new Temperature(20); // Total programming fall-back
        }
    }

    /**
     * Set the state of this ingredient
     *
     * @param state Given state
     *
     * @post State is given state
     *      | new.getState() == state
     *
     * @note This method is package-private: the user should not randomly change the state, but specific in-package uses will!
     */
    void setState(State state) {
        this.state = state;
    }

    /**
     * Set the special name of this ingredient
     *
     * @param specialName Given special name
     *
     * @post Special name is given special name
     *      | new.getSpecialName() == specialName
     *
     * @throws IllegalArgumentException If the given special name is invalid
     *      | !canHaveAsSpecialName(specialName)
     */
    public void setSpecialName(String specialName) throws IllegalArgumentException {
        if (canHaveAsSpecialName(specialName)) {
            this.specialName = specialName;
        } else {
            throw new IllegalArgumentException("The given special name is invalid.");
        }
    }

    /**
     * Set the quantity of this ingredient
     *
     * @param quantity Given quantity
     *
     * @pre Given quantity is valid
     *      | canHaveAsQuantity(quantity)
     *
     * @post Quantity is given quantity
     *      | new.getQuantity() == quantity
     *
     * @note This method is package-private: the user should not randomly change an ingredient's quantity, only specific in-package use-cases will do this!
     */
    void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    // =================================================================================
    // Getters
    // =================================================================================

    /**
     * Get the ingredient type of this ingredient
     *
     * @return Ingredient type of this ingredient
     */
    public IngredientType getIngredientType() {
        return ingredientType;
    }

    /**
     * Get the temperature of this ingredient
     *
     * @return Temperature of this ingredient
     */
    public Temperature getTemperature() {
        return temperature;
    }

    /**
     * Get the state of this ingredient
     *
     * @return State of this ingredient
     */
    public State getState() {
        return state;
    }

    /**
     * Get the quantity of this ingredient
     *
     * @return Quantity of this ingredient
     */
    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * Get the special name of this ingredient
     *
     * @return Special name of this ingredient
     */
    public String getSpecialName() {
        return specialName;
    }

    // =================================================================================
    // Inspectors
    // =================================================================================

    /**
     * Check whether given special name is valid
     *
     * @param specialName Given special name
     *
     * @return True if given special name upholds same requirements as basic ingredient name; false otherwise
     *      | result == IngredientType.canHaveAsBasicIngredient(specialName)
     */
    public static boolean canHaveAsSpecialName(String specialName) {
        // The special name must uphold the same requirements as a basic ingredient name
        return IngredientType.canHaveAsBasicIngredient(specialName);
    }

    /**
     * Check whether given ingredient type is valid
     *
     * @param ingredientType Given ingredient type
     *
     * @return True if given ingredient type is not null; false otherwise
     *      | result == (ingredientType != null)
     */
    public static boolean canHaveAsIngredientType(IngredientType ingredientType) {
        return ingredientType != null;
    }

    /**
     * Check whether given temperature is valid
     *
     * @param temperature Given temperature
     *
     * @return True if given temperature is not null; false otherwise
     *      | result == (temperature != null)
     */
    public static boolean canHaveAsTemperature(Temperature temperature) {
        return temperature != null;
    }

    /**
     * Check whether given quantity is valid
     *
     * @param quantity Given quantity
     *
     * @return True if given quantity is not null; false otherwise
     *      | result == (quantity != null)
     */
    public static boolean canHaveAsQuantity(Quantity quantity) {
        return quantity != null;
    }

    // =================================================================================
    // Other methods
    // =================================================================================

    /**
     * Get the full name of this ingredient
     *
     * @return Simple name preceded by "Cooled" or "Heated" based on temperature's relation to standard temperature; if special name available, then "SPECIALNAME (Heated/Cooled+SIMPLENAME)"
     *      | WIP
     */
    public String getFullName() {
        // Generic full name
        String genericFullName;

        if (getTemperature().equals(getIngredientType().getStandardTemperature())) { // Temp = st. temp
            genericFullName = getIngredientType().getSimpleName();
        } else if (getTemperature().getValue() < getIngredientType().getStandardTemperature().getValue()) { // Temp < st. temp (cooled)
            genericFullName = "Cooled "+getIngredientType().getSimpleName();
        } else { // Temp > st. temp (heated)
            genericFullName = "Heated"+getIngredientType().getSimpleName();
        }

        // Check for special name
        if (getSpecialName() != null) {
            return getSpecialName()+" ("+genericFullName+")";
        }
        return genericFullName;
    }

    /**
     * Check whether this ingredient equals a given other, regardless of quantity
     *
     * @param other Given other ingredient
     *
     * @return True if states match and temperatures are equal and ingredient types are equal; false otherwise
     *      | result == (getState() == other.getState() && getTemperature().equals(other.getTemperature()) && getIngredientType().equals(other.getIngredientType()))
     */
    public boolean equals(Ingredient other) {
        return getState() == other.getState() &&
               getTemperature().equals(other.getTemperature()) &&
               getIngredientType().equals(other.getIngredientType());
    }
}