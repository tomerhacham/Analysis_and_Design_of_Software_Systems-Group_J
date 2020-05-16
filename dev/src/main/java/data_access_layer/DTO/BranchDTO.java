package data_access_layer.DTO;

import bussines_layer.Branch;
import bussines_layer.supplier_module.Order;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Branch")
public class BranchDTO {
    //fields:
    @DatabaseField(id=true,columnName = "branch_id")
    Integer branch_id;
    @DatabaseField(columnName = "name")
    String name;
    @ForeignCollectionField(eager = false,foreignFieldName = "branch_id")
    ForeignCollection<CategoryDTO> categories;
    @ForeignCollectionField(eager =false, foreignFieldName ="branch_id")
    ForeignCollection<OrderDTO> orders;
    @ForeignCollectionField(eager=false, foreignFieldName = "branch_id")
    ForeignCollection<CatalogProductDTO> catalogProducts;

    //Constructor
    public BranchDTO(Integer branch_id) {
        this.branch_id = branch_id;
        this.name=null;
    }
    public BranchDTO(Branch branch){this.branch_id=branch.getBranchId();this.name=branch.getName();}
    public BranchDTO() {}

    //region Methods
    public Integer getBranch_id() {
        return branch_id;
    }

    public String getName() {
        return name;
    }
    //endregion
}
