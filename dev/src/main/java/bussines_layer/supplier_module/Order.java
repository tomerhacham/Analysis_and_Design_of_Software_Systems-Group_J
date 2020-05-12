package bussines_layer.supplier_module;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.ProductController;

import java.util.HashMap;
import java.util.LinkedList;


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
    private OrderType status; //TODO
    private HashMap<CatalogProduct, Integer> productsAndQuantity; // <product , quantity>
    private HashMap<CatalogProduct , Integer> productsAndPrice; //<product, price>


    public Order(int orderID , int supplierID , OrderType status){
        this.orderID = orderID;
        productsAndQuantity = new HashMap<>();
        this.supplierID = supplierID;
        productsAndPrice = new HashMap<>();
        status = status;
    }

    public Order(int orderID , int supplierID , LinkedList<CatalogProduct> products){
        this.orderID = orderID;
        productsAndQuantity = new HashMap<>();
        this.supplierID = supplierID;
        productsAndPrice = new HashMap<>();
    }

    public int getOrderID() {
        return orderID;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public OrderType getStatus() {
        return status;
    }

    public  HashMap<CatalogProduct, Integer> getProductsAndQuantity() {return productsAndQuantity;}

    public void addProduct(CatalogProduct product , Integer quantity , Integer price){

        if (productsAndQuantity.containsKey(product)){
            //sz_Result ( "Product Already Exists In The Order" ); //TODO RESULT
            return;
        }
        productsAndQuantity.put(product , quantity);
        productsAndPrice.put(product , price);
    }

    public void removeProduct(CatalogProduct product){
        if (!(productsAndQuantity.containsKey(product))){
            //sz_Result ( "Product Is Not In The Order" ); //TODO RESULT
            return;
        }
        productsAndQuantity.remove(product);
        productsAndPrice.remove(product);
    }

    public void updateProductQuantityInOrder(CatalogProduct product , Integer newQuantity){
        productsAndQuantity.replace(product , newQuantity);
    }

    public String display() {
        String toDisplay = "Order id : "+'\t'+this.orderID.toString() +'\t'+"Supplier id : "+this.supplierID+ '\n';
        toDisplay = toDisplay+ "Product"+'\t'+'\t'+"Quantity" + '\n';

        for (CatalogProduct p : productsAndQuantity.keySet()){
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
        Integer price;

        for (CatalogProduct cp :productsAndQuantity.keySet()) {
            quantity = productsAndQuantity.get(cp);
            price = productsAndPrice.get(cp);
            total = total + (price * quantity);
        }
        return total;
    }








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
