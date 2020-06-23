//import bussines_layer.Branch;
//import bussines_layer.SupplierCard;
//import bussines_layer.SupplierController;
//import bussines_layer.enums.supplierType;
//import bussines_layer.inventory_module.CatalogProduct;
//import bussines_layer.inventory_module.GeneralProduct;
//import bussines_layer.supplier_module.Contract;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.util.LinkedList;
//import static junit.framework.TestCase.*;
//import static org.junit.Assert.assertNotEquals;
//
//
//public class SupplierCardTest {
//    Branch branch = new Branch(2 , "BeerSheva");
//    SupplierController supplierController= SupplierController.getInstance();
//    SupplierCard supplierCard;
//    GeneralProduct gp ;
//    GeneralProduct gp3;
//    CatalogProduct p;
//    CatalogProduct p3;
//
//    @BeforeEach
//    public void setUp(){
//        LinkedList<String> contact = new LinkedList();
//        contact.add("Moshe");
//        contact.add("Rachel");
//        LinkedList<String> category = new LinkedList<>();
//        category.add("Dairy");
//        category.add("Vegetables");
//
//        supplierCard = new SupplierCard("halavi-lee" , "ringelbloom 97, beer-sheva" , "halavi@gmail.com" , "08- 1234567" , 14458727  , "0975635" , "CreditCard" , contact , supplierType.byOrder );
//
//        gp = new GeneralProduct("Tnuva" , "milk" , (float)7.0 , (float)6.0 , 5 , 12 , 121 , 14458727 , "Dairy" , 2 , (float) 1.0);
//
//        p = new CatalogProduct(2 , 12 ,121 , 7.0 ,14458727, "milk" , 7 , "Tnuva" , "Dairy" , 27784);
//        Integer branch_id,Integer catalogID, Integer gpID, Float supplier_price, Integer supplier_id, String supplier_category , String name , Float weight) {
//        p3 = new CatalogProduct(18 , "Pepper" , 8 , "Meshek77" , "Vegetables" , 27718);
//
//        supplierController.addProductToContract(14458727 ,p);
//        supplierController.addProductToContract(14458727 ,p3);
//
//
//        Contract contract = new Contract(category , 14458727 , "by Order");
//
//        SupplierCard supplier , Integer contractID , Integer branchID , LinkedList<String> categories){
//
//        }
//
//    @AfterEach
//    public void tearDown(){
//        supplierController.deleteSupplierCard(14458727);
//    }
//
//    @Test
//    public void testName(){
//        try{
//            supplierController.ChangeSupplierName(14458727 , "Zoe");
//        }catch (Exception e){
//            fail("Exception "+ e);
//        }
//
//        assertEquals(supplierCard.getSupplierName(),"Zoe");
//        assertNotEquals(supplierCard.getSupplierName(), "halavi-lee");
//    }
//
//    @Test
//    public void testAddress(){
//        try{
//            supplierController.ChangeAddress(14458727 ,"TelAviv");
//        }catch (Exception e){
//            fail("Exception "+ e);
//        }
//        assertEquals(supplierCard.getAddress(),"TelAviv");
//        assertNotEquals(supplierCard.getAddress(), "ringelbloom 97, beer-sheva");
//    }
//
//    @Test
//    public void testAddCategory(){
//        Contract contract= supplierCard.getContract();
//
//        try{
//            supplierController.addCategory(14458727 ,"Meat");
//        }catch (Exception e){
//            fail("Exception "+ e);
//        }
//        assertTrue(contract.getCategory().contains("Meat"));
//    }
//
//    @Test
//    public void testDeleteCategory(){
//        Contract contract = supplierCard.getContract();
//        try{
//            supplierController.deleteCategory(14458727 ,"Dairy");
//        }catch (Exception e){
//            fail("Exception "+ e);
//        }
//        assertFalse(contract.getCategory().contains("Dairy"));
//    }
//
//    @Test
//    public void testAddProductToContract(){
//
//        Product p2 = new Product(15 , "yogurt" , 12 , "Tnuva" , "Dairy" , 27845);
//        assertFalse(supplierCard.getContract().getProducts().isEmpty());
//        assertFalse(supplierCard.getContract().getProducts().contains(p2));
//        supplierController.addProductToContract(14458727 ,p2);
//        assertTrue(supplierCard.getContract().getProducts().contains(p2));
//
//    }
//
//    @Test
//    public void testDeleteProductFromContract(){
//
//        assertFalse(supplierCard.getContract().getProducts().isEmpty());
//
//        try{
//            supplierController.deleteProduct(14458727 ,p3.getCatalogID());
//        }catch (Exception e){
//            fail("Exception "+ e);
//        }
//
//        assertFalse(supplierCard.getContract().getProducts().contains(p3));
//    }
//
//    @Test
//    public void testAddProductToCostEngineering(){
//
//        Product p4 = new Product(27 , "Banana" , 15 , "Meshek77" , "Vegetables" , 27568);
//        supplierController.addProductToContract(14458727 ,p4);
//        supplierController.createCostEngineering(14458727);
//        try{
//            supplierController.addProductToCostEng(14458727,27568 , 20 , 5);
//        }catch (Exception e){
//            fail("Exception "+ e);
//        }
//        assertFalse(supplierCard.getCostEngineering().getMinQuntity().isEmpty());
//        assertTrue(supplierCard.getCostEngineering().getMinQuntity().containsKey(27568));
//        assertEquals(new Integer(20) , supplierCard.getCostEngineering().getMinQuntity().get(27568));
//
//    }
//
//
//
//}
