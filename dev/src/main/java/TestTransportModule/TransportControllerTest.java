package TestTransportModule;
/*
import BusinessLayer.Workers.Driver;
import BusinessLayer.Transport.Transport;
import BusinessLayer.Transport.TransportController;
import BusinessLayer.Transport.Truck;
import InterfaceLayer.Transport.FacadeController;
import org.junit.jupiter.api.*;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
*/

/*public class TransportControllerTest {
    TransportController transportController;
    FacadeController facadeController;
    int transportID;
    int transportID2;

    public TransportControllerTest(){
        transportController = TransportController.getInstance();
        facadeController = FacadeController.getInstance();
        transportID = transportController.createTransport();
        createTransport();
    }

    private void createTransport() {
        transportID2 = facadeController.createTransport();
        try {
            facadeController.setTransportDate(transportID2, "12-06-2020");
        } catch (Exception e) {}
        facadeController.createTruck("12345678", "XX32", 1000, 2550, "C1");
        facadeController.setTransportTruck(transportID2, 0);
        facadeController.createDriver("Yossi", "C1");
        facadeController.setTransportDriver(transportID2, 0);
        facadeController.createSite("Rabi akiva Beer Shave", "0541234567", "Einav", 2);
        facadeController.setTransportSource(transportID2, 0);
        HashMap<Integer, Integer> destFiles = new HashMap<>();
        facadeController.createSite("Oren Ashkelon", "0521234567", "Shira", 2);
        facadeController.createSite("Avraham avinu Ofakim", "0501234567", "Mai", 2);
        int fileID1 = facadeController.createProductsFile();
        facadeController.createProduct("milk", 2, fileID1, 100);
        facadeController.createProduct("sugar", 1, fileID1, 300);
        int fileID2 = facadeController.createProductsFile();
        facadeController.createProduct("salt", 1, fileID2, 200);
        facadeController.createProduct("chocolate", 12, fileID2, 150);
        destFiles.put(1, fileID1);
        destFiles.put(2, fileID2);
        facadeController.setTransportWeight(transportID2);
    }

    @Test
    public void setTransportDate(){
        // wrong format
        Exception e1 = assertThrows(Exception.class, () -> transportController.setTransportDate("12/5/2020", transportID));
        String expectedMessage = "Format is incorrect. Try again.";
        String actualMessage = e1.getMessage();

        assertEquals(expectedMessage, actualMessage);

        // date that already passed
        Exception e2 = assertThrows(Exception.class, () -> transportController.setTransportDate("12-5-2005", transportID));
        expectedMessage = "Date already passed. Try again.";
        actualMessage = e2.getMessage();

        assertEquals(expectedMessage, actualMessage);

        // compatible date with available trucks and drivers
        try {
            assertTrue(transportController.setTransportDate("12-5-2020", transportID));
        }catch (Exception e) {fail("exception thrown");}
    }

    @Test
    public void addDatesToDriverAndTruck(){
        // function adds transport date to driver and truck so they won't be available for other transports
        Date transportDate = transportController.getTransportDate(transportID2);
        transportController.addDatesToDriverAndTruck(transportID2);
        Transport t = transportController.getByID(transportID2);
        Truck truck = t.getTruck();
        Driver driver = t.getDriver();
        assertFalse(truck.checkIfAvailableByDate(transportDate));
        assertFalse(driver.checkIfAvailableByDate(transportDate));
    }

    @Test
    public void removeDatesToDriverAndTruck(){
        // function removes transport date from driver and truck so they will be available for other transports
        Date transportDate = transportController.getTransportDate(transportID2);
        transportController.removeDatesToDriverAndTruck(transportID2);
        Transport t = transportController.getByID(transportID2);
        Truck truck = t.getTruck();
        Driver driver = t.getDriver();
        assertTrue(truck.checkIfAvailableByDate(transportDate));
        assertTrue(driver.checkIfAvailableByDate(transportDate));
    }
}*/
