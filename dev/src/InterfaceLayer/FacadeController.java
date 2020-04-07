package InterfaceLayer;

import BusinessLayer.*;

import java.util.Date;

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

    public String getAvailbleTrucks(Date date) {
        return truckController.getAvailbleTrucks(date);
    }

    public String getAvailbleDrivers(Date date, int truckID) {
        String truckLicense = truckController.getDriverslicense(truckID);
        return driverController.getAvailbleDrivers(date, truckLicense);
    }

    public void createTruck(String license_plate, String model, int weight, String drivers_license) {
        truckController.CreateTruck(license_plate, model, weight, drivers_license);
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
}
