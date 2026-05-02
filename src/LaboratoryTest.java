import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testing suite for the Laboratory class
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class LaboratoryTest {
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
    public void exampleTest() {
        // Your test here

        assertEquals(2,1+1);
    }

    @Test
    public void exampleExceptionTest() {
        assertThrows(ArithmeticException.class, () -> {
            // Your test here

            int result = 12 / 0;
        });
    }
}
