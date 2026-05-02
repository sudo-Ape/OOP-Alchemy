package alchemy;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.*;

/**
 * Describes the quantity of the ingredient types
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class Quantity {
    // =================================================================================
    // Fields
    // =================================================================================
    private State state;
    private Map<Unit,Integer> amounts;


    // =================================================================================
    // Constructors
    // =================================================================================

    /**
     * Create a new quantity with given state and amounts map
     *
     * @pre The given amounts should be valid
     *      | canHaveAsAmounts(amounts,state)
     *
     * @effect State is set to given state
     *      | setState(state)
     *
     * @effect Amount is set to given amount
     *      | setAmount(amounts)
     *
     * @param state The given state
     * @param amounts The given amounts (per unit)
     */
    public Quantity(State state, Map<Unit, Integer> amounts) {
        setState(state);
        setAmounts(amounts);
    }

    /**
     * Create a new quantity with given state and amount equal to one of the given unit
     *
     * @param state Given state
     * @param unit Given unit
     *
     * @effect Creates a new quantity with constructed amounts map of one given unit
     *      | this(state,Map(unit: 1))
     */
    public Quantity(State state, Unit unit) {
        Map<Unit,Integer> amountsMap = new HashMap<>();
        amountsMap.put(unit,1);

        this(state,amountsMap);
    }


    // =================================================================================
    // Getters
    // =================================================================================

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
     * Get the amount that this quantity has of a given unit
     *
     * @param unit Given unit
     *
     * @return The amount that this quantity has of the given unit
     */
    public int getAmountOf(Unit unit) {
        // Return the unit if key set, or 0 if this is not in keys
        return amounts.getOrDefault(unit,0);
    }

    // =================================================================================
    // Setter
    // =================================================================================

    /**
     * Set the amounts map for this quantity
     *
     * @param amounts Amounts map for this quantity
     */
    private void setAmounts(Map<Unit, Integer> amounts) {
        this.amounts = amounts;
    }

    // =================================================================================
    // Inspector
    // =================================================================================

    /**
     * Check if given amounts map is allowed for the given state
     *
     * @param amounts Given amounts map
     * @param state Given state to check validity for
     *
     * @return Whether the given amounts map is allowed for the given state
     */
    public static boolean canHaveAsAmounts(Map<Unit, Integer> amounts, State state) {
        for (Unit unit : amounts.keySet()) {
            if (!(state.getAllowedUnits().contains(unit))) {
                return false;
            }
        }
        return true;
    }

    // =================================================================================
    // Calculation methods
    // =================================================================================
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
                    int runningSmallestUnit = q.getAmountOf(state.getAllowedUnits().getFirst());
                    totalSmallestGoalUnit += runningSmallestUnit;

                    double runningTotalUnit = q.getSpoons();
                    runningTotalUnit -= runningSmallestUnit * state.getAllowedUnits().getFirst().getSpoons();

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
        newQuantityMap.put(goalState.getAllowedUnits().getFirst(), totalSmallestGoalUnit);

        Quantity outputQuantity = new Quantity(goalState,newQuantityMap);
        outputQuantity.simplifyUnit();

        return outputQuantity;
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
     * @pre This quantity and given quantity are of equal state
     *      | getState() == other.getState()
     *
     * @param other Other quantity to sum with
     *
     * @return New quantity equal to this quantity plus given other quantity
     */
    public Quantity plus(Quantity other) {
        List<Quantity> input = new ArrayList<>();
        input.add(this);
        input.add(other);

        return sum(input, getState());
    }

    /**
     */
    public void simplifyUnit() {
        double totalSpoons = getSpoons();
        State myState = getState();

        // Create the map
        Map<Unit, Integer> outputQuantityMap = new HashMap<>();

        // Cycle through all units
        for (int i = myState.getAllowedUnits().size() - 1; i >= 0; i--) {
            Unit currentUnit = myState.getAllowedUnits().get(i);
            double remainder = totalSpoons % currentUnit.getSpoons();
            outputQuantityMap.put(currentUnit, (int) Math.floor(totalSpoons / currentUnit.getSpoons()));
            totalSpoons = remainder;
        }

        // Set the new quantity
        this.amounts = outputQuantityMap;
    }

    /**
     * Subtract a given other quantity from this quantity
     *
     * @param other Given other quantity
     *
     * @pre This and other quantity are of equal state
     *      | getState() == other.getState()
     *
     * @pre Other quantity is less than this quantity
     *      | other.lessThan(this)
     *
     * @return New quantity equal to this quantity minus given other quantity
     */
    public Quantity minus(Quantity other) {
        double resultingSpoons = getSpoons() - other.getSpoons();

        // Create the map
        Map<Unit, Integer> outputQuantityMap = new HashMap<>();

        // Cycle through all units
        for (int i = getState().getAllowedUnits().size() - 1; i >= 0; i--) {
            Unit currentUnit = getState().getAllowedUnits().get(i);
            double remainder = resultingSpoons % currentUnit.getSpoons();
            outputQuantityMap.put(currentUnit, (int) Math.floor(resultingSpoons / currentUnit.getSpoons()));
            resultingSpoons = remainder;
        }

        return new Quantity(getState(),outputQuantityMap);
    }

    // =================================================================================
    // Other methods
    // =================================================================================
    /**
     * Check whether this quantity is less than or equal to one of the given unit
     *
     * @param unit Given unit
     *
     * @return Whether this quantity is less than or equal to one of the given unit
     */
    public boolean lessThan(Unit unit) {
        return getSpoons() <= unit.getSpoons();
    }

    /**
     * Check whether this quantity is less than or equal to the given quantity
     *
     * @param other Other quantity
     * @return Whether this quantity is less than or equal to the given quantity
     */
    public boolean lessThan(Quantity other) {
        return getSpoons() <= other.getSpoons();
    }

    /**
     * Selects the minimum appropriate Unit for storing the given ingredient.
     *
     * @param ingredient The ingredient to select a unit for
     * @return The minimum appropriate Unit
     *
     * @throws IllegalArgumentException If the given ingredient does not fit into a container.
     *      | WIP
     */
    public static Unit selectAppropriateUnit(Ingredient ingredient) throws IllegalArgumentException {
        List<Unit> allowed = ingredient.getState().getAllowedUnits();

        // Loop forwards from second to second-to-last
        for (int i = 1; i < allowed.size() - 1; i++) {
            Unit unit = allowed.get(i);

            // Check if the ingredient's quantity fits in this unit
            if (ingredient.getQuantity().lessThan(unit)){
                return unit;
            }
        }

        throw new IllegalArgumentException("The given ingredient does not fit into a container.");
    }

    /**
     * Check whether this quantity is equal to zero
     *
     * @return Whether this quantity is equal to zero
     */
    public boolean isZero() {
        return getSpoons() == 0;
    }

    // =================================================================================
    // State
    // =================================================================================
    /**
     * Get the state associated with this quantity
     *
     * @return State associated with this quantity
     */
    @Basic @Raw
    public State getState() {
        return state;
    }

    /**
     * Set the state for this quantity
     *
     * @param state Given state
     */
    private void setState(State state) {
        this.state = state;
    }

    /**
     * Get a string reflecting the amounts held by this quantity
     *
     * @return String reflecting the amounts held by this quantity
     */
    public String getDisplay() {
        String output = "";
        for (Unit unit : amounts.keySet()) {
            if (!output.isEmpty()) {
                output = output.concat(", ");
            }
            output = output.concat(amounts.get(unit)+" "+unit.getDisplayName());
        }
        return output;
    }
}