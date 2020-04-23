package Test;

import BusinessLayer.TruckController;
import InterfaceLayer.FacadeController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TruckControllerTest {

    FacadeController facadeController;
    TruckController truckController;

    public TruckControllerTest(){
        facadeController = FacadeController.getInstance();
        truckController = TruckController.getInstance();
        createTrucks();
    }

    private void createTrucks() {
        truckController.reset();
        facadeController.createTruck("12-23FF","XXL8",1000,1500,"C1");
        facadeController.createTruck("17-45LD","X23",1050,1260,"C1");
        facadeController.createTruck("J0-38AV","1X6",700,1000,"C");
        facadeController.createTruck("12345678", "XX32", 1000, 2550, "C1");
    }

    @Test
    public void createTruck(){
        // max weight smaller than net weight
        assertFalse(truckController.CreateTruck("WE-1234-FD", "RNO51", 3000, 2500, "A"));

        // max weight equal net weight
        assertFalse(truckController.CreateTruck("WE-1234-FD", "RNO51", 3000, 3000, "A"));
    }

    @Test
    public void checkIfAvailableByWeight(){
        // weight is smaller than max weight
        assertTrue(truckController.checkIfAvailableByWeight(1500, 3));
        assertTrue(truckController.checkIfAvailableByWeight(250, 2));
        assertTrue(truckController.checkIfAvailableByWeight(100, 1));
        assertTrue(truckController.checkIfAvailableByWeight(500, 0));

        // weight is bigger than max weight
        assertFalse(truckController.checkIfAvailableByWeight(2000, 0));
        assertFalse(truckController.checkIfAvailableByWeight(2000, 1));
        assertFalse(truckController.checkIfAvailableByWeight(2000, 2));
        assertFalse(truckController.checkIfAvailableByWeight(2800, 3));
    }
}
