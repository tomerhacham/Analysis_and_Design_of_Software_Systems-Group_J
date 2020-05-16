package BusinessLayer.Transport;


import DataAccessLayer.Mapper;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class TransportController {
    private static TransportController instance = null;
    private static SiteController siteController = SiteController.getInstance();
    private static ProductsController productsController = ProductsController.getInstance();
    private static TruckController truckController = TruckController.getInstance();
    private static Mapper mapper = Mapper.getInstance();
    private Hashtable<Integer, Transport> transports;
    private int Id_Counter;

    private TransportController() {
        transports = new Hashtable<>();
        Id_Counter = (int)mapper.MaxIDTransport() + 1;
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

    //if a transport exist in the system, delete it - not remove from DB
    public boolean DeleteTransport(Integer id) {
        if (transports.containsKey(id)) {
            transports.remove(id);
            return true;
        }
        return false;
    }

    public boolean DeleteTransportFromDB(Integer id)
    {
        DeleteTransport(id);
        return mapper.deleteTransport(id);
    }

    public void SubmitTransportToDB(int transportToSubmit)
    {
        if(transports.containsKey(transportToSubmit))
        {
            Transport t =transports.get(transportToSubmit);
            HashMap<Site,ProductFile> destFile = t.getDestFiles();
            for (ProductFile pf:destFile.values() ) {
                mapper.addProductFile(pf);
            }
            mapper.addTransport(t);
        }

    }

/*  redundant!! -  //if a transport exist in the system return its details, else return an empty string
    public String getTransportDetails(Integer id) {
        Transport transport = mapper.getTransport(id);
        if(transports.containsKey(id)) {
            return transports.get(id).toString();
        }
        else if (transport != null){
            transport.toString();
        }
        return "";
    }
*/
    //return the details of all transports in the system
    public String getAllTransportsDetails() {
        List<Transport> all_transports = mapper.getAllTransports();
        String details = "";
        int count = 1;
        for (Transport t : all_transports) {
            details = details + count + ". " + t.toString() + "\n";
            count++;
        }
        return details;
    }

    //set a transport date - check if its valid and throw an exception according to the problem
    public boolean setTransportDateTime(String date, String time, int id) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
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

        boolean shift = false;
        LocalTime transportTime = LocalTime.parse(time);
        LocalTime noonShift = LocalTime.of(14, 00);
        if (transportTime.isBefore(noonShift)){
            shift = true;
        }
        if(transports.containsKey(id)) {
            transports.get(id).setDateTime(transportDate, shift);
            return true;
        }
        return false;
    }

    //if a transport exist in the system return its date, else null
    public Date getTransportDate(int id) {
        Transport t = mapper.getTransport(id);
        if(transports.containsKey(id)) {
            return transports.get(id).getDate();
        }
        else if (t != null){
            return t.getDate();
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
    public void setTransportDriver(String driverID, int TransportId, String driver_name) {
        if(transports.containsKey(TransportId))
        {
            transports.get(TransportId).setDriver(driverID,driver_name);
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
    public void addDatesToTruck(int transportID) {
        if(transports.containsKey(transportID))
        {
            Date d =getTransportDate(transportID);
            boolean shift=getTransportShift(transportID);
            truckController.addDate(d,shift, transports.get(transportID).getTruck().getId());
        }
    }

    //remove the date of the given transport to its truck and driver
    public void removeDatesFromTruck(int transportID) {
        Transport t = mapper.getTransport(transportID);
        if(t != null)
        {
            Date d = getTransportDate(transportID);
            boolean shift = getTransportShift(transportID);
            truckController.removeDate(d,shift, t.getTruck().getId());
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

    public boolean getTransportShift(int transportID)
    {
        Transport t = mapper.getTransport(transportID);
        if (t != null){
            return t.getShift();
        }
        return transports.get(transportID).getShift();
    }

    public void changeDriverInTransport(String prevDriverId, String newDriverId, Date date, Boolean shift, String newDriverName)
    {
        Transport t = mapper.getTransportToUpdate(prevDriverId, date, shift);
        mapper.updateTransportDriver(t.getID(), newDriverId, newDriverName);
        /*for (Transport t:transports.values()) {
            if(t.getDriverId()==prevDriverId&&t.getDate()==date&&t.getShift()==shift)
            {
                t.setDriver(newDriverId,newDriverName);
                return;
            }
        }*/
    }

    public Boolean isTransportExist(Date d, Boolean partOfDay)
    {
        return mapper.getTransportByShift(d, partOfDay) != null;
        /*
        for (Transport t:transports.values()) {
            if(t.getDate()==d&&t.getShift()==partOfDay)
            {
               return true;
            }
        }
        return false;*/
    }

    public String getTransportDriverID(int TransportID)
    {
        Transport t = mapper.getTransport(TransportID);
        if(transports.containsKey(TransportID))
            return transports.get(TransportID).getDriverId();
        else if (t != null)
            return t.getDriverId();
        return "";
    }
}