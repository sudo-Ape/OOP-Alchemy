package alchemy;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * Transmogrifier class for a machine that changes the state of ingredients.
 * A transmogrifier can hold at most one ingredient at a time and changes the state to the given goal state.
 *
 * @invar The goal state of this transmogrifier must always be non-null
 *      | getGoalState() != null
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
     * The state which the ingredient will be converted to
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
     */
    @Basic
    public State getGoalState() throws IllegalStateException {
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
     *
     * @throws IllegalArgumentException If goal state is null
     *      | goalState == null
     */
    public void setGoalState(State goalState) throws IllegalStateException, IllegalArgumentException {
        if (isTerminated()) {
            throw new IllegalStateException("This transmogrifier has been terminated.");
        }
        if (goalState == null) {
            throw new IllegalArgumentException("Goal state cannot be null.");
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

    /**
     * Terminate this transmogrifier
     *
     * @post Transmogrifier is terminated
     *      | new.isTerminated()
     *
     * @post Location of transmogrifier is set to null
     *      | new.getLocation() == null
     *
     * @post If location of transmogrifier was non-null, remove the transmogrifier from that location.
     *      | if getLocation() != null:
     *      |   (new) getLocation().getTransmogrifier() == null
     */
    @Override @Raw
    public void terminate() {
        if (getLocation() != null) {
            getLocation().setTransmogrifier(null);
        }
        super.terminate();
    }
}

