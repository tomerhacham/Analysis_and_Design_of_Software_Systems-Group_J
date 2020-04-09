import bussines_layer.Category;
import bussines_layer.Inventory;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class Initializer {
    //fields:
    Inventory inv;
    /*String[] crema_500 = {"Crema", "Crema1", "Crema shampoo 500ml", "20.0", "30.0", "10", "1"};
    String[] crema_750 = {"Crema", "Crema11", "Crema shampoo 750ml", "31.0", "45.0", "10", "1"};
    Pair<String,String[]> _500ml_products = new Pair<>("500ml",crema_500);
    Pair<String,String[]> _750ml_products = new Pair<>("750ml",crema_750);
    LinkedList<Pair<String, String[]>> shampoo_sizes = new LinkedList<>();

    LinkedList<Pair<String,String[]>> sub_cat_cnh =new LinkedList<>();
    Pair shampoo= new Pair("Shampoo",shampoo_sizes);

    List<Pair<String,List>> main_categories= new LinkedList<>();
    Pair main_cnh = new Pair("Cleaning&Hygiene",sub_cat_cnh);

     */



    public Initializer(Inventory inv) {
        this.inv = inv;
        /*
        shampoo_sizes.add(_500ml_products);
        shampoo_sizes.add(_750ml_products);
        sub_cat_cnh.add(shampoo);
        main_categories.add(main_cnh);*/
    }
    public void initCategories(){
        /*
        for(Pair main_category:main_categories){
            Category category = (Category)(inv.addMainCategory((String)main_category.getKey()).getData());
            Integer main_cat_id = category.getId();
            List<Pair<String,String[]>> sub_cat = (List) main_category.getValue();
            for(Pair sub_category:sub_cat){
                Category sub = (Category)(inv.addSubCategory(main_cat_id,(String)(sub_category.getValue())).getData());
                Integer sub_cat_id = sub.getId();
                List<Pair<String,String[]>> sub_sub_cat = (List) sub_category.getValue();
                for(Pair sub_sub_category:sub_sub_cat){
                    Category sub_sub = (Category)(inv.addSubCategory(main_cat_id,(String)(sub_category.getValue())).getData());
                    Integer sub_sub_cat_id = sub_sub.getId();
                    String[] products = (String[])sub_sub_category.getValue();
                    for(String[] product:products){

                    }
                }
            }
        }*/
    }
}
