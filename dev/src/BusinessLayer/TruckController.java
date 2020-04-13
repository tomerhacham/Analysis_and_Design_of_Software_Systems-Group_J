package BusinessLayer;

import java.util.Date;
import java.util.Hashtable;

//singleton
public class TruckController {

    private static  TruckController instance = null;
    private Hashtable<Integer,Truck> trucks; // aggregates all trucks in the system <truckID, truck object>
    private int Id_Counter;

    // constructor
    private TruckController(){
        trucks = new Hashtable<>();
        Id_Counter = 0;
    }

    public static TruckController getInstance() {
        if(instance == null){
            instance = new TruckController();
        }
        return instance;
    }

    // returns truck by specified id
    public Truck getById(int id)
    {
        return trucks.get(id);
    }

    // create new instance of truck and add it to the table
    public void CreateTruck(String license_plate, String model, float net_weight, float max_weight, String drivers_license) {
        Truck t = new Truck(Id_Counter, license_plate, model, net_weight, max_weight, drivers_license);
        Id_Counter++;
        trucks.put(t.getId(),t);
    }

    // removes truck with the specified id from the table
    public void DeleteTruck(Integer id)
    {
        trucks.remove(id);
    }

    // returns string of the details of truck with the specified id
    public String getTruckDetails(Integer id)
    {
        return trucks.get(id).toString();
    }

    // returns string of the details of all trucks in the system
    public String getAllTrucksDetails() {
        String details = "";
        int count = 1;
        for (Integer i : trucks.keySet()) {
            details = details + count + ". " + getTruckDetails(i);
            count++;
        }
        return details;
    }

    // returns true if the truck with the specified id is available at date
    // otherwise returns false
    public boolean checkIfAvailableByDate(Date date, Integer id)
    {
        return trucks.get(id).checkIfAvailableByDate(date);
    }

    // returns true if the truck with the specified id have max weight sufficient to weight
    // otherwise returns false
    public boolean checkIfAvailableByWeight(float weight, Integer id) {
        return trucks.get(id).checkIfAvailableByWeight(weight);
    }

    // returns true if there is truck in the system which is available at date
    // otherwise returns false
    public boolean checkIfTrucksAvailableByDate(Date d) {
        for (Integer i:trucks.keySet()) {
            if(checkIfAvailableByDate(d, i))
                return true;
        }
        return false;
    }

    // returns string of details of all the trucks in the system which have max weight sufficient to weight
    public String getAvailableTrucks(float Weight) {
        String ret = "";
        int count = 1;
        for (Integer i:trucks.keySet()) {
            if(checkIfAvailableByWeight(Weight, i)) {
                ret = ret + count + ". " + trucks.get(i).toString();
                count++;
            }
        }
        return ret;
    }

    // adds a date to the list of occupied dates of the truck with the specified id
    public void addDate(Date date, int id){trucks.get(id).addDate(date);}

    // removes a date from the list of occupied dates of the truck with the specified id
    public void removeDate(Date date, int id){trucks.get(id).removeDate(date);}

    // returns a string with the driver license compatible to the truck with the specified id
    public String getDriversLicense(Integer id) {
        return trucks.get(id).getDrivers_license();
    }

}
