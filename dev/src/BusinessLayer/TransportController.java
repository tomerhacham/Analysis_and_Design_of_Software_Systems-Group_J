package BusinessLayer;

import java.text.SimpleDateFormat;
import java.util.*;

public class TransportController {

    private static TransportController instance = null;
    private static SiteController siteController = SiteController.getInstance();
    private static DriverController driverController = DriverController.getInstance();
    private static ProductsController productsController = ProductsController.getInstance();
    private static TruckController truckController = TruckController.getInstance();

    private Hashtable<Integer, Transport> transports;
    private int Id_Counter;

    private TransportController() {
        transports = new Hashtable<>();
        Id_Counter = 0;
    }

    public static TransportController getInstance() {
        if (instance == null) {
            instance = new TransportController();
        }
        return instance;
    }

    //create a new and empty transport
    public int createTransport() {
        Transport t = new Transport(Id_Counter);
        Id_Counter++;
        transports.put(t.getID(), t);
        return t.getID();
    }

    //if a transport exist in the system, delete it
    public boolean DeleteTransport(Integer id) {
        if (transports.containsKey(id)) {
            transports.remove(id);
            return true;
        }
        return false;
    }

    //if a transport exist in the system return its details, else return an empty string
    public String getTransportDetails(Integer id) {
        if(transports.containsKey(id)) {
            return transports.get(id).toString();
        }
        return "";
    }

    //return the details of all transports in the system
    public String getAllTransportsDetails() {
        String details = "";
        int count = 1;
        for (Integer i : transports.keySet()) {
            details = details + count + ". " + getTransportDetails(i) + "\n";
            count++;
        }
        return details;
    }

    //set a transport date - check if its valid and throw an exception according to the problem
    public boolean setTransportDate(String date, int id) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date transportDate;
        try {
            transportDate = formatter.parse(date);
        } catch (Exception e) {
            throw new Exception("Format is incorrect. Try again.");
        }
        Date today = new Date();
        formatter.format(today);
        if (today.after(transportDate))
            throw new Exception("Date already passed. Try again.");
        boolean trucksAvailable = truckController.checkIfTrucksAvailableByDate(transportDate);
        boolean driversAvailable = driverController.checkIfDriversAvailableByDate(transportDate);
        if (trucksAvailable && driversAvailable) {
            transports.get(id).setDate(transportDate);
            return true;
        }
        else
            return false;
    }

    //if a transport exist in the system return its date, else null
    public Date getTransportDate(int id) {
        if(transports.containsKey(id)) {
            return transports.get(id).getDate();
        }
        return null;
    }

    //if a transport exist in the system sets its truck
    public void setTransportTruck(int truck_id, int id) {
        if (transports.containsKey(id)) {
            Truck t = truckController.getById(truck_id);
            if(t!=null)
            {
                transports.get(id).setTruck(t);
            }
        }
    }

    //if a transport exist in the system return its truck ID, -1 if truck is null
    public int getTransportTruck(int id) {
        if(transports.containsKey(id))
        {
            Truck t = transports.get(id).getTruck();
            if(t!=null)
                return t.getId();
        }
        return -1;
    }


    //if a transport exist in the system sets its driver
    public void setTransportDriver(int driver, int id) {
        if(transports.containsKey(id))
        {
            Driver d =driverController.getById(driver);
            if(d!=null)
            {
                transports.get(id).setDriver(d);
            }
        }
    }

    //if a transport exist in the system sets its source
    public void setTransportSource(int source, int id) {
        if(transports.containsKey(id))
        {
            Site s = siteController.getById(source);
            if(s!=null)
                transports.get(id).setSource(s);
        }
    }

    //if a transport exist in the system sets its total weight
    public void setTransportWeight(int id) {
        if(transports.containsKey(id)) {
            transports.get(id).setWeight();
        }
    }
    //if a transport exist in the system sets its destination file
    public void setTransportDestFiles(HashMap<Integer, Integer> destFiles, int id) {
        if(transports.containsKey(id)) {
            HashMap<Site, ProductFile> D_F = new HashMap<>();
            for (int i : destFiles.keySet()) {
                Site s = siteController.getById(i);
                ProductFile f = productsController.getFileByID(destFiles.get(i));
                if (s != null && f != null) {
                    D_F.put(s, f);
                }
            }
            transports.get(id).setDestFiles(D_F);
        }
    }

    //if a transport exist in the system, get the File for a specific destination
    public int getFileID(int transport_id, int dest_id) {
        if(transports.containsKey(transport_id))
        {
            Site s= siteController.getById(dest_id);
            if(s!=null)
            {
                ProductFile f =transports.get(transport_id).getFileByDest(s);
                if(f!=null)
                {
                    return f.getFileID();
                }
            }
        }
        return -1;
    }

    //if a transport exist in the system, remove a destination from its destFile
    public boolean removeDestinationFromTransport(int site_id, int t_id) {
        if (transports.containsKey(t_id)) {
            Site s =siteController.getById(site_id);
            if(s!=null) {
                return transports.get(t_id).removeFromDestFiles(s);
            }
        }
        return false;
    }

    //add a string to a transport log
    public void addToLog(String s, Integer id) {
       if(transports.containsKey(id))
            transports.get(id).addToLog(s);
    }


    //add the date of the given transport to its truck and driver
    public void addDatesToDriverAndTruck(int transportID) {
        if(transports.containsKey(transportID))
        {
            Date d =getTransportDate(transportID);
            driverController.addDate(d, transports.get(transportID).getDriver().getId());
            truckController.addDate(d, transports.get(transportID).getTruck().getId());
        }

    }

    //remove the date of the given transport to its truck and driver
    public void removeDatesToDriverAndTruck(int transportID) {
        if(transports.containsKey(transportID))
        {
            Date d =getTransportDate(transportID);
            driverController.removeDate(d, transports.get(transportID).getDriver().getId());
            truckController.removeDate(d, transports.get(transportID).getTruck().getId());
        }
    }

    //check if a specific destination is in the destFile of a transport
    public boolean checkIfDestInFile(int transportID, int destToEdit) {
        if(transports.containsKey(transportID))
        {
            Site s = siteController.getById(destToEdit);
            if(s!= null)
            {
                return transports.get(transportID).checkIfDestInFile(s);
            }
        }
        return false;
    }

    //check if a transport exist in the system
    public boolean checkIfTransportExist(int transportID) {
        return transports.containsKey(transportID);
    }

    //get the total weight in the transport, if it exist in the system
    public float getTotalWeight(int transport_id) {
        if(transports.containsKey(transport_id)) {
            return transports.get(transport_id).getTotalWeight();
        }
        return 0;
    }

    //get details of the product for each destination in a given transport
    public String getProductByDest(int transportId)
    {
        String s = "";
        if(transports.containsKey(transportId)){
            HashMap<Site,ProductFile> destFiles = transports.get(transportId).getDestFiles();
            for (Site site:destFiles.keySet()) {
                s = s + "\tdestination id: "+site.getId()+" "+ productsController.geFileDetails(destFiles.get(site).getFileID())+"\n";
            }
        }
        return s;
    }

    public Transport getByID(int transportID){
        return transports.get(transportID);
    }

}