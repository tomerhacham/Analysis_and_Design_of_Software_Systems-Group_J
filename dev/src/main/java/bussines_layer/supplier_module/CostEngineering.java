package bussines_layer.supplier_module;

import bussines_layer.Result;

import java.util.HashMap;

/**
 * Class Cost Engineering.
 * Optional extension of the information that the SupplierCard contains about a Supplier.
 * Represent the agreement with the Supplier about sales and new prices.
 *
 */

public class CostEngineering {

    private HashMap<Integer , Integer> minQuntity; // <catalogid , quantity>
    private HashMap<Integer , Float> newPrice; // <catalogid , newPrice>

    public CostEngineering(){
        minQuntity = new HashMap<Integer, Integer>();
        newPrice = new HashMap<Integer, Float>();
    }

    public HashMap<Integer, Integer> getMinQuntity() {
        return minQuntity;
    }

    public HashMap<Integer, Float> getNewPrice() {
        return newPrice;
    }

    public Result updateMinQuantity(Integer catalogid , Integer newQuantity){
        minQuntity.replace(catalogid , minQuntity.get(catalogid) , newQuantity);
        return new Result(true,newQuantity, String.format("CatalogID:%s min quantity has been change to:%d", catalogid,newQuantity));
    }

    public Result updatePriceAfterSale(Integer catalogid , Float newp){
        newPrice.replace(catalogid , newPrice.get(catalogid) , newp);
        return new Result(true,newp, String.format("CatalogID:%s price has been change to:%d", catalogid,newp));
    }

    public Result removeProduct(Integer catalogid){
        Result result;
        if (minQuntity.containsKey(catalogid)){
            minQuntity.remove(catalogid);
            newPrice.remove(catalogid);
            result=new Result(true, catalogid, String.format("CatalogID:%d has been removed", catalogid));
        }
        else{
            result=new Result(false,catalogid, String.format("Could not find CatalogID:%d in the cost engineering", catalogid));
        }
        return result;
    }

    public Result addProduct(Integer catalogid , Integer quantity , Float price){
        Result result;
        if (minQuntity.containsKey(catalogid) || (newPrice.containsKey(catalogid))){
            result=new Result(false, catalogid, String.format("CatalogId:%d already exist",catalogid ));
        }
        else {
            minQuntity.put(catalogid, quantity);
            newPrice.put(catalogid, price);
            result= new Result(true, catalogid, String.format("Catalog Product:%d has been added",catalogid ));
        }
        return result;
    }

    public Result getUpdatePrice (Integer catalogId , Integer quantity){
        Result result;
        Float price = (float)-1;
        if (minQuntity.containsKey(catalogId)) {
            int min = minQuntity.get(catalogId);
            if (quantity >= min) {
                price = newPrice.get(catalogId);
            }
            result=new Result(true, price, String.format("Relevant price for CatalogID:%d", catalogId));
        }
        else{
            result = new Result(false, catalogId, String.format("Could not find CatalogID:%d in cost engineering", catalogId));
        }
        return result;
    }


}
