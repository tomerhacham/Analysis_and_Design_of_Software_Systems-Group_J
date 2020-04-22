package BusinessLayer;
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
    public static OrdersController getInstance()
    {
        if (instance == null)
            instance = new OrdersController();

        return instance;
    }

    public int createOrder(){
        orderidCounter++;
        orders.add(new Order(orderidCounter));
        return orderidCounter;
    }

    //get specific order from the systems order list
    public Order getOrder(int orderid){
        int i =0;
        for (Order or:orders) {
            if(or.getOrderID() == orderid){
                break;
            }
            i++;
        }

        return orders.get(i);
    }

    public LinkedList<Order> getOrders() { return this.orders;}

    //getsupplierAndProduct
    public HashMap<Integer, LinkedList<Product>> suppliersProductsinOrder(int orderid){
        Order o = getOrder(orderid);
        return o.getsupplierAndProduct();
    }

    //addProductForSupplier
    public void addProductForSupplierInOrder(int orderid , int supid , Product product , int quantity){
        Order o = getOrder(orderid);
        o.addProductForSupplier(supid , product.getProductID() , quantity);
    }

    //updateProductQuantity
    public void updateProductQuantityInOrder(int orderid ,int catalogig , int quantity){
        Order o = getOrder(orderid);
        o.updateProductQuantity(catalogig , quantity);
    }

    //removeProductFromOrder
    public void removeFromOrder(int productID , int supid) {
        orders.getLast().removeProductFromOrder(productID , supid);
    }

    //changeSupplierForProduct
    public void changeSupplierForProductInOrder(int orderid , Integer supplierId , int productid ,int catalogid, Integer quantity){
        Order o = getOrder(orderid);
        o.changeSupplierForProduct(supplierId , productid , catalogid , quantity);
    }

    //get total price of an order
    public Double getTotalPrice (int orderID){
        for (Order o : orders){
            if (o.getOrderID() == orderID)
                return o.getTotalAmount();
        }
        return -1.0;
    }

    public Double getTotalAmountLastOrder (){
        return orders.getLast().getTotalAmount();
    }

    public void addProductToOrder(int supID, int productID, int quantity) {

        orders.getLast().addProductForSupplier(supID , productID , quantity);
    }

    public  HashMap<Product , Integer> endOrder() {
       return orders.getLast().getProductsAndQuantity();
    }

    public void removeOrder (){
        orders.removeLast();
    }

    public void updateProductQuantity(int productID, int quantity) {
        orders.getLast().updateProductQuantity(productID, quantity);
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

    public LinkedList<String> displayOrderBySupplier(int supId) {
        LinkedList<String> toDisplay = new LinkedList<>();

        if(orders.isEmpty()){
            toDisplay.add("There Are No Orders In The System"+'\n');
            return toDisplay;
        }

        for (Order o : orders){
            toDisplay.add(o.display(supId));
        }
        return toDisplay;
    }

    public Product createNewProduct(int productID, String name, int price, String producer, String category, int catalogid) {
        return new Product(productID, name, price, producer, category, catalogid);
    }
}


