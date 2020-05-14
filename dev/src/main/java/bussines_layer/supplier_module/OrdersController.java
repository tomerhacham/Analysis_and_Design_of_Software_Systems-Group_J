package bussines_layer.supplier_module;
import bussines_layer.Result;
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


    //region methods

    // static method to create instance of Singleton class
    public static OrdersController getInstance(){
        if (instance == null)
            instance = new OrdersController();

        return instance;
    }

    //get specific order from the systems order list
    public Result<Order> getOrder(int orderid){
        for (Order order:orders) {
            if(order.getOrderID() == orderid){
                return new Result<>(true, order, String.format("Find order"));
            }
        }
        return new Result<Order>(false, null, String.format(" The Order does not exist"));

    }

    public LinkedList<Order> getAllOrders() { return this.orders;}

    public Result addProductToOrder(int orderID , CatalogProduct product , Integer quantity , Float price){
        Result<Order> result = getOrder(orderID);
        if (result.isOK()){
            return result.getData().addProduct(product, quantity, price);
        }
        return result;
    }

    //get total price of an order
    public Result<Float> getTotalPrice (Integer orderID){
        for (Order o : orders){
            if (o.getOrderID() == orderID) {
                Result<Order> result = getOrder(orderID);
                if (result.isOK()) {
                    return result.getData().getTotalAmount();
                }
            }
        }
        return new Result<>(true, new Float(-1), String.format(" The Order : %d dose not exist", orderID));
    }

    public Result<LinkedList<String>> displayAllSupplierOrders(int supId) {
        LinkedList<String> toDisplay = new LinkedList<>();

        if(orders.isEmpty()){
            toDisplay.add("There Are No Orders In The System"+'\n');
            return new Result(false,toDisplay, String.format("There Are No Orders In The System\n"));
        }

        for (Order o : orders){
            if(o.getSupplierID()==supId){
                toDisplay.add(o.display().getData());
            }
        }
        return new Result(true,toDisplay, String.format("Display all supplier Orders"));
    }

    public Result<LinkedList<String>> displayAllOrders (){
        LinkedList<String> toDisplay = new LinkedList<>();

        if(orders.isEmpty()){
            toDisplay.add("There Are No Orders In The System"+'\n');
            return new Result(false,toDisplay, String.format("There Are No Orders In The System\n"));
        }

        for (Order o: orders){
            toDisplay.add(o.display().getData());
        }
        return new Result(true,toDisplay, String.format("Display all Orders"));
    }

    public Result<HashMap<CatalogProduct, Integer>> getProductsToAcceptOrder(Integer orderID) {
        for (Order order: orders){
            if (order.getOrderID() == orderID && order.getStatus()== OrderStatus.sent){
                if (order.getStatus()== OrderStatus.sent){
                    return new Result<>(true,order.getProductsAndQuantity(), String.format("Order %d has been received", orderID));
                }
                else if(order.getStatus()== OrderStatus.inProcess){
                    return new Result<>(false,null, String.format("Order %d hasn't been sent", orderID));
                }
                else if (order.getStatus()== OrderStatus.received){
                    return new Result<>(false,null, String.format("Order %d already received", orderID));
                }
            }
        }
        return new Result<>(false,null, String.format("Order %d does not exist", orderID));
    }

    //endregion

    //region OutOfStockOrder
    public Result<Integer> createOrder(Integer supplierID , OrderType type){
        orderidCounter++;
        orders.add(new Order(orderidCounter  , supplierID , type));
        return new Result(true,orderidCounter, String.format("The new order id is  : %d" ,orderidCounter));
    }

    //endregion

    //region PeriodicOrder
    public Result<Integer> createPeriodicOrder(Integer supplierID ){
        orderidCounter++;
        orders.add(new Order(orderidCounter , supplierID ,OrderType.PeriodicOrder ));
        return new Result(true,orderidCounter, String.format("The new order id is  : %d" ,orderidCounter));
    }

    public Result removeProductFromPeriodicOrder(Integer orderID , CatalogProduct product) {
        if(getOrder(orderID).getData().getType() == OrderType.PeriodicOrder){
            return getOrder(orderID).getData().removeProductFromPeriodicOrder(product);
        }

        return new Result(false,product, String.format("The order : %d is not a periodic order , therefore the product %s can not be removed", product.getName() , orderID));
    }

    public Result updateProductQuantityInPeriodicOrder(Integer orderId , CatalogProduct product , Integer newQuantity , Float newPrice){
        if(getOrder(orderId).getData().getType() == OrderType.PeriodicOrder){
            return getOrder(orderId).getData().updateProductQuantityInPeriodicOrder(product , newQuantity , newPrice);
        }

        return new Result(false,orderId, String.format("The order %d is not a periodic order therefore can not be modified " , orderId));

    }

    public Result removePeriodicOrder (Integer orderId){
        if (getOrder(orderId).getData().getType() != OrderType.PeriodicOrder){
            return new Result(false,orderId, String.format("The order : %d is not a periodic order , therefore the order can not be removed", orderId));
        }
        orders.remove(getOrder(orderId));
        return new Result(true,orderId, String.format("The periodic order : %d has been removed", orderId));
    }


    //endregion




//    //changeSupplierForProduct //TODO - not relevant eny more
//    public void changeSupplierForProductInOrder(int orderid , Integer supplierId , int productid ,int catalogid, Integer quantity){
//        Order o = getOrder(orderid);
//        o.changeSupplierForProduct(supplierId , productid , catalogid , quantity);
//    }



}


