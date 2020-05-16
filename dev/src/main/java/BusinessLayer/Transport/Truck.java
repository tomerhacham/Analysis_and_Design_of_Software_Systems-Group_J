package BusinessLayer.Transport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Truck {
    private String license_plate;
    private String model;
    private float net_weight;
    private float max_weight;
    private String drivers_license;
    private ArrayList<Date> night_shifts;
    private ArrayList<Date> morning_shifts;
    private Integer id;

    public Truck(Integer id, String license_plate, String model, float net_weight, float max_weight , String drivers_license)
    {
        this.id=id;
        this.license_plate=license_plate;
        this.model = model;
        this.net_weight = net_weight;
        this.max_weight = max_weight;
        this.drivers_license = drivers_license;
        night_shifts=new ArrayList<>();
        morning_shifts = new ArrayList<>();
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
    public Boolean checkIfAvailableByDateAndShift(Date d, boolean shift) //shift- true:morning, false:night
    {
        if(shift==true)//morning
        {
            for(int i=0; i<morning_shifts.size(); i++)
            {
                if(morning_shifts.get(i).equals(d))
                {
                    return false;
                }
            }
        }
        else { //night shift
            for (int i = 0; i < night_shifts.size(); i++) {
                if (night_shifts.get(i).equals(d)) {
                    return false;
                }
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

    public void addDate(Date d, boolean shift){
        if(shift)//morning
        {
            morning_shifts.add(d);
        }
        else
        {
            night_shifts.add(d);
        }
    }

    public void removeDate(Date d, boolean shift){
        if(shift==true)//morning
        {
            for(int i=0; i<morning_shifts.size(); i++)
            {
                if(morning_shifts.get(i).equals(d))
                {
                    morning_shifts.remove(d);
                }
            }
        }
        else { //night shift
            for (int i = 0; i < night_shifts.size(); i++) {
                if (night_shifts.get(i).equals(d)) {
                    night_shifts.remove(d);
                }
            }
        }
    }

    public String toString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = "id: "+id+ " license plate: "+license_plate+" model: "+model+" net weight: "+net_weight+" max weight: "+max_weight+" drivers license: "+drivers_license+"\n";
        if(morning_shifts.size()>0 || night_shifts.size()>0) {
            s = s + "\tunavailable shifts:\n";
            for (int i = 0; i < morning_shifts.size(); i++) {
                s = s + "\t\t" + formatter.format(morning_shifts.get(i)) + " morning shift.\n";
            }
            for (int i = 0; i < night_shifts.size(); i++) {
                s = s + "\t\t" + formatter.format(night_shifts.get(i)) + " night shift.\n";
            }
        }
        else {
            s=s + "\tunavailable dates: none\n";
        }
        return s;
    }

    public String getModel() {
        return model;
    }

    public float getMax_weight() {
        return max_weight;
    }

    public float getNet_weight() {
        return net_weight;
    }
}
