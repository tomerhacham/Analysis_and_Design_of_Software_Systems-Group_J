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
    Integer catalog_id;
    @DatabaseField(foreign = true, foreignColumnName = "branch_id", foreignAutoRefresh = true, columnName = "branch_id")
    BranchDTO branch;
    @DatabaseField(foreign = true, foreignColumnName = "GPID", foreignAutoRefresh = true, columnName = "GPID")
    GeneralProductDTO generalProduct;
    @DatabaseField(columnName = "supplier_price")
    Float supplier_price;
    @DatabaseField(columnName = "category")
    String category;

    //Constructor
    public catalog_product_in_contractDTO(ContractDTO contract, Integer catalog_id, String category,BranchDTO branch, GeneralProductDTO generalProduct, Float supplier_price) {
        this.contract = contract;
        this.catalog_id = catalog_id;
        this.category=category;
        this.branch = branch;
        this.generalProduct = generalProduct;
        this.supplier_price = supplier_price;
    }
    public catalog_product_in_contractDTO(ContractDTO contract, CatalogProduct catalogProduct, GeneralProductDTO generalProduct){
        this.contract=contract;
        this.catalog_id=catalogProduct.getCatalogID();
        this.category=catalogProduct.getSupplierCategory();
        this.branch=contract.getBranch();
        this.generalProduct=generalProduct;
        this.supplier_price=catalogProduct.getSupplierPrice();
    }
    public catalog_product_in_contractDTO(){
    }

    //region Methods

    public ContractDTO getContract() {
        return contract;
    }

    public Integer getCatalog_id() {
        return catalog_id;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    public GeneralProductDTO getGeneralProduct() {
        return generalProduct;
    }

    public Float getSupplier_price() {
        return supplier_price;
    }

    public String getCategory() {
        return category;
    }

    //endregion

}

