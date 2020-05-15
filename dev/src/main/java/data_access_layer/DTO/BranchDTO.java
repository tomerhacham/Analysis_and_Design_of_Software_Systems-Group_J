package data_access_layer.DTO;

import bussines_layer.Branch;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Branch")
public class BranchDTO {
    //fields:
    @DatabaseField(id=true,columnName = "branch_id")
    Integer branch_id;
    @DatabaseField(columnName = "name")
    String name;

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
