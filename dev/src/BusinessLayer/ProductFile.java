package BusinessLayer;

import java.util.ArrayList;
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

    public int getFileID() { return fileID; }

    public float getTotalWeight() {
        return totalWeight;
    }

    //add a product and calculate the total weight
    public void addProduct(Product p, int quantity) {
        products.put(p, quantity);
        totalWeight += p.getWeight()*quantity;
    }

    // check if all the products exists in the file, if they are remove them and calculate the total weight, else return false
    public boolean removeProducts(ArrayList<Product> P)
    {
        for (Product p:P) {
            if(!products.containsKey(p))
            {
                return false;
            }
        }
        for (Product p:P) {
            totalWeight -= p.getWeight() * products.get(p);
            products.remove(p);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String s = "file id: "+fileID+" total weight: " + totalWeight +"\n\tproducts:\n";
        int count = 1;
        for (Product p :products.keySet()) {
            s += "\t\t" + count + ". " + p.toString()+" quantity: " + products.get(p) +"\n";
            count ++;
        }
        return s;
    }
}
