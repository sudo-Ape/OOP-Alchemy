import alchemy.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing suite for the Laboratory class, mainly its storage
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class LaboratoryTest {
    // =================================================================================
    // Before tests
    // =================================================================================

    public Laboratory myLaboratory = new Laboratory(5);

    @BeforeAll
    public static void BeforeAllTests() {
        // Stuff to run once before all the tests begin
    }

    @BeforeEach
    public void BeforeEachTest() {
        // Stuff to run before each test begins
        myLaboratory.clear();
        myLaboratory.setTransmogrifier(null);
        myLaboratory.setKettle(null);
        myLaboratory.setOven(null);
        myLaboratory.setCoolingBox(null);
    }

    // =================================================================================
    // Tests
    // =================================================================================

    @Test
    public void addTestNew() { // Adding something new
        Ingredient myIngredient = new Ingredient("Tomato Juice","(0,5)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)));
        myLaboratory.addIngredient(myIngredient);

        assertEquals(210,myLaboratory.getStoredTotal());
        assertTrue(myLaboratory.contains(myIngredient));
    }

    @Test
    public void addTestSimpleName() { // Adding an already present ingredient (by simple name)
        Ingredient myIngredient1 = new Ingredient("Tomato Juice","(0,5)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)));
        myLaboratory.addIngredient(myIngredient1);

        Kettle myKettle = new Kettle();
        myLaboratory.setKettle(myKettle);

        Ingredient myIngredient2 = new Ingredient("Tomato Juice","(0,10)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)),State.LIQUID,"(0,10)");
        myLaboratory.addIngredient(myIngredient2);

        assertTrue(myLaboratory.contains(Kettle.mix(List.of(myIngredient1,myIngredient2))));
    }

    @Test
    public void addTestSpecialName() { // Adding an already present ingredient (by special name)
        Ingredient myIngredient1 = new Ingredient("Tomato Juice","(0,5)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)));
        myIngredient1.setSpecialName("Red Food");
        myLaboratory.addIngredient(myIngredient1);

        Kettle myKettle = new Kettle();
        myLaboratory.setKettle(myKettle);

        Ingredient myIngredient2 = new Ingredient("Brandished Pepper","(0,10)", State.POWDER,new Quantity(State.POWDER, Map.of(Unit.SACK,3)),State.POWDER,"(0,10)");
        myIngredient2.setSpecialName("Red Food");
        myLaboratory.addIngredient(myIngredient2);

        assertTrue(myLaboratory.contains(Kettle.mix(List.of(myIngredient1,myIngredient2))));
    }

    @Test
    public void addTestTransmogrifier() { // Add something with use of transmogrifier
        Transmogrifier myTransmogrifier = new Transmogrifier(State.POWDER);
        myLaboratory.setTransmogrifier(myTransmogrifier);

        Ingredient myIngredient1 = new Ingredient("Tomato Juice","(0,5)", State.POWDER,new Quantity(State.POWDER, Map.of(Unit.JUG,2)),State.LIQUID,"(0,5)");
        myLaboratory.addIngredient(myIngredient1);
        Ingredient myIngredient1Standard = new Ingredient("Tomato Juice","(0,5)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)),State.LIQUID,"(0,5)");

        assertTrue(myLaboratory.contains(myIngredient1Standard));
    }

    @Test
    public void addTestTransmogrifierMissing() { // Add something with use of transmogrifier, but it's missing
        assertThrows(IllegalStateException.class, () -> {
            Ingredient myIngredient1 = new Ingredient("Tomato Juice","(0,5)", State.POWDER,new Quantity(State.POWDER, Map.of(Unit.JUG,2)),State.LIQUID,"(0,5)");
            myLaboratory.addIngredient(myIngredient1);
        });
    }

    @Test
    public void addTestCoolingBox() { // Add something with use of cooling box
        CoolingBox myCoolingBox = new CoolingBox("(18,0)");
        myLaboratory.setCoolingBox(myCoolingBox);

        Ingredient myIngredient1 = new Ingredient("Tomato Juice","(0,10)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)),State.LIQUID,"(0,5)");
        myLaboratory.addIngredient(myIngredient1);
        Ingredient myIngredient1Standard = new Ingredient("Tomato Juice","(0,5)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)),State.LIQUID,"(0,5)");

        assertTrue(myLaboratory.contains(myIngredient1Standard));
    }

    @Test
    public void addTestCoolingBoxMissing() { // Add something with use of cooling box, but it's missing
        assertThrows(IllegalStateException.class, () -> {
            Ingredient myIngredient1 = new Ingredient("Tomato Juice","(0,10)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)),State.LIQUID,"(0,5)");
            myLaboratory.addIngredient(myIngredient1);
        });
    }

    @Test
    public void addTestOven() { // Add something with use of oven
        Oven myOven = new Oven("(0,200)");
        myLaboratory.setOven(myOven);

        Ingredient myIngredient1 = new Ingredient("Tomato Juice","(0,0)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)),State.LIQUID,"(0,5)");
        myLaboratory.addIngredient(myIngredient1);
        Ingredient myIngredient1Standard = new Ingredient("Tomato Juice","(0,5)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)),State.LIQUID,"(0,5)");

        assertTrue(myLaboratory.contains(myIngredient1Standard));
    }

    @Test
    public void addTestOvenMissing() { // Add something with use of oven, but it's missing
        assertThrows(IllegalStateException.class, () -> {
            Ingredient myIngredient1 = new Ingredient("Tomato Juice","(0,0)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)),State.LIQUID,"(0,5)");
            myLaboratory.addIngredient(myIngredient1);
        });
    }

    @Test
    public void addTestKettleMissing() { // Add something that is already present, but the kettle is missing
        assertThrows(IllegalStateException.class, () -> {
            Ingredient myIngredient1 = new Ingredient("Tomato Juice","(0,5)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)));
            myLaboratory.addIngredient(myIngredient1);

            Ingredient myIngredient2 = new Ingredient("Tomato Juice","(0,10)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.JUG,2)),State.LIQUID,"(0,10)");
            myLaboratory.addIngredient(myIngredient2);
        });
    }

    @Test
    public void getTestBasic() { // Basic get ingredient test
        Ingredient myIngredient1 = new Ingredient("Grandma's Ashes","(0,10)", State.POWDER,new Quantity(State.POWDER, Map.of(Unit.SACHET,1)));
        myLaboratory.addIngredient(myIngredient1);

        IngredientContainer myTakeOut = myLaboratory.getIngredient("Grandma's Ashes",new Quantity(State.POWDER,Map.of(Unit.SPOON,3)));

        assertTrue(myTakeOut.getContents().equals(myIngredient1));
        assertEquals(3, myTakeOut.getContents().getQuantity().getSpoons());
        assertEquals(4,myLaboratory.getAmountOf(myIngredient1).getSpoons());
    }

    @Test
    public void getTestNonExistent() { // Get something that does not exist
        assertThrows(IllegalArgumentException.class, () -> {
            Ingredient myIngredient1 = new Ingredient("Rotten Banana Juice","(0,10)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.BOTTLE,1)));
            myLaboratory.addIngredient(myIngredient1);

            IngredientContainer myTakeOut = myLaboratory.getIngredient("Healthy Smoothie",new Quantity(State.LIQUID,Map.of(Unit.SPOON,3)));
        });
    }

    @Test
    public void getTestTooMuch() { // Get something that there is not enough of
        assertThrows(IllegalArgumentException.class, () -> {
            Ingredient myIngredient1 = new Ingredient("Rotten Banana Juice","(0,10)", State.LIQUID,new Quantity(State.LIQUID, Map.of(Unit.BOTTLE,1)));
            myLaboratory.addIngredient(myIngredient1);

            IngredientContainer myTakeOut = myLaboratory.getIngredient("Rotten Banana Juice",new Quantity(State.LIQUID,Map.of(Unit.SPOON,16)));
        });
    }

    @Test
    public void getTestAllByHand() { // Get all of something by hand
        Ingredient myIngredient1 = new Ingredient("Sugar","(0,10)", State.POWDER,new Quantity(State.POWDER, Map.of(Unit.SACHET,1)));
        myLaboratory.addIngredient(myIngredient1);

        IngredientContainer myTakeOut = myLaboratory.getIngredient("Sugar",new Quantity(State.POWDER,Map.of(Unit.SPOON,7)));

        assertTrue(myTakeOut.getContents().equals(myIngredient1));
        assertEquals(7, myTakeOut.getContents().getQuantity().getSpoons());
        assertFalse(myLaboratory.contains(myIngredient1));
    }

    @Test
    public void getTestAll() { // Get all of an ingredient
        Ingredient myIngredient1 = new Ingredient("Sugar","(0,10)", State.POWDER,new Quantity(State.POWDER, Map.of(Unit.SACHET,1)));
        myLaboratory.addIngredient(myIngredient1);

        IngredientContainer myTakeOut = myLaboratory.getAllIngredient("Sugar");
        assertTrue(myTakeOut.getContents().equals(myIngredient1));
        assertEquals(7, myTakeOut.getContents().getQuantity().getSpoons());
        assertFalse(myLaboratory.contains(myIngredient1));
    }

    @Test
    public void getTestAllOverflow() { // Get all of an ingredient, confirming the overflow is wasted
        Ingredient myIngredient1 = new Ingredient("Sugar","(0,10)", State.POWDER,new Quantity(State.POWDER, Map.of(Unit.STOREROOM,1)));
        myLaboratory.addIngredient(myIngredient1);

        IngredientContainer myTakeOut = myLaboratory.getAllIngredient("Sugar");
        assertTrue(myTakeOut.getContents().equals(myIngredient1));
        assertEquals(1260, myTakeOut.getContents().getQuantity().getSpoons());
        assertFalse(myLaboratory.contains(myIngredient1));
    }

    @Test
    public void deviceTest() { // Test setting devices and proper associations
        Laboratory otherLaboratory = new Laboratory(4);

        Kettle firstKettle = new Kettle();
        Kettle secondKettle = new Kettle();

        myLaboratory.setKettle(firstKettle);
        otherLaboratory.setKettle(secondKettle);

        myLaboratory.setKettle(secondKettle);

        assertNull(otherLaboratory.getKettle());
        assertEquals(myLaboratory,secondKettle.getLocation());
        assertNull(firstKettle.getLocation());
    }
}
