package BusinessLayer;

import java.util.LinkedList;

public class TransportController {
    private static  TransportController instance = null;

    private LinkedList<Transport> transports;
    private int Id_Counter;

    private TransportController(){
        transports=new LinkedList<>();
        Id_Counter=0;
    }

    public static TransportController getInstance()
    {
        if(instance==null){
            instance=new TransportController();
        }
        return instance;
    }
}
