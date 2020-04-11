package BusinessLayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Transport {

    private int ID;
    private Date Date;
    private int TruckNumber;
    private Driver Driver;
    private Site Source;
    //private HashMap<Site, Integer> DestFiles;
    private ArrayList<Site> destinations;
    private int TotalWeight;
    private ArrayList<String> log;

    public Transport(int id, Date date, int truckNumber, Driver driver, Site source,
                     ArrayList<Site> destinations, int weight){
        ID = id;
        Date = date;
        TruckNumber = truckNumber;
        Driver = driver;
        Source = source;
       // DestFiles = destFiles;
        this.destinations=destinations;
        TotalWeight = weight;
        log =new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }

    public int getTruckNumber() {
        return TruckNumber;
    }

    public void setTruckNumber(int truckNumber) {
        TruckNumber = truckNumber;
    }

    public Driver getDriverName() {
        return Driver;
    }

    public void setDriverName(Driver driver) {
        Driver = driver;
    }

    public Site getSource() {
        return Source;
    }

    public void setSource(Site source) {
        Source = source;
    }
//
//    public HashMap<Site, Integer> getDestFiles() {
//        return DestFiles;
//    }
//
//    public void setDestFiles(HashMap<Site, Integer> destFiles) {
//        DestFiles = destFiles;
//    }

    public int getWeight() {
        return TotalWeight;
    }

    public void setWeight(int weight) {
        TotalWeight = weight;
    }

/*    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }*/


    public void addDestination(Site s)
    {
        destinations.add(s);
    }

    public void removeDestination(int siteId)
    {
        for (int i=0; i<destinations.size();i++)
        {
            if (destinations.get(i).getId()==siteId)
            {
                destinations.remove(i);
            }
        }
    }

    @Override
    public String toString() {
        String s = "id: " + ID + " Date: " + Date.toString() + " TruckNumber: " + TruckNumber + " Driver: " + Driver.getName()
                +" Source: "+Source.toString() +"\n";
        if(destinations.size()>0)
        {
            s = s + " destinations: \n";
            for (int i = 0; i < destinations.size(); i++) {
                s = s + i + ". " + destinations.get(i).toString();
            }
        }
        else {
            s = s + " destinations: none\n";
        }
        s = s + "TotalWeight: " + TotalWeight +"\n";
        if(log.size()>0) {
            s = s + "log messages: " + getLogMessages();
        }
        else {
            s = s + "log messages: none";
        }
        return s;
    }

    public void addToLog (String s)
    {
        log.add(s);
    }

    public String getLogMessages()
    {
        String s = "";
        for (int i=0; i<log.size();i++)
        {
            s=s+i+". "+log.get(i)+"\n";
        }
        return s;
    }
}
