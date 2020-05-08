package BusinessLayer.Transport;

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

    //create a new site in the system
    public void CreateSite( String address, String phone, String contact, Integer area)
    {
        Site s = new Site(Id_Counter, address, phone, contact, area);
        Id_Counter++;
        sites.put(s.getId(),s);
    }

    //if a site exist in the system return it, else return null
    public Site getById(int id)
    {
        if(sites.containsKey(id)) {
            return sites.get(id);
        }
        return null;
    }

    //if a site exist in the system delete it
    public boolean DeleteSite(Integer id)
    {
        if(sites.containsKey(id))
        {
            sites.remove(id);
            return true;
        }
        return false;
    }

    //if a site exist in the system return its details, else return empty string
    public String getSiteDetails(Integer id)
    {
        if(sites.containsKey(id)) {
            return sites.get(id).toString();
        }
        return "";
    }

    //return all sites details
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

    //if a site exist in the system, check if its has an other site shipping area
    public boolean checkIfAvailable(Integer my_id, Integer other_id, HashMap<Integer,Integer>destFile)
    {
        if(sites.containsKey(my_id)&&sites.containsKey(other_id)) {
            boolean Available=true;
            for(Integer j:destFile.keySet())
            {
                //check if the site my_id is not equal to a site j - that was already chosen to be a destination in the transport
                if(my_id==j)
                {
                    Available=false;
                }
            }
            return Available && sites.get(my_id).checkIfAvailable(sites.get(other_id).getShipping_area());
        }
        else return false;
    }

    //return the details of all available sites
    public String getAvailableSites(int other_id, HashMap<Integer,Integer>destFile)
    {
        String available = "";
        int count = 1;
        for (Integer i:sites.keySet()) {

            //add details of sites who are not equals to other chosen, to the source and are available by shipping area
            if( i != other_id && checkIfAvailable(i,other_id,destFile))
            {
                available = available + count + ". " + getSiteDetails(i)+"\n";
                count++;
            }
        }
        return available;
    }

    //check if a site exist in teh system
    public boolean checkIfSiteExist(int siteId)
    {
        return sites.containsKey(siteId);
    }

    public void reset()
    {
        sites.clear();
        Id_Counter=0;
    }
}
