package main.java.bussines_layer;
import PresentationLayer.IO;
import presentation_layer.CLController;

/**
 * Class Result.
 *
 * Service static class for passing messages from the
 * business layer to presentation layer.
 *
 */

public class sz_Result {

    // static variable single_instance of type Singleton
    public static String msg="";

    public static void setMsg(String s){
        msg = s;
        sendToIO();
    }

    public static void sendToIO(){
        if (msg.length()>0){
            CLController.printResult(msg);
        }
        msg = "";
    }

}
