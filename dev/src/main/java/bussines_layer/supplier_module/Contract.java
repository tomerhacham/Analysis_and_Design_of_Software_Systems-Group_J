package bussines_layer.supplier_module;
import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.GeneralProduct;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Class Contract.
 * Extension of the information that the SupplierCard contains about a Supplier.
 * Represent the agreement with the Supplier including Category, Product, etc.
 *
 */

public class Contract {

    private LinkedList<String> categories; // a supplier can have a lot of categories
    private HashMap<Integer , GeneralProduct> products; // <product ID, product>
    private SupplierCard supplier;
    private CostEngineering costEngineering;
    private int contractID;
    private int branchID;

    public Contract(SupplierCard supplier , int contractID , int branchID){
        this.categories = new LinkedList<>();
        this.supplier = supplier;
        this.products = new HashMap<>();
        this.costEngineering = null;
        this.contractID = contractID;
        this.branchID = branchID;
    }

    public int getSupplierID() {
        return supplier.getId();
    }

    public SupplierCard getSupplierCard() {return supplier; }


//#region Categories

    public boolean isCategoryExist (String category){
        for (String c : categories) {
            if(c.equals(category)){
                return true;
            }
        }
        return false;
    }

    public LinkedList<String> getCategory() {
        return categories;
    }

    public void setCategory(LinkedList<String> category) {
        this.categories = category;
    }

    public void addCategory(String c) {
        if (isCategoryExist(c)) {
            //sz_Result.setMsg("The Category IS Already In The Contract");  //TODO RESULT
            return;
        }
        categories.add(c);
    }

    public void removeCategory(String c){
        if (!isCategoryExist(c)) {
            //sz_Result.setMsg("The Category dose not Exist");  //TODO RESULT
            return;
        }
        categories.remove(c);
        //remove all product from this category from products list
        LinkedList <GeneralProduct> toRemove = new LinkedList<>();
        for (Integer i : products.keySet()){
            if (products.get(i).getSupplierCategory(supplier.getId()).equals(c) ){
                toRemove.add(products.get(i));
            }
        }
        for (GeneralProduct p : toRemove){
            removeProduct(p);
        }

    }

//#endregion

//#region Products

    public LinkedList<GeneralProduct> getProducts() {
        if (products.isEmpty()){
            //sz_Result.setMsg("The Supplier Has No Products");  //TODO result
        }
        LinkedList<GeneralProduct> listP = new LinkedList<>();
        for (Integer i : products.keySet()){
            listP.add(products.get(i));
        }
        return listP;
    }

    public void setProducts(HashMap<Integer, GeneralProduct> products) {
        this.products = products;
    }

    public void addProduct(GeneralProduct p){
        // first check if the supplier can supply this category (check if the category is in the category list)
        boolean categoryInList = isCategoryExist(p.getSupplierCategory(supplier.getId()));

        if (categoryInList){
            if (products.containsKey(p.getProductID())){
                //sz_Result.setMsg("The Product Is Already In Your Product List");  //TODO RESULT
            }
            else{
                products.put(p.getProductID() , p);
            }
        }
        else{
            //sz_Result.setMsg("The Category Of This Product Is Not In The Category List ");  //TODO RESULT
        }
    }

    public void removeProduct(GeneralProduct product){
        if (!products.containsKey(product.getProductID())){
            //sz_Result.setMsg("There's no such Product on Contract"); //TODO RESULT
            return;
        }
        products.remove(product.getProductID()); // remove product
    }

    public boolean isProductExist(GeneralProduct product){
        if (products.containsKey(product.getProductID()))
            return false;
        else
            return true;
    }


    public float getProductPrice(Integer productID){
        if (products.containsKey(productID)){
            return products.get(productID).getSupplierPrice();
        }
        //sz_result ("The Product Is Not In The Contract");  //TODO- result
        return Integer.MAX_VALUE;
    }


//#endregion

//#region CostEngineering

//    public boolean isProductExist (Integer catalogID){
//        for (Integer pid : products.keySet()){
//            if (products.get(pid).getCatalogID() == catalogID){
//                return true;
//            }
//        }
//        return false;
//    }

    //TODo - update the changes
    public boolean isProductExist (Integer id , boolean isCatalogid){
        if(isCatalogid){
            for (Integer pid : products.keySet()){
                if (products.get(pid).getCatalogID(supplier.getId()).equals(id)){
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

    public void addCostEngineering() {
        if (costEngineering != null){
            //sz_result ("There is already CostEngineering to the Supplier");  //TODO- result
        }
        this.costEngineering = new CostEngineering();
    }

    public void removeCostEngineering() {
        costEngineering = null;
    }

    public CostEngineering getCostEngineering() {
        return costEngineering;
    }

    //update min quantity
    public void updateMinQuantity(int catalogid , int minQuantity){
        if (!isProductExist(catalogid , true)){
            //sz_Result ("Product does not exist in the Contract" )  //TODO result
        }
       costEngineering.updateMinQuantity(catalogid , minQuantity);
    }

    //update new price with sale
    public void updatePriceAfterSale(int catalogid , int price){
        costEngineering.updatePriceAfterSale(catalogid , price);
    }

    //add product to cost engineering
    public void addProductToCostEng(int catalogid , int minQuantity , int price){
        //check if the product is in the suppliers product list
        if(isProductExist(catalogid , true)){
            costEngineering.addProduct(catalogid , minQuantity , price);
        }
        else{
            //sz_Result.setMsg("The Product Is Not In The Product list");   //TODO result
        }
    }

    //delete product from cost engineering
    public void removeProductFromCostEng(int catalogid){
        costEngineering.removeProduct(catalogid);
    }


    public boolean isCostEngExist(){
        return costEngineering!=null;
    }


//#endregion

}


