package alchemy;

import java.util.Objects;
import java.util.Set;

/**
 * Ingredient class for OGP alchemy
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class Ingredient {
    // =================================================================================
    // Attributes
    // =================================================================================
    private IngredientType ingredientType;
    private Temperature temperature;
    private State state;
    private Quantity quantity;
    private String specialName;

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
     */
    private void setIngredientType(IngredientType ingredientType) {
        this.ingredientType = ingredientType;
    }

    /**
     * Set the temperature of this ingredient
     *
     * @param temperature Given temperature
     */
    private void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    /**
     * Set the state of this ingredient
     *
     * @param state Given state
     */
    private void setState(State state) {
        this.state = state;
    }

    /**
     * Set the special name of this ingredient
     *
     * @param specialName Given special name
     */
    public void setSpecialName(String specialName) {
        this.specialName = specialName;
    }

    /**
     * Set the quantity of this ingredient
     *
     * @param quantity Given quantity
     */
    private void setQuantity(Quantity quantity) {
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
    // Other methods
    // =================================================================================

    /**
     * Get the full name of this ingredient
     *
     * @return The full name of this ingredient
     */
    public String getFullName() {
        if (getTemperature().equals(getIngredientType().getStandardTemperature())) { // Temp = st. temp
            return getIngredientType().getSimpleName();
        } else if (getTemperature().getValue() < getIngredientType().getStandardTemperature().getValue()) { // Temp < st. temp (cooled)
            return "Cooled "+getIngredientType().getSimpleName();
        } else { // Temp > st. temp (heated)
            return "Heated"+getIngredientType().getSimpleName();
        }
    }
}
