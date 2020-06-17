package bussines_layer.transport_module;

import data_access_layer.Mapper;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class TruckController {
    private static Mapper mapper = Mapper.getInstance();
    private Hashtable<Integer, Truck> trucks; // aggregates all trucks in the system <truckID, truck object>
    private int Id_Counter;
    private int branch_id;

    public TruckController(int branchId){
        Id_Counter = (int)mapper.MaxIdTrucks() + 1;
        branch_id=branchId;
        trucks = mapper.getAllTrucksByBranch(branch_id);
    }

    // create new instance of truck and add it to the table
    public boolean CreateTruck(String license_plate, String model, float net_weight, float max_weight, String drivers_license) {
        if (max_weight <= net_weight){
            return false;
        }
        Truck t = new Truck(Id_Counter, license_plate, model, net_weight, max_weight, drivers_license, branch_id);
        Id_Counter++;
        trucks.put(t.getId(),t);
        mapper.addTruck(t);
        return true;
    }

    // removes truck with the specified id from the table
    public boolean DeleteTruck(Integer id) {
        if(trucks.containsKey(id)) {
            trucks.remove(id);
        }
        return mapper.deleteTruck(id);
    }

    public boolean getTruckFromDB(Integer id){
        Truck truck = mapper.getTruck(id);
        if (truck != null){
            trucks.put(id, truck);
            return true;
        }
        return false;
    }


    // returns string of the details of all trucks in the system
    public String getAllTrucksDetails() {
        String details = "";
        int count = 1;
        for (Truck t : trucks.values()) {
            details = details + count + ". " + t.toString();
            count++;
        }
        return details;
    }

    // returns true if there is a truck in the system which is available at date
    // otherwise returns false
    // shift- morning:true, night:false
    public boolean checkIfTrucksAvailableByDate(Date d, boolean partOfDay) {
        return mapper.checkIfTrucksAvailableByDate(d, partOfDay, branch_id);
    }

    // returns list of all the trucks in the system which have max weight
    // sufficient to weight and are available by date
    // shift- morning:true, night:false
    public List<Truck> getAvailableTrucks(Date date,boolean partOfDay, float Weight) {
        return mapper.getAvailableTrucks(date, partOfDay, Weight, branch_id);
    }

    // adds a date to the list of occupied dates of the truck with the specified id
    // shift- morning:true, night:false
    public void addDate(Date date,boolean shift, int id){
        if(trucks.containsKey(id)||getTruckFromDB(id)){
            trucks.get(id).addDate(date,shift);
        }
        if(shift)
            mapper.addMorningShift(id,date);
        else
            mapper.addNightShift(id,date);
    }

    // removes a date from the list of occupied dates of the truck with the specified id
    // shift- morning:true, night:false
    public void removeDate(Date date,boolean shift, int id){
        if(trucks.containsKey(id)||getTruckFromDB(id))
        {
            trucks.get(id).removeDate(date, shift);
        }
        if(shift)
            mapper.delete_MorningShift(date, id);
        else
            mapper.deleteNightShift(date,id);
    }




}
