package bussines_layer.supplier_module;
import bussines_layer.Result;
import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import data_access_layer.DTO.ContractDTO;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Class Contract.
 * Extension of the information that the SupplierCard contains about a Supplier.
 * Represent the agreement with the Supplier including Category, Product, etc.
 *
 */

public class Contract {

    private Integer contractID;
    private Integer branchID;
    private HashMap<Integer , CatalogProduct> products; // <product ID, product>
    private SupplierCard supplier;
    private CostEngineering costEngineering;
    private LinkedList<String> categories; // a supplier can have a lot of categories

    public Contract(SupplierCard supplier , Integer contractID , Integer branchID , LinkedList<String> categories){
        this.categories = new LinkedList<>();
        this.supplier = supplier;
        this.products = new HashMap<>();
        this.costEngineering = null;
        this.contractID = contractID;
        this.branchID = branchID;
        this.categories = categories;
    }

    public Contract(ContractDTO contractDTO) {
        this.contractID=contractDTO.getContract_id();
        this.branchID=contractDTO.getBranch().getBranch_id();
    }

    public int getSupplierID() {
        return supplier.getId();
    }

    public SupplierCard getSupplierCard() {return supplier; }

    public void setSupplier(SupplierCard supplier) {
        this.supplier = supplier;
    }

    public void setCostEngineering(CostEngineering costEngineering) {
        this.costEngineering = costEngineering;
    }

    public void setCategories(LinkedList<String> categories) {
        this.categories = categories;
    }

    //region Getters
    public LinkedList<String> getCategories() {
        return categories;
    }

    public SupplierCard getSupplier() {
        return supplier;
    }

    public int getContractID() {
        return contractID;
    }

    public Integer getBranchID() {
        return branchID;
    }
    //endregion

    //#region Categories
    public LinkedList<String> getCategory() {
        return categories;
    }
    public void setCategory(LinkedList<String> category) {
        this.categories = category;
    }

    /**
     * add category to the contract
     * @param category
     */
    public Result addCategory(String category) {
        Result result;
        if (isCategoryExist(category)) {
            result=new Result(false,category, String.format("Category: %s already exist in contract ID:%d", category,contractID));
        }
        else{
            categories.add(category);
            result=new Result(true, category, String.format("Category %s has been added to contract ID:%d", category,contractID));
        }
        return result;
        }

    /**
     * removes category from the contract
     * @param category
     */
    public Result removeCategory(String category){
        Result result;
        if (!isCategoryExist(category)) {
            result=new Result(false, category, String.format("Could not find category: %s in contract ID:%d", category,contractID));
        }
        else {
            categories.remove(category);
            //remove all product from this category from products list
            LinkedList<CatalogProduct> toRemove = new LinkedList<>();
            for (Integer i : products.keySet()) {
                if (products.get(i).getSupplierCategory().equals(category)) {
                    toRemove.add(products.get(i));
                }
            }
            for (CatalogProduct p : toRemove) {
                removeProduct(p);
            }
            result=new Result(true,category, String.format("Category: %s has beem removed from contract ID:%d", category,contractID));
        }
        return result;
    }

    /**
     * Utility function which checks if the category in the contract
     * @param category
     * @return
     */
    private boolean isCategoryExist (String category){
        for (String c : categories) {
            if(c.equals(category)){
                return true;
            }
        }
        return false;
    }
    //#endregion

    //#region Products

    /**
     * reutrn all the catalogProduct under this contract
     * @return
     */
    public Result<LinkedList<CatalogProduct>> getProducts() {
        Result<LinkedList<CatalogProduct>> result;
            if (products.isEmpty()){
                result = new Result<>(false,null, String.format("There is no catalog product under contract %d", this.contractID));
            }
            else {
                LinkedList<CatalogProduct> listP = new LinkedList<>();
                for (Integer i : products.keySet()) {
                    listP.add(products.get(i));
                }
                result=new Result<>(true, listP, String.format("List of all the catalog product under contract ID: %d", this.contractID));
            }
            return result;
        }

    /**
     * set all catalog product under this contract
     * @param products
     * @return
     */
    public Result setProducts(HashMap<Integer, CatalogProduct> products) {
        this.products = products;
        return new Result(true, this, String.format("Product has been added under contract ID: %d", contractID));
    }

    /**
     * add new Catalog product to the product under this contract
     * @param catalogProduct
     * @return
     */
    public Result addProduct(CatalogProduct catalogProduct){
        Result result;
            // first check if the supplier can supply this category (check if the category is in the category list)
            boolean categoryInList = isCategoryExist(catalogProduct.getSupplierCategory());
            if (categoryInList){
                if (products.containsKey(catalogProduct.getGpID())){
                    result=new Result<>(false, catalogProduct, String.format("Catalog product already under contract ID:%d", contractID));
                }
                else{
                    products.put(catalogProduct.getGpID() , catalogProduct);
                    result=new Result<>(true,catalogProduct, String.format("%s has been added to contract ID:%d", catalogProduct,contractID));
                }
            }
            else{
                result=new Result<>(false,catalogProduct, String.format("The Category Of %s Is Not In The Category List under contract ID:%d", catalogProduct,contractID));
            }
            return result;
        }

