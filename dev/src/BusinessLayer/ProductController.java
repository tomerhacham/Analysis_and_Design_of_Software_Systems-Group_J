package BusinessLayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//Singleton
public class ProductController {

    // static variable single_instance of type Singleton
    private static ProductController instance = null;

    private LinkedList<Product> AllProducts;
    private HashMap<Integer , LinkedList<Product>> SupProducts; // <supplierId , ProductsList>
    private HashMap<Integer , SupplierCard> supplierCardHashMap; // <supplierId , SupplierCard>


    private ProductController(){
        AllProducts = new LinkedList<>();
        SupProducts = new HashMap<>();
        supplierCardHashMap = new HashMap<>();
    }

    // static method to create instance of Singleton class
    public static ProductController getInstance(){
        if (instance == null)
            instance = new ProductController();

        return instance;
    }

    public void addSupplier(int supid, SupplierCard sp){
         SupProducts.put(supid , new LinkedList<>());
         supplierCardHashMap.put(supid , sp);
    }

    public void addProduct(Integer supId , Product product){

        // Notice : we already checked that the products category is in the Suppliers list

        LinkedList<Product> productlist ;
        if (SupProducts.containsKey(supId)){
            productlist = SupProducts.get(supId);
            productlist.add(product);
            AllProducts.add(product);
        }
        else{
            System.out.println("The Supplier Dose Not Have A Contract");
        }
    }

    public void removeProduct(Integer supid , Product product){
        if (SupProducts.containsKey(supid)){

            LinkedList<Product> products = SupProducts.get(supid);
            products.remove(product);

            int i =0;
            boolean erase= false;
            while(i<AllProducts.size()){
                if (product.getCatalogID() == AllProducts.get(i).getCatalogID()){
                    erase = true;
                    break;
                }
                i++;
            }

            //the product is supposed to be in the list anyway but just incase
            if(erase){
                AllProducts.remove(i);
            }
        }

        else {
            System.out.println("The Supplier Is Not In The List");
        }
    }

    public LinkedList<Product> getAllProducts() {
        return AllProducts;
    }

    public LinkedList<Product> getAllSupProducts(Integer supid){

        if (SupProducts.containsKey(supid)){
            return SupProducts.get(supid);
        }
        else{
            System.out.println("The Supplier Is Not In The List");
            return null;
        }
    }

    public void removeSupplierAndProducts(Integer supid){
        if (SupProducts.containsKey(supid)){
            LinkedList<Product> products = SupProducts.get(supid);

            for (Product p:products) {

                int i =0;
                boolean erase= false;
                while(i<AllProducts.size()){
                    if (p.getCatalogID() == AllProducts.get(i).getCatalogID()){
                        erase = true;
                        break;
                    }
                    i++;
                }

                //the product is supposed to be in the list anyway but just incase
                if(erase){
                    AllProducts.remove(i);
                }
            }

            SupProducts.remove(supid);
        }
        else {
            System.out.println("The Supplier Is Not In The List");
        }
    }

    public Product checkProduct(int supid , int catalogid){

        boolean containProduct = false;

        LinkedList<Product> sp = SupProducts.get(supid);
        Product p = null;
        int i = 0;

        for (Product product : sp) {
            if(product.getCatalogID() == catalogid){
                containProduct = true;
                break;
            }
            i++;
        }

        if (containProduct){
            p = sp.get(i);
        }
        return p;
    }

    public Product getProductsById (int supID, int productID){
        Product p= null;
        LinkedList<Product> list = SupProducts.get(supID);
        for (Product product : list){
            if (product.getProductID() == productID)
                p= product;
        }
        return p;
    }

    public int getUpdatePrice (int supId , Product product, int quantity){

        int price = -1;

        if (!(supplierCardHashMap.containsKey(supId))) {
            System.out.println("There Is No Such Supplier");
            return price;
        }

        CostEngineering cost = supplierCardHashMap.get(supId).getCostEngineering();
        if (cost == null) {
            return product.getPrice();
        }

        price = cost.getUpdatePrice(product.getCatalogID() , quantity);

        // no update in product price
        if (price == -1)
            return product.getPrice();

        return price;

    }
}
