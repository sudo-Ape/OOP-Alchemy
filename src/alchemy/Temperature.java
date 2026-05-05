package alchemy;

/**
 * Helper class to describe the temperature of ingredients and ingredient types
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class Temperature {
    // =================================================================================
    // Fields
    // =================================================================================
    /**
     * Integer temperature value associated with this temperature
     */
    private final int value;

    // =================================================================================
    // Constructor
    // =================================================================================

    /**
     * Create a new temperature with given value
     *
     * @param value Given temperature value
     *
     * @post Value is given value
     *      | new.getValue() == value
     *
     * @note This constructor is package-private: the user cannot create temperatures, as other classes will handle this!
     */
    Temperature(int value) {
        this.value = value;
    }

    /**
     * Create a new temperature with given user-formatted value
     *
     * @post Value is parsed user input
     *      | new.getValue() == fromUser(userTemperature)
     *
     * @param userTemperature Given user-formatted temperature value
     */
    Temperature(String userTemperature) {
        this.value = fromUser(userTemperature);
    }

    // =================================================================================
    // User parsing
    // =================================================================================

    /**
     * Return integer temperature based on user-formatted input
     *
     * @param userTemperature User-formatted input, e.g. "(50,0)" for -50
     *
     * @return After splitting "(x,y)" into integers x and y: -x if y=0; y if x==0; 20 if anything else happens
     *      | WIP
     */
    private static int fromUser(String userTemperature) {
        // Strip parentheses/spaces and split by comma
        String clean = userTemperature.replaceAll("[()\\s]", "");
        String[] parts = clean.split(",");

        int left = Integer.parseInt(parts[0]);
        int right = Integer.parseInt(parts[1]);

        if (left != 0 && right != 0) {
            // Do not throw an error, but fall back to default case (total programming)
            return 20;
        }

        return (left == 0) ? right : -left;
    }

    /**
     * Return the user-formatted string for this temperature's value
     *
     * @return (0,temp) if temperature is positive; (-temp,0) if temperature is negative; (0,0) otherwise
     *      | WIP
     */
    public String toUser() {
        if (getValue() < 0) {
            return "(" + Math.abs(getValue()) + ",0)";
        } else {
            return "(0,"+getValue()+")";
        }
    }

    // =================================================================================
    // Value
    // =================================================================================

    /**
     * Get the value of this temperature
     *
     * @return The value of this temperature
     */
    public int getValue() {
        return value;
    }

    // =================================================================================
    // Comparison
    // =================================================================================

    /**
     * Check whether this temperature equals a given other
     *
     * @param other Given other temperature
     *
     * @return Whether this temperature's integer value is equal to the other's integer value
     *      | result == (getValue() == other.getValue())
     */
    public boolean equals(Temperature other) {
        return this.getValue() == other.getValue();
    }

    /**
     * Get the absolute difference between this and a given other temperature
     *
     * @param other Given other temperature
     *
     * @return Absolute difference in value between this and given other temperature
     *      | result == abs(getValue() - other.getValue())
     */
    public int difference(Temperature other) {
        return Math.abs(getValue() - other.getValue());
    }

    /**
     * Check whether this temperature value is less than given other temperature's value
     *
     * @param other Given other temperature
     *
     * @return Whether this temperature value is less than given other temperature's value
     *      | result == (getValue() < other.getValue())
     */
    public boolean lessThan(Temperature other) {
        return this.getValue() < other.getValue();
    }
}
