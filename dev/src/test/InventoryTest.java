package test;

import bussines_layer.*;
import bussines_layer.inventory_module.*;
import javafx.util.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import presentation_layer.CLController;


import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    static private BranchController branchController;

    @BeforeAll
    static void setUp(){
        initialize();
        branchController.switchBranch(1);
    }

    @Test
    void addMainCategory() {
        Result result = branchController.addMainCategory("Main_cat_test");
        assertTrue(result.isOK());
        branchController.removeCategory(((Category)result.getData()).getId());
    }

    @Test
    void addSubCategory() {
        Category dummy_main = (Category)(branchController.addMainCategory("Main_cat_test")).getData();
        Result result_sub = branchController.addSubCategory(dummy_main.getId(),"Sub_cat_test");
        assertTrue(result_sub.isOK());
        branchController.removeCategory(dummy_main.getId());
        branchController.removeCategory(((Category)result_sub.getData()).getId());
    }

    @Test
    void removeCategory() {
        Category dummy_main = (Category)(branchController.addMainCategory("Main_cat_test")).getData();
        Category dummy_sub = (Category)(branchController.addSubCategory(dummy_main.getId(),"Sub_cat_test")).getData();
        Result result = branchController.removeCategory(dummy_sub.getId());
        assertTrue(result.isOK());
        result = branchController.removeCategory(dummy_main.getId());
        assertTrue(result.isOK());
    }

    @Test
    void editCategoryname() {
        Category dummy_main = (Category)(branchController.addMainCategory("Main_cat_test")).getData();
        branchController.editCategoryName(dummy_main.getId(),"Main_cat_edit");
        assertEquals(dummy_main.getName(),"Main_cat_edit");
        branchController.removeCategory(dummy_main.getId());
    }


    @Test
    void markAsFlaw() {
        Result result = branchController.markAsFlaw(1);
        assertTrue(result.isOK());
        assertTrue(((SpecificProduct)result.getData()).isFlaw());
    }

    @Test
    void moveLocation() {
        Result result = branchController.moveLocation(1);
        assertTrue(result.isOK());
        assertEquals(((SpecificProduct)result.getData()).getLocation().name(),"store");
    }

    @AfterAll
    static void tearDown(){
        BranchController.clearDB();
    }

    public static void initialize() {
        System.err.println("Warning: all of the data in the DB will be erase\n");
        BranchController.clearDB();
        branchController = new BranchController(true);
        branchController.loadID();
        // Open and switch to initial branch
        Branch initialBranch = branchController.createNewBranch("Initial branch").getData();
        branchController.switchBranch(initialBranch.getBranchId());

        //Add categories:
        //              Hygiene
        //                  Toilet Paper
        //                      30 units
        //                  Shampoo
        //                      500ml
        //                      750ml
        //              Meat%Fish
        //                  Meat
        //                      1/2kg
        //                      1kg
        //                  Fish
        //                      1/2kg
        //                      1kg
        Result res_cat_hygiene = branchController.addMainCategory("Hygiene");
        Result res_cat_tp = branchController.addSubCategory(((Category) res_cat_hygiene.getData()).getId(), "Toilet paper");
        Result res_cat_30 = branchController.addSubCategory(((Category) res_cat_tp.getData()).getId(), "30 units");
        Result res_cat_shampoo = branchController.addSubCategory(((Category) res_cat_hygiene.getData()).getId(), "Shampoo");
        Result res_cat_500ml = branchController.addSubCategory(((Category) res_cat_shampoo.getData()).getId(), "500ml");
        Result res_cat_750ml = branchController.addSubCategory(((Category) res_cat_shampoo.getData()).getId(), "750ml");
        Result res_cat_mnf = branchController.addMainCategory("Meat%Fish");
        Result res_cat_meat = branchController.addSubCategory(((Category) res_cat_mnf.getData()).getId(), "Meat");
        Result res_cat_fish = branchController.addSubCategory(((Category) res_cat_mnf.getData()).getId(), "Fish");
        Result meat_half_kg = branchController.addSubCategory(((Category) res_cat_meat.getData()).getId(), "1/2kg");
        Result meat_one_kg = branchController.addSubCategory(((Category) res_cat_meat.getData()).getId(), "1kg");
        Result fish_half_kg = branchController.addSubCategory(((Category) res_cat_fish.getData()).getId(), "1/2kg");
        Result fish_one_kg = branchController.addSubCategory(((Category) res_cat_fish.getData()).getId(), "1kg");


//-----------------------------------------------------


        Integer supplierID = 1;
        Integer catalogID = 10;
        Integer gpID = 100;
        Float sup_price = 18.5f;
        String name = "Toilet paper double layer 30u";

        LinkedList<String> contact = new LinkedList<>();
        contact.add("Moshe");
        contact.add("Rachel");

        //Create supplier halavi-lee
        branchController.createSupplierCard("halavi-lee", "ringelbloom 97 beer-sheva", "halavi@gmail.com", "081234567",
                supplierID, "0975635", "CreditCard", contact, "self delivery");

        LinkedList<String> categories = new LinkedList<>();
        categories.add("Hygiene");
        categories.add("Meat");

        //create contract
        branchController.addContract(supplierID, categories);

        //Create GP
        branchController.addGeneralProduct(((Category) res_cat_30.getData()).getId(), "Niguvim", name, sup_price, 31.5f, 20, catalogID, gpID, supplierID, "Hygiene", (float) 0.7);
        branchController.addSpecificProduct(gpID, CLController.convertStringToDate("11/04/2025"), 21);
        //Add Product to contract
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Hygiene");

        name = "Crema shampoo for men 500ml";
        sup_price = 15.5f;
        catalogID = 9;
        gpID = 101;
        //Create GP
        branchController.addGeneralProduct(((Category) res_cat_500ml.getData()).getId(), "Crema", name, sup_price, 25.5f, 5, catalogID, gpID, supplierID, "Hygiene", (float) 0.5);
        branchController.addSpecificProduct(gpID, CLController.convertStringToDate("11/04/2025"), 6);
        //Add Product to contract
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Hygiene");

        name = "Dove shampoo for women 500ml";
        sup_price = 15.5f;
        catalogID = 11;
        gpID = 102;
        //Create GP
        branchController.addGeneralProduct(((Category) res_cat_500ml.getData()).getId(), "Dove", name, sup_price, 25.5f, 5, 5, gpID, supplierID, "Hygiene", (float) 0.5);
        branchController.addSpecificProduct(gpID, CLController.convertStringToDate("11/04/2025"), 6);
        //Add Product to contract
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Hygiene");

        name = "Crema shampoo for men 750ml";
        sup_price = 22.0f;
        catalogID = 12;
        gpID = 103;
        //Create GP
        branchController.addGeneralProduct(((Category) res_cat_500ml.getData()).getId(), "Crema", name, sup_price, 32.5f, 5, catalogID, gpID, supplierID, "Hygiene", (float) 0.75);
        branchController.addSpecificProduct(gpID, CLController.convertStringToDate("11/04/2025"), 6);
        //Add Product to contract
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Hygiene");

        String manufacture = "Moosh";
        gpID = 104;
        name = "Moosh packed ground meat 1/2kg";
        sup_price = 30.0f;
        Float ret_price = 40.0f;
        catalogID = 13;
        branchController.addGeneralProduct(((Category) meat_half_kg.getData()).getId(), manufacture, name, sup_price, ret_price, 5, catalogID, gpID, supplierID, "Meat", (float) 0.5);
        branchController.addSpecificProduct(gpID, CLController.convertStringToDate("11/04/2025"), 4);
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Meat");

//------------------------------------- initialize periodic order -----------------------------
        LinkedList<Pair<Integer, Integer>> product_quantity = new LinkedList<>();
        product_quantity.add(new Pair<>(100, 10));
        branchController.createPeriodicOrder(1, product_quantity, 3);    //wednesday
//--------------------------------------------
        gpID = 104;
        name = "Moosh packed ground meat 1/2kg";
        sup_price = 32.5f;
        catalogID = 20;
        supplierID = 2;

        LinkedList<String> contact2 = new LinkedList<>();
        contact.add("Yossi");

        LinkedList<String> categories2 = new LinkedList<>();
        categories2.add("Meat");
        categories2.add("Fish");


        //Create supplier niceToMeat
        branchController.createSupplierCard("niceToMeat", "mesada 37 beer-sheva", "niceToMeat@gmail.com", "087594456",
                supplierID, "09754432", "CreditCard", contact2, "fix days", 5);

        //create contract
        branchController.addContract(supplierID, categories2);

        ////branchController.addGeneralProduct(((Category)meat_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,5,catalogID,gpID,supplierID,"Meat");
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Meat");

        manufacture = "Moosh";
        gpID = 201;
        name = "Moosh packed ground meat 1kg";
        sup_price = 40.0f;
        ret_price = 45.0f;
        catalogID = 24;

        branchController.addGeneralProduct(((Category) meat_one_kg.getData()).getId(), manufacture, name, sup_price, ret_price, 3, catalogID, gpID, supplierID, "Meat", (float) 1);

        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Meat");


        manufacture = "Lakerda";
        gpID = 202;
        name = "Lakerda 1/2kg semi-fresh";
        sup_price = 10.0f;
        ret_price = 13.0f;
        catalogID = 21;
        branchController.addGeneralProduct(((Category) fish_half_kg.getData()).getId(), manufacture, name, sup_price, ret_price, 5, catalogID, gpID, supplierID, "Meat", (float) 0.5);
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Fish");


        manufacture = "Merluza";
        gpID = 203;
        name = "Merluza 1/2kg semi-fresh";
        sup_price = 9.5f;
        ret_price = 12.0f;
        catalogID = 22;
        branchController.addGeneralProduct(((Category) fish_half_kg.getData()).getId(), manufacture, name, sup_price, ret_price, 7, catalogID, gpID, supplierID, "Fish", (float) 0.5);
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Fish");

        manufacture = "Merluza";
        gpID = 204;
        name = "Merluza 1kg semi-fresh";
        sup_price = 15.5f;
        ret_price = 17.0f;
        catalogID = 23;
        branchController.addGeneralProduct(((Category) fish_one_kg.getData()).getId(), manufacture, name, sup_price, ret_price, 10, catalogID, gpID, supplierID, "Fish", (float) 1);
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Fish");
    }
}