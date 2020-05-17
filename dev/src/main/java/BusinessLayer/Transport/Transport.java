package BusinessLayer.Transport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Transport {
    private int ID;
    private Date Date;
    private boolean Shift; //in which shift is the transport
    private Truck Truck;
    private String driverId;
    private String driverName;
    private Site Source;
    private HashMap<Site, ProductFile> DestFiles;
    private float TotalWeight;
    private ArrayList<String> log;

    public Transport(int id){
        ID = id;
        DestFiles = new HashMap<>();
        log =new ArrayList<>();
    }

    public Transport(int id, Date date, boolean partOfDay, Truck truck, String driverId, String driverName, Site source, float totalWeight)
    {
        ID=id;
        Date=date;
        Shift=partOfDay;
        Truck=truck;
        this.driverId =driverId;
        this.driverName=driverName;
        Source= source;
        TotalWeight=totalWeight;
        DestFiles = new HashMap<>();
        log =new ArrayList<>();
    }
    public int getID() { return ID;}

    public Date getDate() { return Date;}

    public void setDateTime(Date date,boolean shift) {
        Date = date;
        Shift = shift;
    }

    public Truck getTruck() {return Truck; }

    public void setTruck(Truck truck) {Truck = truck;}

    public String getDriverId() { return driverId; }

    public void setDriver(String driver_Id, String driver_Name) {
        driverId = driver_Id;
        driverName = driver_Name;
    }

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
    public boolean removeFromDestFiles(Site site){
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
    public ProductFile getFileByDest(Site dest){
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

    public String getLogMessages(){
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = "Transport Details:\n" + "\tid: " + ID + "\tDate: " + formatter.format(Date) +
                " \tTruckNumber: " + Truck.getLicense_plate() +
                " \tDriver: " + driverName + "\n"
                +"\tSource details:\n\t\t" + Source.toString() + "\n";
        if(DestFiles.size()>0) {
            int count = 1;
            s = s + "\tDestinations and products details: \n";
            for (Site site:DestFiles.keySet()) {
                s=s+"\t\t"+count+". site: "+site.toString()+"\n";
                s=s+"\t\tproducts File: "+DestFiles.get(site).toString();
                count++;
            }
        }
        else {
            s = s + "\tDestinations and products: none\n";
        }
        s = s + "\tTotalWeight: " + TotalWeight +"\n";
        if(log.size()>0) {
            s = s + "\tLog messages:\n" + getLogMessages();
        }
        else {
            s = s + "\tLog messages: none";
        }
        return s;
    }

    public boolean getShift(){return Shift;}

    public ArrayList<String> getLog() {
        return log;
    }

    public Site getSource() {
        return Source;
    }

    public String getDriverName() {
        return driverName;
    }

}
