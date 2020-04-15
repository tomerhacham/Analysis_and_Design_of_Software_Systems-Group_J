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

    public String getAvailableTrucks(Date date, float totalWeight) {
        return truckController.getAvailableTrucks(totalWeight);
    }

    public String getAvailableDrivers(Date date, int truckID) {
        String truckLicense = truckController.getDriversLicense(truckID);
        return driverController.getAvailableDrivers(truckLicense);
    }

    public String getAvailableSites(int sourceID, HashMap<Integer,Integer> destfile) {
        return siteController.getAvailableSites(sourceID, destfile);
    }

    public String getSiteDetails(int siteID) {
        return siteController.getSiteDetails(siteID);
    }

    public void createTruck(String license_plate, String model, float netWeight, float maxWeight, String drivers_license) {
        truckController.CreateTruck(license_plate, model, netWeight, maxWeight, drivers_license);
    }

    public void createDriver(String name, String license) {
        driverController.CreateDriver(license, name);
    }

    public void createSite(String address, String phone_number, String contact, int shipping_area) {
        siteController.CreateSite(address, phone_number, contact, shipping_area);
    }

    public boolean deleteTruck(int truckToDelete) {
        return truckController.DeleteTruck(truckToDelete);
    }

    public boolean deleteDriver(int driverToDelete) {
        return driverController.DeleteDriver(driverToDelete);
    }

    public boolean deleteSite(int siteToDelete) {
        return siteController.DeleteSite(siteToDelete);
    }

    public boolean deleteTransport(int transportToDelete) {
        return transportController.DeleteTransport(transportToDelete);
    }

    public int createTransport() {
        return transportController.createTransport();
    }

    public int createProductsFile() {
        return productsController.CreateFile();
    }

    public void createProduct(String productName, float productWeight, int fileID, int quantity) {
        productsController.CreateProduct(productName, productWeight, fileID, quantity);
    }

    public float getTotalWeight(HashMap<Integer, Integer> destFiles) {
        return productsController.getTotalWeight(destFiles);
    }

    public String getProductsByDest(int transportID) {
        HashMap<Integer, Integer> destFiles = getTransportDestFiles(transportID);
        return productsController.getProductByDest(destFiles);
    }

    public void removeProducts(String[] productsToRemove, Integer fileToEdit) {
        productsController.removeProducts(productsToRemove, fileToEdit);
    }

    public boolean setTransportDate(int transportID, String date) throws Exception {
        return transportController.setTransportDate(date, transportID);
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

    public void setTransportWeight(int transportID, float totalWeight) {
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

    public void addInlayDate(Date transportDate, int transportID) {
        transportController.addInlayDate(transportDate, transportID);
    }

    public void removeInlayDate(Date transportDate, int transportID) {
        transportController.removeInlayDate(transportDate, transportID);
    }

    public boolean checkIfSiteExist(int siteID)
    {
        return siteController.checkIfSiteExist(siteID);
    }

    public boolean checkIfTruckExist(int truckID) {
        return truckController.checkIfTruckExist(truckID);
    }

    public boolean checkIfDriverExist(int driverID) {
        return driverController.checkIfDriverExist(driverID);
    }

    public boolean validateProducts(String[] products, int fileID) {
        return productsController.validateProducts(products, fileID);
    }

    public boolean checkIfDestInFile(int transportID, int destToEdit) {
        return transportController.checkIfDestInFile(transportID, destToEdit);
    }

    public boolean checkIfTransportExist(int transportID) {
        return transportController.checkIfTransportExist(transportID);
    }
}
