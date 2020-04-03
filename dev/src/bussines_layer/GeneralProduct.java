package bussines_layer;

import javafx.util.Pair;
import java.util.List;

public class GeneralProduct {
    //fields
    private String manufacture;
    private String catalogID;
    private String name;
    private List<Pair<String, Float>> supplier_price; //will be Pair<supplier_name,price>
    private Float retail_price;
    private Integer quantity;
    private Integer min_quantity;
}
