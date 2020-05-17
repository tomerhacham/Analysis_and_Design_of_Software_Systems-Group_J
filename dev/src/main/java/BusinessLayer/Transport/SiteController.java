package BusinessLayer.Transport;

import DataAccessLayer.Mapper;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

//singleton
public class SiteController {
    private static  SiteController instance = null;
    private static Mapper mapper = Mapper.getInstance();
    private Hashtable<Integer , Site> sites;
    private int Id_Counter;

    private SiteController(){
        sites=new Hashtable<>();
        Id_Counter=(int)mapper.MaxIdSite() + 1;
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
        mapper.addSite(s);
    }

    public boolean getSiteFromDB(int siteID)
    {
        Site s = mapper.getSite(siteID);
        if(s!=null) {
            sites.put(siteID, s);
            return true;
        }
        return false;
    }

    //if a site exist in the system return it, else return null
    public Site getById(int id)
    {
        if(sites.containsKey(id) || getSiteFromDB(id)) {
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
        }
        return mapper.deleteSite(id);

    }

    //if a site exist in the system return its details, else return empty string
    public String getSiteDetails(Integer id)
    {
        if(sites.containsKey(id) || getSiteFromDB(id)) {
            return sites.get(id).toString();
        }
        return "";
    }

    //return all sites details
    public String getAllSitesDetails()
    {
        List<Site> all_sites = mapper.getAllSites();
        String details="";
        int count=1;
        for (Site s :all_sites) {
            details=details+count+". "+ s.toString() +"\n";
            count++;
        }
        return details;
    }

    //if a site exist in the system, check if its has an other site shipping area
    public boolean checkIfChosen(Integer my_id, HashMap<Integer,Integer>destFile)
    {
        for(Integer j:destFile.keySet())
        {
            //check if the site my_id is not equal to a site j - that was already chosen to be a destination in the transport
            if(my_id == j)
            {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfAvailable(int siteID, int sourceID, HashMap<Integer, Integer> destFile) {
        Site site = mapper.getSite(siteID);
        Site source = mapper.getSite(sourceID);
        if (site != null && source != null) {
            return siteID != sourceID && site.getShipping_area() == source.getShipping_area() && !checkIfChosen(siteID, destFile);
        }
        return false;
    }

    //return the details of all available sites
    public String getAvailableSites(int other_id, HashMap<Integer,Integer>destFile)
    {
        List<Site> availableSites = mapper.getAvailableSites(other_id); // returns all sites with same shipping area as other_id and with different id
        String available = "";
        int count = 1;
        for (Site s : availableSites) {

            //add details of sites who are not equals to other chosen, to the source and are available by shipping area
            if(!checkIfChosen(s.getId(), destFile))
            {
                available = available + count + ". " + s.toString() +"\n";
                count++;
            }
        }
        return available;
    }

    //check if a site exist in teh system
    public boolean checkIfSiteExist(int siteId)
    {
        Site site = mapper.getSite(siteId);
        return sites.containsKey(siteId) || site != null;
    }

    public void reset()
    {
        sites.clear();
        Id_Counter=0;
    }
}
