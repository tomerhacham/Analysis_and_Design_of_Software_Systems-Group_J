package bussines_layer.supplier_module;

import bussines_layer.Result;
import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import javafx.util.Pair;


import java.util.LinkedList;

public class ContractController {

    private LinkedList<Contract> contracts;
    private Integer contractidCounter;
    private Integer branchID;

    public ContractController(Integer branchID) {
        contracts = new LinkedList<>();
        contractidCounter = 0;
        this.branchID = branchID;
    }

    //region Contract

    /**
     * return the contract by its associate suplier id
     * @param supplierID - Allocated by the SupplierController
     * @return Result with contract, if found
     */
    public Result<Contract> findContract(Integer supplierID) {
        Result<Contract> result=null;
        for (Contract c : contracts) {
            if (c.getSupplierID() == supplierID) {
                result = new Result<>(true, c, "contract has been found");
                break;
            }
        }
        if(result == null){
            result=new Result<>(false, null, String.format("Could not find contract for supplier %d", supplierID));
        }
        return result;
    }

    public boolean isExistSupplier (Integer supplierID){
        for (Contract c : contracts) {
            if (c.getSupplierID() == supplierID) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add contract with supplier
     * @param supplier - the supplier to create contract with
     * @return Result with supplier
     */
    public Result<SupplierCard> addContract(SupplierCard supplier, LinkedList<String> categories) {
        if (findContract(supplier.getId()).getData() != null) {
            return new Result<>(false, supplier, String.format("Contract for supplier %s already exists", supplier));
        }
        //create and add new contract
        Contract c = new Contract(supplier, contractidCounter, branchID , categories);
        contractidCounter++;
        supplier.incNumOfContract();
        contracts.add(c);
        return new Result<>(true, supplier, String.format("New contract with supplier %s added successfully", supplier));
    }

    /**
     * Remove contract with supplier, if exists
     * @param supplier - the supplier to remove contract with
     * @return Result with supplier
     */
    public Result removeContract(SupplierCard supplier) {
        Contract toRemove = findContract(supplier.getId()).getData();
        if (toRemove == null) {
            return new Result<>(false, null, String.format("There is no contract with supplier %s", supplier));
        }
        contracts.remove(toRemove);
        supplier.decNumOfContract();
        return new Result<>(true, supplier, String.format("Contract with supplier %s removed successfully \n NOTICE - All periodic orders with this suppliers had been deleted except for those who are expected to be delivered tomorrow  ", supplier));
    }

    //endregion

    //region Products

    /**
     * Add product to contract with supplier
     * @param supplierID - id of supplier
     * @param product - product to add
     * @return Result with contract if successful
     */
    public Result addProductToContract(Integer supplierID, CatalogProduct product) {
        // Notice : we checked that the products category is in the Suppliers list inside the contract
        Contract c = findContract(supplierID).getData();
        if (c == null) {
            return new Result<>(false, null, String.format("No contract found for supplier %d", supplierID));
        }
        return c.addProduct(product);
    }

    /**
     * remove product from contract with supplier
     * @param supplierID - ID of supplier
     * @param product - product to remove
     * @return Result with Catalog Product if successful
     */
    public Result removeProductFromContract(Integer supplierID, CatalogProduct product) {

        if (findContract(supplierID).getData() == null) {
            return new Result<>(false, null, String.format("Contract with supplier (ID: %d) not found", supplierID));
        }
        return findContract(supplierID).getData().removeProduct(product);
    }

    /**
     * Get all supplier products
     * @param supplierID
     * @return Result with LinkedList of all supplier's products, if successful
     */
    public Result<LinkedList<CatalogProduct>> getAllSupplierProducts (Integer supplierID){
        if (findContract(supplierID).getData() == null) {
            return new Result<>(false, null, String.format("Contract with supplier (ID: %d) not found", supplierID));
        }
        return findContract(supplierID).getData().getProducts();
    }

    public Pair<SupplierCard,Float> getBestSupplierForProduct(Integer productID , Integer quantity){
        Float price = (float) Integer.MAX_VALUE;
        SupplierCard supplierCard = null;

        for (Contract c: contracts) {
            if(c.isProductExist(productID , false)){
                if(c.isCostEngExist()){
                    Float priceFromCostEng = c.getCostEngineering().getUpdatePrice(c.getProductCatalogID(productID) , quantity).getData();
                    if((priceFromCostEng != -1) && (priceFromCostEng < price)){
                        price = priceFromCostEng;
                        supplierCard = c.getSupplierCard();
                    }
                }
                else if (c.getProductPrice(productID).getData() < price){
                    price = c.getProductPrice(productID).getData();
                    supplierCard = c.getSupplierCard();
                }
            }
        }
        return new Pair<>(supplierCard, price);
    }

    //endregion

    //region Category

    public Result addCategory (Integer supplierID, String category){
        Result<Contract> res = findContract(supplierID);
        if (res == null) {
            return res;
        }
        return res.getData().addCategory(category);
    }

    public Result removeCategory (Integer supplierID, String category){
        Result<Contract> res = findContract(supplierID);
        if (res.getData() == null) {
            return res;
        }
        return res.getData().removeCategory(category);
    }

    //endregion

    //region CostEngineering

    //create cost engineering
    public Result addCostEngineering(Integer supplierID){
        Result<Contract> res = findContract(supplierID);
        if (res.getData() == null) {
            return res;
        }
        return res.getData().addCostEngineering();
    }

    public Result removeCostEngineering (Integer supplierID){
        Result<Contract> res = findContract(supplierID);
        if (res.getData() == null) {
            return res;
        }
        return res.getData().removeCostEngineering();
    }

    public Result updateMinQuantity(Integer supplierID , Integer catalogID , Integer minQuantity){
        Result<Contract> res = findContract(supplierID);
        if (res.getData() == null) {
            return res;
        }
        return res.getData().updateMinQuantity(catalogID, minQuantity);
    }

    public Result updatePriceAfterSale(Integer supplierID , Integer catalogID , Float price){
        Result<Contract> res = findContract(supplierID);
        if (res.getData() == null) {
            return res;
        }
        return res.getData().updatePriceAfterSale(catalogID, price);
    }

    public Result addProductToCostEng(Integer supplierID , Integer catalogID , Integer minQuantity , Float price){
        Result<Contract> res = findContract(supplierID);
        if (res.getData() == null) {
            return res;
        }
        return res.getData().addProductToCostEng(catalogID , minQuantity, price);
    }

    public Result removeProductFromCostEng(Integer supplierID , Integer catalogID){
        Result<Contract> res = findContract(supplierID);
        if (res.getData() == null) {
            return res;
        }
        return res.getData().removeProductFromCostEng(catalogID);
    }

    //endregion


    public Integer getBranchID() {
        return branchID;
    }
}
