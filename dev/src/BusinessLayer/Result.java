package BusinessLayer;
import PresentationLayer.IO;

/**
 * Class Result.
 *
 * Service static class for passing messages from the
 * business layer to presentation layer.
 *
 */

public class Result {

    // static variable single_instance of type Singleton
    public static String msg="";

    public static void setMsg(String s){
        msg = s;
        sendToIO();
    }

    public static void sendToIO(){
        if (msg.length()>0){
            IO.getInstance().printResult(msg);
        }
        msg = "";
    }

}
