package data_access_layer.DTO;

import bussines_layer.inventory_module.Category;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Category")
public class CategoryDTO {
    //fields:
    @DatabaseField(columnName = "name")
    String name;
    @DatabaseField(columnName = "category_id")
    Integer id;
    @DatabaseField(columnName = "super_category_id",canBeNull = true)
    Integer super_category;
    @DatabaseField(columnName = "level")
    Integer level;
    @DatabaseField(foreign = true, foreignAutoRefresh = true,foreignColumnName = "branch_id",columnName = "branch_id")
    BranchDTO branch_id;

    /*@ForeignCollectionField(eager = false,foreignFieldName = "category_id")
    ForeignCollection<GeneralProductDTO> generalProducts;
    @ForeignCollectionField(eager=false,foreignFieldName = "super_category_id")
    ForeignCollection<CategoryDTO> sub_categories;*/

    //Constructor
    //for UPDATE situation
    public CategoryDTO(Category category){
        this.super_category=category.getSuper_category_id();
        this.name=category.getName();
        this.id=category.getId();
        this.level=category.getLevel();
        this.branch_id=new BranchDTO(category.getBranch_id());
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

    public Integer getSuper_category() {
        return super_category;
    }

    public Integer getLevel() {
        return level;
    }

    public BranchDTO getBranch_id() {
        return branch_id;
    }
    /*public ForeignCollection<GeneralProductDTO> getGeneralProducts() {
        return generalProducts;
    }

    public ForeignCollection<CategoryDTO> getSub_categories() {
        return sub_categories;
    }*/

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
