package TestTransportModule;
import BusinessLayer.Transport.Site;
import BusinessLayer.Transport.SiteController;
import InterfaceLayer.Transport.FacadeController;
import org.junit.jupiter.api.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class SiteControllerTest {
    SiteController siteController;
    FacadeController facadeController;

    public SiteControllerTest() {
        siteController = SiteController.getInstance();
        facadeController = FacadeController.getInstance();
        createSites();
    }

    private void createSites() {
        siteController.reset();
        facadeController.createSite("Beer-Sheva", " 054-1234567", "Moshe", 1);
        facadeController.createSite("Ofakim", " 054-1234567", "Dan", 1);
        facadeController.createSite("Omer", " 054-1234567", "Moran", 1);
        facadeController.createSite("Tel-Aviv", " 054-1234567", "Gali", 2);
    }

    @Test
    public void getAvailableSites() {
        //check if there are more sites with the same shipping area
        String noMoreSites = "";
        //there are more sites with shipping area 1
        String S_A_1=facadeController.getAvailableSites(0, new HashMap<>());
        assertFalse(noMoreSites.equals(S_A_1));


        //no others sites with shipping area 2
        String S_A_2 =facadeController.getAvailableSites(3, new HashMap<>());
        assertTrue(noMoreSites.equals(S_A_2));
    }

    @Test
    public void checkCreateAndDelete() {
        //check if the site was created successful
        Site s = siteController.getById(3);
        assertNotNull(s);
        siteController.DeleteSite(3);
        //check if the site was deleted successful
        s = siteController.getById(3);
        assertNull(s);
    }
}