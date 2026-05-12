package alchemy;

/**
 * Transmogrifier class for a machine that changes the state of ingredients.
 * A transmogrifier can hold at most one ingredient at a time and changes the state to the given goal state.
 * The quantity is converted to the new state, with any remainder smaller than a drop or pinch lost in the process.
 *
 * @invar The goal state of this transmogrifier must always be non-null, or this transmogrifier is terminated
 *      | goalState != null || isTerminated()
 *
 * @invar The transmogrifier holds at most one ingredient at a time
 *      | internalIngredients.size() <= 1
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class Transmogrifier extends Device {
    // =================================================================================
    // Fields
    // =================================================================================
    /**
     * The state to which the ingredient will be converted to
     */
    private State goalState;

    // =================================================================================
    // Constructor
    // =================================================================================

    /**
     * Creates a new transmogrifier with a given goal state.
     *
     * @param goalState The given goal state for this transmogrifier.
     *
     * @effect State is set to given goal state.
     *      | setGoalState(goalState)
     */
    public Transmogrifier(State goalState) {
        this.setGoalState(goalState);
    }

    // =================================================================================
    // GoalState
    // =================================================================================

    /**
     * Get the goal state for this transmogrifier
     *
     * @return Goal state for this transmogrifier
     *      | result == goalState
     *
     * @throws IllegalStateException If transmogrifier has been terminated
     *      | isTerminated()
     */
    public State getGoalState() throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This transmogrifier has been terminated.");
        }
        return goalState;
    }

    /**
     * Set the goal state for this transmogrifier
     *
     * @param goalState Given goal state
     *
     * @post Goal state is given goal state
     *      | new.getGoalState() == goalState
     *
     * @throws IllegalStateException If transmogrifier has been terminated
     *      | isTerminated()
     */
    public void setGoalState(State goalState) throws IllegalStateException {
        if (isTerminated()) {
            throw new IllegalStateException("This transmogrifier has been terminated.");
        }
        this.goalState = goalState;
    }

    // =================================================================================
    // Methods
    // =================================================================================

    /**
     * Adds a new ingredient from the container to this transmogrifier
     *
     * @param container IngredientContainer to extract from
     *
     * @post The ingredient from the container is added to this transmogrifier's internal ingredients
     *      | new.internalIngredients.contains(container.getContents())
     *
     * @throws IllegalStateException If transmogrifier already holds an ingredient
     *      | !internalIngredients.isEmpty()
     *
     * @throws IllegalStateException If transmogrifier has been terminated
     *      | isTerminated()
     *
     * @throws IllegalStateException If Transmogrifier is not in a (valid) laboratory
     *      | getLocation() == null
     *
     * @throws IllegalArgumentException If container is null or empty
     *      | container == null || container.isEmpty()
     */
    @Override
    public void add(IngredientContainer container) throws IllegalStateException {
        if (!internalIngredients.isEmpty()) {
            throw new IllegalStateException("Transmogrifier can only hold one ingredient.");
        }
        if (isTerminated()) {
            throw new IllegalStateException("This transmogrifier has been terminated.");
        }
        super.add(container);
    }

    /**
     * Changes the state of the ingredient to this transmogrifier's goal state
     *
     * @post Result is a new ingredient with the same type and temperature as the original,
     *       the goal state, and the quantity converted to the goal state
     *      | result.getIngredientType() == internalIngredients.getFirst().getIngredientType()
     *      | && result.getTemperature() == internalIngredients.getFirst().getTemperature()
     *      | && result.getState() == getGoalState()
     *      | && result.getQuantity() == internalIngredients.getFirst().getQuantity().toState(getGoalState())
     *
     * @post Internal ingredients list is emptied after the run
     *      | new.internalIngredients.isEmpty()
     *
     * @throws IllegalStateException If transmogrifier is not in a (valid) laboratory
     *      | getLocation() == null
     *
     * @throws IllegalStateException If this transmogrifier is empty
     *      | internalIngredients.isEmpty()
     *
     * @throws IllegalStateException If transmogrifier has been terminated
     *      | isTerminated()
     */
    @Override
    public void run() throws IllegalStateException {
        super.run(); // Do repetitive checks here!

        Ingredient ingredient = internalIngredients.getFirst();

        result = new Ingredient(ingredient.getIngredientType(),
                ingredient.getTemperature(),
                getGoalState(),
                ingredient.getQuantity().toState(getGoalState()));

        // Clear internal ingredients
        clearInternalIngredients();
    }

    @Override
    public void terminate() {
        if (getLocation() != null) {
            getLocation().setTransmogrifier(null);
        }
        super.terminate();
    }
}

