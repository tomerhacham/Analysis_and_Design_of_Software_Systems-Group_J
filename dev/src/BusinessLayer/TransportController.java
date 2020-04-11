package BusinessLayer;

import java.util.*;

public class TransportController {
    private static  TransportController instance = null;
    private static SiteController siteController=SiteController.getInstance();
    private static DriverController driverController=DriverController.getInstance();
    private static ProductsController productsController=ProductsController.getInstance();


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

    public void CreateTransport(Date date, int truckNumber, int driver, int source, HashMap<Integer, Integer> destFiles, int weight)
    {
        HashMap<Site,ProductPerSite> D_F=new HashMap<>();
        for (int i:destFiles.keySet()) {
            D_F.put(siteController.getById(i),productsController.getFileByID(destFiles.get(i)));
        }
        Transport t = new Transport(Id_Counter, date, truckNumber, driverController.getById(driver),siteController.getById(source), D_F, weight);
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


    public void addDestinationToTransport(int id, Site s, ProductPerSite productPerSite){transports.get(id).addDestFiles(s,productPerSite);}

    public void removeDestinationFromTransport(int site_id, int t_id){transports.get(t_id).removeDestFiles(siteController.getById(site_id));}

    public void addToLog(String s, Integer id)
    {
        transports.get(id).addToLog(s);
    }

    public String getLogMessages(int id){return transports.get(id).getLogMessages();}
}
