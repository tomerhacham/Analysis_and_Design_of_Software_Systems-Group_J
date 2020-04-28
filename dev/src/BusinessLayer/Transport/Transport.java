package BusinessLayer.Transport;
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
    private float TotalWeight;
    private ArrayList<String> log;

    public Transport(int id){
        ID = id;
        DestFiles = new HashMap<>();
        log =new ArrayList<>();
    }


    public int getID() { return ID;}

    public Date getDate() { return Date;}

    public void setDate(Date date) { Date = date; }

    public Truck getTruck() {return Truck; }

    public void setTruck(Truck truck) {Truck = truck;}

    public Driver getDriver() { return Driver; }

    public void setDriver(Driver driver) { Driver = driver; }

    public void setSource(Site source) { Source = source; }

    //set the weight according to the destination file
    public void setWeight() {
        TotalWeight=0;
        for (ProductFile f:DestFiles.values()) {
            TotalWeight=+f.getTotalWeight();
        }
    }


    //every time the destFile change, set the weight as well
    public void setDestFiles(HashMap<Site, ProductFile> destFiles) {
        DestFiles = destFiles;
        setWeight();
    }

    //if a Site exist in the system remove it from the destFile and calculate the total weight
    public boolean removeFromDestFiles(Site site)
    {
        if(DestFiles.containsKey(site))
        {
            DestFiles.remove(site);
            setWeight();
            return true;
        }
        return false;
    }

    public HashMap<Site, ProductFile> getDestFiles() {
        return DestFiles;
    }

    //return a specific file according to its destination
    public ProductFile getFileByDest(Site dest)
    {
        if(DestFiles.containsKey(dest))
        {
            return DestFiles.get(dest);
        }
        return null;
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
            s=s+ (i+1) +". "+log.get(i)+"\n";
        }
        return s;
    }

    //check if a specific destination is in the destfile
    public boolean checkIfDestInFile(Site site) {
        return DestFiles.containsKey(site);
    }

    //doing first the setWeight method to validate the data
    public float getTotalWeight() {
        setWeight();
        return TotalWeight;
    }

    @Override
    public String toString() {
        String s = "id: " + ID + " Date: " + Date.toString() + " TruckNumber: " + Truck.getLicense_plate() + " Driver: " + Driver.getName()
                +" Source: "+Source.toString() +"\n";
        if(DestFiles.size()>0)
        {
            int count = 1;
            s = s + "\tdestinations and products: \n";
            for (Site site:DestFiles.keySet()) {
                s=s+"\t\t"+count+". site: "+site.toString()+"\n";
                s=s+"\tproducts File: "+DestFiles.get(site).toString();
                count++;
            }
        }
        else {
            s = s + "\tdestinations and products: none\n";
        }
        s = s + "\tTotalWeight: " + TotalWeight +"\n";
        if(log.size()>0) {
            s = s + "\tlog messages:\n" + getLogMessages();
        }
        else {
            s = s + "\tlog messages: none";
        }
        return s;
    }
}
