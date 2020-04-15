package BusinessLayer;

import java.text.SimpleDateFormat;
import java.util.*;

public class TransportController {

    private static  TransportController instance = null;
    private static SiteController siteController=SiteController.getInstance();
    private static DriverController driverController=DriverController.getInstance();
    private static ProductsController productsController=ProductsController.getInstance();
    private static TruckController truckController=TruckController.getInstance();


    private Hashtable<Integer,Transport> transports;
    private int Id_Counter;

    private TransportController() {
        transports=new Hashtable<>();
        Id_Counter=0;
    }

    public static TransportController getInstance() {
        if(instance==null){
            instance=new TransportController();
        }
        return instance;
    }

    public int createTransport() {
        Transport t = new Transport(Id_Counter);
        Id_Counter++;
        transports.put(t.getID(),t);
        return t.getID();
    }

    public boolean DeleteTransport(Integer id) {
        if(transports.containsKey(id)) {
            transports.remove(id);
            return true;
        }
        return false;
    }

    public String getTransportDetails(Integer id)
    {
        return transports.get(id).toString();
    }

    public String getAllTransportsDetails() {
        String details="";
        int count=1;
        for (Integer i:transports.keySet()) {
            details = details+count+". "+getTransportDetails(i)+"\n";
            count++;
        }
        return details;
    }

    public boolean setTransportDate(String date, int id) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date transportDate;
        try {
            transportDate = formatter.parse(date);
        }
        catch (Exception e){
            throw new Exception("Format is incorrect. Try again.");
        }
        Date today = new Date();
        formatter.format(today);
        if (today.after(transportDate))
            throw new Exception("Date already passed. Try again.");
        boolean trucksAvailable = truckController.checkIfTrucksAvailableByDate(transportDate);
        boolean driversAvailable = driverController.checkIfDriversAvailableByDate(transportDate);
        if (trucksAvailable && driversAvailable){
            transports.get(id).setDate(transportDate);
            return true;
        }
        else
            return false;
    }

    public Date getTransportDate(int id){return transports.get(id).getDate();}

    public void setTransportTruck(int truck_id, int id) {
        transports.get(id).setTruck(truckController.getById(truck_id));
    }

    public int getTransportTruck(int id){return transports.get(id).getTruck().getId();}

    public void setTransportDriver(int driver, int id) { transports.get(id).setDriver(driverController.getById(driver));    }

    public void setTransportSource(int source, int t_id) {

        transports.get(t_id).setSource(siteController.getById(source));
    }

    public void setTransportWeight(float weight, int id){transports.get(id).setWeight(weight); }

    public void setTransportDestFiles(HashMap<Integer,Integer> destFiles, int id)
    {
        HashMap<Site, ProductFile> D_F=new HashMap<>();
        for (int i:destFiles.keySet()) {
            D_F.put(siteController.getById(i),productsController.getFileByID(destFiles.get(i)));
        }
        transports.get(id).setDestFiles(D_F);
    }

    public HashMap<Integer,Integer> getTransportDestFiles(int id)
    {
        HashMap<Integer,Integer> convertedDestFiles=new HashMap<>();
        HashMap<Site, ProductFile> DestFile = transports.get(id).getDestFiles();

        for (Site s :DestFile.keySet()) {
            convertedDestFiles.put(s.getId(),DestFile.get(s).getFileID());
        }
        return convertedDestFiles;
    }

    public int getFileID(int transport_id, int dest_id)
    {
        return transports.get(transport_id).getFileByDest(siteController.getById(dest_id)).getFileID();
    }

    public void removeDestinationFromTransport(int site_id, int t_id){transports.get(t_id).removeDestFiles(siteController.getById(site_id));}

    public void addToLog(String s, Integer id)
    {
        transports.get(id).addToLog(s);
    }

    public void addInlayDate(Date transportDate, int transportID) {
        driverController.addDate(transportDate, transports.get(transportID).getDriver().getId());
        truckController.addDate(transportDate, transports.get(transportID).getTruck().getId());
    }

    public void removeInlayDate(Date transportDate, int transportID) {
        driverController.removeDate(transportDate, transports.get(transportID).getDriver().getId());
        truckController.removeDate(transportDate, transports.get(transportID).getTruck().getId());
    }

    public boolean checkIfDestInFile(int transportID, int destToEdit) {
        return transports.get(transportID).checkIfDestInFile(siteController.getById(destToEdit));
    }

    public boolean checkIfTransportExist(int transportID) {
        return transports.containsKey(transportID);
    }
}
