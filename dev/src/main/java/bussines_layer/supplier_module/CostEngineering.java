package bussines_layer.supplier_module;

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
        minQuntity = new HashMap<Integer, Integer>();
        newPrice = new HashMap<Integer, Integer>();
    }

    public HashMap<Integer, Integer> getMinQuntity() {
        return minQuntity;
    }

    public HashMap<Integer, Integer> getNewPrice() {
        return newPrice;
    }

    public void updateMinQuantity(int catalogid , int newQuantity){
        minQuntity.replace(catalogid , minQuntity.get(catalogid) , newQuantity);
    }

    public void updatePriceAfterSale(int protuctid , int newp){
        newPrice.replace(protuctid , newPrice.get(protuctid) , newp);
    }

    public void removeProduct(int catalogid){
        if (minQuntity.containsKey(catalogid)){
            minQuntity.remove(catalogid);
        }
        else{
           // sz_Result.setMsg("The Product Is Not In The Cost Engineering"); //TODO result
            return;
        }
        if (newPrice.containsKey(catalogid)) {
            newPrice.remove(catalogid);
        }
        else{
            //sz_Result.setMsg("The Product Is Not In The Cost Engineering"); //TODO result
        }
    }

    public void addProduct(int catalogid , int quantity , int price){

        if (minQuntity.containsKey(catalogid) || (newPrice.containsKey(catalogid))){
            //sz_Result.setMsg("Product Already exist, You Can Update the Quantity or the Price on main Menu."); //TODO result
            return;
        }
        if (!(minQuntity.containsKey(catalogid))){
            minQuntity.put(catalogid ,quantity );
        }
        if (!(newPrice.containsKey(catalogid))) {
            newPrice.put(catalogid , price);
        }
    }

    public float getUpdatePrice (int catalogId , int quantity){
        float price = -1;
        if (minQuntity.containsKey(catalogId)) {
            int min = minQuntity.get(catalogId);

            if (quantity >= min) {
                price = newPrice.get(catalogId);
            }
        }

        return price;
    }


}
