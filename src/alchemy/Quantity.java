package alchemy;
import java.util.Dictionary;

public class Quantity {
    private State state;
    private Dictionary Amount;


    /**
     * Calculate the amount of spoons based on the current unit the ingredient type has
     *
     * @return the amount of spoons
     */
    @Basic
    public int getSpoons(){}

    /**
     * List of quantities to sum to a given state.
     *
     * @note auto converts using the spoon unit
     * @note Not entirely sure yet of the documentation for this method
     */
    public static void sum(){}

    /**
     * Auto converts the units of the ingredient to the most efficient unit to store the ingredient in
     *
     * @note Documentation should get clearer during implementation
     */
    private static void simplifyUnit(){}
}
