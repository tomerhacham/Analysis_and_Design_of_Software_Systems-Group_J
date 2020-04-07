package BusinessLayer;

import java.util.LinkedList;

public class Driver {
    private String name;
    private String license;
    private LinkedList<Transport> transports;
    private int id;

    public Driver(int id, String license, String name)
    {
        this.license = license;
        transports= new LinkedList();
        this.id=id;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public LinkedList getTransports() {
        return transports;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void addTransport(Transport t)
    {
        //need to check that the time isn't overlapping another transport
        transports.add(t);
    }
}
