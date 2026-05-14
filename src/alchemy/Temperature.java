package alchemy;

import be.kuleuven.cs.som.annotate.Basic;

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

    public static final int maxTemperature = 10000;

    public static final int defaultTemperature = 20;

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
     * Return integer temperature based on user-formatted input of the form "(x,y)",
     * where x represents coldness and y represents hotness
     *
     * @param userTemperature User-formatted input, e.g. "(50,0)" for coldness 50 (value -50); "(0,50)" for hotness 50 (value 50)
     *
     * @return Integer temperature parsed from "(x,y)": returns defaultTemperature if input is null or not of the form "(digits,digits)";
     *         returns defaultTemperature if both x and y are non-zero; otherwise clamps each to maxTemperature and returns -x for coldness or y for hotness
     *      | if userTemperature == null || !userTemperature.matches("\\(\\d+,\\d+\\)"):
     *      |   result == defaultTemperature
     *      | else:
     *      |   clean = userTemperature.replaceAll("[()\\s]","")
     *      |   parts = clean.split(",")
     *      |   left = Integer.parseInt(parts[0])
     *      |   right = Integer.parseInt(parts[1])
     *      |   if left != 0 && right != 0:
     *      |     result == defaultTemperature
     *      |   else:
     *      |     left = min(left, maxTemperature)
     *      |     right = min(right, maxTemperature)
     *      |     result == (left == 0) ? right : -left
     */
    private static int fromUser(String userTemperature) {
        if (userTemperature == null || !userTemperature.matches("\\(\\d+,\\d+\\)")) { // For total programming
            return defaultTemperature;
        }

        // Strip parentheses/spaces and split by comma
        String clean = userTemperature.replaceAll("[()\\s]", "");
        String[] parts = clean.split(",");

        int left = Integer.parseInt(parts[0]);
        int right = Integer.parseInt(parts[1]);

        if (left != 0 && right != 0) {
            // Do not throw an error, but fall back to default case (total programming)
            return defaultTemperature;
        }

        if (left > maxTemperature) {
            left = maxTemperature;
        }

        if (right > maxTemperature) {
            right = maxTemperature;
        }

        return (left == 0) ? right : -left;
    }

    /**
     * Return the user-formatted string for this temperature's value
     *
     * @return (0,temp) if temperature is positive; (-temp,0) if temperature is negative; (0,0) otherwise
     *      | if getValue() < 0:
     *      |   result == "("+abs(getvalue())+",0)"
     *      | else:
     *      |   result == "(0,"+getValue()+")"
     */
    public String toUser() {
        if (getValue() < 0) {
            return "(" + Math.abs(getValue()) + ",0)";
        } else {
            return "(0,"+getValue()+")";
        }
    }

    /**
     * Get the hotness of this temperature
     *
     * @return Temperature value if positive; zero otherwise
     *      | if getValue() > 0:
     *      |   result == getValue()
     *      | else:
     *      |   result == 0
     */
    public int getHotness() {
        return getValue() > 0 ? getValue() : 0;
    }

    /**
     * Get the coldness of this temperature
     *
     * @return Temperature value if negative; zero otherwise
     *      | if getValue() < 0:
     *      |   result == getValue()
     *      | else:
     *      |   result == 0
     */
    public int getColdness() {
        return getValue() < 0 ? getValue() : 0;
    }

    // =================================================================================
    // Value
    // =================================================================================

    /**
     * Get the value of this temperature
     *
     * @return The value of this temperature
     */
    @Basic
    public int getValue() {
        return value;
    }

    /**
     * Get the array value description for this temperature
     *
     * @return Array with coldness and hotness
     *      | result == {getColdness(),getHotness()}
     */
    public int[] getTemperature() {
        return {getColdness(),getHotness()};
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
