package BusinessLayer;

import java.util.HashMap;
import java.util.LinkedList;

public class Contract {

    private LinkedList<String> category; // a supplier can have a lot of categories
    private HashMap<Integer , Product> products; // <catalogid, product>
    private int supplierID;
    private String kindOfSupplier;

    public Contract(LinkedList<String> category , int supplierID , String kindOfSupplier , SupplierCard sp){
        this.category = category;
        this.supplierID = supplierID;
        this.kindOfSupplier = kindOfSupplier;
        products = new HashMap<>();

        ProductController.getInstance().addSupplier(supplierID , sp);
    }

    public Contract(int supplierID , SupplierCard sp){
        category= new LinkedList<>();
        products = new HashMap<>();
        this.supplierID = supplierID;
        kindOfSupplier = "";

        ProductController.getInstance().addSupplier(supplierID,sp);
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public LinkedList<String> getCategory() {
        return category;
    }

    public void setCategory(LinkedList<String> category) {
        this.category = category;
    }

    public void addCategory(String c){
        if (!category.contains(c)){
            category.add(c);
        }
        else{
            System.out.println("The Category IS Already In The Contract");
        }
    }

    public void removeCategory(String c){

        if (category.contains(c)){
            category.remove(c);
            LinkedList <Integer> toRemove = new LinkedList<>();
            for (Integer i : products.keySet()){
                if (products.get(i).getCategory().equals(c) ){
                    toRemove.add(i);
                }
            }
            for (Integer index : toRemove){
                ProductController.getInstance().removeProduct(this.supplierID , products.get(index));
                products.remove(index);
            }
        }
        else {
            System.out.println("The Category IS Not In The Contract Category List");
        }
    }

    public boolean checkCategory(String c){

        boolean containsCategory= false;

        for (String s : category) {
            if(s.equals(c)){
                containsCategory = true;
                break;
            }
        }
        return containsCategory;
    }

    public String getKindOfSupplier() {
        return kindOfSupplier;
    }

    public void setKindOfSupplier(String kindOfSupplier) {
        this.kindOfSupplier = kindOfSupplier;
    }

    public LinkedList<Product> getProducts() {
        LinkedList<Product> listP = new LinkedList<>();
        for (Integer i : products.keySet()){
            listP.add(products.get(i));
        }
        return listP;
    }

    public void setProducts(HashMap<Integer, Product> products) {
        this.products = products;
    }

    public void addProduct(Product p){

        // first check if the supplier can supply this category (check if the category is in the category list)
        String prodCategory = p.getCategory();
        boolean categoryInList = false;

        for (String s : category) {
            if(s.equals(prodCategory)){
                categoryInList = true;
                break;
            }
        }

        if (categoryInList){
            if (products.containsKey(p.getCatalogID())){
                System.out.println("The Product Is Already In Your Product List");
            }
            else{
                products.put(p.getCatalogID() , p);
                ProductController.getInstance().addProduct(supplierID ,p); // add to product controller
            }
        }
        else{
            System.out.println("The Category Of This Product Is Not In The Category List ");
        }
    }

    public boolean checkProduct(int catalogid){

        if (ProductController.getInstance().checkProduct(supplierID , catalogid) == null)
            return false;

        else
            return true;
    }

    public  void removeProduct(Integer productcatalogid){
        Product p = products.get(productcatalogid); // get product
        ProductController.getInstance().removeProduct(supplierID , p); // remove product from product controller
        products.remove(productcatalogid); // remove product from contract
    }

    public void removeSupplier(){
        ProductController.getInstance().removeSupplierAndProducts(supplierID);
    }

}
