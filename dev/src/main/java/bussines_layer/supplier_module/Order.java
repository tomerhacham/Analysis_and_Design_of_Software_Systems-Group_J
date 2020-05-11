package bussines_layer.supplier_module;
import bussines_layer.inventory_module.GeneralProduct;
import main.java.bussines_layer.sz_Result;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Class Order.
 * Holds all the Information about an Order.
 *
 * Functionality that related to Order information.
 *
 */

public class Order {

    private Integer orderID;
    private Integer supplierID; // TODO - how else can we know who is the supplier ?
    //private status //TODO
    private HashMap<GeneralProduct, Integer> productsAndQuantity; // <product , quantity>
    //private HashMap<Integer , LinkedList<GeneralProduct>> supplierAndProduct; //<supplierid, product>


    public Order(int orderID , int supplierID ){
        this.orderID = orderID;
        productsAndQuantity = new HashMap<>();
        this.supplierID = supplierID;
        //supplierAndProduct = new HashMap<>();
    }

    public Order(int orderID , int supplierID , LinkedList<GeneralProduct> products){
        this.orderID = orderID;
        productsAndQuantity = new HashMap<>();
        this.supplierID = supplierID;
        //supplierAndProduct = new HashMap<>();
    }

    public int getOrderID() {
        return orderID;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public  HashMap<GeneralProduct, Integer> getProductsAndQuantity() {return productsAndQuantity;}

    public void addProduct(GeneralProduct product , Integer quantity){

        if (productsAndQuantity.containsKey(product)){
            //sz_Result ( "Product Already Exists In The Order" ); //TODO RESULT
            return;
        }
        productsAndQuantity.put(product , quantity);
    }

    public void removeProduct(GeneralProduct product){
        if (!(productsAndQuantity.containsKey(product))){
            //sz_Result ( "Product Is Not In The Order" ); //TODO RESULT
            return;
        }
        productsAndQuantity.remove(product);
    }

    public void updateProductQuantity(GeneralProduct product , Integer newQuantity){
        if (!(productsAndQuantity.containsKey(product))){
            //sz_Result ( "Product Is Not In The Order" ); //TODO RESULT
            return;
        }
        productsAndQuantity.replace(product , newQuantity);
    }

    public String display() {
        String toDisplay = "Order id : "+'\t'+this.orderID.toString() + '\n';
        toDisplay = toDisplay+ "Product"+'\t'+'\t'+"Quantity" + '\n';

        for (GeneralProduct p : productsAndQuantity.keySet()){
            toDisplay = toDisplay+ p.getName() + '\t' +'\t'+ productsAndQuantity.get(p).toString() + '\n';
        }

        if (productsAndQuantity.isEmpty()){
            toDisplay = toDisplay + "----------------\n";
            toDisplay = toDisplay +"No Products In This Order\n";
        }
        else{
            toDisplay = toDisplay + "Order Total Amount : " + getTotalAmount().toString() +'\n' ;
        }
        return toDisplay;
    }

    public Double getTotalAmount (){
        Double total = 0.0;
        int quantity;
        Double price;

        for (GeneralProduct gp :productsAndQuantity.keySet()) {


        }

        for (GeneralProduct p : productsAndQuantity) {
            quantity = productsAndQuantity.get(p);
            price = ProductController.getInstance().getUpdatePrice(supId, p, quantity);
            if (price==(-1.0)){
                break;
            }
            else{
                total = total + (price * quantity);
            }
        }

        return total;
    } //TODO - do we need this at all ?? (it is an amount for a specific supplier and not really for the whole report)








//
//    public void addProductForSupplier(Integer supplierID, int productID , Integer quantity) {
//
//        GeneralProduct product= ProductController.getInstance().getProductsById(supplierID , productID);
//        if (product == null ){
//            return;
//        }
//
//        //in case this is the first product from this supplier
//        if (! supplierAndProduct.containsKey(supplierID)){
//            supplierAndProduct.put (supplierID , new LinkedList<>());
//            supplierAndProduct.get(supplierID).add(product);
//            productsAndQuantity.put(product , quantity);
//            return;
//        }
//
//        LinkedList<GeneralProduct> supProd = supplierAndProduct.get(supplierID);
//        boolean exists = false;
//        for (GeneralProduct p : supProd) {
//            if (p.getCatalogID()==product.getCatalogID()){
//                exists = true;
//                break;
//            }
//        }
//
//        if(exists){
//            int currentQ = productsAndQuantity.get(product);
//            //adds the new quantity to an exist product
//            productsAndQuantity.replace(product , currentQ , quantity + currentQ);
//        }
//        else{
//            supProd.add(product);
//            productsAndQuantity.put(product , quantity);
//        }
//
//    }



//    public void changeSupplierForProduct(Integer supplierId , int productid ,int catalogid, Integer quantity){
//        Integer supId = 0;
//        boolean exists= false;
//        for (Integer supplier:supplierAndProduct.keySet()) {
//            for (GeneralProduct p: supplierAndProduct.get(supplier) ) {
//                if (p.getProductID() == productid) {
//                    supId = supplier;
//                    exists = true;
//                    break;
//                }
//            }
//            if (exists)
//                break;
//        }
//        if(exists){
//            int i = 0;
//            LinkedList<GeneralProduct> oldSupprod = supplierAndProduct.get(supId);
//            for (GeneralProduct p:oldSupprod) {
//                if (p.getProductID() == productid){
//                    break;
//                }
//                i++;
//            }
//            GeneralProduct pnewSup = oldSupprod.get(i);
//            oldSupprod.remove(i);
//            productsAndQuantity.remove(pnewSup);
//
//
//            // if the suppliers product list is empty then remove him from the order
//            if(oldSupprod.size()==0){
//                supplierAndProduct.remove(supId);
//            }
//
//            pnewSup.setCatalogID(catalogid); // update the product catalog id with the new suppliers catalogid
//            LinkedList<GeneralProduct> supProd = supplierAndProduct.get(supplierId);
//            if (supProd==null) {
//                supProd = new LinkedList<>();
//                supProd.add(pnewSup);
//            }
//            productsAndQuantity.put(pnewSup , quantity);
//        }
//        else{
//            sz_Result.setMsg("The Product Is Not In The Order Or The Supplier Is Not In The System");
//        }
//    }

   }
