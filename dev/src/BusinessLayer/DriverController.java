package BusinessLayer;

import java.util.LinkedList;

//Singleton
public class DriverController {
    private static  DriverController instance = null;

    private LinkedList<Driver> drivers;
    private int Id_Counter;

    private DriverController(){
        drivers=new LinkedList<>();
        Id_Counter=0;
    }

    public static DriverController getInstance()
    {
        if(instance==null){
            instance=new DriverController();
        }
        return instance;
    }
}
