package test;

import Initializer.Initializer;
import bussines_layer.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
   Inventory inv = new Inventory();

    @Test
    void addMainCategory() {
        Result result = inv.addMainCategory("Main_cat_test");
        assertTrue(result.isOK());
    }

    @Test
    void addSubCategory() {
        Category dummy_main = (Category)(inv.addMainCategory("Main_cat_test")).getData();
        Result result_sub = inv.addSubCategory(dummy_main.getId(),"Sub_cat_test");
        assertTrue(result_sub.isOK());
    }

    @Test
    void removeCategory() {
        Category dummy_main = (Category)(inv.addMainCategory("Main_cat_test")).getData();
        Category dummy_sub = (Category)(inv.addSubCategory(dummy_main.getId(),"Sub_cat_test")).getData();
        Result result = inv.removeCategory(dummy_sub.getId());
        assertTrue(result.isOK());
    }

    @Test
    void editCategoryname() {
        Category dummy_main = (Category)(inv.addMainCategory("Main_cat_test")).getData();
        Result result = inv.editCategoryname(dummy_main.getId(),"Main_cat_edit");
        assertEquals(dummy_main.getName(),"Main_cat_edit");
    }

    @Test
    void addGeneralProduct() {
        Category dummy_main = (Category)(inv.addMainCategory("Main_cat_test")).getData();
        Category dummy_sub = (Category)(inv.addSubCategory(dummy_main.getId(),"Sub_cat_test")).getData();
        Category dummy_sub_sub = (Category)(inv.addSubCategory(dummy_sub.getId(),"Sub_sub_cat_test")).getData();
        Result result = inv.addGeneralProduct(dummy_sub_sub.getId(),"man","123","testgp",10.5f,10.5f,1);
        assertTrue(result.isOK());
    }

    @Test
    void removeGeneralProduct() {
        Category dummy_main = (Category)(inv.addMainCategory("Main_cat_test")).getData();
        Category dummy_sub = (Category)(inv.addSubCategory(dummy_main.getId(),"Sub_cat_test")).getData();
        Category dummy_sub_sub = (Category)(inv.addSubCategory(dummy_sub.getId(),"Sub_sub_cat_test")).getData();
        GeneralProduct generalProduct =(GeneralProduct) (inv.addGeneralProduct(dummy_sub_sub.getId(),"man","123","testgp",10.5f,10.5f,1)).getData();
        Result result = inv.removeGeneralProduct(dummy_sub_sub.getId(),generalProduct.getCatalogID());
        assertTrue(result.isOK());
    }

    @Test
    void addSpecificProduct() {
        Category dummy_main = (Category)(inv.addMainCategory("Main_cat_test")).getData();
        Category dummy_sub = (Category)(inv.addSubCategory(dummy_main.getId(),"Sub_cat_test")).getData();
        Category dummy_sub_sub = (Category)(inv.addSubCategory(dummy_sub.getId(),"Sub_sub_cat_test")).getData();
        GeneralProduct generalProduct =(GeneralProduct) (inv.addGeneralProduct(dummy_sub_sub.getId(),"man","123","testgp",10.5f,10.5f,1)).getData();
        Result result = inv.addSpecificProduct(generalProduct.getCatalogID(),new Date(),1);
        assertTrue(result.isOK());
        assertTrue(generalProduct.getQuantity()==1);
    }

    @Test
    void removeSpecificProduct() {
        Category dummy_main = (Category)(inv.addMainCategory("Main_cat_test")).getData();
        Category dummy_sub = (Category)(inv.addSubCategory(dummy_main.getId(),"Sub_cat_test")).getData();
        Category dummy_sub_sub = (Category)(inv.addSubCategory(dummy_sub.getId(),"Sub_sub_cat_test")).getData();
        GeneralProduct generalProduct =(GeneralProduct) (inv.addGeneralProduct(dummy_sub_sub.getId(),"man","123","testgp",10.5f,10.5f,1)).getData();
        SpecificProduct specificProduct = (SpecificProduct)(inv.addSpecificProduct(generalProduct.getCatalogID(),new Date(),1)).getData();
        Result result = inv.removeSpecificProduct(specificProduct.getId());
        assertTrue(result.isOK());
        Result another_remove_res = inv.removeSpecificProduct(specificProduct.getId());
        assertFalse(another_remove_res.isOK());
        assertTrue(generalProduct.getQuantity()==0);
    }

    @Test
    void markAsFlaw() {
        Category dummy_main = (Category)(inv.addMainCategory("Main_cat_test")).getData();
        Category dummy_sub = (Category)(inv.addSubCategory(dummy_main.getId(),"Sub_cat_test")).getData();
        Category dummy_sub_sub = (Category)(inv.addSubCategory(dummy_sub.getId(),"Sub_sub_cat_test")).getData();
        GeneralProduct generalProduct =(GeneralProduct) (inv.addGeneralProduct(dummy_sub_sub.getId(),"man","123","testgp",10.5f,10.5f,1)).getData();
        SpecificProduct specificProduct =(SpecificProduct)( inv.addSpecificProduct(generalProduct.getCatalogID(),new Date(),1)).getData();
        Result result = inv.markAsFlaw(specificProduct.getId());
        assertTrue(result.isOK());
        assertTrue(specificProduct.isFlaw());
    }

    @Test
    void moveLocation() {
        Category dummy_main = (Category)(inv.addMainCategory("Main_cat_test")).getData();
        Category dummy_sub = (Category)(inv.addSubCategory(dummy_main.getId(),"Sub_cat_test")).getData();
        Category dummy_sub_sub = (Category)(inv.addSubCategory(dummy_sub.getId(),"Sub_sub_cat_test")).getData();
        GeneralProduct generalProduct =(GeneralProduct) (inv.addGeneralProduct(dummy_sub_sub.getId(),"man","123","testgp",10.5f,10.5f,1)).getData();
        SpecificProduct specificProduct =(SpecificProduct)( inv.addSpecificProduct(generalProduct.getCatalogID(),new Date(),1)).getData();
        Result result = inv.moveLocation(specificProduct.getId());
        assertTrue(result.isOK());
        assertEquals(specificProduct.getLocation().name(),"store");
    }
}