package Initializer;

import bussines_layer.Category;
import bussines_layer.Inventory;
import bussines_layer.Result;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

import static presentation_layer.CLController.convertStringToDate;

public class Initializer {

    public static void initialize(Inventory inventory) {
        Result res_cat_hygiene = inventory.addMainCategory("Hygiene");
        Result res_cat_tp = inventory.addSubCategory(((Category)res_cat_hygiene.getData()).getId(), "Toilet paper");
        Result res_cat_30 = inventory.addSubCategory(((Category)res_cat_tp.getData()).getId(), "30 units");
        inventory.addGeneralProduct(((Category)res_cat_30.getData()).getId(), "Niguvim", "174", "Toilet paper double layer 30u", 18.5f, 31.5f,  20);
        inventory.addSpecificProduct("174", convertStringToDate("13/07/2019"), 13);

        Result res_cat_shampoo = inventory.addSubCategory(((Category)res_cat_hygiene.getData()).getId(), "Shampoo");
        Result res_cat_500ml = inventory.addSubCategory(((Category)res_cat_shampoo.getData()).getId(), "500ml");
        inventory.addGeneralProduct(((Category)res_cat_500ml.getData()).getId(), "Crema", "188", "Crema shampoo for men 500ml", 15.99f, 25.99f,  5);
        inventory.addGeneralProduct(((Category)res_cat_500ml.getData()).getId(), "Dove", "111", "Dove shampoo for women 500ml", 15.99f, 25.99f,  5);
        inventory.addSpecificProduct("188", convertStringToDate("11/04/2025"), 20);
        inventory.addSpecificProduct("111", convertStringToDate("11/04/2019"), 15);


        Result res_cat_750ml = inventory.addSubCategory(((Category)res_cat_shampoo.getData()).getId(), "750ml");
        inventory.addGeneralProduct(((Category)res_cat_750ml.getData()).getId(), "Crema", "189", "Crema shampoo for men 750ml", 22.0f, 32.99f,  4);
        inventory.addGeneralProduct(((Category)res_cat_750ml.getData()).getId(), "Dove", "112", "Dove shampoo for women 750ml", 22.0f, 32.99f,  4);
        inventory.addSpecificProduct("189", convertStringToDate("11/04/2025"), 5);
        inventory.addSpecificProduct("112", convertStringToDate("11/04/2025"), 5);

        Result res_cat_mnf = inventory.addMainCategory("Meet%Fish");
        Result res_cat_meet = inventory.addSubCategory(((Category)res_cat_mnf.getData()).getId(), "Meet");
        Result res_cat_fish = inventory.addSubCategory(((Category)res_cat_mnf.getData()).getId(), "Fish");

        Result meet_half_kg = inventory.addSubCategory(((Category)res_cat_meet.getData()).getId(), "1/2kg");
        Result meet_one_kg = inventory.addSubCategory(((Category)res_cat_meet.getData()).getId(), "1kg");

        Result fish_half_kg = inventory.addSubCategory(((Category)res_cat_fish.getData()).getId(), "1/2kg");
        Result fish_one_kg = inventory.addSubCategory(((Category)res_cat_fish.getData()).getId(), "1kg");

        inventory.addGeneralProduct(((Category)meet_half_kg.getData()).getId(), "Moosh", "100", "Moosh packed ground meet 1/2kg", 35.0f, 40.0f,  3);
        inventory.addGeneralProduct(((Category)meet_one_kg.getData()).getId(), "Moosh", "101", "Moosh packed ground meet 1kg", 45.0f, 50.0f,  3);
        inventory.addSpecificProduct("100", convertStringToDate("11/05/2020"), 12);
        inventory.addSpecificProduct("101", convertStringToDate("11/05/2020"), 1);

        inventory.addGeneralProduct(((Category)fish_half_kg.getData()).getId(), "Lakerda", "120", "Lakerda 1/2kg semi-fresh", 10.5f, 13.5f,  3);
        inventory.addSpecificProduct("120", convertStringToDate("11/05/2019"), 3);

        inventory.addGeneralProduct(((Category)fish_half_kg.getData()).getId(), "Merluza", "121", "Merluza 1/2kg semi-fresh", 9.5f, 13.5f,  3);
        inventory.addSpecificProduct("121", convertStringToDate("11/05/2020"), 3);

        inventory.addGeneralProduct(((Category)fish_one_kg.getData()).getId(), "Lakerda", "130", "Lakerda 1kg semi-fresh", 21.3f, 30.99f,  3);
        inventory.addSpecificProduct("130", convertStringToDate("11/05/2020"), 3);

        inventory.addGeneralProduct(((Category)fish_one_kg.getData()).getId(), "Merluza", "131", "Merluza 1kg semi-fresh", 17.3f, 24.99f,  3);
        inventory.addSpecificProduct("131", convertStringToDate("11/05/2019"), 1);






    }
}
