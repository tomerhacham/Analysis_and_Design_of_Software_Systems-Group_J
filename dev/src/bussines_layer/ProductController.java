package bussines_layer;

import java.util.LinkedList;
import java.util.List;

public class ProductController {
    //fields
    private Integer next_id;
    private List<GeneralProduct> generalProducts;

    public ProductController(){
        generalProducts=new LinkedList<>();
        this.next_id=1;
    }
    //region Methods

    //region General Product Managment

        /**
         * remove general product from the list in condition is was removed from the category.
         * @param category - the specific category the general product belongs to
         * @param catalogID - catalogId of the general product
         * @return
         */
        public Result removeGeneralProduct(Category category, String catalogID){
            GeneralProduct toRemove = searchGeneralProductbyCatalogID(catalogID);
            Result result;
            if(toRemove!=null){
                result = category.removeGeneralProduct(toRemove);
                if (result.isOK()){
                    generalProducts.remove(toRemove);
                }
            }
            else{
                result=new Result<String>(false,catalogID,"Could not find general product by it catalogID");
            }
            return result;
        }

        /**
         * add the general product the the list in condition the associate with the category was successful
         * @param category - the sub-sub category the general product belongs to
         * @param manufacture
         * @param catalogID
         * @param name
         * @param supplier_price
         * @param retail_price
         * @param quantity
         * @param min_quantity
         * @return
         */
        public Result addGeneralProduct(Category category,String manufacture, String catalogID, String name, Float supplier_price, Float retail_price, Integer quantity, Integer min_quantity){
            GeneralProduct newProduct = new GeneralProduct(manufacture, catalogID, name, supplier_price, retail_price, quantity, min_quantity);
            Result result = category.addGeneralProduct(newProduct);
            if (result.isOK()){
                generalProducts.add(newProduct);
            }
            return result;
        }

        public Result editGeneralProduct_name(String catalogID, String new_name){
            GeneralProduct toEdit = searchGeneralProductbyCatalogID(catalogID);
            Result result;
            if(toEdit!=null){
                toEdit.setName(new_name);
                result=new Result<GeneralProduct>(true,toEdit,"Name of general product has been changed");
            }
            else{
                 result = new Result<String>(false,catalogID,"Could not find general product");
                }
            return result;
        }
        public Result editGeneralProduct_supplier_price(String catalogID, Float new_supplier_price){
            GeneralProduct toEdit = searchGeneralProductbyCatalogID(catalogID);
            Result result;
            if(toEdit!=null){
                toEdit.setSupplier_price(new_supplier_price);
                result=new Result<GeneralProduct>(true,toEdit,"Supplier price of general product has been changed");
            }
            else{
                result = new Result<String>(false,catalogID,"Could not find general product");
            }
            return result;
        }
        public Result editGeneralProduct_retail_price(String catalogID, Float new_retail_price){
            GeneralProduct toEdit = searchGeneralProductbyCatalogID(catalogID);
            Result result;
            if(toEdit!=null){
                toEdit.setSupplier_price(new_retail_price);
                result=new Result<GeneralProduct>(true,toEdit,"Retail price of general product has been changed");
            }
            else{
                result = new Result<String>(false,catalogID,"Could not find general product");
            }
            return result;
        }
        public Result editGeneralProduct_quantity(String catalogID, Integer new_quantity){
            GeneralProduct toEdit = searchGeneralProductbyCatalogID(catalogID);
            Result result;
            if(toEdit!=null){
                toEdit.setQuantity(new_quantity);
                result=new Result<GeneralProduct>(true,toEdit,"Quantity price of general product has been changed");
            }
            else{
                result = new Result<String>(false,catalogID,"Could not find general product");
            }
            return result;
        }
        public Result editGeneralProduct_min_quantity(String catalogID, Integer new_min_quantity){
            GeneralProduct toEdit = searchGeneralProductbyCatalogID(catalogID);
            Result result;
            if(toEdit!=null){
                toEdit.setMin_quantity(new_min_quantity);
                result=new Result<GeneralProduct>(true,toEdit,"Min quantity price of general product has been changed");
            }
            else{
                result = new Result<String>(false,catalogID,"Could not find general product");
            }
            return result;
        }
    //endregion

    //region Specific Product Managment

    //endregion

    /**
     * search general product by catalogId, if not found return null
     * @param catalogID
     * @return
     */
    private GeneralProduct searchGeneralProductbyCatalogID(String catalogID){
        for (GeneralProduct product:generalProducts) {
            if (product.getCatalogID().equals(catalogID)){
                return product;
            }
        }
        return null;
    }

    /**
     * allocate the next id available for Specific product
     * @return
     */
    private Integer getNext_id() {
        Integer ret= next_id;
        this.next_id++;
        return ret;
    }
    //endregion
}
