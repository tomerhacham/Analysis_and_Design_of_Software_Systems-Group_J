package bussines_layer.inventory_module;

import bussines_layer.Result;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GeneralProduct {
    //fields
    private final Integer gpID;
    private final String manufacture;
    private String name;
    private Float retail_price;
    private Float sale_price;
    private Integer quantity;
    private Integer min_quantity;
    private List<SpecificProduct> products;
    private List<CatalogProduct> catalog_products;

    //Constructor
    public GeneralProduct(String manufacture, String name, Float supplier_price,
                            Float retail_price, Integer min_quantity, Integer catalogID,
                            Integer gpID, Integer supplier_id, String supplier_category)
    {
        this.manufacture = manufacture;
        this.name = name;
        this.retail_price = retail_price;
        this.sale_price=new Float(-1);
        this.quantity = 0;
        this.min_quantity = min_quantity;
        this.products = new LinkedList<>();
        this.gpID = gpID;
        this.catalog_products = new LinkedList<>();
        addCatalogProduct(catalogID, gpID, supplier_price, supplier_id, supplier_category , name);
    }

    //region Getters - Setters

    public String getManufacture() {
        return manufacture;
    }

    public Integer getGpID() {return this.gpID;}

    /*public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getRetailPrice() {
        if (sale_price>-1){
            return sale_price;
        }
        return retail_price;
    }

    public void setRetailPrice(Float retail_price) {
        this.retail_price = retail_price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMinQuantity() {
        return min_quantity;
    }

    public void setMinQuantity(Integer min_quantity) {
        if(min_quantity>=0)
        this.min_quantity = min_quantity;
    }

    //endregion

    //region Methods
    public Result addProduct(Integer product_id, Date expiration_date){
        SpecificProduct product = new SpecificProduct(product_id,Location.warehouse,expiration_date);
        boolean res= products.add(product);
        Result<SpecificProduct> result;
        if(res){
            this.quantity++;
            result = new Result<>(res, product, "Product:"+this.name+"("+product_id+")"+" added successfully");
        }
        else{
            result = new Result<>(res,product,"There was a problem in adding the product:"+product_id );
        }
        return result;
    }

    public Result removeProduct(Integer product_id){
        SpecificProduct toRemove = getProductbyID(product_id);
        Result<SpecificProduct> result;
        if (toRemove!=null){
           products.remove(toRemove);
           this.quantity--;
           String msg="Product " +this.name+"("+product_id+")"+" has been removed from inventory";
           if(lowBoundCheck()){msg="Product has been removed from inventory\n ALERT: low quantity has been reached";}
           result = new Result(true,toRemove,msg);
        }
        else{
            result = new Result(false,toRemove,"There was a problem in removing the product");
        }
        return result;
    }

    public Result markAsFlaw(Integer product_id){
        SpecificProduct product = getProductbyID(product_id);
        Result<SpecificProduct> result;
        if (product!=null){
            product.setFlaw_flag(true);
            result=new Result<>(true,product,"product:"+this.name+"("+product_id+")"+" marked as flaw");
        }
        else{
            result = new Result(false,product,"There was a problem marking the product");
        }
        return result;
    }

    public Result moveLocation(Integer product_id){
        SpecificProduct product = getProductbyID(product_id);
        Result<SpecificProduct> result;
        if (product!=null){
            product.shiftLocation();
            result=new Result<>(true,product,"product:"+this.name+"("+product_id+")"+" moved to the "+product.getLocation().name());
        }
        else{
            result = new Result(false,product,"There was a problem moving the product");
        }
        return result;
    }

    public Result setSale(Float sale_price){
        this.sale_price=sale_price;
        return new Result<GeneralProduct>(true,this,"general product: "+name+"is on sale. new price: "+sale_price);
    }

    public Result cancelSale(){
        this.sale_price=new Float(-1);
        return new Result<GeneralProduct>(true,this,"general product: "+name+"return to retail price: "+retail_price);
    }

    public String report(ReportType type){
        String selfReport="";
        switch(type){
            case InStock:
                Integer store=0;
                Integer warehouse=0;
                for(SpecificProduct product:products){
                    if(product.getLocation().equals(Location.store)){
                        store++;
                    }
                    else{
                        warehouse++;
                    }
                }
                selfReport =toString().concat("\n\t\t\t\t store:"+store+", warehouse:"+warehouse+"\n\n");
                break;

            case OutOfStock:
                if(quantity<min_quantity){
                    selfReport=toString().concat("\n\t").concat("missing "+(min_quantity-quantity)+" to minimum quantity\n\n");
                }
                break;

            case ExpiredDamaged:
                selfReport=this.name.concat("\n");
                String damaged="\t-Damaged items:\n";
                String expired="\t-Expired items:\n";
                Boolean at_least_one=false;
                for(SpecificProduct product:products){
                    if (product.isFlaw()){
                        at_least_one=true;
                        damaged=damaged.concat("\t\t").concat(product.toString()).concat("\n");
                    }
                    if (product.isExpired()){
                        at_least_one=true;
                        expired=expired.concat("\t\t").concat(product.toString()).concat("\n");
                    }
                }
                if(at_least_one) {
                    selfReport = selfReport.concat(damaged).concat(expired);
                }
                else{
                    selfReport="";
                }
        }
        return selfReport;
    }

    /**
     * search if the specific product is type of this general product
     * @param product_id - id of the specific product (allocated by the product controller)
     * @return true/false
     */
    public boolean typeOf(Integer product_id){
        for(SpecificProduct product:products){
            if (product.getId().equals(product_id)) {
                return true;
            }
        }
        return false;
    }

    public boolean setSupplierPrice(Float supplier_price, Integer supplier_id) {
        for (CatalogProduct cp : catalog_products){
            if (cp.getSupplierId().equals(supplier_id)){
                cp.setSupplierPrice(supplier_price);
                return true;
            }
        }
        return false;
    }

    private SpecificProduct getProductbyID(Integer id){
        for (SpecificProduct product:products) {
            if(product.getId().equals(id)){
                return product;
            }
        }
        return null;
    }
    private boolean lowBoundCheck(){
        return products.size()==min_quantity;
    }

    public int quantityToOrder(){
        return (Math.abs(min_quantity-quantity)+5);
    }

    /**
     * Search supplier's category of this general product
     * @param supplier_id - id of requested supplier
     * @return name of category, null if supplier not found
     */
    public String getSupplierCategory(Integer supplier_id) {
        for (CatalogProduct cp : catalog_products){
            if (cp.getSupplierId().equals(supplier_id)){
                return cp.getSupplierCategory();
            }
        }
        return null;
    }

    /**
     * Search supplier id's catalogID of this general product
     * @param supplier_id - id of requested supplier
     * @return catalod ID, null if supplier not found
     */
    public Integer getCatalogID(Integer supplier_id){
        for (CatalogProduct cp : catalog_products){
            if (cp.getSupplierId().equals(supplier_id)){
                return cp.getCatalogID();
            }
        }
        return null;
    }

    /**
     * Search supplier's price of this general product
     * @param supplier_id - id of requested supplier
     * @return price, null if supplier not found
     */
    public Float getSupplierPrice(Integer supplier_id){
        for (CatalogProduct cp : catalog_products){
            if (cp.getSupplierId().equals(supplier_id)){
                return cp.getSupplierPrice();
            }
        }
        return null;
    }

    public CatalogProduct getSupplierCatalogProduct(Integer supplierID){

        for ( CatalogProduct cp: catalog_products  ) {
            if(cp.getSupplierId().equals(supplierID)){
                return cp;
            }
        }
        return null;
    }

    public Result<CatalogProduct> addCatalogProduct(Integer catalogID, Integer gpID, Float supplier_price, Integer supplier_id, String supplier_category , String name){
        CatalogProduct toAdd = new CatalogProduct(catalogID, gpID, supplier_price, supplier_id, supplier_category , name);
        catalog_products.add(toAdd);
        return new Result<>(true,toAdd,"Catalog Product " + name + " of supplier " + supplier_id + " added successfully");
    }


    @Override
    public String toString() {
        return "" +
                "name:'" + name + '\'' +
                ", manufacture:'" + manufacture + '\'' +
                ", gpID:'" + gpID + '\'' +
                ", retail price:" + retail_price +
                ", sale price:" + sale_price +
                ", quantity:" + quantity +
                ", min quantity:" + min_quantity +
                "";
    }
    public String print(){
        String toReturn="\t-"+this.name+" gpID: "+ gpID +"("+products.size()+")\n";
        for(SpecificProduct product:products){
            toReturn=toReturn.concat("\t\t"+product.toString()+"\n");
        }
        return toReturn;
    }

    //endregion
}
