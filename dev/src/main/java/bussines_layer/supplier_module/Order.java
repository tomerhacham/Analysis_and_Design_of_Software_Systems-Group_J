package bussines_layer.supplier_module;
import bussines_layer.Result;
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
enum OrderType
{PeriodicOrder,OutOfStockOrder;}

enum OrderStatus
{received,inProcess , sent}

public class Order {

    private Integer orderID;
    private Integer supplierID;
    private OrderType type;
    private OrderStatus status;
    private HashMap<CatalogProduct, Integer> productsAndQuantity; // <product , quantity>
    private HashMap<CatalogProduct , Float> productsAndPrice; //<product, price>


    public Order(int orderID , Integer supplierID , OrderType type){
        this.orderID = orderID;
        productsAndQuantity = new HashMap<>();
        this.supplierID = supplierID;
        productsAndPrice = new HashMap<>();
        this.type = type;
        this.status=OrderStatus.inProcess;
    }

    public int getOrderID() {
        return orderID;
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }
    public void setSupplierID (Integer supplierID){ this.supplierID = supplierID;}

    public  HashMap<CatalogProduct, Float> getProductsAndPrice() {return productsAndPrice;}

    public  HashMap<CatalogProduct, Integer> getProductsAndQuantity() {return productsAndQuantity;}

    public Result addProduct(CatalogProduct product , Integer quantity , Float price){

        if (productsAndQuantity.containsKey(product)){
            return new Result<>(false,product, String.format("The product %s is already in the order:%d . Therefore it is not possible to add the product (only possible is to change the quantity)", product.getName() , getOrderID()));
        }
        productsAndQuantity.put(product , quantity);
        productsAndPrice.put(product , price);
        return new Result<>(true,product, String.format("The product %s has been added to the order:%d", product.getName() , getOrderID()));
    }

    public Result removeProductFromPeriodicOrder(CatalogProduct product){
        if (!(productsAndQuantity.containsKey(product))){
            return new Result<>(false,product, String.format("The product %s is not in the order:%d , therefore can not be removed", product.getName() , getOrderID()));
        }
        productsAndQuantity.remove(product);
        productsAndPrice.remove(product);

        if (productsAndQuantity.size()==0){
            return new Result(true,product, String.format("The product %s has been removed from the order:%d , - But notice , the order now is empty and therefore is deleted", product.getName() , getOrderID()));
        }
        return new Result(true,product, String.format("The product %s has been removed from the order:%d", product.getName() , getOrderID()));
    }

    public Result updateProductQuantityInPeriodicOrder(CatalogProduct product , Integer newQuantity , Float newPrice){
        productsAndQuantity.replace(product , newQuantity);
        productsAndPrice.replace(product , newPrice);
        return new Result(true,product, String.format("The product %s has been updated in the order:%d", product.getName() , getOrderID()));
    }

    public Result<String> display() {
        status = OrderStatus.sent;
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
            toDisplay = toDisplay + "Order Total Amount : " + getTotalAmount().toString() +'\n'+" Employee - Do Not Forget To Send The Order To The supplier\n" ;
        }
        return new Result<>(true, toDisplay, String.format(" The Order Is Ready And Has Been Sent Back To The Employee: %s", toDisplay));
    }

    public Result<Float> getTotalAmount (){
        Float total = new Float(0);
        int quantity;
        Float price;

        for (CatalogProduct cp :productsAndQuantity.keySet()) {
            quantity = productsAndQuantity.get(cp);
            price = productsAndPrice.get(cp);
            total = total + (price * quantity);
        }
        return new Result<>(true, total, String.format(" The orders total amount is : %d", total));
    }

   }
