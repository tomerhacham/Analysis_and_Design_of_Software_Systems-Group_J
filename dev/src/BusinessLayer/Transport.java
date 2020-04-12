package BusinessLayer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Transport {

    private int ID;
    private Date Date;
    private Truck Truck;
    private Driver Driver;
    private Site Source;
    private HashMap<Site, ProductFile> DestFiles;
    private int TotalWeight;
    private ArrayList<String> log;

    public Transport(int id){
        ID = id;
        DestFiles = new HashMap<>();
        log =new ArrayList<>();
    }

    public Transport(int id, Date date, Truck truck, Driver driver, Site source,
                     HashMap<Site, ProductFile> destFiles, int weight){
        ID = id;
        Date = date;
        Truck = truck;
        Driver = driver;
        Source = source;
        DestFiles=destFiles;
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

    public Truck getTruck() {
        return Truck;
    }

    public void setTruck(Truck truck) {
        Truck = truck;
    }

    public Driver getDriver() {
        return Driver;
    }

    public void setDriver(Driver driver) {
        Driver = driver;
    }

    public Site getSource() {
        return Source;
    }

    public void setSource(Site source) {
        Source = source;
    }

    public int getWeight() {
        return TotalWeight;
    }

    public void setWeight(int weight) {
        TotalWeight = weight;
    }

    public void setDestFiles(HashMap<Site, ProductFile> destFiles) {
        DestFiles = destFiles;
    }

    public void addDestFiles(Site s, ProductFile productPerSite)
    {
        DestFiles.put(s, productPerSite);
    }

    public void removeDestFiles(Site site)
    {
       DestFiles.remove(site);

    }
    public void editDestFiles(Site s, ProductFile newProductPerSite)
    {
        DestFiles.remove(s);
        DestFiles.put(s, newProductPerSite);
    }

    public HashMap<Site, ProductFile> getDestFiles() {
        return DestFiles;
    }

    public ProductFile getFileByDest(Site dest)
    {
        return DestFiles.get(dest);
    }

    @Override
    public String toString() {
        String s = "id: " + ID + " Date: " + Date.toString() + " TruckNumber: " + Truck.getLicense_plate() + " Driver: " + Driver.getName()
                +" Source: "+Source.toString() +"\n";
        if(DestFiles.size()>0)
        {
            s = s + " destinations and products: \n";
            for (Site site:DestFiles.keySet()) {
                s=s+"site: "+site.toString()+"\n";
                s=s+"products: "+DestFiles.get(site).toString();
            }
        }
        else {
            s = s + " destinations and products: none\n";
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
