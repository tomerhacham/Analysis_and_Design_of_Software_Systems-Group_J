package data_access_layer.DTO;

import bussines_layer.inventory_module.CatalogProduct;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "catalog_product_in_contract")
public class catalog_product_in_contractDTO {
    //fields:
    @DatabaseField(foreign = true,foreignColumnName ="contract_id",foreignAutoRefresh = true,columnName = "contract_id")
    ContractDTO contract;
    @DatabaseField(columnName = "catalog_id")
    CatalogProductDTO catalog_id;
    @DatabaseField(foreign = true, foreignColumnName = "branch_id", foreignAutoRefresh = true, columnName = "branch_id")
    BranchDTO branch;

    //Constructor
    public catalog_product_in_contractDTO(ContractDTO contract, CatalogProduct catalogProduct){
        this.contract=contract;
        this.catalog_id=new CatalogProductDTO(catalogProduct);
        this.branch=contract.getBranch();
    }
    public catalog_product_in_contractDTO(){
    }

    //region Methods

    public ContractDTO getContract() {
        return contract;
    }

    public CatalogProductDTO getCatalog_id() {
        return catalog_id;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    //endregion

}

