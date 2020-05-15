package data_access_layer.DTO;

import bussines_layer.inventory_module.CatalogProduct;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "CatalogProduct")
public class CatalogProductDTO {
    //fields:
    @DatabaseField(id=true,columnName = "catalog_id")
    Integer catalogID;
    @DatabaseField(columnName = "GPID")
    Integer gpID;
    @DatabaseField(columnName = "supplier_price")
    Float supplier_price;
    @DatabaseField(columnName = "supplier_id")
    Integer supplier_id;
    @DatabaseField(columnName = "supplier_category")
    String supplier_category;
    @DatabaseField(columnName = "name")
    String name;

    //Constructor
    public CatalogProductDTO(CatalogProduct catalogProduct) {
        this.catalogID=catalogProduct.getCatalogID();
        this.gpID=catalogProduct.getGpID();
        this.supplier_price=catalogProduct.getSupplierPrice();
        this.supplier_id=catalogProduct.getSupplierId();
        this.supplier_category=catalogProduct.getSupplierCategory();
        this.name=catalogProduct.getName();
    }
    public CatalogProductDTO() {
    }

    //region Methods

    public Integer getCatalogID() {
        return catalogID;
    }

    public Integer getGpID() {
        return gpID;
    }

    public Float getSupplier_price() {
        return supplier_price;
    }

    public Integer getSupplier_id() {
        return supplier_id;
    }

    public String getSupplier_category() {
        return supplier_category;
    }

    public String getName() {
        return name;
    }

    //endregion
}
