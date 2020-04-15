package BusinessLayer;

import java.util.*;

//Singleton
public class DriverController {
    private static  DriverController instance = null;

    private Hashtable<Integer,Driver> drivers;
    private Integer Id_Counter;

    private DriverController(){
        drivers=new Hashtable<>();
        Id_Counter=0;
    }

    public static DriverController getInstance()
    {
        if(instance==null){
            instance=new DriverController();
        }
        return instance;
    }

    //if the driver exist in the system return it, else return null
    public Driver getById(int id)
    {
        if (drivers.containsKey(id)) {
            return drivers.get(id);
        }
        else return null;
    }

    //creating a new driver
    public void CreateDriver(String license, String name)
    {
        Driver d = new Driver(Id_Counter, license, name);
        Id_Counter++;
        drivers.put(d.getId(),d);
    }

    //if the driver exist in the system delete it
    public boolean DeleteDriver(Integer id)
    {
        if(drivers.containsKey(id)) {
            drivers.remove(id);
            return true;
        }
        return false;
    }

    //return a specific driver details
    public String getDriverDetails(Integer id)
    {
        if(drivers.containsKey(id)) {
            return drivers.get(id).toString();
        }
        else return "";
    }

    //return all drivers details
    public String getAllDriversDetails()
    {
        String details="";
        int count=1;
        for (Integer i:drivers.keySet()) {
           details=details+count+". "+getDriverDetails(i);
           count++;
        }
        return details;
    }

    //if the driver exist in the system check if he available in a specific date
    public boolean checkIfAvailableByDate(Date date, Integer id)
    {
        if(drivers.containsKey(id)) {
            return drivers.get(id).checkIfAvailableByDate(date);
        }
        return false;
    }

    //if the driver exist in the system check if he has a specific licence
    public boolean checkIfAvailableByLicence(String licence, Integer id)
    {
        if(drivers.containsKey(id)) {
            return drivers.get(id).checkIfAvailableByLicence(licence);
        }
        return false;
    }

    //check if there is a driver in the system that is available in a specific date
    public boolean checkIfDriversAvailableByDate(Date d)
    {
        for (Integer i:drivers.keySet()) {
            if(checkIfAvailableByDate(d, i))
            {
                return true;
            }
        }
        return false;
    }

    //get all drivers that has available both by license and date
    public String getAvailableDrivers(Date d, String licence)
    {
        String ret = "";
        int count=1;
        for (Integer i:drivers.keySet()) {
            if(checkIfAvailableByLicence(licence, i))
            {
                if(checkIfAvailableByDate(d, i)) {
                    ret = ret + count + ". " + drivers.get(i).toString() + "\n";
                    count++;
                }
            }
        }
        return ret;
    }

    //if the driver exist in the system, add a date of a transport
    public void addDate(Date d, int id){
        if(drivers.containsKey(id)) {
            drivers.get(id).addDate(d);
        }
    }
    //if the driver exist in the system, remove a date of a transport
    public void removeDate(Date d, int id){
        if(drivers.containsKey(id)) {
            drivers.get(id).removeDate(d);
        }
    }

    //if the driver exist in the system check if he has a specific licence and if he available in a specific date
    public boolean checkIfDriverExistAndValid(int driverID, String licence, Date d) {
        if(drivers.containsKey(driverID))
        {
            return (checkIfAvailableByLicence(licence,driverID)&&checkIfAvailableByDate(d,driverID));
        }
        return false;
    }
}
