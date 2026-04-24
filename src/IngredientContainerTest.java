import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testing suite for the IngredientContainer class
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class IngredientContainerTest {
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
    public void constructorTest() {
        // Your test here

        assertEquals(2,1+1);
    }

    @Test
    public void emptyConstructorTest() {
        // Your test here

        assertEquals(2,1+1);
    }

    @Test
    public void addTest() {
        // Your test here

        assertEquals(2,1+1);
    }

    @Test
    public void illegalCapacityTest() { // Try illegally setting to smallest/highest unit of a state
        assertThrows(ArithmeticException.class, () -> {
            // Your test here

            int result = 12 / 0;
        });
    }

    @Test
    public void illegalAddTest1() { // Try overflowing the container
        assertThrows(ArithmeticException.class, () -> {
            // Your test here

            int result = 12 / 0;
        });
    }

    @Test
    public void illegalAddTest2() { // Try adding non-equal ingredient to partially filled container
        assertThrows(ArithmeticException.class, () -> {
            // Your test here

            int result = 12 / 0;
        });
    }
}
