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
        inventory.addGeneralProduct(((Category)res_cat_30.getData()).getId(), "Niguvim", "174", "Toilet paper double layer 30u", 18.5f, 31.5f, 1, 1);
        inventory.addSpecificProduct("174", convertStringToDate("13/07/2025"), 1);
    }
}
