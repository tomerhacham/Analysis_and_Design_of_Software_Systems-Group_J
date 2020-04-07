package BusinessLayer;

import java.util.Date;
import java.util.LinkedList;

public class Truck {
    private String license_plate;
    private String model;
    private Integer weight;
    private String drivers_license;
    private LinkedList<Transport> transports;
    private Integer id;

    public Truck(Integer id, String license_plate, String model, Integer weight, String drivers_license)
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

    public Boolean checkIfAvailable(Date d)
    {
        //that the time isn't overlapping another transport
        for(int i=0; i<transports.size(); i++)
        {
            if(transports.get(i).getDate().equals(d))
            {
                return false;
            }
        }
        return true;
    }

    public void AddTransport(Transport t)
    {
        transports.add(t);
    }

    public String toString()
    {
        String s = "id: "+id+ " license plate: "+license_plate+" model: "+model+" weight: "+weight+" drivers license: "+drivers_license+"\n";
        if(transports.size()>0) {
            s = s + "transports:\n";
            for (int i = 0; i < transports.size(); i++) {
                s = s + i + ". " + transports.get(i).toString();
            }
        }
        else {
            s=s + "transports: none\n";
        }
        return s;
    }


}
