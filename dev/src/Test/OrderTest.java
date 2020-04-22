package Test;
import BusinessLayer.*;
import BusinessLayer.Order;
import org.junit.jupiter.api.*;
import java.util.LinkedList;
import static junit.framework.TestCase.*;


public class OrderTest {
    FacadeController facadeController;
    OrdersController ordersController;
    Order order1;
    Order order2;
    Product p;

    @BeforeEach
    public void setUp() {
        ordersController = OrdersController.getInstance();
        facadeController = FacadeController.getInstance();

        LinkedList<String> contact = new LinkedList();
        contact.add("Moshe");
        contact.add("Rachel");
        LinkedList<String> category = new LinkedList<>();
        category.add("Dairy");
        category.add("Vegetables");
        Contract contract = facadeController.CreateContract(category , 14458727 , "by Order");

        facadeController.createSupplierCard("halavi-lee" , "ringelbloom 97, beer-sheva" , "halavi@gmail.com" , "08- 1234567" ,
                14458727  , "0975635" , "CreditCard" , contact, contract);

        p = facadeController.createNewProduct(12 , "milk" , 7 , "Tnuva" , "Dairy" , 27784);
        facadeController.addProductToContract(14458727 ,p);

        Product p1 = facadeController.createNewProduct(15 , "yogurt" , 12 , "Tnuva" , "Dairy" , 27845);
        facadeController.addProductToContract(14458727 ,p1);

        facadeController.createOrder();
        facadeController.addProductToOrder(14458727 , 12 , 10);

        facadeController.createOrder();
        facadeController.addProductToOrder(14458727 , 12 , 10);
        facadeController.addProductToOrder(14458727 , 15 , 10);

        try {
            order1= ordersController.getOrders().getFirst();
            order2= ordersController.getOrders().getLast();
        }catch (Exception e){
            fail("Exception "+ e);
        }
    }

    @AfterEach
    public void tearDown (){
        try {
            ordersController.removeOrder();
            ordersController.removeOrder();
            facadeController.deleteSupplierCard(14458727);
        } catch (Exception e){
            fail("Exception "+ e);
        }
        order1 = null;
        order2 = null;
        p = null;
    }

    @Test
    public void totalPriceOrder (){
        double total=0;
        try {
            total = ordersController.getTotalPrice(1);
        }catch (Exception e){
            fail("Exception "+ e);
        }
        assertEquals(70.0 , total);
    }

    @Test
    public void updateQuantity (){
        try {
                ordersController.updateProductQuantity(12 , 50);

        }catch (Exception e){
            fail("Exception "+ e);
        }

        assertEquals(new Integer(50), order2.getProductsAndQuantity().get(p));
    }

    @Test
    public void removeProduct (){
        assertTrue(order2.getProductsAndQuantity().keySet().size()==2);

        try {
            ordersController.removeFromOrder(15 ,14458727 );
        } catch (Exception e){
            fail("Exception "+ e);
        }

        assertEquals(order2.getProductsAndQuantity().keySet().size() , 1);
        assertTrue(order2.getProductsAndQuantity().keySet().contains(p));
    }

    @Test
    public void createOrder(){
        assertFalse(ordersController.getOrders().isEmpty());
    }

    @Test
    public void removeOrder (){
        ordersController.createOrder();
        ordersController.addProductToOrder(14458727 , 15 , 10);

        Order order3 = ordersController.getOrders().getLast();

        assertTrue(ordersController.getOrders().contains(order3));
        assertEquals(3 ,ordersController.getOrders().size());

        try{
            ordersController.removeOrder();
        }catch (Exception e){
            fail("Exception "+ e);
        }
        assertFalse(ordersController.getOrders().contains(order3));
        assertEquals(2 ,ordersController.getOrders().size());
    }

    @Test
    public void addProductToOrder(){
        Product p3 = facadeController.createNewProduct(18 , "Pepper" , 8 , "Meshek77" , "Vegetables" , 27718);
        facadeController.addProductToContract(14458727 ,p3);

        try{
            ordersController.addProductToOrder(14458727 ,18 , 30 );
        }catch (Exception e){
            fail("Exception "+ e);
        }

        assertTrue(ordersController.getOrders().getLast().getProductsAndQuantity().containsKey(p3));
        assertEquals(new Integer(30) ,ordersController.getOrders().getLast().getProductsAndQuantity().get(p3));

        ordersController.removeFromOrder(18 , 14458727);
    }
}
