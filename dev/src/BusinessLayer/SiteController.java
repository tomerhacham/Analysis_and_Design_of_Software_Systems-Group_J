package BusinessLayer;

import java.util.HashMap;
import java.util.Hashtable;

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

    public Site getById(int id)
    {
        return sites.get(id);
    }

    public boolean DeleteSite(Integer id)
    {
        if(sites.containsKey(id))
        {
            sites.remove(id);
            return true;
        }
        return false;
    }

    public String getSiteDetails(Integer id)
    {
        return sites.get(id).toString();
    }

    public String getAllSitesDetails()
    {
        String details="";
        int count=1;
        for (Integer i:sites.keySet()) {
            details=details+count+". "+getSiteDetails(i)+"\n";
            count++;
        }
        return details;
    }

    public boolean checkIfAvailable(Integer my_id, Integer other_id)
    {
        return sites.get(my_id).checkIfAvailable(sites.get(other_id).getShipping_area());
    }

    public String getAvailableSites(int other_id, HashMap<Integer,Integer>destFile)
    {
        String available = "";
        int count=1;
        for (Integer i:sites.keySet()) {
            boolean NotAvailable=false;
            for(Integer j:destFile.keySet())
            {
                if(i==j)
                {
                    NotAvailable=true;
                }
            }
            if(NotAvailable==false&&i!=other_id && checkIfAvailable(i,other_id))
            {
                available=available+count+". "+getSiteDetails(i)+"\n";
                count++;
            }
        }
        return available;
    }

    public boolean checkIfSiteExistAndAvailable(int destId, int sourceId)
    {
        if(sites.containsKey(destId)&&sites.containsKey(sourceId)){
            if(sites.get(destId).getShipping_area()==sites.get(sourceId).getShipping_area())
            {
                return true;
            }
        }
        return false;
    }
}
