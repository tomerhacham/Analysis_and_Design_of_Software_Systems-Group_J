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
    
    public ArrayList<String> getAllDriversDetails()
    {
        ArrayList<String> details=new ArrayList<>();
        for (Integer i:drivers.keySet()) {
           details.add(getDriverDetails(i));
        }
        return details;
    }

    public boolean checkIfAvailable(Date date, Integer id, String licence)
    {
        return drivers.get(id).checkIfAvailable(date, licence);
    }

    public ArrayList<String> getAvailbleDrivers(Date date, String licence)
    {
        ArrayList<String> available = new ArrayList<>();
        for (Integer i:drivers.keySet()) {
            if(checkIfAvailable(date, i, licence))
            {
                available.add(getDriverDetails(i));
            }
        }
        return available;
    }

    public void addTransportToDriver(Integer id, Transport t)
    {
        drivers.get(id).AddTransport(t);
    }

}
