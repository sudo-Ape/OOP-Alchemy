package alchemy;

import java.util.*;

/**
 * Ingredient type class to hold fixed standard information about an ingredient
 *
 * @invar List of basic ingredients must always be valid
 *      | hasProperBasicIngredients()
 *
 * @invar Standard temperature must always be valid
 *      | canHaveAsStandardTemperature(getStandardTemperature())
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class IngredientType {
    // =================================================================================
    // Fields
    // =================================================================================
    /**
     * Variable referencing the standard temperature of this ingredient type
     *
     * @invar standardTemperature references an effective object
     *      | standardTemperature != null
     */
    private Temperature standardTemperature;

    /**
     * Variable referencing the state enum of this ingredient type
     */
    private State standardState;

    /**
     * Variable referencing a set containing the basic ingredient names present in this ingredient type
     *
     * @invar basicIngredients references an effective set
     *      | basicIngredients != null
     *
     * @invar Each string element in the set follows the encapsulated ruleset
     *      | for each S in basicIngredients:
     *      |  canHaveAsBasicIngredient(S)
     */
    private Set<String> basicIngredients = new HashSet<>(); // Set to ensure no duplicate basic ingredients (possibly caused by mixing)

    // =================================================================================
    // Constructors
    // =================================================================================
    /**
     * Create a new ingredientType with given standard temperature, given standard state, and given list of basic ingredient names
     *
     * @param standardTemperature Given standard temperature
     * @param standardState Given standard state
     * @param basicIngredients Given basic ingredient names
     *
     * @effect Standard temperature is set to given standard temperature
     *      | setStandardTemperature(standardTemperature)
     *
     * @effect Standard state is set to given standard state
     *      | setStandardState(standardState)
     *
     * @effect Basic ingredients set is set to given set
     *      | setBasicIngredients(basicIngredients)
     *
     * @note This constructor is package-private: the user cannot use it, only other code can!
     */
    IngredientType(Temperature standardTemperature, State standardState, Set<String> basicIngredients) {
        this.setStandardTemperature(standardTemperature);
        this.setStandardState(standardState);
        this.setBasicIngredients(basicIngredients);
    }

    // =================================================================================
    // Getters
    // =================================================================================
    /**
     * Get the standard temperature for this ingredient type
     *
     * @return Standard temperature for this ingredient type
     */
    public Temperature getStandardTemperature() {
        return standardTemperature;
    }

    /**
     * Get the standard temperature for this ingredient type, displayed in the user format
     *
     * @return Standard temperature for this ingredient type, displayed in the user format
     */
    public String getStandardTemperatureDisplay() {
        return standardTemperature.toUser();
    }

    /**
     * Get the standard state for this ingredient type
     *
     * @return Standard state for this ingredient type
     */
    public State getStandardState() {
        return standardState;
    }

    /**
     * Get the basic ingredients list for this ingredient type
     *
     * @return Basic ingredients list for this ingredient type
     */
    Set<String> getBasicIngredients() {
        return basicIngredients;
    }

    // =================================================================================
    // Setters
    // =================================================================================

    /**
     * Set the basic ingredients for this ingredient type
     *
     * @throws IllegalArgumentException If the given set of basic ingredients is not allowed
     *      | !canHaveAsBasicIngredients(basicIngredients)
     *
     * @post Basic ingredients set is given set
     *      | new.getBasicIngredients() == basicIngredients
     *
     * @param basicIngredients Given set of basic ingredient names
     */
    public void setBasicIngredients(Set<String> basicIngredients) throws IllegalArgumentException {
        if (canHaveAsBasicIngredients(basicIngredients)) {
            this.basicIngredients = basicIngredients;
        } else {
            throw new IllegalArgumentException("At least one of the given basic ingredients is invalid!");
        }
    }

    /**
     * Sets the standard temperature of this ingredient type
     *
     * @param standardTemperature The given standard temperature
     *
     * @post Standard temperature is given value if valid; standard temperature is 20 otherwise
     *      | if (canHaveAsStandardTemperature(standardTemperature)):
     *      |   new.getStandardTemperature() == standardTemperature
     *      | else:
     *      |   new.getStandardTemperature() == new Temperature(20);
     */
    private void setStandardTemperature(Temperature standardTemperature) {
        if (canHaveAsStandardTemperature(standardTemperature)) {
            this.standardTemperature = standardTemperature;
        } else {
            this.standardTemperature = new Temperature(20); // Escape route for total programming
        }
    }

    /**
     * Sets the standard state of this ingredient type
     *
     * @param standardState The given standard state
     *
     * @post Standard state is given state
     *      | new.getStandardState() == standardState
     */
    private void setStandardState(State standardState) {
        this.standardState = standardState;
    }

    // =================================================================================
    // Inspectors
    // =================================================================================

    /**
     * Check whether given list of basic ingredients is allowed
     *
     * @param basicIngredients Given list of basic ingredients to check
     *
     * @return Whether this list of basic ingredients is allowed by checking if each individual string is allowed
     *      | for S in basicIngredients:
     *      |  if not canHaveAsBasicIngredient(S): result == false
     *      | result == true
     */
    public static boolean canHaveAsBasicIngredients(Set<String> basicIngredients) {
        for (String ingredient : basicIngredients) {
            if (!canHaveAsBasicIngredient(ingredient)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if given basic ingredient name is allowed name
     *
     * @param basicIngredient Basic ingredient name to check
     *
     * @return False if string is empty; false if string contains "mixed" or "with"; false if string contains illegal characters; false if each word does not start with capital or non-letter; false if non-first letters of word are capital; false if one word of length < 3; false if multiple words with at least one word of length < 2; true otherwise
     *      | if (basicIngredient == ""): result == false
     *      | if (basicIngredient.contains("mixed") || basicIngredient.contains("with")): result == false
     *      | if (!basicIngredient.matches("[\\p{L}'() ]*"): result == false
     *      | for word in basicIngredient.words():
     *      |  if (!(word[0].isCapital() || word[0].isNonLetter())): result == false
     *      |   for i in 1..word.size():
     *      |       if (word[i].isCapital()): result == false
     *      |   if (basicIngredient.words().size() == 1 && word.size() < 3): result == false
     *      |   else if (word.size() < 2): result == false
     */
    public static boolean canHaveAsBasicIngredient(String basicIngredient) {
        // No empty strings
        if (basicIngredient.isEmpty()) {
            return false;
        }

        // Name of basic ingredient cannot contain "mixed" or "with", as this is reserved for mixed name generation
        if (basicIngredient.toLowerCase().contains("mixed") || basicIngredient.toLowerCase().contains("with")) {
            return false;
        }

        // Legal characters only (p{L} is a unicode letter category containing letters from every language, so that letters like ï are allowed!)
        if (!(basicIngredient.matches("[\\p{L}'() ]*"))) {
            return false;
        }

        // Analyze word by word
        String[] words = basicIngredient.split(" ");

        // Every word starts with capital or special character, other letters are not capital
        for (String word : words) {
            if (!(Character.isUpperCase(word.charAt(0)) || !Character.isLetter(word.charAt(0)))) {
                return false;
            }

            for (int i = 1; i < word.length(); i++) {
                if (Character.isUpperCase(word.charAt(i))) {
                    return false;
                }
            }
        }

        // Word length checking
        if (words.length == 1) { // Only one word
            if (words[0].length() < 3) {
                return false;
            }
        } else { // Multiple words
            for (String word : words) {
                if (word.length() < 2) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check whether this ingredient type has a valid set of basic ingredients
     *
     * @return Whether this ingredient type has a valid set of basic ingredients
     *      | canHaveAsBasicIngredients(getBasicIngredients())
     */
    public boolean hasProperBasicIngredients() {
        return canHaveAsBasicIngredients(getBasicIngredients());
    }

    /**
     * Check if given standard temperature is valid
     *
     * @param standardTemperature Given standard temperature to check
     *
     * @return False if given standard temperature is null; true otherwise
     *      | result == (standardTemperature != null)
     *
     * @note No deeper checks on the validity of a temperature need to be performed, since the Temperature class logic assures no broken objects can be formed.
     */
    public static boolean canHaveAsStandardTemperature(Temperature standardTemperature) {
        return standardTemperature != null;
    }

    // =================================================================================
    // Simple name
    // =================================================================================
    /**
     * Get the simple name for this ingredient type
     *
     * @return Name consisting of all basic ingredients ordered alphabetically, connected by "mixed with", "and", and appropriate punctuation
     *      | result == sorted(basicIngredients)[0]+" mixed with "+sorted(basicIngredients)[1]+", "+ ... +", "+sorted(basicIngredients)[-2]+", and "+sorted(basicIngredients)[-1]
     */
    public String getSimpleName() {
        String output = "";

        // Convert set to sorted list
        List<String> basicIngredients = new ArrayList<>(getBasicIngredients());
        basicIngredients.sort(Comparator.naturalOrder());

        int N = basicIngredients.size();
        for (int i = 0; i < N; i++) {
            output += basicIngredients.get(i);

            // Check if "mixed with" is necessary
            if (i == 0 && N > 1) {
                output.join(" mixed with ");
            }

            // Check if "and" or "," is necessary
            if (i > 0 && i < N-1) {
                if (i == N-2) {
                    output.join(" and ");
                } else {
                    output.join(", ");
                }
            }
        }

        return output;
    }

    // =================================================================================
    // Comparison
    // =================================================================================

    /**
     * Check whether this ingredient type equals another given ingredient type
     *
     * @param other Given other ingredient type
     *
     * @return True if equal standard state, equal standard temperature, and equal basic ingredients; false otherwise
     *      | result == (this.getStandardState() == other.getStandardState() && this.getStandardTemperature().equals(other.getStandardTemperature()) && this.getBasicIngredients().equals(other.getBasicIngredients()))
     */
    public boolean equals(IngredientType other) {
        return getStandardState() == other.getStandardState() &&
               getStandardTemperature().equals(other.getStandardTemperature()) &&
               getBasicIngredients().equals(other.getBasicIngredients());
    }
}
