package bussines_layer.supplier_module;
import bussines_layer.BranchController;
import bussines_layer.Result;
import bussines_layer.SupplierCard;
import bussines_layer.enums.OrderStatus;
import bussines_layer.enums.OrderType;
import bussines_layer.inventory_module.CatalogProduct;
import data_access_layer.Mapper;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Responsible of all the Orders in the system.
 * Holds documentation of all the Orders.
 *
 * Functionality that related to Orders.
 *
 */



//Singleton
public class OrdersController {

    private LinkedList<Order> orders;
    private Integer next_id;
    private Integer branch_id;
    private Mapper mapper;

    public OrdersController(Integer branch_id){
        this.branch_id = branch_id;
        this.mapper=Mapper.getInstance();
        this.orders=mapper.loadOrders(branch_id);
        if(orders.isEmpty()){orders = new LinkedList<>();}
        this.next_id=mapper.loadID("order");
    }

    //region methods

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
            result= result.getData().addProduct(product, quantity, price);
            if (result.isOK()){mapper.addCatalogProductToOrder(product,result.getData(),quantity,price);}
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

    public Result<String> issueOrder (Order order){
        order.setStatus(OrderStatus.sent);
        order.setIssuedDate(BranchController.system_curr_date);
        return order.display();
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
            return new Result<>(false,toDisplay, String.format("There Are No Orders In The System\n"));
        }

        for (Order o: orders){
            toDisplay.add(o.display().getData());
        }
        return new Result<>(true,toDisplay, String.format("Display all Orders"));
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

    public Integer getBranch_id() {
        return branch_id;
    }

    /**
     * allocate the next free ID
     * @return
     */
    private Integer getNext_id() {
        Integer next = next_id;
        this.next_id++;
        mapper.writeID("contract",next_id);
        return next;
    }

    //endregion

    //region OutOfStockOrder
    public Result<Integer> createOrder(SupplierCard supplier , OrderType type){
        Integer id = getNext_id();
        Order order = new Order(branch_id,id, supplier , type);
        orders.add(order);
        mapper.create(order);
        return new Result<>(true,id, String.format("The new order id is  : %d" ,id));
    }

    //endregion

    //region PeriodicOrder
    public Result<Integer> createPeriodicOrder(SupplierCard supplier,Integer dayToDeliver){
        Integer id = getNext_id();
        Order order = new Order(branch_id,id , supplier ,OrderType.PeriodicOrder,dayToDeliver );
        orders.add(order);
        mapper.create(order);
        return new Result<>(true,id, String.format("The new order id is  : %d" ,id));
    }

    public Result removeProductFromPeriodicOrder(Integer orderID , CatalogProduct product) {
        Result result;
        if(getOrder(orderID).getData().getType() == OrderType.PeriodicOrder){
            result= getOrder(orderID).getData().removeProductFromPeriodicOrder(product);
            Order order=getOrder(orderID).getData();
            if (result.isOK()){mapper.deleteCatalogProductFromOrder(order,product);}
        }
        else {
            result = new Result<>(false, product, String.format("The order : %d is not a periodic order , therefore the product %s can not be removed", product.getName(), orderID));
        }
        return result;
    }

    public Result updateProductQuantityInPeriodicOrder(Integer orderId , CatalogProduct product , Integer newQuantity , Float newPrice){
        Result result;
        if(getOrder(orderId).getData().getType() == OrderType.PeriodicOrder){
            result= getOrder(orderId).getData().updateProductQuantityInPeriodicOrder(product , newQuantity , newPrice);
            Order order = getOrder(orderId).getData();
            if (result.isOK()){mapper.update(order);}
        }
        else{
            result= new Result<>(false,orderId, String.format("The order %d is not a periodic order therefore can not be modified " , orderId));
        }
        return result;
    }

    public Result removePeriodicOrder (Integer orderId){
        if (getOrder(orderId).getData().getType() != OrderType.PeriodicOrder){
            return new Result<>(false,orderId, String.format("The order : %d is not a periodic order , therefore the order can not be removed", orderId));
        }
        Order order=getOrder(orderId).getData();
        orders.remove(order);
        mapper.delete(order);
        return new Result<>(true,orderId, String.format("The periodic order : %d has been removed", orderId));
    }

    public Result updateDayToDeliver(Integer orderid , Integer dayToDeliver){
        Result result=getOrder(orderid).getData().updateDayToDeliver(dayToDeliver);
        Order order = getOrder(orderid).getData();
        mapper.update(order);
        return result;
}

    public Result<LinkedList<String>> issuePeriodicOrder(){

        Integer sys_day = BranchController.system_curr_date.getDay();
        LinkedList<String> periodicOrdersToIsuue = new LinkedList<>();

        for (Order order : orders) {
            //issue one day before delivery date
            Integer order_day =  order.getDayToDeliver().getData();
            if (order_day == 1) { order_day=7;}
            else { order_day = order_day-1;}

            if ((order.getType()== OrderType.PeriodicOrder) && (order_day).equals(sys_day)){
                periodicOrdersToIsuue.add(issueOrder(order).getData());
            }
        }

        if(periodicOrdersToIsuue.size()>0){
            return new Result<>(true,periodicOrdersToIsuue, String.format("All periodic orders that delivery day is tomorrow had been reported"));
        }
        return new Result<>(false,null, String.format("There are no periodic orders to issue"));
    }

    //endregion

}


