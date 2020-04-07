package BusinessLayer;

import java.util.*;

public class TransportController {
    private static  TransportController instance = null;

    private Hashtable<Integer,Transport> transports;
    private int Id_Counter;

    private TransportController(){
        transports=new Hashtable<>();
        Id_Counter=0;
    }

    public static TransportController getInstance()
    {
        if(instance==null){
            instance=new TransportController();
        }
        return instance;
    }

    public void CreateTransport( Date date, int truckNumber, Driver driver, Site source,
                                 ArrayList<Site> destinations)
    {
        Transport t = new Transport(Id_Counter, date, truckNumber, driver, source, destinations, 0);
        Id_Counter++;
        transports.put(t.getID(),t);
    }

    public void DeleteTransport(Integer id)
    {
        transports.remove(id);
    }

    public String getTransportDetails(Integer id)
    {
        return transports.get(id).toString();
    }

    public String getAllTransportsDetails()
    {
        String details="";
        for (Integer i:transports.keySet()) {
            details = details+getTransportDetails(i)+"\n";
        }
        return details;
    }

    public void setTransportDate(Date date, int id) {
        transports.get(id).setDate(date);
    }

    public void setTransportTruckNumber(int truckNumber, int id) {
        transports.get(id).setTruckNumber(truckNumber);
    }

    public void setTransportDriverName(Driver driver, int id) { transports.get(id).setDriverName(driver);    }

    public void setTransportSource(Site source, int id) {transports.get(id).setSource(source); }

    public void setTransportWeight(int weight, int id){transports.get(id).setWeight(weight); }

    public void setTransportStatus(int status,int id){transports.get(id).setStatus(status); }

    public void addDestinationToTransport(int id, Site s){transports.get(id).addDestination(s);}

    public void removeDestinationFromTransport(int site_id, int t_id){transports.get(t_id).removeDestination(site_id);}




}
