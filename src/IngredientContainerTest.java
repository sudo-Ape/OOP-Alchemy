import alchemy.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing suite for the IngredientContainer class
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class IngredientContainerTest {
    // =================================================================================
    // Before tests
    // =================================================================================

    public IngredientContainer myContainer = new IngredientContainer(Unit.CHEST);

    @BeforeAll
    public static void BeforeAllTests() {
        // Stuff to run once before all the tests begin
    }

    @BeforeEach
    public void BeforeEachTest() {
        // Stuff to run before each test begins
        myContainer.empty();
    }

    // =================================================================================
    // Tests
    // =================================================================================
    @Test
    public void constructorTest() {
        Ingredient myIngredient = new Ingredient("Pink Rose","(0,10)", State.POWDER, new Quantity(State.POWDER, Map.of(Unit.SPOON,5)));
        IngredientContainer container = new IngredientContainer(Unit.BOX,myIngredient);

        assertEquals(myIngredient,container.getContents());
        assertEquals(Unit.BOX,container.getCapacity());
    }

    @Test
    public void emptyConstructorTest() {
        IngredientContainer container = new IngredientContainer(Unit.BOX);

        assertNull(container.getContents());
        assertEquals(Unit.BOX,container.getCapacity());
    }

    @Test
    public void properCapacityConstructorTest() {
        Ingredient myIngredient = new Ingredient("Fanta Lemon","(0,5)", State.LIQUID, new Quantity(State.LIQUID, Map.of(Unit.SPOON,5)));
        IngredientContainer container = new IngredientContainer(myIngredient);

        assertEquals(myIngredient,container.getContents());
        assertEquals(Unit.VIAL,container.getCapacity());
    }

    @Test
    public void addTest() {
        Ingredient myIngredient1 = new Ingredient("Fanta Peach","(0,5)", State.LIQUID, new Quantity(State.LIQUID, Map.of(Unit.SPOON,5)));
        Ingredient myIngredient2 = new Ingredient("Fanta Peach","(0,5)", State.LIQUID, new Quantity(State.LIQUID, Map.of(Unit.SPOON,8)));

        myContainer.add(myIngredient1);
        myContainer.add(myIngredient2);

        assertEquals(13,myContainer.getContents().getQuantity().getSpoons());
        assertTrue(myContainer.getContents().equals(myIngredient1));
    }

    @Test
    public void illegalCapacityTest() { // Try illegally setting to smallest/highest unit of a state
        assertThrows(IllegalArgumentException.class, () -> {
            IngredientContainer brokenContainer = new IngredientContainer(Unit.STOREROOM);
        });
    }

    @Test
    public void illegalAddTest1() { // Try overflowing the container
        assertThrows(IllegalArgumentException.class, () -> {
            Ingredient myIngredient1 = new Ingredient("Protein Powder","(0,5)", State.POWDER, new Quantity(State.POWDER, Map.of(Unit.SPOON,500)));
            Ingredient myIngredient2 = new Ingredient("Protein Powder","(0,5)", State.POWDER, new Quantity(State.POWDER, Map.of(Unit.SPOON,800)));

            myContainer.add(myIngredient1);
            myContainer.add(myIngredient2);
        });
    }

    @Test
    public void illegalAddTest2() { // Try adding non-equal ingredient to partially filled container
        assertThrows(IllegalArgumentException.class, () -> {
            Ingredient myIngredient1 = new Ingredient("Strawberry Juice","(0,5)", State.LIQUID, new Quantity(State.LIQUID, Map.of(Unit.SPOON,50)));
            Ingredient myIngredient2 = new Ingredient("Blueberry Juice","(0,5)", State.LIQUID, new Quantity(State.LIQUID, Map.of(Unit.SPOON,80)));

            myContainer.add(myIngredient1);
            myContainer.add(myIngredient2);
        });
    }

    @Test
    public void illegalAddTest3() { // Try adding a null ingredient
        assertThrows(IllegalArgumentException.class, () -> {
            myContainer.add(null);
        });
    }

    @Test
    public void emptyTest() { // Test the emptying
        Ingredient myIngredient = new Ingredient("Forgotten Dust","(0,5)", State.POWDER, new Quantity(State.POWDER, Map.of(Unit.SPOON,50)));

        myContainer.add(myIngredient);

        myContainer.empty();

        assertNull(myContainer.getContents());
        assertTrue(myIngredient.isTerminated());
    }

    @Test
    public void terminateContentsTest() { // Terminate a container's contents and see if it catches up
        Ingredient myIngredient = new Ingredient("Delete This Ingredient","(0,5)", State.POWDER, new Quantity(State.POWDER, Map.of(Unit.SPOON,5)));

        myContainer.add(myIngredient);

        myIngredient.terminate();

        assertNull(myContainer.getContents());
    }
}
