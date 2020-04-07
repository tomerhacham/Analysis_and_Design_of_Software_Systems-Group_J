package BusinessLayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;

//singleton
public class SiteController {

    private static  SiteController instance = null;

    private Hashtable<Integer , Site> sites;
    private int Id_Counter;

    private SiteController(){
        sites=new Hashtable<>();
        Id_Counter=0;
    }

    public static SiteController getInstance()
    {
        if(instance==null){
            instance=new SiteController();
        }
        return instance;
    }

    public void CreateSite( String address, String phone, String contact, Integer area)
    {
        Site s = new Site(Id_Counter, address, phone, contact, area);
        Id_Counter++;
        sites.put(s.getId(),s);
    }

    public void DeleteSite(Integer id)
    {
        sites.remove(id);
    }

    public String getSiteDetails(Integer id)
    {
        return sites.get(id).toString();
    }

    public ArrayList<String> getAllSitesDetails()
    {
        ArrayList<String> details=new ArrayList<>();
        for (Integer i:sites.keySet()) {
            details.add(getSiteDetails(i));
        }
        return details;
    }

    public boolean checkIfAvailable(Integer my_id, Integer other_id)
    {
        return sites.get(my_id).checkIfAvailable(sites.get(other_id).getShipping_area());
    }

    public ArrayList<String> getAvailbleSites(Integer other_id)
    {
        ArrayList<String> available = new ArrayList<>();
        for (Integer i:sites.keySet()) {
            if(checkIfAvailable(i,other_id))
            {
                available.add(getSiteDetails(i));
            }
        }
        return available;
    }
}
