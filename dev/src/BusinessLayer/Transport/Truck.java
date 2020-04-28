package BusinessLayer.Transport;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class Truck {
    private String license_plate;
    private String model;
    private float net_weight;
    private float max_weight;
    private String drivers_license;
    private ArrayList<Date> Dates;
    private Integer id;

    public Truck(Integer id, String license_plate, String model, float net_weight, float max_weight , String drivers_license)
    {
        this.id=id;
        this.license_plate=license_plate;
        this.model = model;
        this.net_weight = net_weight;
        this.max_weight = max_weight;
        this.drivers_license = drivers_license;
        Dates=new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public String getDrivers_license() {
        return drivers_license;
    }

    //check if the truck is available in a specific date
    public Boolean checkIfAvailableByDate(Date d)
    {
        for(int i=0; i<Dates.size(); i++)
        {
            if(Dates.get(i).equals(d))
            {
                return false;
            }
        }
        return true;
    }

    //check if the truck can carry a given weight and that its not reach the max_weight
    public Boolean checkIfAvailableByWeight(float total_weight)
    {
        if((total_weight+net_weight)>max_weight)
        {
            return false;
        }
        return true;
    }

    public void addDate(Date d){Dates.add(d);}

    public void removeDate(Date d){
        for (int i = 0 ; i<Dates.size() ; i++)
        {
            if(Dates.get(i).equals(d))
                Dates.remove(i);
        }
    }

    public String toString()
    {
        String s = "id: "+id+ " license plate: "+license_plate+" model: "+model+" net weight: "+net_weight+" max weight: "+max_weight+" drivers license: "+drivers_license+"\n";
        if(Dates.size()>0) {
            s = s + "\tunavailable dates:\n";
            for (int i = 0; i < Dates.size(); i++) {
                s = s + "\t\t" + Dates.get(i).toString() + "\n";
            }
        }
        else {
            s=s + "\tunavailable dates: none\n";
        }
        return s;
    }
}
