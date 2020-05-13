package TestTransportModule;
/*import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
*/
public class DriverControllerTest {/*
    //DriverController driverController;
    FacadeController facadeController;

    public DriverControllerTest() {
        facadeController = FacadeController.getInstance();
        driverController = DriverController.getInstance();
        createDrivers();
    }

    private void createDrivers() {
        driverController.reset();
        facadeController.createDriver("Yossi", "C1");
        facadeController.createDriver("David", "C");
        facadeController.createDriver("Shon", "C1");
        facadeController.createDriver("Noam", "C4");
    }

    @Test
    public void checkIfAvailableByLicence() {
        assertTrue(driverController.checkIfAvailableByLicence("C", 1));
        assertTrue(driverController.checkIfAvailableByLicence("C1", 2));
        assertTrue(driverController.checkIfAvailableByLicence("C4", 3));

        //The checked licenses are different than the drivers licenses
        assertFalse(driverController.checkIfAvailableByLicence("D", 1));
        assertFalse(driverController.checkIfAvailableByLicence("B1", 2));
        assertFalse(driverController.checkIfAvailableByLicence("C", 3));

        //driver doesn't exist
        assertFalse(driverController.checkIfAvailableByLicence("C", 20));
    }

    @Test
    public void checkCreateAndDelete() {
        facadeController.createDriver("lola", "C");
        Driver d = driverController.getById(4);
        assertNotNull(d);
        driverController.DeleteDriver(4);
        d = driverController.getById(4);
        assertNull(d);
    }*/
}
