package data_access_layer.DTO;

import bussines_layer.inventory_module.CatalogProduct;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "catalog_product_in_general_product")
public class catalog_product_in_general_productDTO {
    //fields:
    @DatabaseField(columnName = "catalog_id")
    CatalogProductDTO catalogID;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignColumnName = "GPID", columnName = "GPID")
    GeneralProductDTO generalProduct;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignColumnName = "branch_id", columnName = "branch_id")
    BranchDTO branch;

    //Constructor
    public catalog_product_in_general_productDTO(GeneralProductDTO generalProduct, CatalogProduct catalogProduct) {
        this.catalogID = new CatalogProductDTO(catalogProduct);
        this.generalProduct = generalProduct;
        this.branch = generalProduct.branch_id;
    }

    public catalog_product_in_general_productDTO() {
    }
    //region Methods

    public CatalogProductDTO getCatalogID() {
        return catalogID;
    }

    public GeneralProductDTO getGeneralProduct() {
        return generalProduct;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    //endregion
}