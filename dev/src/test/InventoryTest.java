package test;

import bussines_layer.*;
import bussines_layer.inventory_module.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import presentation_layer.CLController;


import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    static private BranchController bc = new BranchController(false);

    @BeforeAll
    static void setUp(){
        CLController.initialize();
        //Result<Branch> res = bc.createNewBranch("Testing");
        bc.switchBranch(1);
    }

    @Test
    void addMainCategory() {
        Result result = bc.addMainCategory("Main_cat_test");
        assertTrue(result.isOK());
        bc.removeCategory(((Category)result.getData()).getId());
    }

    @Test
    void addSubCategory() {
        Category dummy_main = (Category)(bc.addMainCategory("Main_cat_test")).getData();
        Result result_sub = bc.addSubCategory(dummy_main.getId(),"Sub_cat_test");
        assertTrue(result_sub.isOK());
        bc.removeCategory(dummy_main.getId());
        bc.removeCategory(((Category)result_sub.getData()).getId());
    }

    @Test
    void removeCategory() {
        Category dummy_main = (Category)(bc.addMainCategory("Main_cat_test")).getData();
        Category dummy_sub = (Category)(bc.addSubCategory(dummy_main.getId(),"Sub_cat_test")).getData();
        Result result = bc.removeCategory(dummy_sub.getId());
        assertTrue(result.isOK());
        result = bc.removeCategory(dummy_main.getId());
        assertTrue(result.isOK());
    }

    @Test
    void editCategoryname() {
        Category dummy_main = (Category)(bc.addMainCategory("Main_cat_test")).getData();
        bc.editCategoryName(dummy_main.getId(),"Main_cat_edit");
        assertEquals(dummy_main.getName(),"Main_cat_edit");
        bc.removeCategory(dummy_main.getId());
    }


    @Test
    void markAsFlaw() {
        Result result = bc.markAsFlaw(1);
        assertTrue(result.isOK());
        assertTrue(((SpecificProduct)result.getData()).isFlaw());
    }

    @Test
    void moveLocation() {
        Result result = bc.moveLocation(1);
        assertTrue(result.isOK());
        assertEquals(((SpecificProduct)result.getData()).getLocation().name(),"store");
    }

    @AfterAll
    static void tearDown(){
        BranchController.clearDB();
    }
}