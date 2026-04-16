package alchemy;

public enum State {
    LIQUID("liquid"),
    POWDER("powder");

    private String state = null;

    /**
     * Initialize a new ingredient with a given state
     *
     * @param state
     *        The state of the object being liquid or powder
     */
    protected State(String state) {
        this.state = state;
    }

    /**
     * Returns the current current state of the ingredient
     *
     * @return the state in which the ingredient is currently in
     */
    @Basic @Raw
    public String getState() {
        return state;
    }
}
