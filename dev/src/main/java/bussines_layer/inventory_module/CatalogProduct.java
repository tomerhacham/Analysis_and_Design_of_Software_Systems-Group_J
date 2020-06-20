package bussines_layer.inventory_module;

import data_access_layer.DTO.CatalogProductDTO;

public class CatalogProduct {

    //fields
    private Integer branch_id;
    private final Integer catalogID;
    private final Integer gpID;
    private Float supplier_price;
    private final Integer supplier_id;
    private String supplier_category;
    private String name;
    private Float weight;

    public CatalogProduct(Integer branch_id,Integer catalogID, Integer gpID, Float supplier_price, Integer supplier_id, String supplier_category , String name , Float weight) {
        this.branch_id=branch_id;
        this.catalogID = catalogID;
        this.gpID = gpID;
        this.supplier_price = supplier_price;
        this.supplier_id = supplier_id;
        this.supplier_category = supplier_category;
        this.name = name;
        this.weight = weight;
    }
    public CatalogProduct(CatalogProductDTO catalogProductDTO){                 //TODO - add to DTO weight and add to the constructor
        this.branch_id=catalogProductDTO.getBranch_id().getBranch_id();
        this.catalogID=catalogProductDTO.getCatalogID();
        this.gpID=catalogProductDTO.getGpID();
        this.supplier_price=catalogProductDTO.getSupplier_price();
        this.supplier_id=catalogProductDTO.getSupplier_id();
        this.supplier_category=catalogProductDTO.getSupplier_category();
        this.name=catalogProductDTO.getName();

    }

    //region Getters & setters
    public Integer getCatalogID() {
        return catalogID;
    }

    public Integer getGpID() {
        return gpID;
    }

    public Integer getBranch_id() {
        return branch_id;
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

    public String getName() {
        return name;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public void setSupplierCategory(String supplier_category) {
        this.supplier_category = supplier_category;
    }
    //endregion


    @Override
    public String toString() {
        return
                "catalogID=" + catalogID +
                ", gpID=" + gpID +
                ", supplier_price=" + supplier_price +
                ", supplier_id=" + supplier_id +
                ", supplier_category='" + supplier_category + '\'' +
                ", name='" + name + '\''
                ;
    }
}
