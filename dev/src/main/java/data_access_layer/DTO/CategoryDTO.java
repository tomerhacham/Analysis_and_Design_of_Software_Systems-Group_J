package data_access_layer.DTO;

import bussines_layer.inventory_module.Category;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable(tableName = "Category")
public class CategoryDTO {
    //fields:
    @DatabaseField(columnName = "name")
    String name;
    @DatabaseField(id=true,columnName = "category_id")
    Integer id;
    @DatabaseField(foreign = true,foreignAutoRefresh = true, columnName = "super_category_id",canBeNull = true)
    CategoryDTO super_category;
    @DatabaseField(columnName = "level")
    Integer level;
    @ForeignCollectionField(eager = false,foreignFieldName = "category_id")
    ForeignCollection<GeneralProductDTO> generalProducts;

    //Constructor
    public CategoryDTO(String name, Integer id, Integer level,CategoryDTO super_category) {
        this.name = name;
        this.id = id;
        this.level = level;
        this.super_category=super_category;
    }
    public CategoryDTO(CategoryDTO super_category, Category category){
        this.super_category=super_category;
        this.name=category.getName();
        this.id=category.getId();
        this.level=category.getLevel();
    }
    public CategoryDTO(Category category,Integer super_category_id){
        CategoryDTO dummy_super_category = new CategoryDTO();
        dummy_super_category.id=super_category_id;
        this.super_category=dummy_super_category;
        this.name=category.getName();
        this.id=category.getId();
        this.level=category.getLevel();
    }
    public CategoryDTO() {
    }


    //region Methods

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public CategoryDTO getSuper_category() {
        return super_category;
    }

    public Integer getLevel() {
        return level;
    }

    public ForeignCollection<GeneralProductDTO> getGeneralProducts() {
        return generalProducts;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", level=" + level +
                '}';
    }
    //endregion
}
