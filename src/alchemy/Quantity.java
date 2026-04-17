package alchemy;
import be.kuleuven.cs.som.annotate.Basic;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class Quantity {
    private State state;
    private Dictionary<Unit,Integer> Amount;

    public Quantity(State state, Dictionary<Unit, Integer> amount) {
        this.state = state;
        // Needs checks for legal units for this state!
        Amount = amount;
    }

    /**
     * Calculate the amount of spoons based on the current unit the ingredient type has
     *
     * @return the amount of spoons
     */
    @Basic
    public int getSpoons(){
        return 0;
    }

    /**
     * List of quantities to sum to a given state.
     *
     * @note auto converts using the spoon unit
     * @note Not entirely sure yet of the documentation for this method
     * @note WIP
     */
    public static Quantity sum(List<Quantity> quantities, State goalState) {
        return null;
    }

    /**
     * Return this quantity plus given other quantity
     *
     * @param other Other quantity to sum with
     *
     * @return New quantity equal to this quantity plus given other quantity
     */
    public Quantity plus(Quantity other, State goalState) {
        List<Quantity> input = new ArrayList<>();
        input.add(this);
        input.add(other);

        return sum(input, goalState);
    }

    /**
     * Return this quantity plus given other quantity, assuming both are of the same state
     *
     * @param other Other quantity to sum with
     *
     * @return New quantity equal to this quantity plus given other quantity
     *
     * @throws IllegalArgumentException If this quantity and given other quantity are not of equal state
     *      | !getState().equals(other.getState())
     */
    public Quantity plus(Quantity other) throws IllegalArgumentException {
        if (!getState().equals(other.getState())) {
            throw new IllegalArgumentException("This and given quantity are not of equal state!");
        }

        List<Quantity> input = new ArrayList<>();
        input.add(this);
        input.add(other);

        return sum(input, getState());
    }

    /**
     * Auto converts the units of the ingredient to the most efficient unit to store the ingredient in
     *
     * @note Documentation should get clearer during implementation
     */
    private static void simplifyUnit(){}

    /**
     * Check whether this quantity is less than given other quantity
     *
     * @param other Given other quantity
     *
     * @return Whether this quantity is less than given other quantity
     */
    public boolean lessThan(Quantity other) {
        return true;
    }

    /**
     * Check whether this quantity is less than one of the given unit
     *
     * @param unit Given unit
     *
     * @return Whether this quantity is less than one of the given unit
     */
    public boolean lessThan(Unit unit) {
        return true;
    }

    /**
     * Get the state associated with this quantity
     *
     * @return State associated with this quantity
     */
    public State getState() {
        return state;
    }
}
