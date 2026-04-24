import alchemy.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing suite for the Quantity class
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class QuantityTest {
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
    public void simplifyTest() {
        Map<Unit,Integer> myMap = new HashMap<>();

        myMap.put(Unit.DROP,17);
        myMap.put(Unit.SPOON,16);
        myMap.put(Unit.BOTTLE,2);

        Quantity myQuantity = new Quantity(State.LIQUID,myMap);
        myQuantity.simplifyUnit();

        assertEquals(1,myQuantity.getAmountOf(Unit.DROP));
        assertEquals(3,myQuantity.getAmountOf(Unit.SPOON));
        assertEquals(3,myQuantity.getAmountOf(Unit.BOTTLE));
    }

    @Test
    public void sumTest() {
        Map<Unit,Integer> myMap1 = new HashMap<>();
        myMap1.put(Unit.DROP,5);
        myMap1.put(Unit.SPOON,7);
        myMap1.put(Unit.BOTTLE,2);

        Map<Unit,Integer> myMap2 = new HashMap<>();
        myMap2.put(Unit.PINCH,4);

        Map<Unit,Integer> myMap3 = new HashMap<>();
        myMap3.put(Unit.PINCH,3);

        Quantity myQuantity1 = new Quantity(State.LIQUID,myMap1);
        Quantity myQuantity2 = new Quantity(State.POWDER,myMap2);
        Quantity myQuantity3 = new Quantity(State.POWDER,myMap3);

        Quantity sumQuantity = Quantity.sum(List.of(myQuantity1,myQuantity2,myQuantity3),State.LIQUID);

        assertEquals(5,sumQuantity.getAmountOf(Unit.DROP));
        assertEquals(38,sumQuantity.getAmountOf(Unit.SPOON));
    }

    @Test
    public void illegalAmountsTest() {
        Map<Unit,Integer> myMap = new HashMap<>();

        myMap.put(Unit.DROP,3);

        assertFalse(Quantity.canHaveAsAmounts(myMap,State.POWDER));
    }

    @Test
    public void plusTest() {
        Map<Unit,Integer> myMap1 = new HashMap<>();
        myMap1.put(Unit.SPOON,5);

        Map<Unit,Integer> myMap2 = new HashMap<>();
        myMap2.put(Unit.SPOON,4);

        Quantity myQuantity1 = new Quantity(State.LIQUID,myMap1);
        Quantity myQuantity2 = new Quantity(State.LIQUID,myMap2);

        Quantity sumQuantity = myQuantity1.plus(myQuantity2);

        assertEquals(9,sumQuantity.getAmountOf(Unit.SPOON));
    }

    @Test
    public void comparisonTest() {
        Map<Unit,Integer> myMap1 = new HashMap<>();
        myMap1.put(Unit.SPOON,9);

        Map<Unit,Integer> myMap2 = new HashMap<>();
        myMap2.put(Unit.SPOON,5);

        Quantity myQuantity1 = new Quantity(State.LIQUID,myMap1);
        Quantity myQuantity2 = new Quantity(State.LIQUID,myMap2);

        assertFalse(myQuantity1.lessThan(myQuantity2));
        assertTrue(myQuantity1.lessThan(Unit.BOTTLE));
    }
}
