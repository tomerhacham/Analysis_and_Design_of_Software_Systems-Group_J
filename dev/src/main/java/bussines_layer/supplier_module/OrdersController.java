package bussines_layer.supplier_module;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import javafx.util.Pair;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Singleton OrdersController.
 * Responsible of all the Orders in the system.
 * Holds documentation of all the Orders.
 *
 * Functionality that related to Orders.
 *
 */



//Singleton
public class OrdersController {

    // static variable single_instance of type Singleton
    private static OrdersController instance = null;
    private LinkedList<Order> orders;
    private int orderidCounter;

    private OrdersController(){
        orders = new LinkedList<>();
        orderidCounter = 0;
    }

    // static method to create instance of Singleton class
    public static OrdersController getInstance(){
        if (instance == null)
            instance = new OrdersController();

        return instance;
    }

    public int createOrder(int supplierID , OrderType type){
        orderidCounter++;
        orders.add(new Order(orderidCounter  , supplierID , type));
        return orderidCounter;
    }

    public int createPeriodicOrder(LinkedList<Pair<CatalogProduct , Integer>> productsAndQuantity, Date date , int option){
        orderidCounter++;
        orders.add(new Order(orderidCounter , null ,OrderType.PeriodicOrder ));
        getOrder(orderidCounter).
        return orderidCounter;
    }

    //get specific order from the systems order list
    public Order getOrder(int orderid){
        for (Order or:orders) {
            if(or.getOrderID() == orderid){
                return or;
            }
        }
        //sz_Result ( "No Such Order ID" ); //TODO RESULT
        return null;
    }

    public LinkedList<Order> getAllOrders() { return this.orders;}

    public void addProductToOrder(int orderID , CatalogProduct product , Integer quantity , Float price){
        getOrder(orderID).addProduct(product , quantity , price);
    }

    public void removeFromOrder(int orderID , CatalogProduct product) {
        if(getOrder(orderID).getType() == OrderType.PeriodicOrder){
            getOrder(orderID).removeProduct(product);
        }
        else{
            //sz_Result("The order is not a periodic order , Therefor you cant change the order") TODO Result
        }
    }

    //TODO - do we also need to check if the price is updated due to this change?
    public void updateProductQuantityInOrder(int orderID , CatalogProduct product , Integer newQuantity) {
        if(getOrder(orderID).getType() == OrderType.PeriodicOrder){
            getOrder(orderID).updateProductQuantityInOrder(product , newQuantity);
        }
        else{
            //sz_Result("The order is not a periodic order , Therefor you cant change the order") TODO Result
        }
    }

    //get total price of an order
    public Float getTotalPrice (int orderID){
        for (Order o : orders){
            if (o.getOrderID() == orderID)
                return o.getTotalAmount();
        }
        return new Float(-1.0);
    }

    public Float getTotalAmountLastOrder (){
        return orders.getLast().getTotalAmount();
    }

    //removes only the last order
    public void removeOrder (){
        orders.removeLast();
    }


    public LinkedList<String> displayAllSupplierOrders(int supId) {
        LinkedList<String> toDisplay = new LinkedList<>();

        if(orders.isEmpty()){
            toDisplay.add("There Are No Orders In The System"+'\n');
            return toDisplay;
        }

        for (Order o : orders){
            if(o.getSupplierID()==supId){
                toDisplay.add(o.display());
            }
        }
        return toDisplay;
    }


    public LinkedList<String> displayAllOrders (){
        LinkedList<String> toDisplay = new LinkedList<>();

        if(orders.isEmpty()){
            toDisplay.add("There Are No Orders In The System"+'\n');
            return toDisplay;
        }

        for (Order o: orders){
            toDisplay.add(o.display());
        }
        return toDisplay;
    }



    //TODO - it was never used - check if we still need it
//    public  HashMap<GeneralProduct, Integer> endOrder() {
//        return orders.getLast().getProductsAndQuantity();
//    }





//    //TODO - do we need this function at all ?
//    public void updateProductQuantityInOrder(int orderid ,CatalogProduct product , int quantity){
//        getOrder(orderid).updateProductQuantity(product , quantity);
//    }


//    public void addProductToOrder(int supID, int productID, int quantity) {
//        orders.getLast().addProductForSupplier(supID , productID , quantity);
//    }

    //getsupplierAndProduct
//    public HashMap<Integer, LinkedList<GeneralProduct>> suppliersProductsinOrder(int orderid){
//        Order o = getOrder(orderid);
//        return o.getsupplierAndProduct();
//    }

    //addProductForSupplier
//    public void addProductForSupplierInOrder(int orderid , int supid , GeneralProduct product , int quantity){
//        Order o = getOrder(orderid);
//        o.addProductForSupplier(supid , product.getGpID() , quantity);
//    }

    //updateProductQuantity
//    public void updateProductQuantityInOrder(int orderid ,int catalogig , int quantity){
//        Order o = getOrder(orderid);
//        o.updateProductQuantity(catalogig , quantity);
//    }

    //removeProductFromOrder
//    public void removeFromOrder(int productID , int supid) {
//        orders.getLast().removeProductFromOrder(productID , supid);
//    }

//    //changeSupplierForProduct //TODO - not relevant eny more
//    public void changeSupplierForProductInOrder(int orderid , Integer supplierId , int productid ,int catalogid, Integer quantity){
//        Order o = getOrder(orderid);
//        o.changeSupplierForProduct(supplierId , productid , catalogid , quantity);
//    }

















}


