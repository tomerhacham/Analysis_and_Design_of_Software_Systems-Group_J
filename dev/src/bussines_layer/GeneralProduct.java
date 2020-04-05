package bussines_layer;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GeneralProduct {
    //fields
    private String manufacture;
    private String catalogID;
    private String name;
    private Float supplier_price;
    private Float retail_price;
    private Integer quantity;
    private Integer min_quantity;
    private List<SpecificProduct> products;
    private final IDAllocator id_allocator;

    //Constructor
    public GeneralProduct(String manufacture, String catalogID, String name, Float supplier_price, Float retail_price, Integer quantity, Integer min_quantity, IDAllocator id_allocator)
    {
        this.manufacture = manufacture;
        this.catalogID = catalogID;
        this.name = name;
        this.supplier_price = supplier_price;
        this.retail_price = retail_price;
        this.quantity = quantity;
        this.min_quantity = min_quantity;
        this.id_allocator=id_allocator;
        this.products = new LinkedList<>();
    }

    //region Getters - Setters

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getSupplier_price() {
        return supplier_price;
    }

    public void setSupplier_price(Float supplier_price) {
        this.supplier_price = supplier_price;
    }

    public Float getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(Float retail_price) {
        this.retail_price = retail_price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMin_quantity() {
        return min_quantity;
    }

    public void setMin_quantity(Integer min_quantity) {
        if(min_quantity>=0)
        this.min_quantity = min_quantity;
    }
    //endregion

    //region Methods
    public Result addProduct(Date expiration_date){
        SpecificProduct product = new SpecificProduct(id_allocator.getNext_id(),Location.warehouse,expiration_date);
        boolean res= products.add(product);
        Result<SpecificProduct> result;
        if(res){
            result = new Result(res, product, "Product added successfully");
        }
        else{
            result = new Result(res,product,"There was a problem in adding the product");
        }
        return result;
    }

    public Result removeProduct(Integer product_id){
        SpecificProduct toRemove = getProductbyID(product_id);
        Result<SpecificProduct> result;
        if (toRemove!=null){
           products.remove(toRemove);
           String msg="Product has been removed from inventory";
           if(lowBoundCheck()){msg="Product has been removed from inventory\n ALERT: low quantity has been reached";}
           result = new Result(true,toRemove,msg);
        }
        else{
            result = new Result(false,toRemove,"There was a problem in removing the product");
        }
        return result;
    }

    private SpecificProduct getProductbyID(Integer id){
        for (SpecificProduct product:products) {
            if(product.getId()==id){
                return product;
            }
        }
        return null;
    }
    private boolean lowBoundCheck(){
        return products.size()==min_quantity;
    }
    //endregion
}
