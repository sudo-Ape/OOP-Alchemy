package alchemy;

/**
 * Cooling box class for a machine that cools down ingredients in the laboratory
 *
 * @author Casper Vermeeren; Loïck Sansen
 */
public class CoolingBox extends Device {
    // =================================================================================
    // Fields
    // =================================================================================
    private Temperature temperature;

    // =================================================================================
    // Constructor
    // =================================================================================

    // WIP...

    // =================================================================================
    // Temperature
    // =================================================================================

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    // =================================================================================
    // Run
    // =================================================================================

    @Override
    public void run() {
        // WIP
    }
}
