package data_access_layer.DTO;

import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "catalog_product_in_general_product")
public class catalog_product_in_general_productDTO {
    //fields:
    @DatabaseField(columnName = "catalog_id")
    Integer catalogID;
    @DatabaseField(columnName = "GPID")
    Integer generalProduct;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignColumnName = "branch_id", columnName = "branch_id")
    BranchDTO branch;

    //Constructor
    public catalog_product_in_general_productDTO(CatalogProduct catalogProduct) {
        this.catalogID = catalogProduct.getCatalogID();
        this.generalProduct = catalogProduct.getGpID();
        this.branch = new BranchDTO(catalogProduct.getBranch_id());
    }
    public catalog_product_in_general_productDTO(){}

    //region Methods

    public Integer getCatalogID() {
        return catalogID;
    }

    public Integer getGeneralProduct() {
        return generalProduct;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    //endregion
}