package BusinessLayer;

import java.util.HashMap;

public class Transport {

    private int ID;
    private String Date;
    private String Time;
    private int TruckNumber;
    private Driver Driver;
    private Site Source;
    private HashMap<Site, Integer> DestFiles;
    private int TotalWeight;
    private int Status;

    public Transport(int id, String date, String time, int truckNumber, Driver driver, Site source,
                     HashMap<Site, Integer> destFiles, int weight){
        ID = id;
        Date = date;
        Time = time;
        TruckNumber = truckNumber;
        Driver = driver;
        Source = source;
        DestFiles = destFiles;
        TotalWeight = weight;
    }

    public int getID() {
        return ID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
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

    public HashMap<Site, Integer> getDestFiles() {
        return DestFiles;
    }

    public void setDestFiles(HashMap<Site, Integer> destFiles) {
        DestFiles = destFiles;
    }

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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
