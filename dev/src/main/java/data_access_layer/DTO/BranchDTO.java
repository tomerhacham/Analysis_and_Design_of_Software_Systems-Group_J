package data_access_layer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Branch")
public class BranchDTO {
    //fields:
    @DatabaseField(id=true)
    Integer branch_id;

    //Constructor
    public BranchDTO(Integer branch_id) {
        this.branch_id = branch_id;
    }
    public BranchDTO() {}

    //region Methods
    public Integer getBranch_id() {
        return branch_id;
    }
    //endregion
}
