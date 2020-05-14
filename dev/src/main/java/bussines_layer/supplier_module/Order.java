package bussines_layer.supplier_module;
import bussines_layer.Result;
import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.ProductController;

import java.util.Date;
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
    private SupplierCard supplier;
    private OrderType type;
    private OrderStatus status;
    private HashMap<CatalogProduct, Integer> productsAndQuantity; // <product , quantity>
    private HashMap<CatalogProduct , Float> productsAndPrice; //<product, price>
    private Integer dayToDeliver;
    private Date issuedDate;


    //constructor to out of stock order
    public Order(int orderID , SupplierCard supplier , OrderType type){
        this.orderID = orderID;
        productsAndQuantity = new HashMap<>();
        this.supplier = supplier;
        productsAndPrice = new HashMap<>();
        this.type = type;
        this.status=OrderStatus.inProcess;
        this.dayToDeliver = null;
    }

    //constructor to periodic order
    public Order(int orderID , SupplierCard supplierID , OrderType type , Integer dayToDeliver){
        this.orderID = orderID;
        productsAndQuantity = new HashMap<>();
        this.supplier = supplier;
        productsAndPrice = new HashMap<>();
        this.type = type;
        this.status=OrderStatus.inProcess;
        this.dayToDeliver = dayToDeliver;
    }

    public int getOrderID() {
        return orderID;
    }

    public SupplierCard getSupplier() {
        return supplier;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public Integer getSupplierID() {
        return supplier.getId();
    }

    public Enum<OrderStatus> getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setIssuedDate (Date date){this.issuedDate = date;}

    public Enum<OrderType> getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public void setSupplier (SupplierCard supplier){ this.supplier = supplier;}

    public  HashMap<CatalogProduct, Float> getProductsAndPrice() {return productsAndPrice;}

    public  HashMap<CatalogProduct, Integer> getProductsAndQuantity() {return productsAndQuantity;}

    public Result updateDayToDeliver(Integer dayToDeliver) {
        if(this.type != OrderType.PeriodicOrder){
            return new Result<>(false,null, String.format("The delivery day has NOT been updated to %d in the order:%d because it is not a periodic order", dayToDeliver , getOrderID()));
        }
        this.dayToDeliver = dayToDeliver;
        return new Result<>(true,dayToDeliver, String.format("The delivery day has been updated to %d in the order:%d", dayToDeliver , getOrderID()));
    }

    public Result getDayToDeliver() {
        return new Result<>(true,dayToDeliver, String.format("The delivery day it %d in the order:%d", dayToDeliver , getOrderID()));
    }

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
        return new Result<>(true,product, String.format("The product %s has been updated in the order:%d", product.getName() , getOrderID()));
    }

    public Result<String> display() {
        String toDisplay = "Order Details - Type " + type + "order";
        toDisplay = toDisplay + "Supplier name : " + supplier.getSupplierName() + '\t'+ "Address : " + supplier.getAddress() + '\t' + "Order ID : " + orderID;
        toDisplay = toDisplay + "Supplier ID : "+'\t'+supplier.getId() +'\t'+ "IssuedDate : "+ issuedDate + '\t' +"Phone number : "+ supplier.getPhoneNumber()+ '\n';
        toDisplay = toDisplay+ "Product catalogID"+'\t'+'\t'+"Product name"+'\t'+'\t'+ "Quantity" + '\t'+'\t' + "Origin price"+ '\t'+'\t' + "Final price"+'\n';

        for (CatalogProduct p : productsAndQuantity.keySet()){
            toDisplay = toDisplay+ p.getCatalogID() +'\t'+'\t'+ p.getName() +'\t'+'\t'+ productsAndQuantity.get(p).toString() +'\t' +'\t'+ p.getSupplierPrice()+'\t' +'\t'+ this.productsAndPrice.get(p).toString() +'\n';
        }

        if (productsAndQuantity.isEmpty()){
            toDisplay = toDisplay + "----------------\n";
            toDisplay = toDisplay +"No Products In This Order\n";
        }
        else{
            toDisplay = toDisplay + "Order Total Amount : " + getTotalAmount().toString() +'\n'+"Do Not Forget To Send The Order To The supplier !\n" ;
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
