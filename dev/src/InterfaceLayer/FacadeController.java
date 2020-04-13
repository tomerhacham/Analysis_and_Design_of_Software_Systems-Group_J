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
    private static ProductsController productsController = ProductsController.getInstance();

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
        return truckController.getAvailableTrucks(date, totalWeight);
    }

    public String getAvailableDrivers(Date date, int truckID) {
        String truckLicense = truckController.getDriversLicense(truckID);
        return driverController.getAvailableDrivers(date, truckLicense);
    }

    public String getAvailableSites(int sourceID) {
        return siteController.getAvailableSites(sourceID);
    }

    public String getSiteDetails(int siteID) {
        return siteController.getSiteDetails(siteID);
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

    public int createTransport() {
        return transportController.createTransport();
    }

    public int createProductsFile() {
        return productsController.CreateFile();
    }

    public void createProduct(String productName, int productWeight, int fileID, int quantity) {
        productsController.CreateProduct(productName, productWeight, fileID, quantity);
    }

    public int getTotalWeight(HashMap<Integer, Integer> destFiles) {
        return productsController.getTotalWeight(destFiles);
    }

    public String getProductsByDest(int transportID) {
        HashMap<Integer, Integer> destFiles = getTransportDestFiles(transportID);
        return productsController.getProductByDest(destFiles);
    }

    public void removeProducts(String[] productsToRemove, Integer fileToEdit) {
        productsController.removeProducts(productsToRemove, fileToEdit);
    }

    public void setTransportDate(int transportID, Date date) {
        transportController.setTransportDate(date, transportID);
    }

    public void setTransportSource(int transportID, int sourceID) {
        transportController.setTransportSource(sourceID, transportID);
    }

    public void setTransportDestFiles(int transportID, HashMap<Integer, Integer> destFiles) {
        transportController.setTransportDestFiles(destFiles, transportID);
    }

    public void setTransportTruck(int transportID, int truckID) {
        transportController.setTransportTruck(truckID, transportID);
    }

    public void setTransportWeight(int transportID, int totalWeight) {
        transportController.setTransportWeight(totalWeight, transportID);
    }

    public void setTransportDriver(int transportID, int driverID) {
        transportController.setTransportDriver(driverID, transportID);
    }

    public Date getTransportDate(int transportID) {
        return transportController.getTransportDate(transportID);
    }

    public int getTransportTruck(int transportID) {
        return transportController.getTransportTruck(transportID);
    }

    public HashMap<Integer, Integer> getTransportDestFiles(int transportID) {
        return transportController.getTransportDestFiles(transportID);
    }

    public int getDestFileID(int transportID, int destToEdit) {
        return transportController.getFileID(transportID, destToEdit);
    }

    public void removeDestFromTransport(int transportID, int destToRemove) {
        transportController.removeDestinationFromTransport(destToRemove, transportID);
    }

    public void addTransportLog(String message, int transportID) {
        transportController.addToLog(message, transportID);
    }

    public String getProductsDetails(String[] productsToRemove) {
        return productsController.getProductsDetails(productsToRemove);
    }

    public String getTruckDetails(int truckID) {
        return truckController.getTruckDetails(truckID);
    }
}
