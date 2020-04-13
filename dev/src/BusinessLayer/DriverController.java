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

    public Driver getById(int id)
    {
        return drivers.get(id);
    }

    public void CreateDriver(String license, String name)
    {
        Driver d = new Driver(Id_Counter, license, name);
        Id_Counter++;
        drivers.put(d.getId(),d);
    }

    public void DeleteDriver(Integer id)
    {
           drivers.remove(id);
    }

    public String getDriverDetails(Integer id)
    {
        return drivers.get(id).toString();
    }
    
    public String getAllDriversDetails()
    {
        String details="";
        for (Integer i:drivers.keySet()) {
           details=details+getDriverDetails(i)+"\n";
        }
        return details;
    }

    public boolean checkIfAvailableByDate(Date date, Integer id)
    {
        return drivers.get(id).checkIfAvailableByDate(date);
    }

    public boolean checkIfAvailableByLicence(String licence, Integer id)
    {
        return drivers.get(id).checkIfAvailableByLicence(licence);
    }

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

    public String getAvailableDrivers(String licence)
    {
        String ret = "";
        for (Integer i:drivers.keySet()) {
            if(checkIfAvailableByLicence(licence, i))
            {
                ret=ret+drivers.get(i).toString();
            }
        }
        return ret;
    }


    public void addDate(Date d, int id){drivers.get(id).addDate(d);}

    public void removeDate(Date d, int id){drivers.get(id).removeDate(d);}
}
