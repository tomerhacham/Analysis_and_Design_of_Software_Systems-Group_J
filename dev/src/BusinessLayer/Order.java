package BusinessLayer;
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
    private HashMap<Integer , LinkedList<Product>> supplierAndProduct; //<supplierid, product>
    private HashMap<Product , Integer> productsAndQuantity; // <product , quntity>

    public Order(int orderID){
        this.orderID = orderID;
        supplierAndProduct = new HashMap<>();
        productsAndQuantity = new HashMap<>();
    }

    public int getOrderID() {
        return orderID;
    }

    public HashMap<Integer, LinkedList<Product>> getsupplierAndProduct() {
        return supplierAndProduct;
    }

    public  HashMap<Product , Integer> getProductsAndQuantity() {return productsAndQuantity;}

    public void addProductForSupplier(Integer supplierID, int productID , Integer quantity) {

        Product product= ProductController.getInstance().getProductsById(supplierID , productID);
        if (product == null ){
            return;
        }

        //in case this is the first product from this supplier
        if (! supplierAndProduct.containsKey(supplierID)){
            supplierAndProduct.put (supplierID , new LinkedList<>());
            supplierAndProduct.get(supplierID).add(product);
            productsAndQuantity.put(product , quantity);
            return;
        }

        LinkedList<Product> supProd = supplierAndProduct.get(supplierID);
        boolean exists = false;
        for (Product p : supProd) {
            if (p.getCatalogID()==product.getCatalogID()){
                exists = true;
                break;
            }
        }

        if(exists){
            int currentQ = productsAndQuantity.get(product);
            //adds the new quantity to an exist product
            productsAndQuantity.replace(product , currentQ , quantity + currentQ);
        }
        else{
            supProd.add(product);
            productsAndQuantity.put(product , quantity);
        }

    }

    public void updateProductQuantity(Integer productId , Integer newquantity) {
        Set<Product> productSet = productsAndQuantity.keySet();
        Product p2Update;
        boolean found= false;
        for (Product p: productSet) {
            if(p.getProductID() == productId) {
                found = true;
                p2Update = p;
                productsAndQuantity.replace(p2Update , newquantity);
            }
        }
        if (!found) {
            Result.setMsg("No Such Product");
        }
    }

    public void removeProductFromOrder(int productId , int supid){
        Product p2remove= null;

        if(!(supplierAndProduct.containsKey(supid))){
            Result.setMsg("No Such Supplier In This Order");
            return;
        }

        for(Product p: supplierAndProduct.get(supid)){
            if (p.getProductID() == productId) {
                p2remove = p;
                productsAndQuantity.remove(p2remove);
                break;
            }
        }

        if(p2remove==null){
            Result.setMsg("No Such Product From This Supplier In The Order");
            return;
        }
        supplierAndProduct.get(supid).remove(p2remove);
    }

    public void changeSupplierForProduct(Integer supplierId , int productid ,int catalogid, Integer quantity){
        Integer supId = 0;
        boolean exists= false;
        for (Integer supplier:supplierAndProduct.keySet()) {
            for (Product p: supplierAndProduct.get(supplier) ) {
                if (p.getProductID() == productid) {
                    supId = supplier;
                    exists = true;
                    break;
                }
            }
            if (exists)
                break;
        }
        if(exists){
            int i = 0;
            LinkedList<Product> oldSupprod = supplierAndProduct.get(supId);
            for (Product p:oldSupprod) {
                if (p.getProductID() == productid){
                    break;
                }
                i++;
            }
            Product pnewSup = oldSupprod.get(i);
            oldSupprod.remove(i);
            productsAndQuantity.remove(pnewSup);


            // if the suppliers product list is empty then remove him from the order
            if(oldSupprod.size()==0){
                supplierAndProduct.remove(supId);
            }

            pnewSup.setCatalogID(catalogid); // update the product catalog id with the new suppliers catalogid
            LinkedList<Product> supProd = supplierAndProduct.get(supplierId);
            if (supProd==null) {
                supProd = new LinkedList<>();
                supProd.add(pnewSup);
            }
            productsAndQuantity.put(pnewSup , quantity);
        }
        else{
            Result.setMsg("The Product Is Not In The Order Or The Supplier Is Not In The System");
        }
    }

    public Double getTotalAmount (){
        Double total = 0.0;
        int quantity;
        Double price;
        for (Integer supId : supplierAndProduct.keySet()){
            LinkedList<Product> listP = supplierAndProduct.get(supId);
            for (Product p : listP) {
                quantity = productsAndQuantity.get(p);
                price = ProductController.getInstance().getUpdatePrice(supId, p, quantity);
                if (price==(-1.0)){
                    break;
                }
                else{
                    total = total + (price * quantity);
                }
            }
        }
        return total;
    }

    public String display() {
        String toDisplay = "Order id : "+'\t'+this.orderID.toString() + '\n';
        toDisplay = toDisplay+ "Product"+'\t'+'\t'+"Quantity" + '\n';

        for (Product p : productsAndQuantity.keySet()){
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

    public String display(int supId) {
        String toDisplay="";

        if (supplierAndProduct.containsKey(supId)) {
            toDisplay = "Order id : "+'\t'+this.orderID.toString() + '\n';
            toDisplay = toDisplay+ "Product"+'\t'+'\t'+"Quantity" + '\n';
            LinkedList<Product> listP = supplierAndProduct.get(supId);
            for (Product p : listP) {
                toDisplay = toDisplay + p.getName() + '\t'+'\t' + productsAndQuantity.get(p).toString() + '\n';
            }
            toDisplay = toDisplay + "Order Total Amount : " + getTotalAmount().toString() + '\n' ;
        }
        else {
            toDisplay= "There is No Orders From This Supplier\n";
        }

        return toDisplay;
    }
}
