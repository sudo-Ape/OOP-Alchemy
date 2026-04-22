package alchemy;
import be.kuleuven.cs.som.annotate.Basic;

import java.util.*;

public class Quantity {
    private State state;
    private final Map<Unit,Integer> amounts;

    /**
     *
     * @pre The given amounts should be valid
     *      | canHaveAsAmounts(amounts,state)
     *
     * @param state The given state
     *
     * @param amounts The given amounts (per unit)
     */
    public Quantity(State state, Map<Unit, Integer> amounts) {
        this.state = state;
        this.amounts = amounts;
    }

    /**
     *
     *
     * @param unit
     * @return
     */
    public int getAmountOf(Unit unit) {
        return amounts.get(unit);
    }

    /**
     * Check if given amounts map is allowed for the given state
     *
     * @param amounts Given amounts map
     * @param state Given state to check validity for
     *
     * @return Whether the given amounts map is allowed for the given state
     */
    private static boolean canHaveAsAmounts(Map<Unit, Integer> amounts, State state) {
        for (Unit unit : amounts.keySet()) {
            if (!(state.getAllowedUnits().contains(unit))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculate the number of spoons based on the current unit the ingredient type has
     *
     * @return The number of spoons
     */
    @Basic @Raw
    public double getSpoons() {
        double total = 0;

        for (Map.Entry<Unit, Integer> entry : amounts.entrySet()) {
            Unit unit = entry.getKey();
            Integer value = entry.getValue();

            total += unit.getSpoons() * value;
        }
        return total;
    }

    /**
     * Sum a list of quantities to a given goal state, with proper rounding
     *
     * @param quantities List of quantities to sum to given state
     * @param goalState Given state to sum to, determines rounding
     *
     * @return New quantity equal to the (rounded) sum of the given quantities, in given goal state
     */
    public static Quantity sum(List<Quantity> quantities, State goalState) {
        // Sort by state
        Map<State,List<Quantity>> quantityMap = new HashMap<>();

        for (Quantity q : quantities) {
            if (quantityMap.containsKey(q.getState())) {
                quantityMap.get(q.getState()).add(q);
            } else {
                quantityMap.put(q.getState(), new ArrayList<>(List.of(q)));
            }
        }

        // Different state quantities: floor sub-spoon remainders of non-goal states before summation
        int totalSpoons = 0;
        int totalSmallestGoalUnit = 0;
        for (Map.Entry<State, List<Quantity>> entry : quantityMap.entrySet()) {
            State state = entry.getKey();
            List<Quantity> quantityList = entry.getValue();

            if (state == goalState) {
                for (Quantity q : quantityList) {
                    int runningSmallestUnit = q.getAmountOf(state.getAllowedUnits().get(0));
                    totalSmallestGoalUnit += runningSmallestUnit;

                    double runningTotalUnit = q.getSpoons();
                    runningTotalUnit -= runningSmallestUnit * state.getAllowedUnits().get(0).getSpoons();

                    totalSpoons += (int) Math.floor(runningTotalUnit); // Floor should not be necessary, but to be careful.
                }
            } else {
                double runningTotal = 0;
                for (Quantity q : quantityList) {
                    runningTotal += q.getSpoons();
                }
                totalSpoons += (int) Math.floor(runningTotal);
            }
        }

        // Create the final summed quantity to return
        Map<Unit, Integer> newQuantityMap = new HashMap<>();

        newQuantityMap.put(Unit.SPOON, totalSpoons);
        newQuantityMap.put(goalState.getAllowedUnits().get(0), totalSmallestGoalUnit);

        return new Quantity(goalState,newQuantityMap);
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
     * Convert the units of the ingredient to the most efficient unit to store the ingredient in
     */
    private Quantity simplifyUnit() {
        double totalSpoons = getSpoons();
        State myState = getState();

        // Create the map
        Map<Unit, Integer> outputQuantityMap = new HashMap<>();

        for (int i = myState.getAllowedUnits().size() - 1; i >= 0; i--) {
            Unit currentUnit = myState.getAllowedUnits().get(i);
            double remainder = totalSpoons % currentUnit.getSpoons();
            outputQuantityMap.put(currentUnit, (int) Math.floor(totalSpoons / currentUnit.getSpoons()));
            totalSpoons = remainder;
        }

        // Return the new quantity
        return new Quantity(getState(),outputQuantityMap);
    }

    /**
     * Check whether this quantity is less than one of the given unit
     *
     * @param unit Given unit
     *
     * @return Whether this quantity is less than one of the given unit
     */
    public boolean lessThan(Unit unit) {
        // WIP
        return true;
    }

    /**
     * Check whether this quantity is less than the given quantity
     *
     * @param other Other quantity
     * @return Whether this quantity is less than the given quantity
     */
    public boolean lessThan(Quantity other) {
        // WIP
        return true;
    }

    /**
     * Get the state associated with this quantity
     *
     * @return State associated with this quantity
     */
    @Basic @Raw
    public State getState() {
        return state;
    }
}