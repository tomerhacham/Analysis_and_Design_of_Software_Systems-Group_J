package BusinessLayer;

import java.util.LinkedList;

//singleton
public class SiteController {

    private static  SiteController instance = null;

    private LinkedList<Site> sites;
    private int Id_Counter;

    private SiteController(){
        sites=new LinkedList<>();
        Id_Counter=0;
    }

    public static SiteController getInstance()
    {
        if(instance==null){
            instance=new SiteController();
        }
        return instance;
    }
}
