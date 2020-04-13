package BusinessLayer;

import java.util.HashMap;

public class ProductFile {

    private int fileID;
    private float totalWeight;
    private HashMap<Product, Integer> products;

    public ProductFile(int ID){
        fileID = ID;
        totalWeight = 0;
        products = new HashMap<>();
    }

    public int getFileID() {
        return fileID;
    }

    public float getTotalWeight() {
        return totalWeight;
    }

    public void addProduct(Product p, int quantity) {
        products.put(p, quantity);
        totalWeight += p.getWeight()*quantity;
    }

    @Override
    public String toString() {
        String s= "file id: "+fileID+" total weight: " + totalWeight +"\n\t\tproducts:";
        int count=1;
        for (Product p :products.keySet()) {
            s=s+" "+count+". "+p.toString()+" quantity: " +products.get(p)+"\n";
        }
        return s;
    }

    public void removeProduct(Product p )
    {
        totalWeight -= p.getWeight()*products.get(p);
        products.remove(p);
    }
}
