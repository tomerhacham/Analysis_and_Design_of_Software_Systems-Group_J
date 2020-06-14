package data_access_layer.DTO;

import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.supplier_module.Contract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "catalog_product_in_contract")
public class catalog_product_in_contractDTO {
    //fields:
    @DatabaseField(columnName = "contract_id")
    Integer contract_id;
    @DatabaseField(columnName = "catalog_id")
    Integer catalog_id;
    @DatabaseField(foreign = true, foreignColumnName = "branch_id", foreignAutoRefresh = true, columnName = "branch_id")
    BranchDTO branch;

    //Constructor
    public catalog_product_in_contractDTO(Contract contract, CatalogProduct catalogProduct){
        this.contract_id = contract.getContractID();
        this.catalog_id = catalogProduct.getCatalogID();
        this.branch = new BranchDTO(contract.getBranchID());
    }
    public catalog_product_in_contractDTO(){}

    //region Methods

    public Integer getContract_id() {
        return contract_id;
    }

    public Integer getCatalog_id() {
        return catalog_id;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    //endregion

}

