import alchemy.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing suite for the Ingredient class
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class IngredientTest {
    // =================================================================================
    // Before tests
    // =================================================================================

    @BeforeAll
    public static void BeforeAllTests() {
        // Stuff to run once before all the tests begin
    }

    @BeforeEach
    public void BeforeEachTest() {
        // Stuff to run before each test begins
    }

    // =================================================================================
    // Tests
    // =================================================================================
    @Test
    public void constructorTest1() {
        Map<Unit, Integer> myQuantities = new HashMap<>();
        myQuantities.put(Unit.SACK,1);
        Ingredient myIngredient = new Ingredient("Loïck's Teeth","(10,0)",State.POWDER,new Quantity(State.POWDER,myQuantities));

        assertEquals(1,myIngredient.getQuantity().getAmountOf(Unit.SACK));
        assertEquals("Loïck's Teeth",myIngredient.getIngredientType().getSimpleName());
        assertEquals("Loïck's Teeth",myIngredient.getFullName());
        assertEquals(-10,myIngredient.getTemperature().getValue());
        assertEquals(State.POWDER,myIngredient.getState());
    }

    @Test
    public void constructorTest2() {
        Map<Unit, Integer> myQuantities = new HashMap<>();
        myQuantities.put(Unit.JUG,2);
        Ingredient myIngredient = new Ingredient("Casper's Tears","(0,5)",State.LIQUID,new Quantity(State.LIQUID,myQuantities),State.LIQUID,"(0,15)");

        assertEquals(2,myIngredient.getQuantity().getAmountOf(Unit.JUG));
        assertEquals("Casper's Tears",myIngredient.getIngredientType().getSimpleName());
        assertEquals("Cooled Casper's Tears",myIngredient.getFullName());
        assertEquals(5,myIngredient.getTemperature().getValue());
        assertEquals(State.LIQUID,myIngredient.getState());
        assertEquals(State.LIQUID,myIngredient.getIngredientType().getStandardState());
        assertEquals(15,myIngredient.getIngredientType().getStandardTemperature().getValue());
    }

    @Test
    public void specialNameTest() {
        Map<Unit, Integer> myQuantities = new HashMap<>();
        myQuantities.put(Unit.JUG,2);
        Ingredient myIngredient = new Ingredient("Casper's Tears","(0,5)",State.LIQUID,new Quantity(State.LIQUID,myQuantities),State.LIQUID,"(0,15)");
        myIngredient.setSpecialName("Cool Tears");
        assertEquals("Cool Tears",myIngredient.getSpecialName());
    }

    @Test
    public void equalsTest() {
        Map<Unit, Integer> myQuantities1 = new HashMap<>();
        myQuantities1.put(Unit.JUG,2);
        Ingredient myIngredient1 = new Ingredient("Casper's Tears","(0,5)",State.LIQUID,new Quantity(State.LIQUID,myQuantities1),State.LIQUID,"(0,15)");

        Map<Unit, Integer> myQuantities2 = new HashMap<>();
        myQuantities2.put(Unit.BOTTLE,3);
        Ingredient myIngredient2 = new Ingredient("Casper's Tears","(0,5)",State.LIQUID,new Quantity(State.LIQUID,myQuantities2),State.LIQUID,"(0,15)");

        assertTrue(myIngredient1.equals(myIngredient2));
    }

    @Test
    public void illegalNameTest1() { // Illegal char
        assertThrows(IllegalArgumentException.class, () -> {
            Map<Unit, Integer> myQuantities = new HashMap<>();
            myQuantities.put(Unit.JUG,2);
            Ingredient myIngredient = new Ingredient("Mac & Cheese","(0,5)",State.LIQUID,new Quantity(State.LIQUID,myQuantities),State.LIQUID,"(0,15)");
        });
    }

    @Test
    public void illegalNameTest2() { // One-letter word
        assertThrows(IllegalArgumentException.class, () -> {
            Map<Unit, Integer> myQuantities = new HashMap<>();
            myQuantities.put(Unit.JUG,2);
            Ingredient myIngredient = new Ingredient("Eye Of Y","(0,5)",State.LIQUID,new Quantity(State.LIQUID,myQuantities),State.LIQUID,"(0,15)");
        });
    }

    @Test
    public void illegalNameTest3() { // Two-letter single word
        assertThrows(IllegalArgumentException.class, () -> {
            Map<Unit, Integer> myQuantities = new HashMap<>();
            myQuantities.put(Unit.JUG,2);
            Ingredient myIngredient = new Ingredient("Xe","(0,5)",State.LIQUID,new Quantity(State.LIQUID,myQuantities),State.LIQUID,"(0,15)");
        });
    }

    @Test
    public void illegalNameTest4() { // Illegal casing
        assertThrows(IllegalArgumentException.class, () -> {
            Map<Unit, Integer> myQuantities = new HashMap<>();
            myQuantities.put(Unit.JUG,2);
            Ingredient myIngredient = new Ingredient("Eye of the Tiger","(0,5)",State.LIQUID,new Quantity(State.LIQUID,myQuantities),State.LIQUID,"(0,15)");
        });
    }

    @Test
    public void illegalNameTest5() { // Illegal casing (again)
        assertThrows(IllegalArgumentException.class, () -> {
            Map<Unit, Integer> myQuantities = new HashMap<>();
            myQuantities.put(Unit.JUG,2);
            Ingredient myIngredient = new Ingredient("CLANKER CAKE","(0,5)",State.LIQUID,new Quantity(State.LIQUID,myQuantities),State.LIQUID,"(0,15)");
        });
    }
}
