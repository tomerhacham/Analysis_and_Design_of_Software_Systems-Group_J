package bussines_layer.supplier_module;

import bussines_layer.BranchController;
import bussines_layer.Result;
import bussines_layer.SupplierCard;
import bussines_layer.enums.OrderStatus;
import bussines_layer.enums.OrderType;
import bussines_layer.inventory_module.CatalogProduct;
import data_access_layer.DTO.OrderDTO;

import java.util.Date;
import java.util.HashMap;

/**
 * Class Order.
 * Holds all the Information about an Order.
 *
 * Functionality that related to Order information.
 *
 */

public class Order {

    private Integer orderID;
    private OrderType type;
    private OrderStatus status;
    private Integer dayToDeliver;
    private Date issuedDate;
    private Integer branch_id;
    private HashMap<CatalogProduct, Integer> productsAndQuantity; // <product , quantity>
    private HashMap<CatalogProduct , Float> productsAndPrice; //<product, price>
    private SupplierCard supplier;
    private Float totalWeight;


    //constructor to out of stock order
    public Order(Integer branch_id,Integer orderID , SupplierCard supplier , OrderType type){
        this.orderID = orderID;
        this.branch_id = branch_id;
        productsAndQuantity = new HashMap<>();
        this.supplier = supplier;
        productsAndPrice = new HashMap<>();
        this.type = type;
        this.status=OrderStatus.inProcess;
        this.dayToDeliver = null;
        this.totalWeight = (float)0;
        this.issuedDate = BranchController.system_curr_date;
    }

    //constructor to periodic order
    public Order(Integer branch_id , Integer orderID , SupplierCard supplier , OrderType type , Integer dayToDeliver){
        this.branch_id=branch_id;
        this.orderID = orderID;
        productsAndQuantity = new HashMap<>();
        this.supplier = supplier;
        productsAndPrice = new HashMap<>();
        this.type = type;
        this.status=OrderStatus.inProcess;
        this.dayToDeliver = dayToDeliver;
        this.totalWeight = (float)0;
        this.issuedDate = BranchController.system_curr_date;
    }

    public Order(OrderDTO orderDTO){
        this.orderID = orderDTO.getOrder_id();
        this.type = orderDTO.getType();
        this.status = orderDTO.getStatus();
        this.dayToDeliver = orderDTO.getDaytodeliver();
        this.issuedDate = orderDTO.getIssuedDate();
        this.branch_id = orderDTO.getBranch_id().getBranch_id();
        this.totalWeight = orderDTO.getTotalweight();
    }

    public int getOrderID() {
        return orderID;
    }

