/*
package BusinessLayer;

import PresentationLayer.IO;

import java.util.HashMap;

//Singleton
public class logger {

    // static variable single_instance of type Singleton
    private static logger instance = null;
    private String msg;

    //constructor
    private logger(){
        msg = "";
    }

    // static method to create instance of Singleton class
    public static logger getInstance()
    {
        if (instance == null)
            instance = new logger();

        return instance;
    }

    public void toPrint(String s){
        msg = s;
        sendToIO();
    }

    public void sendToIO(){
        if (msg.length()>0){
            IO.getInstance().
        }
        msg = "";
    }

}
*/
