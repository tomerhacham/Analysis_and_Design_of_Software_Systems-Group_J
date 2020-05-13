package data_access_layer.DTO;

import bussines_layer.inventory_module.Category;
import bussines_layer.inventory_module.GeneralProduct;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;
@DatabaseTable(tableName = "Category")
public class CategoryDTO {
    //fields
    //fields:
    private String name;
    private Integer id;
    private List<Category> sub_categories;
    private final Integer level;
    private List<GeneralProduct> generalProducts;
}
