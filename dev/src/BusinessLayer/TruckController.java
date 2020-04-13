package BusinessLayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;

//singleton
public class TruckController {

    private static  TruckController instance = null;

    private Hashtable<Integer,Truck> trucks;
    private int Id_Counter;

    private TruckController(){
        trucks=new Hashtable<>();
        Id_Counter=0;
    }

    public static TruckController getInstance()
    {
        if(instance==null){
            instance=new TruckController();
        }
        return instance;
    }

    public Truck getById(int id)
    {
        return trucks.get(id);
    }

    public void CreateTruck(String license_plate, String model, Integer net_weight, Integer max_weight, String drivers_license)
    {
        Truck t = new Truck(Id_Counter, license_plate, model, net_weight, max_weight, drivers_license);
        Id_Counter++;
        trucks.put(t.getId(),t);
    }

    public void DeleteTruck(Integer id)
    {
        trucks.remove(id);
    }

    public String getTruckDetails(Integer id)
    {
        return trucks.get(id).toString();
    }

    public String getAllTrucksDetails()
    {
        String details="";
        for (Integer i:trucks.keySet()) {
            details=details+getTruckDetails(i);
        }
        return details;
    }

    public boolean checkIfAvailableByDate(Date date, Integer id)
    {
        return trucks.get(id).checkIfAvailableByDate(date);
    }

    public boolean checkIfAvailableByWeight(int Weight, Integer id)
    {
        return trucks.get(id).checkIfAvailableByWeight(Weight);
    }

    public boolean checkIfTrucksAvailableByDate(Date d)
    {
        for (Integer i:trucks.keySet()) {
            if(checkIfAvailableByDate(d, i))
            {
                return true;
            }
        }
        return false;
    }

    public String getAvailableTrucks(int Weight)
    {
        String ret = "";
        for (Integer i:trucks.keySet()) {
            if(checkIfAvailableByWeight(Weight, i))
            {
            ret=ret+trucks.get(i).toString();
            }
        }
        return ret;
    }

    public void addDate(Date d, int id){trucks.get(id).addDate(d);}

    public void removeDate(Date d, int id){trucks.get(id).removeDate(d);}

    public String getDriversLicense(Integer id)
    {
        return trucks.get(id).getDrivers_license();
    }

    public String getTruckNumber(int id)
    {
        return trucks.get(id).getLicense_plate();
    }
}
