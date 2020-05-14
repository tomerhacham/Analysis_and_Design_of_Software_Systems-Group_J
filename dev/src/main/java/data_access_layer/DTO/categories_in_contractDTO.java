package data_access_layer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categories_in_contract")
public class categories_in_contractDTO {
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignColumnName = "contract_id", columnName = "contract_id")
    ContractDTO contract;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignColumnName = "branch_id", columnName = "branch_id")
    BranchDTO branch;
    @DatabaseField(columnName = "category")
    String category;

    //Constructor
    public categories_in_contractDTO(ContractDTO contract, String category) {
        this.contract = contract;
        this.branch = contract.getBranch();
        this.category = category;
    }
    public categories_in_contractDTO() {    }
    //region Methods
    public ContractDTO getContract() {
        return contract;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    public String getCategory() {
        return category;
    }
    //endregion
}

