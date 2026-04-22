package alchemy;

/**
 * Simple temperature class for OGP Alchemy
 *
 * @author Casper Vermeeren; LoÃ¯ck Sansen
 */
public class Temperature {
    // =================================================================================
    // Attributes
    // =================================================================================
    private final int value;

    // =================================================================================
    // Constructor
    // =================================================================================

    /**
     * Create a new temperature with given value
     *
     * @param value Given temperature value
     *
     * @note This constructor is package-private: the user cannot create temperatures, as other classes will handle this!
     */
    Temperature(int value) {
        this.value = value;
    }

    /**
     * Create a new temperature with given user-formatted value
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
     * @throws TemperatureException If both cool/heat are non-zero
     *
     * @return Integer temperature based on user-formatted input
     */
    private static int fromUser(String userTemperature) throws TemperatureException {
        // Strip parentheses/spaces and split by comma
        String clean = userTemperature.replaceAll("[()\\s]", "");
        String[] parts = clean.split(",");

        int left = Integer.parseInt(parts[0]);
        int right = Integer.parseInt(parts[1]);

        if (left != 0 && right != 0) {
            throw new TemperatureException("The given temperature is invalid!");
        }

        return (left == 0) ? right : -left;
    }

    /**
     * Return the user-formatted string for this temperature's value
     *
     * @return User-formatted string for this temperature's value
     */
    public String toUser() {
        if (getValue() < 0) {
            return "(" + Math.abs(getValue()) + ",0)";
        } else {
            return "(0,"+getValue()+")";
        }
    }

    // =================================================================================
    // Getter
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
    public boolean equals(Temperature other) {
        return this.getValue() == other.getValue();
    }
}
