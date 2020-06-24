import bussines_layer.BranchController;
import data_access_layer.Mapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static presentation_layer.CLController.initialize;

public class TruckControllerTest {
    static private BranchController branchController;

    @BeforeAll
    static void setUp(){
        initialize();
        branchController = new BranchController(true);
        branchController.switchBranch(1);
    }

    @AfterEach
    public void clear(){
        Mapper.getInstance().clearDatabase();
    }

    @Test
    public void createTruck() {
        // max weight smaller than net weight
         assertFalse(branchController.createTruck("WE-1234-FD","RNO51", 3000, 2500, "A"));
        //assertFalse(truckController.CreateTruck("WE-1234-FD", "RNO51", 3000, 2500, "A"));

        // max weight equal net weight
        assertFalse(branchController.createTruck("WE-1234-FD", "RNO51", 3000, 3000, "A"));
       // assertFalse(truckController.CreateTruck("WE-1234-FD", "RNO51", 3000, 3000, "A"));
    }

    @Test
    public void deleteTruck() {
        assertTrue(branchController.deleteTruck(1));
    }
}