package bussines_layer.inventory_module;

import bussines_layer.Result;
import data_access_layer.Mapper;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ProductController {
    //fields
    private Integer next_id;
    private List<GeneralProduct> generalProducts;
    private Integer branchId;
    private Mapper mapper;

    public ProductController(Integer branchId) {
        this.mapper=Mapper.getInstance();
        this.generalProducts=mapper.loadGeneralProducts(branchId);
        if(generalProducts.isEmpty()){generalProducts=new LinkedList<>();}
        this.next_id = this.mapper.loadID("product");
        this.branchId = branchId;
    }

    //region General Product Managment

    /**
     * remove general product from the list in condition is was removed from the category.
     *
     * @param category  - the specific category the general product belongs to
     * @param gpID - catalogId of the general product
     * @return
     */
    public Result removeGeneralProduct(Category category, Integer gpID) {
        GeneralProduct toRemove = searchGeneralProductByGpID(gpID);
        Result result;
        if (toRemove != null) {
            result = category.removeGeneralProduct(toRemove);
            if (result.isOK()) {
                generalProducts.remove(toRemove);
                mapper.delete(toRemove);
            }
        } else {
            result = new Result<Integer>(false, gpID, "Could not find general product by it gpID");
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
    public Result addGeneralProduct(Category category, String manufacture, String name, Float supplier_price, Float retail_price,
                                        Integer min_quantity, Integer catalogID, Integer gpID, Integer supplier_id, String supplier_category , Float weight) {
        GeneralProduct newProduct = new GeneralProduct(manufacture, name, supplier_price, retail_price, min_quantity, catalogID, gpID, supplier_id, supplier_category , branchId , weight);
        Result result = category.addGeneralProduct(newProduct);
        if (result.isOK()) {
            generalProducts.add(newProduct);
            mapper.create(newProduct);
        }
        return result;
    }

    public Result editGeneralProductName(Integer gpID, String new_name) {
        GeneralProduct toEdit = searchGeneralProductByGpID(gpID);
        Result result;
        if (toEdit != null) {
            toEdit.setName(new_name);
            result = new Result<GeneralProduct>(true, toEdit, "Name of general product has been changed");
            mapper.update(toEdit);
        } else {
            result = new Result<Integer>(false, gpID, "Could not find general product");
        }
        return result;
    }

    public Result editGeneralProductSupplierPrice(Integer gpID, Float new_supplier_price, Integer supplier_id) {
        GeneralProduct toEdit = searchGeneralProductByGpID(gpID);
        Result result;
        if (toEdit != null) {
            if (toEdit.setSupplierPrice(new_supplier_price, supplier_id)) {
                result = new Result<GeneralProduct>(true, toEdit, "Supplier price of general product has been changed");
                mapper.update(toEdit);
            } else {
                result = new Result<Integer>(false, gpID, String.format("Could not find catalog product with supplier id %d", supplier_id));
            }
        } else {
            result = new Result<Integer>(false, gpID, "Could not find general product");
        }
        return result;
    }

    public Result editGeneralProductRetailPrice(Integer gpID, Float new_retail_price) {
        GeneralProduct toEdit = searchGeneralProductByGpID(gpID);
        Result result;
        if (toEdit != null) {
            toEdit.setRetailPrice(new_retail_price);
            result = new Result<GeneralProduct>(true, toEdit, "Retail price of general product has been changed");
            mapper.update(toEdit);
        } else {
            result = new Result<Integer>(false, gpID, "Could not find general product");
        }
        return result;
    }

    public Result editGeneralProductQuantity(Integer gpID, Integer new_quantity) {
        GeneralProduct toEdit = searchGeneralProductByGpID(gpID);
        Result result;
        if (toEdit != null) {
            toEdit.setQuantity(new_quantity);
            result = new Result<GeneralProduct>(true, toEdit, "Quantity price of general product has been changed");
            mapper.update(toEdit);
        } else {
            result = new Result<Integer>(false, gpID, "Could not find general product");
        }
        return result;
    }

    public Result editGeneralProductMinQuantity(Integer gpID, Integer new_min_quantity) {
        GeneralProduct toEdit = searchGeneralProductByGpID(gpID);
        Result result;
        if (toEdit != null) {
            toEdit.setMinQuantity(new_min_quantity);
            result = new Result<GeneralProduct>(true, toEdit, "Min quantity price of general product has been changed");
            mapper.update(toEdit);
        } else {
            result = new Result<Integer>(false, gpID, "Could not find general product");
        }
        return result;
    }
    //endregion

    //region Specific Product Managment

    /**
     * add specific product to the general product list.
     *
     * @param gpID       - catalog id of the general product
     * @param expiration_date - of the specific product or the batch of products
     * @param quantity        - how many specific product with the SAME expiration date
     * @return
     */
    public Result addSpecificProduct(Integer gpID, Date expiration_date, Integer quantity) {
        GeneralProduct generalProduct = searchGeneralProductByGpID(gpID);
        Result<SpecificProduct> result = null;
        String msg = "";
        if (generalProduct != null) {
            for (int i = 0; i < quantity; i++) {
                result = generalProduct.addProduct(getNext_id(), expiration_date);
                if (result.isOK()){mapper.create(result.getData());}
                msg = msg.concat(result.getMessage().concat("\n"));
            }
            result.setMessage(msg);
        } else {
            result = new Result<>(false, null, String.format("Could not find general product %d",gpID));
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
        Result<SpecificProduct> result = null;
        if (generalProduct != null) {
            result = generalProduct.removeProduct(specific_product_id);
            if (result.isOK()){mapper.delete(result.getData());}
        } else {
            result = new Result(false, null, String.format("Could not find specific product ID:%d", specific_product_id));
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
        Result<SpecificProduct> result = null;
        if (generalProduct != null) {
            result = generalProduct.markAsFlaw(specific_product_id);
            if(result.isOK()){mapper.update(result.getData());}
        } else {
            result = new Result(false, null, String.format("Could not find specific product ID:%d", specific_product_id));
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
        Result<SpecificProduct> result = null;
        if (generalProduct != null) {
            result = generalProduct.moveLocation(specific_product_id);
            if(result.isOK()){mapper.update(result.getData());}
        } else {
            result = new Result(false, null, String.format("Could not find specific product ID:%d", specific_product_id));
        }
        return result;
    }
    //endregion

    //region Generals

    /**
     * search general product by catalogId, if not found return null
     *
     * @param gpID
     * @return
     */
    public GeneralProduct searchGeneralProductByGpID(Integer gpID) {
        for (GeneralProduct product : generalProducts) {
            if (product.getGpID().equals(gpID)) {
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
        Integer next = next_id;
        this.next_id++;
        mapper.writeID("product",next_id);
        return next;
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
