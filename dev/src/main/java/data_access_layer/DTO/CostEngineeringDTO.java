package data_access_layer.DTO;

import bussines_layer.supplier_module.CostEngineering;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Cost Engineering")
public class CostEngineeringDTO {
    //fields:
    @DatabaseField(columnName = "contract_id")
    Integer contract_id;
    @DatabaseField(foreign = true, foreignColumnName = "branch_id", foreignAutoRefresh = true, columnName = "branch_id")
    BranchDTO branch;
    @DatabaseField(columnName = "catalog_id")
    Integer catalog_id;
    @DatabaseField(columnName = "min_quantity")
    Integer min_quantity;
    @DatabaseField(columnName = "discount_price")
    Float discount_price;

    //Constructor
    public CostEngineeringDTO(CostEngineering costEngineering, Integer catalog_id, Integer min_quantity, Float discount_price ){
        this.contract_id =costEngineering.getContract_id() ;
        this.branch = new BranchDTO(costEngineering.getBranch_id());
        this.catalog_id = catalog_id;
        this.min_quantity = min_quantity;
        this.discount_price = discount_price;
    }

    public CostEngineeringDTO(){}

    //region Methods

    public Integer getContract_id() {
        return contract_id;
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

