package BusinessLayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class Order {

    public enum statusOption {inProccess , Done , Exception};
    private Integer orderID;
    private HashMap<Integer , LinkedList<Product>> supplierAndProduct; //<supplierid, product>
    private HashMap<Product , Integer> productsAndQuantity; // <product , quntity>
    private statusOption status;

    public Order(int orderID){
        this.orderID = orderID;
        supplierAndProduct = new HashMap<>();
        productsAndQuantity = new HashMap<>();
        status = statusOption.inProccess;
    }

    public int getOrderID() {
        return orderID;
    }

    public void endOrder(){ status = statusOption.Done;}

    public HashMap<Integer, LinkedList<Product>> getsupplierAndProduct() {
        return supplierAndProduct;
    }

    public  HashMap<Product , Integer> getProductsAndQuantity() {return productsAndQuantity;}

    public void addProductForSupplier(Integer supplierID, int productID , Integer quantity) {
        Product product= ProductController.getInstance().getProductsById(supplierID , productID);
        if (product == null ){
            System.out.println("The Supplier has no such product");
            System.out.println("Please Enter Another Product");
            status = statusOption.Exception;
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
        for (Product p: productSet) {
            if(p.getProductID() == productId) {
                p2Update = p;
                productsAndQuantity.replace(p2Update , newquantity);
            }
        }
    }

    public void removeProductFromOrder(int productId){
        Integer supId = 0;
        boolean exists= false;
        boolean Item = false;
        Product p2remove= null;
        for (Integer supplier:supplierAndProduct.keySet()) {
            for (Product p: supplierAndProduct.get(supplier) ) {
                // we want to delete the product from all suppliers
                if (p.getProductID() == productId) {
                    supId = supplier;
                    exists = true;
                    Item = true;
                    p2remove = p;
                    productsAndQuantity.remove(p2remove);
                    break;
                }
            }
            if(exists)
                supplierAndProduct.get(supId).remove(p2remove);

            exists = false;
        }
        if (!Item){
            System.out.println("The Product Is Not In The Order");
        }
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
            System.out.println("The Product Is Not In The Order Or The Supplier Is Not In The System");
        }
    }

    public Integer getTotalAmount (){
        int total = 0;
        int quantity , price;
        for (Integer supId : supplierAndProduct.keySet()){
            LinkedList<Product> listP = supplierAndProduct.get(supId);
            for (Product p : listP) {
                quantity = productsAndQuantity.get(p);
                price = ProductController.getInstance().getUpdatePrice(supId, p, quantity);
                if (price==(-1)){
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
        toDisplay = toDisplay+ "Product Name "+'\t'+'\t'+"Quantity" + '\n';

        for (Product p : productsAndQuantity.keySet()){
            toDisplay = toDisplay+ p.getName() + '\t' +'\t'+ productsAndQuantity.get(p).toString() + '\n';
        }

        if (productsAndQuantity.keySet().size()==0){
            toDisplay = toDisplay + "---"+'\t' +'\t'+"---\n";
            toDisplay = toDisplay +"No Products In This Order";
        }
        else{
            toDisplay = toDisplay + "Order Total Amount : " + getTotalAmount().toString() +'\n' + '\n';
        }
        return toDisplay;
    }

    public String display(int supId) {
        String toDisplay = "Order id : "+'\t'+this.orderID.toString() + '\n';
        toDisplay = toDisplay+ "Product Name "+'\t'+'\t'+"Quantity" + '\n';

        if (supplierAndProduct.containsKey(supId)) {
            LinkedList<Product> listP = supplierAndProduct.get(supId);
            for (Product p : listP) {
                toDisplay = toDisplay + p.getName() + '\t'+'\t' + productsAndQuantity.get(p).toString() + '\n';
            }
            toDisplay = toDisplay + "Order Total Amount : " + getTotalAmount().toString() + '\n' + '\n';
        }

        return toDisplay;
    }
}
