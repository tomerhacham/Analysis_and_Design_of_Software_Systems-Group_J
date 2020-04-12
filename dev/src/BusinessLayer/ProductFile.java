package BusinessLayer;

import java.util.HashMap;

public class ProductFile {

    private int fileID;
    private int totalWeight;
    private HashMap<Product, Integer> products;

    public ProductFile(int ID){
        fileID = ID;
        products = new HashMap<>();
    }

    public int getFileID() {
        return fileID;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }


    public void addProduct(Product p, int quantity) {
        products.put(p, quantity);
        totalWeight += p.getWeight()*quantity;
    }

    @Override
    public String toString() {
        String s= "file id: "+fileID+" total weight: " + totalWeight +"products:\n";
        for (Product p :products.keySet()) {
            s=s+p.toString()+" quantity: " +products.get(p);
        }
        return s;
    }

    public void removeProduct(Product p )
    {
        products.remove(p);
    }
}
