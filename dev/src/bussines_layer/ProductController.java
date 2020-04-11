package bussines_layer;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ProductController {
    //fields
    private Integer next_id;
    private List<GeneralProduct> generalProducts;

    public ProductController() {
        generalProducts = new LinkedList<>();
        this.next_id = 1;
    }

    //region General Product Managment

    /**
     * remove general product from the list in condition is was removed from the category.
     *
     * @param category  - the specific category the general product belongs to
     * @param catalogID - catalogId of the general product
     * @return
     */
    public Result removeGeneralProduct(Category category, String catalogID) {
        GeneralProduct toRemove = searchGeneralProductbyCatalogID(catalogID);
        Result result;
        if (toRemove != null) {
            result = category.removeGeneralProduct(toRemove);
            if (result.isOK()) {
                generalProducts.remove(toRemove);
            }
        } else {
            result = new Result<String>(false, catalogID, "Could not find general product by it catalogID");
        }
        return result;
    }

    /**
     * add the general product the the list in condition the associate with the category was successful
     *
     * @param category       - the sub-sub category the general product belongs to
     * @param manufacture
     * @param catalogID
     * @param name
     * @param supplier_price
     * @param retail_price
     * @param min_quantity
     * @return
     */
    public Result addGeneralProduct(Category category, String manufacture, String catalogID, String name, Float supplier_price, Float retail_price, Integer min_quantity) {
        GeneralProduct newProduct = new GeneralProduct(manufacture, catalogID, name, supplier_price, retail_price, min_quantity);
        Result result = category.addGeneralProduct(newProduct);
        if (result.isOK()) {
            generalProducts.add(newProduct);
        }
        return result;
    }

    public Result editGeneralProduct_name(String catalogID, String new_name) {
        GeneralProduct toEdit = searchGeneralProductbyCatalogID(catalogID);
        Result result;
        if (toEdit != null) {
            toEdit.setName(new_name);
            result = new Result<GeneralProduct>(true, toEdit, "Name of general product has been changed");
        } else {
            result = new Result<String>(false, catalogID, "Could not find general product");
        }
        return result;
    }

    public Result editGeneralProduct_supplier_price(String catalogID, Float new_supplier_price) {
        GeneralProduct toEdit = searchGeneralProductbyCatalogID(catalogID);
        Result result;
        if (toEdit != null) {
            toEdit.setSupplier_price(new_supplier_price);
            result = new Result<GeneralProduct>(true, toEdit, "Supplier price of general product has been changed");
        } else {
            result = new Result<String>(false, catalogID, "Could not find general product");
        }
        return result;
    }

    public Result editGeneralProduct_retail_price(String catalogID, Float new_retail_price) {
        GeneralProduct toEdit = searchGeneralProductbyCatalogID(catalogID);
        Result result;
        if (toEdit != null) {
            toEdit.setSupplier_price(new_retail_price);
            result = new Result<GeneralProduct>(true, toEdit, "Retail price of general product has been changed");
        } else {
            result = new Result<String>(false, catalogID, "Could not find general product");
        }
        return result;
    }

    public Result editGeneralProduct_quantity(String catalogID, Integer new_quantity) {
        GeneralProduct toEdit = searchGeneralProductbyCatalogID(catalogID);
        Result result;
        if (toEdit != null) {
            toEdit.setQuantity(new_quantity);
            result = new Result<GeneralProduct>(true, toEdit, "Quantity price of general product has been changed");
        } else {
            result = new Result<String>(false, catalogID, "Could not find general product");
        }
        return result;
    }

    public Result editGeneralProduct_min_quantity(String catalogID, Integer new_min_quantity) {
        GeneralProduct toEdit = searchGeneralProductbyCatalogID(catalogID);
        Result result;
        if (toEdit != null) {
            toEdit.setMin_quantity(new_min_quantity);
            result = new Result<GeneralProduct>(true, toEdit, "Min quantity price of general product has been changed");
        } else {
            result = new Result<String>(false, catalogID, "Could not find general product");
        }
        return result;
    }
    //endregion

    //region Specific Product Managment

    /**
     * add specific product to the general product list.
     *
     * @param catalogID       - catalog id of the general product
     * @param expiration_date - of the specific product or the batch of products
     * @param quantity        - how many specific product with the SAME expiration date
     * @return
     */
    public Result addSpecificProduct(String catalogID, Date expiration_date, Integer quantity) {
        GeneralProduct generalProduct = searchGeneralProductbyCatalogID(catalogID);
        Result result = null;
        String msg = "";
        if (generalProduct != null) {
            for (int i = 0; i < quantity; i++) {
                result = generalProduct.addProduct(getNext_id(), expiration_date);
                msg = msg.concat(result.getMessage().concat("\n"));
            }
            result.setMessage(msg);
        } else {
            result = new Result<String>(false, catalogID, "Could not find general product with the same catalog ID");
        }
        return result;
    }

    /**
     * remove the occurrence of the specific product from the list of the general product
     *
     * @param specific_product_id - specific product id (allocated by the productController)
     * @return
     */
    public Result removeSpecificProduct(Integer specific_product_id) {
        GeneralProduct generalProduct = searchGeneralProductbySpecificProductID(specific_product_id);
        Result result = null;
        if (generalProduct != null) {
            result = generalProduct.removeProduct(specific_product_id);
        } else {
            result = new Result<Integer>(false, specific_product_id, "Could not find general product with the same catalog ID");
        }
        return result;
    }

    /**
     * search the relevant general product and deligate the responsibility to mark the specific product
     *
     * @param specific_product_id-the id of the spedific product (allocated by the product controller)
     * @return
     */
    public Result markAsFlaw(Integer specific_product_id) {
        GeneralProduct generalProduct = searchGeneralProductbySpecificProductID(specific_product_id);
        Result result = null;
        if (generalProduct != null) {
            result = generalProduct.markAsFlaw(specific_product_id);
        } else {
            result = new Result<Integer>(false, specific_product_id, "Could not find general product with the same catalog ID");
        }
        return result;
    }

    /**
     * finds the general product of the specific product and delegate responsibility to the general product
     *
     * @param specific_product_id-the id of the spedific product (allocated by the product controller)
     * @return
     */
    public Result moveLocation(Integer specific_product_id) {
        GeneralProduct generalProduct = searchGeneralProductbySpecificProductID(specific_product_id);
        Result result = null;
        if (generalProduct != null) {
            result = generalProduct.moveLocation(specific_product_id);
        } else {
            result = new Result<Integer>(false, specific_product_id, "Could not find general product with the same catalog ID");
        }
        return result;
    }
    //endregion

    //region Generals

    /**
     * search general product by catalogId, if not found return null
     *
     * @param catalogID
     * @return
     */
    public GeneralProduct searchGeneralProductbyCatalogID(String catalogID) {
        for (GeneralProduct product : generalProducts) {
            if (product.getCatalogID().equals(catalogID)) {
                return product;
            }
        }
        return null;
    }

    private GeneralProduct searchGeneralProductbySpecificProductID(Integer product_id) {
        for (GeneralProduct generalProduct : generalProducts) {
            if (generalProduct.typeOf(product_id)) {
                return generalProduct;
            }
        }
        return null;
    }

    /**
     * allocate the next id available for Specific product
     *
     * @return
     */
    private Integer getNext_id() {
        Integer ret = next_id;
        this.next_id++;
        return ret;
    }
    //endregion

    @Override
    public String toString(){
        String toReturn = "General Product:\n";
        for (GeneralProduct product:generalProducts){
            toReturn=toReturn.concat(product.print());
        }
        return toReturn;
    }

}
