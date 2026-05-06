package alchemy;

public class Transmogrifier extends Device {
    // =================================================================================
    // Fields
    // =================================================================================
    private State goalState;

    // =================================================================================
    // Constructor
    // =================================================================================

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
     *      | new.getGoalState() == state
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

    @Override
    public void run() throws IllegalStateException {
        if (getLocation() == null) {
            throw new IllegalStateException("Transmogrifier is not in a (valid) laboratory.");
        }
        if (internalIngredients.isEmpty()) {
            throw new IllegalStateException("The storage of the transmogrifier is empty.");
        }
        if (isTerminated()) {
            throw new IllegalStateException("This transmogrifier has been terminated.");
        }

        Ingredient ingredient = internalIngredients.getFirst();

        result = new Ingredient(ingredient.getIngredientType(),ingredient.getTemperature(),getGoalState(),ingredient.getQuantity().toState(getGoalState()));
    }
}

