package BusinessLayer;

import java.util.LinkedList;

public class Truck {
    private String license_plate;
    private String model;
    private Integer weight;
    private String drivers_license;
    private LinkedList<Transport> transports;
    private int id;

    public Truck(int id, String license_plate, String model, Integer weight, String drivers_license)
    {
        this.id=id;
        this.license_plate=license_plate;
        this.model = model;
        this.weight = weight;
        this.drivers_license = drivers_license;
        transports=new LinkedList();
    }

    public int getId() {
        return id;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public String getModel() {
        return model;
    }

    public Integer getWeight() {
        return weight;
    }

    public String getDrivers_license() {
        return drivers_license;
    }

    public void addTransport(Transport t)
    {
        //need to check that the time isn't overlapping another transport
        transports.add(t);
    }


}
