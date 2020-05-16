package InterfaceLayer.Transport;

import BusinessLayer.Transport.*;
import InterfaceLayer.Workers.ScheduleController;

import java.util.Date;
import java.util.HashMap;

public class FacadeController {
//this class transfer functionality between the businessLayer and the presentationLayer

    private static FacadeController instance = null;
    private static SiteController siteController = SiteController.getInstance();
    private static TransportController transportController = TransportController.getInstance();
    private static TruckController truckController = TruckController.getInstance();
    private static ProductsController productsController = ProductsController.getInstance();
    private static ScheduleController scheduleController = ScheduleController.getInstance();

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

    public String getAvailableTrucks(int transportId, float totalWeight) {
        Date d = transportController.getTransportDate(transportId);
        Boolean shift = transportController.getTransportShift(transportId);
        return truckController.getAvailableTrucks(d,shift, totalWeight);
    }

    public String getTruckDetails(int truckID) {
        return truckController.getTruckDetails(truckID);
    }

    public boolean createTruck(String license_plate, String model, float netWeight, float maxWeight, String drivers_license) {
        return truckController.CreateTruck(license_plate, model, netWeight, maxWeight, drivers_license);
    }

    public boolean deleteTruck(int truckToDelete) {
        return truckController.DeleteTruck(truckToDelete);
    }

    public boolean checkIfTruckExistAndValid(int truckID, int transportId) {
        float totalWeight = getTotalWeight(transportId);
        Date d = getTransportDate(transportId);
        boolean shift = getTransportShift(transportId);
        return truckController.checkIfTruckExistAndValid(truckID, totalWeight, d ,shift);
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

    public boolean checkIfSiteExistAndValid(int siteID, int sourceID, HashMap<Integer,Integer>destFile){
        return siteController.checkIfAvailable(siteID, sourceID, destFile);
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

    public float getFileWeight(int fileID){
        return productsController.getFileWeight(fileID);
    }


    //transport controller functions
    public String getAllTransportsDetails() {
        return transportController.getAllTransportsDetails();
    }

    public int createTransport() {
        return transportController.createTransport();
    }

    public void SubmitTransportToDB(int transportToSubmit) {
         transportController.SubmitTransportToDB(transportToSubmit);
    }

    public boolean deleteTransport(int transportToDelete) {
        return transportController.DeleteTransport(transportToDelete);
    }

    public boolean DeleteTransportFronDB(int transportToDelete) {
        return transportController.DeleteTransportFromDB(transportToDelete);
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

    public void setTransportDriver(int transportID, String driverID) {
        String driverName = scheduleController.getDriverName(driverID);
        transportController.setTransportDriver(driverID, transportID, driverName);
    }

    public float getTotalWeight( int transport_ID) {
        return transportController.getTotalWeight(transport_ID);
    }

    public String getProductsByDest(int transportID) {
        return transportController.getProductByDest(transportID);
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

    public void addDatesToTruck(int transportID) {
        transportController.addDatesToTruck(transportID);
    }

    public void removeDatesFromDriverAndTruck( int transportID) {
        String DriverId = transportController.getTransportDriverID(transportID);
        Date d =transportController.getTransportDate(transportID);
        boolean shift = transportController.getTransportShift(transportID);
        scheduleController.removeDriverFromTransport(d, shift, DriverId);
        transportController.removeDatesFromTruck(transportID);
    }

    public boolean checkIfDestInFile(int transportID, int destToEdit) {
        return transportController.checkIfDestInFile(transportID, destToEdit);
    }

    public boolean getTransportShift(int transportID)
    {
        return transportController.getTransportShift(transportID);
    }

    public boolean checkIfTransportExist(int transportID){
        return transportController.checkIfTransportExist(transportID);
    }

    //added after mergings:
    //shift- true:morning, false:night
    public void changeDriverInTransport(String prevDriverId, String newDriverId, Date date, Boolean shift)
    {
        String newDriverName = scheduleController.getDriverName(newDriverId);
        transportController.changeDriverInTransport(prevDriverId, newDriverId, date, shift, newDriverName);
    }

    public Boolean isTransportExist(Date date, Boolean shift)
    {
        return transportController.isTransportExist(date, shift);
    }

    public boolean setTransportDateTime(int id, String date, String time) throws Exception {
        return transportController.setTransportDateTime(date, time, id);
    }

    public boolean checkIfStorageManInShift(int TransportId)
    {
        Date d = transportController.getTransportDate(TransportId);
        boolean shift = transportController.getTransportShift(TransportId);
        return scheduleController.StorageManInShift(d, shift);
    }

    public boolean checkIfDriversAndTrucksAvailable(int TransportId)
    {
        Date date = transportController.getTransportDate(TransportId);
        boolean shift = transportController.getTransportShift(TransportId);
        boolean trucksAvailable = truckController.checkIfTrucksAvailableByDate(date , shift);
        boolean driversAvailable = scheduleController.DriversAvailability(date , shift);
        return driversAvailable&&trucksAvailable;
    }

    public String chooseDriver(int transportId)
    {
        Date date = transportController.getTransportDate(transportId);
        boolean Shift = transportController.getTransportShift(transportId);
        int TruckID = transportController.getTransportTruck(transportId);
        String licence = truckController.getDriversLicense(TruckID);
        String DriverId = scheduleController.chooseDriverForTransport(date, Shift, licence);
        if(DriverId == null)
        {
            return "";
        }
        else {
            String DriverName = scheduleController.getDriverName(DriverId);
            transportController.setTransportDriver(DriverId,transportId,DriverName);
            return DriverName;
        }
    }
}
