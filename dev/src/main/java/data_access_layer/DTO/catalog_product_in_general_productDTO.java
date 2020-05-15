package data_access_layer.DTO;

import bussines_layer.inventory_module.CatalogProduct;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "catalog_product_in_general_product")
public class catalog_product_in_general_productDTO {
    //fields:
    @DatabaseField(columnName = "catalogID")
    Integer catalogID;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignColumnName = "GPID", columnName = "GPID")
    GeneralProductDTO generalProduct;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignColumnName = "branch_id", columnName = "branch_id")
    BranchDTO branch;
    @DatabaseField(columnName = "name")
    String name;
    @DatabaseField(columnName = "supplier_price")
    Float supplier_price;
    @DatabaseField(columnName = "supplier_category")
    String supplier_category;

    //Constructor
    public catalog_product_in_general_productDTO(GeneralProductDTO generalProduct, CatalogProduct catalogProduct) {
        this.catalogID = catalogID;
        this.generalProduct = generalProduct;
        this.name = catalogProduct.getName();
        this.supplier_price = catalogProduct.getSupplierPrice();
        this.supplier_category = catalogProduct.getSupplierCategory();
        this.branch = generalProduct.branch_id;
    }

    public catalog_product_in_general_productDTO() {
    }
    //region Methods

    public Integer getCatalogID() {
        return catalogID;
    }

    public GeneralProductDTO getGeneralProduct() {
        return generalProduct;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    public String getName() {
        return name;
    }

    public Float getSupplier_price() {
        return supplier_price;
    }

    public String getSupplier_category() {
        return supplier_category;
    }

    //endregion
}