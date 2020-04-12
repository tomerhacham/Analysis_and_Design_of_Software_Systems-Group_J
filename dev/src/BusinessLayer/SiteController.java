package BusinessLayer;

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

    public void DeleteSite(Integer id)
    {
        sites.remove(id);
    }

    public String getSiteDetails(Integer id)
    {
        return sites.get(id).toString();
    }

    public String getAllSitesDetails()
    {
        String details="";
        for (Integer i:sites.keySet()) {
            details=details+getSiteDetails(i)+"\n";
        }
        return details;
    }

    public boolean checkIfAvailable(Integer my_id, Integer other_id)
    {
        return sites.get(my_id).checkIfAvailable(sites.get(other_id).getShipping_area());
    }

    public String getAvailableSites(int other_id)
    {
        String available = "";
        for (Integer i:sites.keySet()) {
            if(checkIfAvailable(i,other_id))
            {
                available=available+getSiteDetails(i)+"\n";
            }
        }
        return available;
    }
}
