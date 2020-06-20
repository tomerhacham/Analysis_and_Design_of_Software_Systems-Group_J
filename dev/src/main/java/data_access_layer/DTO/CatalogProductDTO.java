package data_access_layer.DTO;

import bussines_layer.inventory_module.CatalogProduct;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "CatalogProduct")
public class CatalogProductDTO {
    //fields:
    @DatabaseField(columnName = "catalog_id")
    Integer catalogID;
    @DatabaseField(columnName = "GPID")
    Integer gpID;
    @DatabaseField(foreign = true,foreignAutoRefresh = true ,foreignColumnName = "branch_id",columnName = "branch_id")
    BranchDTO branch_id;
    @DatabaseField(columnName = "supplier_price")
    Float supplier_price;
    @DatabaseField(columnName = "supplier_id")
    Integer supplier_id;
    @DatabaseField(columnName = "supplier_category")
    String supplier_category;
    @DatabaseField(columnName = "name")
    String name;
    @DatabaseField(columnName = "weight")
    Float weight;

    //Constructor
    public CatalogProductDTO(CatalogProduct catalogProduct) {
        this.catalogID=catalogProduct.getCatalogID();
        this.gpID=catalogProduct.getGpID();
        this.branch_id=new BranchDTO(catalogProduct.getBranch_id());
        this.supplier_price=catalogProduct.getSupplierPrice();
        this.supplier_id=catalogProduct.getSupplierId();
        this.supplier_category=catalogProduct.getSupplierCategory();
        this.name=catalogProduct.getName();
        this.weight=catalogProduct.getWeight();
    }
    public CatalogProductDTO() {
    }

    //region Methods

    public BranchDTO getBranch_id() {
        return branch_id;
    }

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

    public Float getWeight() {
        return weight;
    }

    //endregion
}
