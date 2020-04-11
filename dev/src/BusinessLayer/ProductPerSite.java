package BusinessLayer;

import java.util.HashMap;

public class ProductPerSite {

    private int fileID;
    private int totalWeight;
    private HashMap<Product, Integer> products;

    public int getFileID() {
        return fileID;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }
}
