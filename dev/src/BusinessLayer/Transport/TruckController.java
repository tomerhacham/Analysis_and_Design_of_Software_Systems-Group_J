package BusinessLayer.Transport;

import java.util.Date;
import java.util.Hashtable;

//singleton
public class TruckController {
    private static  TruckController instance = null;
    private Hashtable<Integer, Truck> trucks; // aggregates all trucks in the system <truckID, truck object>
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

    // returns truck by specified id if it exist in the system else return null
    public Truck getById(int id)
    {
        if(trucks.containsKey(id)) {
            return trucks.get(id);
        }
        return null;
    }

    // create new instance of truck and add it to the table
    public boolean CreateTruck(String license_plate, String model, float net_weight, float max_weight, String drivers_license) {
        if (max_weight <= net_weight){
            return false;
        }
        Truck t = new Truck(Id_Counter, license_plate, model, net_weight, max_weight, drivers_license);
        Id_Counter++;
        trucks.put(t.getId(),t);
        return true;
    }

    // removes truck with the specified id from the table
    public boolean DeleteTruck(Integer id) {
        if(trucks.containsKey(id)) {
            trucks.remove(id);
            return true;
        }
        return false;
    }

    // returns string of the details of truck with the specified id
    public String getTruckDetails(Integer id)
    {
        if(trucks.containsKey(id)) {
            return trucks.get(id).toString();
        }
        return "";
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
    // shift- morning:true, night:false
    public boolean checkIfAvailableByDate(Date date,Boolean shift, Integer id)
    {
        if(trucks.containsKey(id)) {
            return trucks.get(id).checkIfAvailableByDateAndShift(date,shift);
        }
        return false;
    }

    // returns true if the truck with the specified id have max weight sufficient to weight
    // otherwise returns false
    public boolean checkIfAvailableByWeight(float weight, Integer id) {
        if(trucks.containsKey(id)) {
            return trucks.get(id).checkIfAvailableByWeight(weight);
        }
        return false;
    }

    // returns true if there is a truck in the system which is available at date
    // otherwise returns false
    // shift- morning:true, night:false
    public boolean checkIfTrucksAvailableByDate(Date d, boolean shift) {
        for (Integer i:trucks.keySet()) {
            if(checkIfAvailableByDate(d,shift, i))
                return true;
        }
        return false;
    }

    // returns string of details of all the trucks in the system which have max weight
    // sufficient to weight and are available by date
    // shift- morning:true, night:false
    public String getAvailableTrucks(Date date,boolean shift, float Weight) {
        String ret = "";
        int count = 1;
        for (Integer i:trucks.keySet()) {
            if(checkIfAvailableByWeight(Weight, i)) {
                if(checkIfAvailableByDate(date, shift, i)) {
                    ret = ret + count + ". " + trucks.get(i).toString();
                    count++;
                }
            }
        }
        return ret;
    }

    // adds a date to the list of occupied dates of the truck with the specified id
    // shift- morning:true, night:false
    public void addDate(Date date,boolean shift, int id){
        if(trucks.containsKey(id)){
            trucks.get(id).addDate(date,shift);
        }}

    // removes a date from the list of occupied dates of the truck with the specified id
    // shift- morning:true, night:false
    public void removeDate(Date date,boolean shift, int id){
        if(trucks.containsKey(id))
        {
            trucks.get(id).removeDate(date, shift);
        }}

    // returns a string with the driver license compatible to the truck with the specified id
    public String getDriversLicense(Integer id) {
        if(trucks.containsKey(id)) {
            return trucks.get(id).getDrivers_license();
        }
        return "";
    }

    // returns true if the truck with the specified id exist in the system
    // shift- morning:true, night:false
    public boolean checkIfTruckExistAndValid(int truckID, float totalWeight, Date d, boolean shift) {
        if(trucks.containsKey(truckID))
        {
           return (checkIfAvailableByWeight(totalWeight, truckID)&&checkIfAvailableByDate(d,shift,truckID));
        }
        return false;
    }

    public void reset()
    {
        trucks.clear();
        Id_Counter=0;
    }
}
