import bussines_layer.BranchController;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.supplier_module.Order;
import bussines_layer.supplier_module.OrdersController;
import data_access_layer.Mapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presentation_layer.CLController;

import java.util.LinkedList;

import static junit.framework.TestCase.*;
import static junit.framework.TestCase.fail;

public class OrderTest {
    BranchController branchController;
    Mapper mapper;
    Order order1;
    CatalogProduct p;

    @BeforeEach
    public void setUp() {
        CLController.initialize();
        branchController = new BranchController(true);
        mapper = Mapper.getInstance();
        order1 = mapper.find_Order(1,1);
        p = mapper.find_CatalogProduct(10 , 1);
        branchController.switchBranch(1);
    }

    @AfterEach
    public void tearDown(){
        mapper.clearDatabase();
    }

    @Test
    public void totalPriceOrder (){
        Float total= (float)0;
        try {
            total = order1.getTotalAmount().getData();
        }catch (Exception e){
            fail("Exception "+ e);
        }
        assertEquals((float)185.0 , total);
    }

    @Test
    public void updateQuantity (){
        try {
            order1.updateProductQuantityInPeriodicOrder(p,50,(float)5.0);

        }catch (Exception e){
            fail("Exception "+ e);
        }
        Integer afterInsert = order1.getProductsAndQuantity().get(p);
        assertEquals(new Integer(50), afterInsert);
    }

    @Test
    public void removeProduct (){
        assertTrue(order1.getProductsAndQuantity().keySet().size()==1);

        try {
            order1.removeProductFromPeriodicOrder(p);
        } catch (Exception e){
            fail("Exception "+ e);
        }
        assertEquals(order1.getProductsAndQuantity().keySet().size() , 0);
        assertFalse(order1.getProductsAndQuantity().keySet().contains(p));
    }

    @Test
    public void changeDayToDeliver (){
        assertEquals(order1.getDayToDeliver().getData() , new Integer(3));

        try {
            order1.updateDayToDeliver(5);
        } catch (Exception e){
            fail("Exception "+ e);
        }
        assertEquals(order1.getDayToDeliver().getData() , new Integer(5));
        assertNotSame(order1.getDayToDeliver().getData() , new Integer(3));
    }
}
