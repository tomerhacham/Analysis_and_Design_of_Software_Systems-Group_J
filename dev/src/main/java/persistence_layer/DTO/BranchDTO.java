package persistence_layer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Branch")
public class BranchDTO {
    //fields:
    @DatabaseField(id = true, columnName = "branch_id")
    Integer branch_id;

    //Constructor
    public BranchDTO(Integer branch_id) {
        this.branch_id = branch_id;
    }
    public BranchDTO() {    }

    //region Getters and Setters
    public Integer getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(Integer branch_id) {
        this.branch_id = branch_id;
    }
    //endregion
}
