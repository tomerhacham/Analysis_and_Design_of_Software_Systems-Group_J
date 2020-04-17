package Test;
import BusinessLayer.*;
import InterfaceLayer.FacadeController;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DriverControllerTest {
    DriverController driverController;
    FacadeController facadeController;

    public DriverControllerTest() {
        facadeController = FacadeController.getInstance();
        driverController = DriverController.getInstance();
        createDrivers();
    }

    public void createDrivers() {
        facadeController.createDriver("David", "C");
        facadeController.createDriver("Shon", "C1");
        facadeController.createDriver("Noam", "C4");
    }

    @Test
    public void checkIfAvailableByLicence() {
        assertTrue(driverController.checkIfAvailableByLicence("C", 0));
        assertTrue(driverController.checkIfAvailableByLicence("C1", 1));
        assertTrue(driverController.checkIfAvailableByLicence("C4", 2));

        //The checked licenses are different than the drivers licenses
        assertFalse(driverController.checkIfAvailableByLicence("D", 0));
        assertFalse(driverController.checkIfAvailableByLicence("B1", 1));
        assertFalse(driverController.checkIfAvailableByLicence("C", 2));

        //driver doesn't exist
        assertFalse(driverController.checkIfAvailableByLicence("C", 3));
    }

    @Test
    public void checkCreateAndDelete() {
        facadeController.createDriver("lola", "C");
        Driver d = driverController.getById(3);
        assertNotNull(d);
        driverController.DeleteDriver(3);
        d = driverController.getById(3);
        assertNull(d);
    }
}
