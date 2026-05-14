import alchemy.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing suite for the Device class and its subclasses
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class DeviceTest {
    // =================================================================================
    // Before tests
    // =================================================================================

    public CoolingBox myCoolingBox = new CoolingBox("(0,0)");
    public Oven myOven = new Oven("(0,0)");
    public Transmogrifier myTransmogrifier = new Transmogrifier(State.LIQUID);
    public Kettle myKettle = new Kettle();
    public Laboratory myLaboratory = new Laboratory(5);

    @BeforeAll
    public static void BeforeAllTests() {
        // Stuff to run once before all the tests begin
    }

    @BeforeEach
    public void BeforeEachTest() {
        // Stuff to run before each test begins
        myLaboratory.setCoolingBox(myCoolingBox);
        myLaboratory.setOven(myOven);
        myLaboratory.setTransmogrifier(myTransmogrifier);
        myLaboratory.setKettle(myKettle);
    }

    // =================================================================================
    // Tests: CoolingBox
    // =================================================================================

    @Test
    public void coolTestPossible() { // Cool something down
        Ingredient myIngredient = new Ingredient("Taco Bell Cheese","(0,10)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.BOTTLE,1)));
        IngredientContainer myIngredientContainer = new IngredientContainer(myIngredient);

        myLaboratory.getCoolingBox().setTemperature("(0,5)");
        myLaboratory.getCoolingBox().add(myIngredientContainer);
        myLaboratory.getCoolingBox().run();

        IngredientContainer result = myLaboratory.getCoolingBox().collect();

        assertEquals(5,result.getContents().getTemperature().getValue());
    }

    @Test
    public void coolTestNoEffect() { // Cool something down, but it's already below the temp
        Ingredient myIngredient = new Ingredient("Strawberry Ice Cream","(10,0)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,1)));
        IngredientContainer myIngredientContainer = new IngredientContainer(myIngredient);

        myLaboratory.getCoolingBox().setTemperature("(0,5)");
        myLaboratory.getCoolingBox().add(myIngredientContainer);
        myLaboratory.getCoolingBox().run();

        IngredientContainer result = myLaboratory.getCoolingBox().collect();

        assertEquals(-10,result.getContents().getTemperature().getValue());
    }

    @Test
    public void coolWrongUse() { // Use cooling box while not in a laboratory
        assertThrows(IllegalStateException.class, () -> {
            Ingredient myIngredient = new Ingredient("Loïck's Running Sweat","(10,0)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,1)));
            IngredientContainer myIngredientContainer = new IngredientContainer(myIngredient);

            myLaboratory.setCoolingBox(null); // Remove the cooling box from its laboratory

            myCoolingBox.add(myIngredientContainer); // Error occurs here!
        });
    }

    @Test
    public void coolMultipleIngredients() { // Try to put multiple ingredients into a cooling box (exception)
        assertThrows(IllegalStateException.class, () -> {
            Ingredient myIngredient1 = new Ingredient("Loïck's Running Sweat","(10,0)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,1)));
            IngredientContainer myIngredientContainer1 = new IngredientContainer(myIngredient1);

            Ingredient myIngredient2 = new Ingredient("Casper's Running Sweat","(10,0)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.BOTTLE,1)));
            IngredientContainer myIngredientContainer2 = new IngredientContainer(myIngredient2);

            myLaboratory.getCoolingBox().add(myIngredientContainer1);
            myLaboratory.getCoolingBox().add(myIngredientContainer2); // Error occurs here!
        });
    }

    // =================================================================================
    // Tests: Oven
    // =================================================================================

    @Test
    public void heatTestPossible() { // Heat something up
        Ingredient myIngredient = new Ingredient("Diksmuidse Pannenkoek","(0,15)", State.POWDER,new Quantity(State.POWDER, Map.of(Unit.SACK,1)));
        IngredientContainer myIngredientContainer = new IngredientContainer(myIngredient);

        myLaboratory.getOven().setTemperature("(0,30)");
        myLaboratory.getOven().add(myIngredientContainer);
        myLaboratory.getOven().run();

        IngredientContainer result = myLaboratory.getOven().collect();

        assertTrue(result.getContents().getTemperature().getValue() <= 35);
        assertTrue(result.getContents().getTemperature().getValue() >= 25);
    }

    @Test
    public void heatTestNoEffect() { // Heat something up, but it's already above the temp
        Ingredient myIngredient = new Ingredient("Scorching Lava","(0,1200)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)));
        IngredientContainer myIngredientContainer = new IngredientContainer(myIngredient);

        myLaboratory.getOven().setTemperature("(0,300)");
        myLaboratory.getOven().add(myIngredientContainer);
        myLaboratory.getOven().run();

        IngredientContainer result = myLaboratory.getOven().collect();

        assertEquals(1200,result.getContents().getTemperature().getValue());
    }

    @Test
    public void heatWrongUse() { // Use oven while not in a laboratory
        assertThrows(IllegalStateException.class, () -> {
            Ingredient myIngredient = new Ingredient("Molten Chocolate","(0,25)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,1)));
            IngredientContainer myIngredientContainer = new IngredientContainer(myIngredient);

            myLaboratory.setOven(null); // Remove the oven from its laboratory

            myOven.add(myIngredientContainer); // Error occurs here!
        });
    }

    @Test
    public void heatMultipleIngredients() { // Try to put multiple ingredients into an oven (exception)
        assertThrows(IllegalStateException.class, () -> {
            Ingredient myIngredient1 = new Ingredient("White Chocolate","(0,15)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,1)));
            IngredientContainer myIngredientContainer1 = new IngredientContainer(myIngredient1);

            Ingredient myIngredient2 = new Ingredient("Milk Chocolate","(0,15)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.BOTTLE,1)));
            IngredientContainer myIngredientContainer2 = new IngredientContainer(myIngredient2);

            myLaboratory.getOven().add(myIngredientContainer1);
            myLaboratory.getOven().add(myIngredientContainer2); // Error occurs here!
        });
    }

    // =================================================================================
    // Tests: Transmogrifier
    // =================================================================================

    @Test
    public void changeTestPossible() { // Change something's state
        Ingredient myIngredient = new Ingredient("Siamese Cat","(0,15)", State.POWDER,new Quantity(State.POWDER, Map.of(Unit.SACK,1)));
        IngredientContainer myIngredientContainer = new IngredientContainer(myIngredient);

        myLaboratory.getTransmogrifier().setGoalState(State.LIQUID);
        myLaboratory.getTransmogrifier().add(myIngredientContainer);
        myLaboratory.getTransmogrifier().run();

        IngredientContainer result = myLaboratory.getTransmogrifier().collect();

        assertEquals(State.LIQUID,result.getContents().getState());
    }

    @Test
    public void changeWrongUse() { // Use transmogrifier while not in a laboratory
        assertThrows(IllegalStateException.class, () -> {
            Ingredient myIngredient = new Ingredient("Brussel's Sprouts","(0,25)", State.POWDER,new Quantity(State.POWDER, Map.of(Unit.BARREL,1)));
            IngredientContainer myIngredientContainer = new IngredientContainer(myIngredient);

            myLaboratory.setTransmogrifier(null); // Remove the transmogrifier from its laboratory

            myTransmogrifier.add(myIngredientContainer); // Error occurs here!
        });
    }

    @Test
    public void changeMultipleIngredients() { // Try to put multiple ingredients into a transmogrifier (exception)
        assertThrows(IllegalStateException.class, () -> {
            Ingredient myIngredient1 = new Ingredient("French Fries","(0,15)", State.POWDER,new Quantity(State.POWDER, Map.of(Unit.PINCH,1)));
            IngredientContainer myIngredientContainer1 = new IngredientContainer(myIngredient1);

            Ingredient myIngredient2 = new Ingredient("Belgian Fries","(0,15)", State.POWDER,new Quantity(State.POWDER, Map.of(Unit.SACK,1)));
            IngredientContainer myIngredientContainer2 = new IngredientContainer(myIngredient2);

            myLaboratory.getOven().add(myIngredientContainer1);
            myLaboratory.getOven().add(myIngredientContainer2); // Error occurs here!
        });
    }

    // =================================================================================
    // Tests: Kettle
    // =================================================================================

    @Test
    public void mixTest1() { // Mix two ingredients and check all calculations (no ties)
        Ingredient myIngredient1 = new Ingredient("Apple","(0,10)",State.POWDER,new Quantity(State.POWDER,Map.of(Unit.SPOON,4)),State.POWDER,"(0,15)");
        Ingredient myIngredient2 = new Ingredient("Zebra Melon","(0,35)",State.LIQUID,new Quantity(State.LIQUID,Map.of(Unit.SPOON,12)),State.POWDER,"(0,35)");

        IngredientContainer myIngredientContainer1 = new IngredientContainer(myIngredient1);
        IngredientContainer myIngredientContainer2 = new IngredientContainer(myIngredient2);

        myLaboratory.getKettle().add(myIngredientContainer1);
        myLaboratory.getKettle().add(myIngredientContainer2);
        myLaboratory.getKettle().run();

        IngredientContainer result = myLaboratory.getKettle().collect();

        assertEquals("Apple mixed with Zebra Melon",result.getContents().getIngredientType().getSimpleName());
        assertEquals(State.POWDER,result.getContents().getState());
        assertEquals(State.POWDER,result.getContents().getIngredientType().getStandardState());
        assertEquals(myIngredient1.getQuantity().plus(myIngredient2.getQuantity()).getSpoons(),result.getContents().getQuantity().getSpoons());
        assertEquals(15,result.getContents().getIngredientType().getStandardTemperature().getValue());
        assertEquals(29,result.getContents().getTemperature().getValue());
    }

    @Test
    public void mixTest2() { // Mix two ingredients and check all calculations (ties)
        Ingredient myIngredient1 = new Ingredient("Apple","(0,10)",State.POWDER,new Quantity(State.POWDER,Map.of(Unit.SPOON,4)),State.POWDER,"(0,25)");
        Ingredient myIngredient2 = new Ingredient("Zebra Melon","(0,35)",State.LIQUID,new Quantity(State.LIQUID,Map.of(Unit.SPOON,12)),State.LIQUID,"(0,15)");

        IngredientContainer myIngredientContainer1 = new IngredientContainer(myIngredient1);
        IngredientContainer myIngredientContainer2 = new IngredientContainer(myIngredient2);

        myLaboratory.getKettle().add(myIngredientContainer1);
        myLaboratory.getKettle().add(myIngredientContainer2);
        myLaboratory.getKettle().run();

        IngredientContainer result = myLaboratory.getKettle().collect();

        assertEquals("Apple mixed with Zebra Melon",result.getContents().getIngredientType().getSimpleName());
        assertEquals(State.LIQUID,result.getContents().getState()); // Liquid before powder!
        assertEquals(State.LIQUID,result.getContents().getIngredientType().getStandardState());
        assertEquals(myIngredient1.getQuantity().plus(myIngredient2.getQuantity()).getSpoons(),result.getContents().getQuantity().getSpoons());
        assertEquals(25,result.getContents().getIngredientType().getStandardTemperature().getValue());
        assertEquals(29,result.getContents().getTemperature().getValue());
    }

    @Test
    public void mixWrongUse() { // Try to use a kettle that's not in a laboratory
        assertThrows(IllegalStateException.class, () -> {
            Ingredient myIngredient = new Ingredient("Beef Tenderloin","(0,25)", State.POWDER,new Quantity(State.POWDER, Map.of(Unit.BARREL,1)));
            IngredientContainer myIngredientContainer = new IngredientContainer(myIngredient);

            myLaboratory.setKettle(null); // Remove the kettle from its laboratory

            myKettle.add(myIngredientContainer); // Error occurs here!
        });
    }

    // =================================================================================
    // Tests: General devices
    // =================================================================================

    @Test
    public void collectEmptyTest() { // Try to collect from a device with no result
        assertThrows(IllegalStateException.class, () -> {
            myLaboratory.getOven().collect();
        });
    }

    @Test
    public void runEmptyTest() { // Try to run an empty device
        assertThrows(IllegalStateException.class, () -> {
            myLaboratory.getOven().run();
        });
    }

    @Test
    public void addEmptyContainerTest() { // Try to add an empty container to a device
        assertThrows(IllegalArgumentException.class, () -> {
            IngredientContainer myEmptyContainer = new IngredientContainer(Unit.BOX);
            myLaboratory.getTransmogrifier().add(myEmptyContainer);
        });
    }
}