    /**
     * remove catalog product from the product under this contracr
     * @param catalogProduct
     * @return
     */
    public Result removeProduct(CatalogProduct catalogProduct){
        Result result;
        if (!products.containsKey(catalogProduct.getGpID())){
            result=new Result(false,catalogProduct, String.format("Could not find %s under contract ID:%d", catalogProduct,contractID));
        }
        else {
            products.remove(catalogProduct.getGpID()); // remove product
            result=new Result(true, catalogProduct, String.format("%s has been remove from contract ID:%d", catalogProduct,contractID));
        }
        return result;
        }

    /**
     *checks if the product is under this contract
     * @param product
     * @return
     */
    public Result<CatalogProduct> isProductExist(CatalogProduct product){
        Result result;
        if (products.containsKey(product.getGpID()))
            result=new Result<>(true, product, String.format("Found %s", product));
        else
            result=new Result<>(false,product, String.format("Could not find %s under contract ID:%d", product,contractID));
        return result;
    }

    /**
     * return supplier price for the catalog product associate with the productID
     * @param productID
     * @return
     */
    public Result<Float> getProductPrice(Integer productID){
        Result<Float> result;
        if (products.containsKey(productID)){
            Float supplier_price=products.get(productID).getSupplierPrice();
            result=new Result<>(true,supplier_price, String.format("Supplier price for catalog product:%d", productID));
        }
        else{
            result=new Result<>(false, null, String.format("Could not find product:%d under contract ID:%d", productID,contractID));
        }
        return result;
    }

    public Result<Float> getProductPriceConsideringQuantity(Integer productID , Integer quantity){
        Result<Float> result;
        if (products.containsKey(productID)){
            if(isCostEngExist()){
                result = costEngineering.getUpdatePrice( products.get(productID).getCatalogID() , quantity);
                if (result.getData() == -1){
                    Float supplier_price=products.get(productID).getSupplierPrice();
                    result=new Result<>(true,supplier_price, String.format("Supplier price for catalog product:%d", productID));
                }
            }
            else{
                Float supplier_price=products.get(productID).getSupplierPrice();
                result=new Result<>(true,supplier_price, String.format("Supplier price for catalog product:%d", productID));
            }
        }
        else{
            result=new Result<>(false, null, String.format("Could not find product:%d under contract ID:%d", productID,contractID));
        }
        return result;
    }

    //#endregion

    //#region CostEngineering

    public boolean isProductExist (Integer id , boolean isCatalogid){
        if(isCatalogid){
            for (Integer pid : products.keySet()){
                if (products.get(pid).getCatalogID().equals(id)){
                    return true;
                }
            }
            return false;
        }

        if(products.containsKey(id)){
            return true;
        }
        return false;
    }

    public int getProductCatalogID(Integer productID){

        return products.get(productID).getCatalogID();
    }

    /**
     * add cost engineering to this contract
     */
    public Result addCostEngineering() {
        Result result;
        if (costEngineering != null){
            result=new Result(false,null, String.format("There is already cost engineering associate to contract ID:%d", contractID));
        }
        else {
            this.costEngineering = new CostEngineering(contractID,branchID);
            result=new Result(true, this.costEngineering, String.format("Cost engineering has been associate to contract ID:%d.", contractID));
        }
        return result;
    }

    /**
     * remove the cost engineering associate with the contract
     */
    public Result removeCostEngineering() {
        costEngineering = null;
        return new Result(true, null, String.format("Cost engineering has benn removed from contract ID:%d", contractID));
    }

    /**
     * return cost engineering
     * @return
     */
    public CostEngineering getCostEngineering() {
            return costEngineering;
        }

    /**
     * update min quantity of product in cost engineering
     * @param catalogid
     * @param minQuantity
     * @return
     */
    public Result updateMinQuantity(Integer catalogid , Integer minQuantity){
        Result result;
        if (!isProductExist(catalogid , true)){
            result=new Result(false,catalogid, String.format("Could not find CatalogID:%d under contarct ID:%d", catalogid,contractID));
        }
        else {
            result=costEngineering.updateMinQuantity(catalogid, minQuantity);
        }
        return result;
    }

    /**
     * update price of catalog product in ost engineering
     * @param catalogid
     * @param price
     */
    public Result updatePriceAfterSale(Integer catalogid , Float price){
        return costEngineering.updatePriceAfterSale(catalogid , price);
    }

    /**
     * add product to cost engineering
     * @param catalogid
     * @param minQuantity
     * @param price
     * @return
     */
    public Result addProductToCostEng(Integer catalogid , Integer minQuantity , Float price){
        Result result;
        if(isProductExist(catalogid , true)){
            result = costEngineering.addProduct(catalogid , minQuantity , price);
        }
        else{
            result=new Result<>(false,catalogid, String.format("Could not find CatalogID:%d under contract ID:%d", catalogid,contractID));
        }
        return result;
    }

    /**
     * delete product from cost engineering
     * @param catalogid
     * @return
     */
    public Result removeProductFromCostEng(Integer catalogid){
        return costEngineering.removeProduct(catalogid);
    }

    public boolean isCostEngExist(){
        return costEngineering!=null;
    }

    //#endregion

}


