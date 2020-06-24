import bussines_layer.BranchController;
import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.supplier_module.Contract;
import com.j256.ormlite.logger.LocalLog;
import data_access_layer.Mapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presentation_layer.CLController;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotEquals;


public class SupplierCardTest {
    BranchController branchController;
    Mapper mapper;
    Contract contract ;

    @BeforeEach
    public void setUp(){
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
        CLController.initialize();
        branchController = new BranchController(true);
        branchController.switchBranch(1);
        mapper = Mapper.getInstance();

    }

    @AfterEach
    public void tearDown(){
        mapper.clearDatabase();
    }

    @Test
    public void testAddCategory(){
        try{
            mapper = Mapper.getInstance();
            contract = mapper.find_Contract(1 ,1);
            contract.addCategory("Meat");
            assertTrue(contract.getCategory().contains("Meat"));
        }catch (Exception e){
            fail("Exception "+ e);
        }
    }

    @Test
    public void testDeleteCategory(){
        try{
            mapper = Mapper.getInstance();
            contract = mapper.find_Contract(1 ,1);
            contract.removeCategory("Dairy");
            assertFalse(contract.getCategory().contains("Dairy"));
        }catch (Exception e){
            fail("Exception "+ e);
        }
    }

    @Test
    public void testDeleteProductFromContract(){
        try{
            contract = mapper.find_Contract(1 ,1);
            assertFalse(contract.getProducts().getData().isEmpty());
            CatalogProduct cp = mapper.find_CatalogProduct(10 , 1);
            contract.removeProduct(cp);
            assertFalse(contract.getProducts().getData().contains(cp));
        }catch (Exception e){
            fail("Exception "+ e);
        }
    }


}
