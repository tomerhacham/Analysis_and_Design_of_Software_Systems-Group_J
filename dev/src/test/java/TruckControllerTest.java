package java;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class TruckControllerTest {

    TruckController truckController;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public TruckControllerTest(TruckController t_c){
        truckController = t_c;
    }

    @Test
    public void createTruck() {
        // max weight smaller than net weight
        assertFalse(truckController.CreateTruck("WE-1234-FD", "RNO51", 3000, 2500, "A"));

        // max weight equal net weight
        assertFalse(truckController.CreateTruck("WE-1234-FD", "RNO51", 3000, 3000, "A"));
    }

    @Test
    public void deleteTruck() {
        assertTrue(truckController.DeleteTruck(1));
    }

    @Test
    public void checkIfTrucksAvailableByDate() {
        Date d1;
        try {
            d1 = formatter.parse("07/07/2020");
            assertTrue(truckController.checkIfTrucksAvailableByDate(d1, false));
        } catch (Exception e) {
            System.out.println("Test - checkIfTrucksAvailableByDate - Failed.");
        }
    }
}