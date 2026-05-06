package alchemy;

import java.util.ArrayList;
import java.util.List;

/**
 * Laboratory class for the storing of ingredients and machines
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class Laboratory {
    // =================================================================================
    // Fields
    // =================================================================================
    /**
     * Integer amount of storerooms this laboratory can hold in storage
     *
     * @invar capacity must be strictly positive
     *      | capacity > 0
     */
    private int capacity; // Number of Storerooms in capacity

    /**
     * List of all ingredients stored in the laboratory
     *
     * @invar storage must be an effective list
     *      | storage != null
     *
     * @invar All ingredients in storage must be effective
     *      | for ingredient in storage:
     *      |   ingredient != null
     *
     * @invar The list does not contain two ingredients with the same simple or special name
     *      | for ingredient in storage:
     *      |   !storage.without(ingredient).hasIngredientWithNameOf(ingredient)
     */
    private List<Ingredient> storage = new ArrayList<>();


    /**
     * The cooling box placed in this laboratory
     */
    private CoolingBox coolingBox = null;

    /**
     * The oven placed in this laboratory
     */
    private Oven oven = null;

    /**
     * The kettle placed in this laboratory
     */
    private Kettle kettle = null;

    /**
     * The transmogrifier placed in this laboratory
     */
    private Transmogrifier transmogrifier = null;

    // =================================================================================
    // Constructors
    // =================================================================================

    /**
     * Create a new laboratory with given capacity in number of storerooms
     *
     * @param capacity Capacity in number of storerooms
     *
     * @effect Capacity is set to given value
     *      | setCapacity(capacity)
     */
    public Laboratory(int capacity) {
        this.setCapacity(capacity);
    }

    // =================================================================================
    // Capacity
    // =================================================================================

    /**
     * Get the capacity of this laboratory
     *
     * @return Capacity (number of storerooms) of this laboratory
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Set the capacity of this laboratory
     *
     * @param capacity Given capacity (number of storerooms) for this laboratory
     *
     * @post Capacity is given capacity
     *      | new.getCapacity() == capacity
     */
    private void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // =================================================================================
    // Storage
    // =================================================================================

    /**
     * Get a given amount of the stored ingredient with given name
     *
     * @param name Given name to query, can correspond to simple or special name of the ingredient
     * @param quantity Given amount to take out
     *
     * @return Ingredient container with the requested ingredient and amount
     *      | result.getContents().getIngredientType().getSimpleName() == name || result.getContents().getSpecialName() == name
     *      | result.getContents().getQuantity() == quantity
     *      | result.getCapacity() == Quantity.selectAppropriateUnit(result.getContents())
     *
     * @throws IllegalArgumentException If given quantity's state does not match the state of the stored ingredient
     *      | WIP
     *
     * @throws IllegalArgumentException If requested quantity exceeds the available amount in storage
     *      | WIP
     *
     * @throws IllegalArgumentException If laboratory contains no ingredient with the given name
     *      | for ingredient in storage:
     *      |   ingredient.getIngredientType().getSimpleName() != name && ingredient.getSpecialName() != name
     */
    public IngredientContainer getIngredient(String name, Quantity quantity) throws IllegalArgumentException {
        // Name can refer to both the simple name and the special name
        for (Ingredient ingredient : storage) {
            if (ingredient.getIngredientType().getSimpleName().equals(name) || ingredient.getSpecialName().equals(name)) { // Match found!
                // Check if requested quantity is of correct state
                if (quantity.getState() != ingredient.getState()) {
                    throw new IllegalArgumentException("Given quantity does not match state of ingredient in storage.");
                }

                // Check if there is enough available in storage
                if (ingredient.getQuantity().lessThan(quantity)) {
                    throw new IllegalArgumentException("Requested quantity exceeds available amount of ingredient in storage.");
                }

                // Perform transfer
                Ingredient outputIngredient = new Ingredient(ingredient.getIngredientType(),ingredient.getTemperature(),ingredient.getState(),quantity);
                IngredientContainer outputContainer = new IngredientContainer(outputIngredient);
                ingredient.setQuantity(ingredient.getQuantity().minus(quantity));
                if (ingredient.getQuantity().isZero()) { // Optional: if all ingredient taken out, get rid of it
                    storage.remove(ingredient);
                }
                return outputContainer;
            }
        }

        throw new IllegalArgumentException("Laboratory storage contains no ingredient with given name.");
    }

    /**
     * Get all the stored ingredient with given name
     *
     * @param name Given name to query, can correspond to simple or special name of the ingredient
     *
     * @return Ingredient container with as much of the requested ingredient as possible, wasting any leftovers
     *      | result.getContents().getIngredientType().getSimpleName() == name || result.getContents().getSpecialName() == name
     *      | !storage.contains(result.getContents())
     *
     * @throws IllegalArgumentException If laboratory contains no ingredient with the given name
     *      | for ingredient in storage:
     *      |   ingredient.getIngredientType().getSimpleName() != name && ingredient.getSpecialName() != name
     */
    public IngredientContainer getAllIngredient(String name) throws IllegalArgumentException {
        // Name can refer to both the simple name and the special name
        for (Ingredient ingredient : storage) {
            if (ingredient.getIngredientType().getSimpleName().equals(name) || ingredient.getSpecialName().equals(name)) { // Match found!
                // Initialize quantity
                Quantity quantity;

                // Figure out how much we can take out
                if (ingredient.getQuantity().lessThan(ingredient.getState().getAllowedUnits().get(ingredient.getState().getAllowedUnits().size()-2))) { // It fully fits!
                    quantity = ingredient.getQuantity();
                } else { // Leftovers will be wasted...
                    quantity = new Quantity(ingredient.getState(),ingredient.getState().getAllowedUnits().get(ingredient.getState().getAllowedUnits().size()-2));
                }

                // Perform transfer
                Ingredient outputIngredient = new Ingredient(ingredient.getIngredientType(),ingredient.getTemperature(),ingredient.getState(),quantity);
                IngredientContainer outputContainer = new IngredientContainer(outputIngredient);
                storage.remove(ingredient);
                return outputContainer;
            }
        }

        throw new IllegalArgumentException("Laboratory storage contains no ingredient with given name.");
    }

    /**
     * Get the total number of spoons over all ingredients stored in this laboratory
     *
     * @return Total number of spoons over all ingredients stored in this laboratory
     *      | result == sum([getSpoons(ingredient) for ingredient in storage])
     */
    public double getStoredTotal() {
        double total = 0;
        for (Ingredient ingredient : storage) {
            total += ingredient.getQuantity().getSpoons();
        }

        return total;
    }

    /**
     * Add the given ingredient to laboratory storage
     *
     * @param newIngredient Given ingredient to add
     *
     * @post If ingredient with same simple or special name as newIngredient is already present, mix them together after standardizing
     *      | WIP
     *
     * @post If no ingredient with same simple or special name is present, simply add the standardized ingredient to storage
     *      | storage.contains(new Ingredient(newIngredient.getIngredientType(),newIngredient.getIngredientType().getStandardTemperature(),newIngredient.getIngredientType().getStandardState(),newIngredient.getQuantity())
     *
     * @throws IllegalStateException If a change of state is required and no transmogrifier is available
     *      | newIngredient.getState() != newIngredient.getIngredientType().getStandardState() && getTransmogrifier() == null
     *
     * @throws IllegalStateException If an increase in temperature is required and no oven is available
     *      | newIngredient.getTemperature().lessThan(newIngredient.getIngredientType().getStandardTemperature()) && getOven() == null
     *
     * @throws IllegalStateException If a decrease in temperature is required and no cooling box is available
     *      | newIngredient.getIngredientType().getStandardTemperature().lessThan(newIngredient.getTemperature()) && getCoolingBox() == null
     *
     * @throws IllegalStateException If mixing is required and no kettle is available
     *      | WIP
     *
     * @throws IllegalArgumentException If addition of newIngredient causes storage to exceed its capacity
     *      | WIP
     */
    public void addIngredient(Ingredient newIngredient) {
        // Bring this ingredient to its standard temperature and standard state
        // Make sure to make pseudo-use of the laboratory's devices (check for their proper existence, but don't actually run them to prevent conflicts)

        // Logic for state
        if (newIngredient.getState() != newIngredient.getIngredientType().getStandardState()) {
            if (getTransmogrifier() == null) {
                throw new IllegalStateException("No transmogrifier is available to bring the given ingredient to its standard state.");
            }
            newIngredient.setState(newIngredient.getIngredientType().getStandardState());
        }

        // Logic for temperature
        if (!newIngredient.getTemperature().equals(newIngredient.getIngredientType().getStandardTemperature())) {
            if (newIngredient.getTemperature().lessThan(newIngredient.getIngredientType().getStandardTemperature())) { // Must be heated!
                if (getOven() == null) {
                    throw new IllegalStateException("No oven is available to bring the given ingredient to its standard temperature.");
                }
                newIngredient.setTemperature(newIngredient.getIngredientType().getStandardTemperature());
            } else { // Must be cooled down!
                if (getCoolingBox() == null) {
                    throw new IllegalStateException("No cooling box is available to bring the given ingredient to its standard temperature.");
                }
                newIngredient.setTemperature(newIngredient.getIngredientType().getStandardTemperature());
            }
        }

        // Check whether ingredient with same simple or special name is already present
        for (Ingredient ingredient : storage) {
            if (ingredient.getIngredientType().getSimpleName().equals(newIngredient.getIngredientType().getSimpleName()) || ingredient.getSpecialName().equals(newIngredient.getSpecialName())) {
                // It's already present! Time to mix and replace...

                if (getKettle() == null) {
                    throw new IllegalStateException("No kettle is available to mix the given ingredient with its already present counterpart in storage.");
                }

                Ingredient mixedIngredient = Kettle.mix(List.of(ingredient, newIngredient));

                storage.remove(ingredient);
                storage.add(mixedIngredient);

                // Check if capacity has been exceeded
                if (getStoredTotal() > getCapacity() * Unit.STOREROOM.getSpoons()) {
                    // Revert changes
                    storage.remove(mixedIngredient);
                    storage.add(ingredient);
                    throw new IllegalArgumentException("Addition of given ingredient makes storage exceed its capacity.");
                }
            } else {
                // None of this in storage yet...
                storage.add(newIngredient);

                // Check if capacity has been exceeded
                if (getStoredTotal() > getCapacity() * Unit.STOREROOM.getSpoons()) {
                    // Revert changes
                    storage.remove(newIngredient);
                    throw new IllegalArgumentException("Addition of given ingredient makes storage exceed its capacity.");
                }
            }
        }
    }

    /**
     * Get a string message displaying the full list of stored ingredients in this laboratory
     *
     * @return Non-ordered string message starting with "Storage contents:" and then one line per ingredient, displaying the special name (if present) and simple name, along with full quantity display
     *      | output = "Storage contents:\n\n"
     *      | for ingredient in storage:
     *      |   output += ingredient.getIngredientType().getSimpleName()+"("+ingredient.getSpecialName()+"): "+ingredient.getQuantity().getDisplay()+"\n"
     *      | result == output
     */
    public String getStorage() {
        String output = "Storage contents:\n\n";
        for (Ingredient ingredient : storage) {
            if (ingredient.getSpecialName() == null) { // No special name
                output = output.concat(ingredient.getIngredientType().getSimpleName()+": "+ingredient.getQuantity().getDisplay());
            } else { // Has a special name
                output = output.concat(ingredient.getIngredientType().getSimpleName()+" ("+ingredient.getSpecialName()+"): "+ingredient.getQuantity().getDisplay());
            }
            output = output.concat("\n");
        }
        return output;
    }

    // =================================================================================
    // Owned devices
    // =================================================================================

    /**
     * Get the cooling box stored in this laboratory
     *
     * @return Cooling box stored in this laboratory
     */
    public CoolingBox getCoolingBox() {
        return coolingBox;
    }

    /**
     * Set the cooling box stored in this laboratory
     *
     * @param coolingBox Given cooling box to store in this laboratory
     *
     * @post Cooling box is given cooling box
     *      | new.getCoolingBox() == coolingBox
     *
     * @post Given cooling box is connected to this laboratory
     *      | (new) coolingBox.getLocation() == this
     *
     * @post If another cooling box was connected here, it is disconnected
     *      | getCoolingBox().getLocation() == null
     *
     * @post If given cooling box was connected somewhere, it is disconnected
     *      | coolingBox.getLocation().getCoolingBox() == null
     */
    public void setCoolingBox(CoolingBox coolingBox) {
        // Disconnect existing device, if one exists
        if (getCoolingBox() != null) {
            getCoolingBox().setLocation(null);
        }

        // Disconnect the given device from existing laboratory, if needed
        if (coolingBox != null && coolingBox.getLocation() != null) {
            coolingBox.getLocation().setCoolingBox(null);
        }

        // Connect the device here
        if (coolingBox != null) {
            coolingBox.setLocation(this);
        }
        this.coolingBox = coolingBox;
    }

    /**
     * Get the oven stored in this laboratory
     *
     * @return Oven stored in this laboratory
     */
    public Oven getOven() {
        return oven;
    }

    /**
     * Set the oven stored in this laboratory
     *
     * @param oven Given oven to store in this laboratory
     *
     * @post Oven is given oven
     *      | new.getOven() == oven
     *
     * @post Given oven is connected to this laboratory
     *      | (new) oven.getLocation() == this
     *
     * @post If another oven was connected here, it is disconnected
     *      | getOven().getLocation() == null
     *
     * @post If given oven was connected somewhere, it is disconnected
     *      | oven.getLocation().getOven() == null
     */
    public void setOven(Oven oven) {
        // Disconnect existing device, if one exists
        if (getOven() != null) {
            getOven().setLocation(null);
        }

        // Disconnect the given device from existing laboratory, if needed
        if (oven != null && oven.getLocation() != null) {
            oven.getLocation().setOven(null);
        }

        // Connect the device here
        if (oven != null) {
            oven.setLocation(this);
        }
        this.oven = oven;
    }

    /**
     * Get the kettle stored in this laboratory
     *
     * @return Kettle stored in this laboratory
     */
    public Kettle getKettle() {
        return kettle;
    }

    /**
     * Set the kettle stored in this laboratory
     *
     * @param kettle Given kettle to store in this laboratory
     *
     * @post Kettle is given kettle
     *      | new.getKettle() == kettle
     *
     * @post Given kettle is connected to this laboratory
     *      | (new) kettle.getLocation() == this
     *
     * @post If another kettle was connected here, it is disconnected
     *      | getKettle().getLocation() == null
     *
     * @post If given kettle was connected somewhere, it is disconnected
     *      | kettle.getLocation().getKettle() == null
     */
    public void setKettle(Kettle kettle) {
        // Disconnect existing device, if one exists
        if (getKettle() != null) {
            getKettle().setLocation(null);
        }

        // Disconnect the given device from existing laboratory, if needed
        if (kettle != null && kettle.getLocation() != null) {
            kettle.getLocation().setKettle(null);
        }

        // Connect the device here
        if (kettle != null) {
            kettle.setLocation(this);
        }
        this.kettle = kettle;
    }

    /**
     * Get the transmogrifier stored in this laboratory
     *
     * @return Transmogrifier stored in this laboratory
     */
    public Transmogrifier getTransmogrifier() {
        return transmogrifier;
    }

    /**
     * Set the transmogrifier stored in this laboratory
     *
     * @param transmogrifier Given transmogrifier to store in this laboratory
     *
     * @post Transmogrifier is given transmogrifier
     *      | new.getTransmogrifier() == transmogrifier
     *
     * @post Given transmogrifier is connected to this laboratory
     *      | (new) transmogrifier.getLocation() == this
     *
     * @post If another transmogrifier was connected here, it is disconnected
     *      | getTransmogrifier().getLocation() == null
     *
     * @post If given transmogrifier was connected somewhere, it is disconnected
     *      | transmogrifier.getLocation().getTransmogrifier() == null
     */
    public void setTransmogrifier(Transmogrifier transmogrifier) {
        // Disconnect existing device, if one exists
        if (getTransmogrifier() != null) {
            getTransmogrifier().setLocation(null);
        }

        // Disconnect the given device from existing laboratory, if needed
        if (transmogrifier != null && transmogrifier.getLocation() != null) {
            transmogrifier.getLocation().setTransmogrifier(null);
        }

        // Connect the device here
        if (transmogrifier != null) {
            transmogrifier.setLocation(this);
        }
        this.transmogrifier = transmogrifier;
    }
}
