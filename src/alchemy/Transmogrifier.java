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

    public State getGoalState() {
        return goalState;
    }

    public void setGoalState(State goalState) {
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

        Ingredient ingredient = internalIngredients.getFirst();

        result = new Ingredient(ingredient.getIngredientType(),ingredient.getTemperature(),getGoalState(),ingredient.getQuantity().toState(getGoalState()));
    }
}

