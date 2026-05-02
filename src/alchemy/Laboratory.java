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
    private int capacity; // Number of Storerooms in capacity
    private List<Ingredient> storage = new ArrayList<>();
    private CoolingBox coolingBox = null;
    private Oven oven = null;
    private Kettle kettle = null;
    private Transmogrifier transmogrifier = null;

    // =================================================================================
    // Constructors
    // =================================================================================

    /**
     * Create a new laboratory with given capacity in number of storerooms
     *
     * @param capacity Capacity in number of storerooms
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
     *
     * @throws IllegalArgumentException If given quantity's state does not match the state of the stored ingredient
     * @throws IllegalArgumentException If requested quantity exceeds the available amount in storage
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
     */
    public IngredientContainer getAllIngredient(String name) {
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
     * @param newIngredient Given ingredient
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
     * @return String list of stored ingredients with name and quantity
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
