package data_access_layer.DTO;

import bussines_layer.supplier_module.Contract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categories_in_contract")
public class categories_in_contractDTO {
    @DatabaseField(columnName = "contract_id")
    Integer contract_id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignColumnName = "branch_id", columnName = "branch_id")
    BranchDTO branch;
    @DatabaseField(columnName = "category")
    String category;

    //Constructor
    public categories_in_contractDTO(Contract contract, String category) {
        this.contract_id = contract.getContractID();
        this.branch = new BranchDTO(contract.getBranchID());
        this.category = category;
    }
    public categories_in_contractDTO() {}

    //region Methods
    public Integer getContract_id() {
        return contract_id;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "categories_in_contractDTO{" +
                "contract_id=" + contract_id +
                ", branch=" + branch +
                ", category='" + category + '\'' +
                '}';
    }
    //endregion
}

