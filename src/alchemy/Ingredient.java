package alchemy;

import java.util.Objects;

public class Ingredient {
    private IngredientType ingredientType;
    private Temperature temperature;
    private State state;
    private Quantity quantity;
    private String specialName;

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

    /**
     * Checks if this ingredient equals given other ingredient, ignoring quantity
     *
     * @param other Other ingredient to compare with
     * @return Whether this ingredient equals given other ingredient, ignoring quantity
     *
     * @note Not finished! WIP!
     */
    public boolean equals(Ingredient other) {
        return true;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(Quantity quantity) {
        this.setQuantity(getQuantity().plus(quantity));
    }
}
