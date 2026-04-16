package alchemy;
import java.util.ArrayList;


public class IngredientType {
    private String[] standardTemperature = new String[2];
    private String standardState = null;
    private ArrayList<String> basicIngredients = new ArrayList<String>();

    /**
     * Construct the name of the ingredient
     *
     * @invar The words 'mixed' and 'with' never start with a capital letter
     *
     * @post If a word contains numbers, special characters or punctuation marks, they are trimmed from the String
     *       unless it's an apostrophe (') or round brackets: ().
     *
     * @post Every word in the name of an ingredient consists of at least 2 characters.
     *       If the name is just one word, then it has to contain at least 3 characters
     *
     * @post every new word at the beginning of the name starts with a capital letter or an allowed special sign.
     *       The other letters in the name are lower case.
     *
     * @return Returns the simple name of the
     */
    @Basic @Raw
    public String getSimpleName(){
    }
}
