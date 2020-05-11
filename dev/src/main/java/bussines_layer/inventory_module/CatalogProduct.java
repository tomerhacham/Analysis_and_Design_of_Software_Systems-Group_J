package bussines_layer.inventory_module;

public class CatalogProduct {

    //fields
    private final String catalogID;
    private final Integer gpID;
    private Float supplier_price;
    private final Integer supplier_id;
    private String supplier_category;

    public CatalogProduct(String catalogID, Integer gpID, Float supplier_price, Integer supplier_id, String supplier_category) {
        this.catalogID = catalogID;
        this.gpID = gpID;
        this.supplier_price = supplier_price;
        this.supplier_id = supplier_id;
        this.supplier_category = supplier_category;
    }

    //region Getters & setters
    public String getCatalogID() {
        return catalogID;
    }

    public Integer getGpID() {
        return gpID;
    }

    public Float getSupplierPrice() {
        return supplier_price;
    }

    public void setSupplierPrice(Float supplier_price) {
        this.supplier_price = supplier_price;
    }

    public Integer getSupplierId() {
        return supplier_id;
    }

    public String getSupplierCategory() {
        return supplier_category;
    }

    public void setSupplierCategory(String supplier_category) {
        this.supplier_category = supplier_category;
    }
    //endregion
}
