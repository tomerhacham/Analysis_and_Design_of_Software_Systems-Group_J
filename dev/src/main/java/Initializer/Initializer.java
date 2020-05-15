package Initializer;

import bussines_layer.BranchController;
import bussines_layer.inventory_module.Category;
import bussines_layer.Result;
import bussines_layer.supplier_module.Contract;

import java.util.LinkedList;

import static presentation_layer.CLController.convertStringToDate;

public class Initializer {

    public static void initialize(BranchController branchController) {

        //Add categories:
        //              Hygiene
        //                  Toilet Paper
        //                      30 units
        //                  Shampoo
        //                      500ml
        //                      750ml
        //              Meet%Fish
        //                  Meet
        //                      1/2kg
        //                      1kg
        //                  Fish
        //                      1/2kg
        //                      1kg
        Result res_cat_hygiene = branchController.addMainCategory("Hygiene");
        Result res_cat_tp = branchController.addSubCategory(((Category)res_cat_hygiene.getData()).getId(), "Toilet paper");
        Result res_cat_30 = branchController.addSubCategory(((Category)res_cat_tp.getData()).getId(), "30 units");
        Result res_cat_shampoo = branchController.addSubCategory(((Category)res_cat_hygiene.getData()).getId(), "Shampoo");
        Result res_cat_500ml = branchController.addSubCategory(((Category)res_cat_shampoo.getData()).getId(), "500ml");
        Result res_cat_750ml = branchController.addSubCategory(((Category)res_cat_shampoo.getData()).getId(), "750ml");
        Result res_cat_mnf = branchController.addMainCategory("Meet%Fish");
        Result res_cat_meet = branchController.addSubCategory(((Category)res_cat_mnf.getData()).getId(), "Meet");
        Result res_cat_fish = branchController.addSubCategory(((Category)res_cat_mnf.getData()).getId(), "Fish");
        Result meet_half_kg = branchController.addSubCategory(((Category)res_cat_meet.getData()).getId(), "1/2kg");
        Result meet_one_kg = branchController.addSubCategory(((Category)res_cat_meet.getData()).getId(), "1kg");
        Result fish_half_kg = branchController.addSubCategory(((Category)res_cat_fish.getData()).getId(), "1/2kg");
        Result fish_one_kg = branchController.addSubCategory(((Category)res_cat_fish.getData()).getId(), "1kg");


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
        branchController.createSupplierCard("halavi-lee" , "ringelbloom 97 beer-sheva" , "halavi@gmail.com" , "081234567" ,
                supplierID, "0975635" , "CreditCard" , contact,"periodic");

        branchController.addCategory(supplierID, "Hygiene");

        //Create GP
        branchController.addGeneralProduct(((Category)res_cat_30.getData()).getId(), "Niguvim",name, sup_price, 31.5f,  20,catalogID,gpID,supplierID,"Hygiene");
        branchController.addContract(supplierID);
        //Add Product to contract
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,supplierID,((Category)res_cat_hygiene.getData()).getName(),name);

        name = "Crema shampoo for men 500ml";
        sup_price = 15.99f;
        catalogID = 10;
        gpID = 101;
        //Create GP
        branchController.addGeneralProduct(((Category)res_cat_500ml.getData()).getId(), "Crema", name, sup_price, 25.99f, 5,catalogID,gpID,supplierID,"Hygiene");
        //Add Product to contract
        branchController.addContract(supplierID);
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,supplierID,((Category)res_cat_hygiene.getData()).getName(),name);

        name = "Dove shampoo for women 500ml";
        sup_price = 15.99f;
        catalogID = 11;
        gpID = 102;
        //Create GP
        branchController.addGeneralProduct(((Category)res_cat_500ml.getData()).getId(), "Dove", name, sup_price, 25.99f, 5,  5,gpID,supplierID,"Hygiene");
        //Add Product to contract
        branchController.addContract(supplierID);
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,supplierID,((Category)res_cat_hygiene.getData()).getName(),name);

        name = "Crema shampoo for men 750ml";
        sup_price = 22.0f;
        catalogID = 12;
        gpID = 103;
        //Create GP
        branchController.addGeneralProduct(((Category)res_cat_500ml.getData()).getId(), "Crema", name, sup_price, 32.99f, 5,catalogID,gpID,supplierID,"Hygiene");
        //Add Product to contract
        branchController.addContract(supplierID);
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,supplierID,((Category)res_cat_hygiene.getData()).getName(),name);

//-------------------------------------------


        String manufacture = "Moosh";
        gpID = 200;
        name = "Moosh packed ground meet 1/2kg";
        sup_price = 35.f;
        Float ret_price = 40.0f;
        catalogID = 20;
        supplierID = 2;

        LinkedList<String> contact2 = new LinkedList<>();
        contact.add("Yossi");

        //Create supplier niceToMeet
        branchController.createSupplierCard("niceToMeet" , "mesada 37 beer-sheva" , "niceToMeat@gmail.com" , "087594456" ,
                supplierID, "09754432", "CreditCard" , contact2, "byOrder");

        branchController.addCategory(supplierID, "Meet");

        branchController.addGeneralProduct(((Category)meet_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,5,catalogID,gpID,supplierID,"Meet");
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,supplierID,((Category)res_cat_hygiene.getData()).getName(),name);

        manufacture = "Moosh";
        gpID = 201;
        name = "Moosh packed ground meet 1kg";
        sup_price = 40.0f;
        ret_price = 45.0f;
        catalogID = 24;

        branchController.addGeneralProduct(((Category)meet_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,3,catalogID,gpID,supplierID,"Meet");
        branchController.addContract(supplierID);
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,supplierID,((Category)res_cat_hygiene.getData()).getName(),name);


        manufacture = "Lakerda";
        gpID = 202;
        name = "Lakerda 1/2kg semi-fresh";
        sup_price = 10.0f;
        ret_price = 13.0f;
        catalogID = 21;
        branchController.addGeneralProduct(((Category)meet_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,5,catalogID,gpID,supplierID,"Meet");
        branchController.addContract(supplierID);
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,supplierID,((Category)res_cat_hygiene.getData()).getName(),name);


        manufacture = "Merluza";
        gpID = 203;
        name = "Merluza 1/2kg semi-fresh";
        sup_price = 9.5f;
        ret_price = 12.0f;
        catalogID = 22;
        branchController.addGeneralProduct(((Category)meet_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,7,catalogID,gpID,supplierID,"Fish");
        branchController.addContract(supplierID);
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,supplierID,((Category)res_cat_hygiene.getData()).getName(),name);

        manufacture = "Merluza";
        gpID = 204;
        name = "Merluza 1kg semi-fresh";
        sup_price = 15.5f;
        ret_price = 17.0f;
        catalogID = 23;
        branchController.addGeneralProduct(((Category)meet_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,10,catalogID,gpID,supplierID,"Fish");
        branchController.addContract(supplierID);
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,supplierID,((Category)res_cat_hygiene.getData()).getName(),name);

    }
}
