package BusinessLayer;

import java.util.LinkedList;

//singleton
public class TruckController {

    private static  TruckController instance = null;

    private LinkedList<Truck> trucks;
    private int Id_Counter;

    private TruckController(){
        trucks=new LinkedList<>();
        Id_Counter=0;
    }

    public static TruckController getInstance()
    {
        if(instance==null){
            instance=new TruckController();
        }
        return instance;
    }
}
