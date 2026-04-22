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
    public Ingredient(String basicIngredient, Temperature temperature, State state, Quantity quantity, State standardState, Temperature standardTemperature) {
        // Create the necessary ingredient type
        IngredientType newIngredientType = new IngredientType(standardTemperature,standardState, Set.of(basicIngredient));

        // Set stuff
        setQuantity(quantity);
        setIngredientType(newIngredientType);
        setTemperature(temperature);
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
    public Ingredient(String basicIngredient, Temperature temperature, State state, Quantity quantity) {
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
    private void setIngredientType(IngredientType ingredientType) {
        this.ingredientType = ingredientType;
    }

    private void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    private void setState(State state) {
        this.state = state;
    }

    public void setSpecialName(String specialName) {
        this.specialName = specialName;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    // =================================================================================
    // Getters
    // =================================================================================
    public IngredientType getIngredientType() {
        return ingredientType;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public State getState() {
        return state;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public String getSpecialName() {
        return specialName;
    }
}