    public Integer getBranch_id() {
        return branch_id;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setIssuedDate (Date date){this.issuedDate = date;}

    public OrderType getType() {
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

    public Result<Integer> getDayToDeliver() {
        return new Result<>(true,dayToDeliver, String.format("The delivery day it %d in the order:%d", dayToDeliver , getOrderID()));
    }

    public Result<CatalogProduct> addProduct(CatalogProduct product , Integer quantity , Float price){

        if (productsAndQuantity.containsKey(product)){
            return new Result<>(false,product, String.format("The product %s is already in the order:%d . Therefore it is not possible to add the product (only possible is to change the quantity)", product.getName() , getOrderID()));
        }
        productsAndQuantity.put(product , quantity);
        productsAndPrice.put(product , price);
        addToTotalWeight(product.getWeight());
        return new Result<>(true,product, String.format("The product %s has been added to the order:%d", product.getName() , getOrderID()));
    }

    public Result removeProductFromPeriodicOrder(CatalogProduct product){
        if (!(productsAndQuantity.containsKey(product))){
            return new Result<>(false,product, String.format("The product %s is not in the order:%d , therefore can not be removed", product.getName() , getOrderID()));
        }
        productsAndQuantity.remove(product);
        productsAndPrice.remove(product);
        subFromTotalWeight(product.getWeight());

        if (productsAndQuantity.size()==0){
            return new Result<>(true,product, String.format("The product %s has been removed from the order:%d , - But notice , the order now is empty and therefore is deleted", product.getName() , getOrderID()));
        }
        return new Result<>(true,product, String.format("The product %s has been removed from the order:%d", product.getName() , getOrderID()));
    }

    public Result updateProductQuantityInPeriodicOrder(CatalogProduct product , Integer newQuantity , Float newPrice){
        Integer oldQuantity = productsAndQuantity.get(product);
        Float subFromTotalWeight = product.getWeight()*oldQuantity;
        Float addToTotalWeight = product.getWeight()*newQuantity;
        productsAndQuantity.replace(product , newQuantity);
        productsAndPrice.replace(product , newPrice);

        subFromTotalWeight(subFromTotalWeight);
        addToTotalWeight(addToTotalWeight);

        return new Result<>(true,product, String.format("The product %s has been updated in the order:%d", product.getName() , getOrderID()));
    }

    public Result<String> display() {
        String toDisplay = "Order Details - Type " + type + "\n";
        toDisplay = toDisplay + "Supplier name: " + supplier.getSupplierName() + '\t'+ "Address: " + supplier.getAddress() + '\t' + "Order ID: " + orderID;
        toDisplay = toDisplay + "\tSupplier ID: "+'\t'+supplier.getId() +'\t'+ "IssuedDate: "+ issuedDate + '\t' +"Phone number : "+ supplier.getPhoneNumber()+ '\n';
        toDisplay = toDisplay+ "CatalogID"+"\t\t"+"Name"+"\t\t\t\t\t\t\t"+ "Quantity" + "\t\t" + "Origin price"+ "\t\t" + "Final price"+'\n';

        for (CatalogProduct p : productsAndQuantity.keySet()){
            toDisplay = toDisplay+ p.getCatalogID() +"\t\t\t\t"+ p.getName() +"\t\t"+ productsAndQuantity.get(p).toString() +"\t\t"+ p.getSupplierPrice()+'\t' +'\t'+ this.productsAndPrice.get(p).toString() +'\n';
        }

        if (productsAndQuantity.isEmpty()){
            toDisplay = toDisplay + "----------------\n";
            toDisplay = toDisplay +"No Products In This Order\n";
        }
        else{
            toDisplay = toDisplay.concat(String.format("Order Total Amount: %.02f\n", getTotalAmount().getData()));
        }
        return new Result<>(true, toDisplay, String.format(" The Order Is Ready And Has Been Sent Back To The Employee: %s", toDisplay));
    }

    public Result<String> displayProductsInOrder() {
        String toDisplay = "";
        toDisplay = toDisplay+ "Product Name\t\t\tCatalogID\t\t\tQuantity";

        for (CatalogProduct p : productsAndQuantity.keySet()){
            toDisplay = toDisplay+p.getName()+"\t\t\t"+ p.getCatalogID() +"\t\t\t"+ productsAndQuantity.get(p)+'\n';
        }

        return new Result<>(true, toDisplay, "");
    }

    public Result<Float> getTotalAmount (){
        Float total = (float) 0;
        int quantity;
        Float price;

        for (CatalogProduct cp :productsAndQuantity.keySet()) {
            quantity = productsAndQuantity.get(cp);
            price = productsAndPrice.get(cp);
            total = total + (price * quantity);
        }
        return new Result<>(true, total, String.format(" The orders total amount is: %f", total));
    }

    public Float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Float totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void setProductsAndQuantity(HashMap<CatalogProduct, Integer> productsAndQuantity) {
        this.productsAndQuantity = productsAndQuantity;
        Float addToTotalWeigt  = (float) 0;
        for (CatalogProduct p: productsAndQuantity.keySet()) {
            addToTotalWeigt = addToTotalWeigt + p.getWeight();
        }
        addToTotalWeight(addToTotalWeigt);
    }

    public void setProductsAndPrice(HashMap<CatalogProduct, Float> productsAndPrice) {
        this.productsAndPrice = productsAndPrice;
    }

    public void replaceDetails(CatalogProduct product, CatalogProduct newCatalogProduct, Float price) {
        //update productsAndQuantity
        Integer quantity = productsAndQuantity.get(product);
        productsAndQuantity.remove(product);
        productsAndQuantity.put(newCatalogProduct , quantity);

        subFromTotalWeight(product.getWeight());
        addToTotalWeight(newCatalogProduct.getWeight());

        //update productsAndPrice
        productsAndPrice.remove(product);
        productsAndPrice.put(newCatalogProduct , price);
    }

    public void addToTotalWeight(Float weight){
        this.totalWeight = this.totalWeight + weight;
    }
    public void subFromTotalWeight(Float weight){
        this.totalWeight = this.totalWeight - weight;
    }
}
