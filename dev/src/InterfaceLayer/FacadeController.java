package InterfaceLayer;

import BusinessLayer.*;

import java.util.Date;
import java.util.HashMap;

public class FacadeController {
//this class transfer functionality between the businessLayer and the presentationLayer

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

    //truck controller functions
    public String getAllTrucksDetails() {
        return truckController.getAllTrucksDetails();
    }

    public String getAvailableTrucks(Date date, float totalWeight) {
        return truckController.getAvailableTrucks(date, totalWeight);
    }

    public boolean createTruck(String license_plate, String model, float netWeight, float maxWeight, String drivers_license) {
        return truckController.CreateTruck(license_plate, model, netWeight, maxWeight, drivers_license);
    }

    public boolean deleteTruck(int truckToDelete) {
        return truckController.DeleteTruck(truckToDelete);
    }

    public String getTruckDetails(int truckID) {
        return truckController.getTruckDetails(truckID);
    }

    public boolean checkIfTruckExistAndValid(int truckID, int transportId) {
        float totalWeight = getTotalWeight(transportId);
        Date d = getTransportDate(transportId);
        return truckController.checkIfTruckExistAndValid(truckID, totalWeight, d);
    }



    //driver controller functions
    public String getAllDriversDetails() {
        return driverController.getAllDriversDetails();
    }

    public String getAvailableDrivers(int transportId) {
        Date date = getTransportDate(transportId);
        int truckID = getTransportTruck(transportId);
        String truckLicense = truckController.getDriversLicense(truckID);
        return driverController.getAvailableDrivers(date, truckLicense);
    }

    public void createDriver(String name, String license) {
        driverController.CreateDriver(license, name);
    }

    public boolean deleteDriver(int driverToDelete) {
        return driverController.DeleteDriver(driverToDelete);
    }

    public boolean checkIfDriverExistAndValid(int driverID, int transport_id) {
        int truckID = getTransportTruck(transport_id);
        Date d =getTransportDate(transport_id);
        String truckLicense = truckController.getDriversLicense(truckID);
        return driverController.checkIfDriverExistAndValid(driverID, truckLicense, d);
    }



    //site controller functions
    public String getAllSitesDetails() {
        return siteController.getAllSitesDetails();
    }

    public String getAvailableSites(int sourceID, HashMap<Integer,Integer> destfile) {
        return siteController.getAvailableSites(sourceID, destfile);
    }

    public String getSiteDetails(int siteID) {
        return siteController.getSiteDetails(siteID);
    }

    public void createSite(String address, String phone_number, String contact, int shipping_area) {
        siteController.CreateSite(address, phone_number, contact, shipping_area);
    }

    public boolean deleteSite(int siteToDelete) {
        return siteController.DeleteSite(siteToDelete);
    }

    public boolean checkIfSiteExist(int siteID)
    {
        return siteController.checkIfSiteExist(siteID);
    }

    public boolean checkIfSiteExistAndValid(int siteID, int sourceID, HashMap<Integer,Integer>destFile)
    {
        return siteController.checkIfAvailable(siteID, sourceID, destFile);
    }



    //transport controller functions
    public String getAllTransportsDetails() {
        return transportController.getAllTransportsDetails();
    }

    public boolean deleteTransport(int transportToDelete) {
        return transportController.DeleteTransport(transportToDelete);
    }

    public int createTransport() {
        return transportController.createTransport();
    }

    public float getTotalWeight( int transport_ID) {
        return transportController.getTotalWeight(transport_ID);
    }

    public String getProductsByDest(int transportID) {
        return transportController.getProductByDest(transportID);
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

    public void setTransportWeight(int transportID) {
        transportController.setTransportWeight(transportID);
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

    public int getDestFileID(int transportID, int destToEdit) {
        return transportController.getFileID(transportID, destToEdit);
    }

    public boolean removeDestFromTransport(int transportID, int destToRemove) {
        return transportController.removeDestinationFromTransport(destToRemove, transportID);
    }

    public void addTransportLog(String message, int transportID) {
        transportController.addToLog(message, transportID);
    }

    public void addDatesToDriverAndTruck(int transportID) {
        transportController.addDatesToDriverAndTruck(transportID);
    }

    public void removeDatesToDriverAndTruck( int transportID) {
        transportController.removeDatesToDriverAndTruck(transportID);
    }

    public boolean checkIfDestInFile(int transportID, int destToEdit) {
        return transportController.checkIfDestInFile(transportID, destToEdit);
    }

    public boolean checkIfTransportExist(int transportID) {
        return transportController.checkIfTransportExist(transportID);
    }



    //product controller functions
    public int createProductsFile() {
        return productsController.CreateFile();
    }

    public void createProduct(String productName, float productWeight, int fileID, int quantity) {
        productsController.CreateProduct(productName, productWeight, fileID, quantity);
    }

    public boolean removeProducts(String[] productsToRemove, Integer fileToEdit) {
        return productsController.removeProducts(productsToRemove, fileToEdit);
    }

    public String getProductsDetails(String[] productsToRemove) {
        return productsController.getProductsDetails(productsToRemove);
    }

    public float getFileWeight(int fileID)
    {
        return productsController.getFileWeight(fileID);
    }

}
