package bussines_layer.supplier_module;

import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.GeneralProduct;

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

//#region Contract

    public Contract findContract(Integer supplierID) {
        for (Contract c : contracts) {
            if (c.getSupplierID() == supplierID) {
                return c;
            }
        }
        return null;
    }

    public void addContract(SupplierCard supplier) {
        if (findContract(supplier.getId()) != null) {
            //sz_Result ( "Contract Already exist" ); //TODO RESULT
            return;
        }
        //create and add new contract
        Contract c = new Contract(supplier, contractidCounter, branchID);
        contractidCounter++;
        supplier.incNumOfContract();
        contracts.add(c);
        //add supplier to hashMap
       // supplierProducts.put(supplier.getId(), new LinkedList<>());
    }

    public void removeContract(SupplierCard supplier) {
        Contract toRemove = findContract(supplier.getId());

        if (toRemove == null) {
            //sz_Result ( "There's No Contract with the Supplier" ); //TODO RESULT
            return;
        }
        contracts.remove(toRemove);
        supplier.decNumOfContract();
        //remove from product list
       // supplierProducts.remove(supplier.getId());
    }

//#endregion

//#region Products

    public void addProductToContract(Integer supplierID, GeneralProduct product) {
        // Notice : we checked that the products category is in the Suppliers list inside the contract
        if (findContract(supplierID) == null) {
            //sz_Result ( "There's No Contract with the Supplier" ); //TODO RESULT
            return;
        }
        findContract(supplierID).addProduct(product);
        //add to supplier product list
        //supplierProducts.get(supplierID).add(product);
    }

    public void removeProductFromContract(Integer supplierID, GeneralProduct product) {

        if (findContract(supplierID) == null) {
            //sz_Result ( "There's No Contract with the Supplier" ); //TODO RESULT
            return;
        }
        findContract(supplierID).removeProduct(product);
        //remove product from supplier hash map
        //supplierProducts.get(supplierID).remove(product);
    }

    public LinkedList<GeneralProduct> getAllSupplierProducts (Integer supplierID){
        if (findContract(supplierID) == null) {
            //sz_Result ( "There's No Contract with the Supplier" ); //TODO RESULT
            return null;
        }
        return findContract(supplierID).getProducts();
    }

//#endregion

//#region Category

    public void addCategory (Integer supplierID, String category){
        if (findContract(supplierID) == null) {
            //sz_Result ( "There's No Contract with the Supplier" ); //TODO RESULT
            return;
        }
        findContract(supplierID).addCategory(category);
    }

    public void removeCategory (Integer supplierID, String category){
        if (findContract(supplierID) == null) {
            //sz_Result ( "There's No Contract with the Supplier" ); //TODO RESULT
            return;
        }
        findContract(supplierID).removeCategory(category);
    }

//#endregion

//#region CostEngineering

    //create cost engineering
    public void addCostEngineering(Integer supplierID){
        if (findContract(supplierID) == null) {
            //sz_Result ( "There's No Contract with the Supplier" ); //TODO RESULT
            return;
        }
        findContract(supplierID).addCostEngineering();
    }

    public void removeCostEngineering (Integer supplierID){
        if (findContract(supplierID) == null) {
            //sz_Result ( "There's No Contract with the Supplier" ); //TODO RESULT
            return;
        }
        findContract(supplierID).removeCostEngineering();
    }

    public void updateMinQuantity(Integer supplierID , int catalogID , int minQuantity){
        if (findContract(supplierID) == null) {
            //sz_Result ( "There's No Contract with the Supplier" ); //TODO RESULT
            return;
        }
        findContract(supplierID).updateMinQuantity(catalogID, minQuantity);
    }

    public void updatePriceAfterSale(Integer supplierID , int catalogID , int price){
        if (findContract(supplierID) == null) {
            //sz_Result ( "There's No Contract with the Supplier" ); //TODO RESULT
            return;
        }
        findContract(supplierID).updatePriceAfterSale(catalogID, price);
    }

    public void addProductToCostEng(Integer supplierID , int catalogID , int minQuantity , int price){
        if (findContract(supplierID) == null) {
            //sz_Result ( "There's No Contract with the Supplier" ); //TODO RESULT
            return;
        }
        findContract(supplierID).addProductToCostEng(catalogID , minQuantity, price);
    }

    public void removeProductFromCostEng(Integer supplierID , int catalogID){
        if (findContract(supplierID) == null) {
            //sz_Result ( "There's No Contract with the Supplier" ); //TODO RESULT
            return;
        }
        findContract(supplierID).removeProductFromCostEng(catalogID);
    }

//#endregion


}
