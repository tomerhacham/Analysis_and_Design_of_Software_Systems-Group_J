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

    public boolean checkIfAvailable(Date date,Integer total_weight, Integer id)
    {
        return trucks.get(id).checkIfAvailable(date, total_weight);
    }

    public String getAvailableTrucks(Date date, Integer total_weight)
    {
        String available = "";
        for (Integer i:trucks.keySet()) {
            if(checkIfAvailable(date,total_weight, i))
            {
                available=available+getTruckDetails(i);
            }
        }
        return available;
    }

    public void addTransportToTruck(Integer id, Transport t)
    {
        trucks.get(id).AddTransport(t);
    }

    public String getDriversLicense(Integer id)
    {
        return trucks.get(id).getDrivers_license();
    }

    public String getTruckNumber(int id)
    {
        return trucks.get(id).getLicense_plate();
    }
}
