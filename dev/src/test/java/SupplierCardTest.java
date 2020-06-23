import bussines_layer.Branch;
import bussines_layer.BranchController;
import bussines_layer.SupplierCard;
import bussines_layer.SupplierController;
import bussines_layer.enums.supplierType;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.Category;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.ProductController;
import bussines_layer.supplier_module.Contract;
import data_access_layer.Mapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presentation_layer.CLController;

import java.util.LinkedList;
import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotEquals;


public class SupplierCardTest {
    BranchController branchController = new BranchController(true);
    Mapper mapper = Mapper.getInstance();
    SupplierCard halavi_Lee = mapper.find_Supplier(1);
    Contract contract = mapper.find_Contract(1 ,1);

    @BeforeEach
    public void setUp(){
        CLController.initialize();
        branchController.switchBranch(1);
    }

    @AfterEach
    public void tearDown(){
        branch.removeContract(supplierCard);
    }

    @Test
    public void testName(){
        try{
            halavi_Lee.setSupplierName("zoe");
        }catch (Exception e){
            fail("Exception "+ e);
        }

        assertEquals(halavi_Lee.getSupplierName(),"Zoe");
        assertNotEquals(halavi_Lee.getSupplierName(), "halavi-Lee");
    }

    @Test
    public void testAddress(){
        try{
            halavi_Lee.setAddress("TelAviv");
        }catch (Exception e){
            fail("Exception "+ e);
        }
        assertEquals(halavi_Lee.getAddress(),"TelAviv");
        assertNotEquals(halavi_Lee.getAddress(), "ringelbloom 97, beer-sheva");
    }

    @Test
    public void testAddCategory(){
        try{
            contract.addCategory("Meat");
        }catch (Exception e){
            fail("Exception "+ e);
        }
        assertTrue(contract.getCategory().contains("Meat"));
    }

    @Test
    public void testDeleteCategory(){
        try{
            contract.removeCategory("Dairy");
        }catch (Exception e){
            fail("Exception "+ e);
        }
        assertFalse(contract.getCategory().contains("Dairy"));
    }

    @Test
    public void testDeleteProductFromContract(){

        assertFalse(contract.getProducts().getData().isEmpty());
        CatalogProduct cp = mapper.find_CatalogProduct(10 , 1);
        try{
            contract.removeProduct(cp);
        }catch (Exception e){
            fail("Exception "+ e);
        }
        assertFalse(contract.getProducts().getData().contains(cp));
    }


}
