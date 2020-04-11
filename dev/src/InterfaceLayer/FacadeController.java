package InterfaceLayer;

import BusinessLayer.*;

import java.util.Date;
import java.util.HashMap;

public class FacadeController {

    private static FacadeController instance = null;
    private static DriverController driverController = DriverController.getInstance();
    private static SiteController siteController = SiteController.getInstance();
    private static TransportController transportController = TransportController.getInstance();
    private static TruckController truckController = TruckController.getInstance();

    private FacadeController(){}

    public static FacadeController getInstance(){
        if (instance == null)
            instance = new FacadeController();
        return instance;
    }

    public String getAllTrucksDetails() {
        return truckController.getAllTrucksDetails();
    }

    public String getAllDriversDetails() {
        return driverController.getAllDriversDetails();
    }

    public String getAllSitesDetails() {
        return siteController.getAllSitesDetails();
    }

    public String getAllTransportsDetails() {
        return transportController.getAllTransportsDetails();
    }

    public String getAvailableTrucks(Date date, int totalWeight) {
        return truckController.getAvailbleTrucks(date, int totalWeight);
    }

    public String getAvailableDrivers(Date date, int truckID) {
        String truckLicense = truckController.getDriversLicense(truckID);
        return driverController.getAvailbleDrivers(date, truckLicense);
    }

    public void createTruck(String license_plate, String model, int netWeight, int maxWeight, String drivers_license) {
        truckController.CreateTruck(license_plate, model, netWeight, maxWeight, drivers_license);
    }

    public void createDriver(String name, String license) {
        driverController.CreateDriver(license, name);
    }

    public void createSite(String address, String phone_number, String contact, int shipping_area) {
        siteController.CreateSite(address, phone_number, contact, shipping_area);
    }

    public void deleteTruck(int truckToDelete) {
        truckController.DeleteTruck(truckToDelete);
    }

    public void deleteDriver(int driverToDelete) {
        driverController.DeleteDriver(driverToDelete);
    }

    public void deleteSite(int siteToDelete) {
        siteController.DeleteSite(siteToDelete);
    }

    public void deleteTransport(int transportToDelete) {
        transportController.DeleteTransport(transportToDelete);
    }

    //TODO:: figure out what to do ???
    public void createTransport(Date date, int truckID, int driverID, int sourceID, HashMap<Integer, Integer> DestFiles, int totalWeight) {
        String truckNumber = truckController.getTruckNumber(truckID);
        //transportController.CreateTransport(date, truckNumber);
    }

    public String getAvailableSites(int sourceID) {
        return siteController.getAvailableSites(sourceID);
    }
}
