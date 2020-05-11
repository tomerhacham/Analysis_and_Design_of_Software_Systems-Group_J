package bussines_layer.supplier_module;
import bussines_layer.sz_Result;

import java.util.HashMap;

/**
 * Class Cost Engineering.
 * Optional extension of the information that the SupplierCard contains about a Supplier.
 * Represent the agreement with the Supplier about sales and new prices.
 *
 */

public class CostEngineering {

    private HashMap<Integer , Integer> minQuntity; // <catalogid , quantity>
    private HashMap<Integer , Integer> newPrice; // <catalogid , newPrice>

    public CostEngineering(){
        minQuntity = new HashMap<>();
        newPrice = new HashMap<>();
    }

    public HashMap<Integer, Integer> getMinQuntity() {
        return minQuntity;
    }

    public void setMinQuntity(HashMap<Integer, Integer> minQuntity) {
        this.minQuntity = minQuntity;
    }

    public HashMap<Integer, Integer> getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(HashMap<Integer, Integer> newPrice) {
        this.newPrice = newPrice;
    }

    public void changeMinQuantity(int catalogid , int newQuantity){
        minQuntity.replace(catalogid , minQuntity.get(catalogid) , newQuantity);
    }

    public void changeNewPrice(int protuctid , int newp){
        newPrice.replace(protuctid , newPrice.get(protuctid) , newp);
    }

    public void removeProduct(int catalogid){
        if (minQuntity.containsKey(catalogid)){
            minQuntity.remove(catalogid);
        }
        else{
            sz_Result.setMsg("The Product Is Not In The Cost Engineering");
            return;
        }

        if (newPrice.containsKey(catalogid)) {
            newPrice.remove(catalogid);
        }
        else{
            sz_Result.setMsg("The Product Is Not In The Cost Engineering");
        }
    }

    public void addProduct(int catalogid , int quantity , int price){

        if (minQuntity.containsKey(catalogid) || (newPrice.containsKey(catalogid))){
            sz_Result.setMsg("Product Already exist, You Can Update the Quantity or the Price on main Menu.");
            return;
        }
        if (!(minQuntity.containsKey(catalogid))){
            minQuntity.put(catalogid ,quantity );
        }
        if (!(newPrice.containsKey(catalogid))) {
            newPrice.put(catalogid , price);
        }
    }

    public Double getUpdatePrice (int catalogId , int quantity){
        double price = -1;
        if (minQuntity.containsKey(catalogId)) {
            int min = minQuntity.get(catalogId);

            if (quantity >= min) {
                price = newPrice.get(catalogId);
            }
        }

        return price;
    }
}
