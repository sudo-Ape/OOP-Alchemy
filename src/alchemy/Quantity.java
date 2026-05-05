package alchemy;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import java.util.*;

/**
 * Helper class to describe the quantity of ingredients
 *
 * @invar Amounts map must always be valid
 *      | canHaveAsAmounts(amounts)
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class Quantity {
    // =================================================================================
    // Fields
    // =================================================================================
    /**
     * State enum associated with this quantity, for checking allowed units
     */
    private State state;

    /**
     * Main map object that holds what amounts of each unit this quantity comprises
     *
     * @invar Only state-allowed units should be used as keys
     *      | for unit in amounts.keys:
     *      |   state.getAllowedUnits().contains(unit)
     *
     * @invar Values should never be negative
     *      | for val in amounts.values:
     *      |   val >= 0
     */
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
     * @effect Amounts are set to given amounts
     *      | setAmounts(amounts)
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
     * Calculate the total number of spoons this quantity is equal to
     *
     * @return The total, over all units, this quantity is equal to
     *      | result == sum([unit.getSpoons() * value for (unit,value) in amounts])
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
     * @return The amount that this quantity has of the given unit; zero if unset
     *      | result == amounts.getOrDefault(unit)
     */
    public int getAmountOf(Unit unit) {
        // Return the unit if key set, or 0 if this is not in keys
        return amounts.getOrDefault(unit,0);
    }

    // =================================================================================
    // Amounts
    // =================================================================================

    /**
     * Set the amounts map for this quantity
     *
     * @param amounts Amounts map for this quantity
     *
     * @post Amounts map is set to given map object
     *      | new.amounts == amounts
     */
    private void setAmounts(Map<Unit, Integer> amounts) {
        this.amounts = amounts;
    }

    /**
     * Check if given amounts map is allowed for the given state
     *
     * @param amounts Given amounts map
     * @param state Given state to check validity for
     *
     * @return True if map is non-null, if all key units are present in state's allowed units list and all amount values are non-negative; false otherwise
     *      | if amounts == null: result == false
     *      | for unit in amounts.keys:
     *      |   if not state.getAllowedUnits.contains(unit): result == false
     *      |   if amounts[unit] < 0: result == false
     *      | result == true
     */
    public static boolean canHaveAsAmounts(Map<Unit, Integer> amounts, State state) {
        if (amounts == null) {
            return false;
        }

        for (Unit unit : amounts.keySet()) {
            if (!(state.getAllowedUnits().contains(unit))) { // Unit must be in state's allowed list
                return false;
            }

            if (amounts.get(unit) < 0) { // Actual amounts cannot be negative
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
     *
     * @param goalState Given state to sum to, determines rounding
     *
     * @return New quantity equal to the (rounded) sum of the given quantities, in given goal state
     *      | WIP (please god nee please)
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
     * Return this quantity plus given other quantity, in some goal state
     *
     * @param other Other quantity to sum with
     *
     * @param goalState Goal state for output quantity
     *
     * @return New quantity equal to this quantity plus given other quantity, in goal state
     *      | result == Quantity.sum([this,other],goalState)
     */
    public Quantity plus(Quantity other, State goalState) {
        return sum(List.of(this,other), goalState);
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
     *      | result == Quantity.sum([this,other], this.getState())
     */
    public Quantity plus(Quantity other) {
        return this.plus(other,getState());
    }

    /**
     * Simplify this unit to the most expanded form it can be
     *
     * @post Unit's map layout (x spoons, y sacks, z barrels, ...) is as optimal as possible, meaning no unit amounts overflow into the next unit's worth
     *      | WIP (ik denk dat het niet van ons verwacht wordt dat we dit soort dingen formeel kunnen lol)
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
     *      | WIP
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
     * Get a quantity equal to this quantity, but with state changed to given newState
     *
     * @param newState Given new state
     * @return New quantity with same contents (possibly rounded by state/unit allowances) and new state
     *      | result == Quantity.sum([this],newState)
     */
    public Quantity toState(State newState) {
        return Quantity.sum(List.of(this),newState);
    }

    /**
     * Check whether this quantity is less than or equal to one of the given unit
     *
     * @param unit Given unit
     *
     * @return Whether this quantity's spoon value is less than or equal to the given unit's spoon value
     *      | result == (getSpoons() <= unit.getSpoons())
     */
    public boolean lessThan(Unit unit) {
        return getSpoons() <= unit.getSpoons();
    }

    /**
     * Check whether this quantity is less than or equal to the given quantity
     *
     * @param other Other quantity
     *
     * @return Whether this quantity's spoon value is less than or equal to the given quantity's spoon value
     *      | result == (getSpoons() <= other.getSpoons())
     */
    public boolean lessThan(Quantity other) {
        return getSpoons() <= other.getSpoons();
    }

    /**
     * Selects the minimum appropriate Unit for storing the given ingredient.
     *
     * @param ingredient The ingredient to select a unit for
     *
     * @return The minimum appropriate Unit: where the ingredient's quantity fits into the resulting Unit, but not a smaller Unit
     *      | ingredient.getQuantity().lessThan(result) && !ingredient.getQuantity().lessThan(ingredient.getState().getAllowedUnits().itemBefore(result))
     *
     * @throws IllegalArgumentException If the given ingredient does not fit into a container.
     *      | WIP, also this should be nominal...
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
     *      | getSpoons() == 0
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
     *
     * @post State is given state
     *      | new.getState() == state
     */
    private void setState(State state) {
        this.state = state;
    }

    /**
     * Get a string reflecting the amounts held by this quantity
     *
     * @return Non-ordered string constructed as "AMOUNT0 UNIT0NAME, AMOUNT1 UNIT1NAME, ..."
     *      | output = ""
     *      | for unit in amounts.keys:
     *      |   output += "amounts[unit] unit.getDisplayName()"
     *      | result == output
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