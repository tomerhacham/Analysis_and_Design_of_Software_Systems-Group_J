package data_access_layer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Cost Engineering")
public class CostEngineeringDTO {
    //fields:
    @DatabaseField(foreign = true, foreignColumnName = "contract_id", foreignAutoRefresh = true, columnName = "contract_id")
    ContractDTO contract;
    @DatabaseField(foreign = true, foreignColumnName = "branch_id", foreignAutoRefresh = true, columnName = "branch_id")
    BranchDTO branch;
    @DatabaseField(columnName = "catalog_id")
    Integer catalog_id;
    @DatabaseField(columnName = "min_quantity")
    Integer min_quantity;
    @DatabaseField(columnName = "discount_price")
    Float discount_price;

    //Constructor
    public CostEngineeringDTO(ContractDTO contract, Integer catalog_id, Integer min_quantity, Float discount_price) {
        this.contract = contract;
        this.branch=contract.getBranch();
        this.catalog_id = catalog_id;
        this.min_quantity = min_quantity;
        this.discount_price = discount_price;
    }
    public CostEngineeringDTO(){}

    //region Methods

    public ContractDTO getContract() {
        return contract;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    public Integer getCatalog_id() {
        return catalog_id;
    }

    public Integer getMin_quantity() {
        return min_quantity;
    }

    public Float getDiscount_price() {
        return discount_price;
    }

    //endregion
}

