package BusinessLayer;


import java.util.HashMap;
import java.util.Hashtable;

public class ProductsController {
    private static  ProductsController instance = null;

    private int productID_Counter;
    private int fileID_Counter;
    private Hashtable<Integer,ProductPerSite> files;
    private Hashtable<Integer,Product> products;


    private ProductsController(){
        productID_Counter = 0;
        fileID_Counter = 0;
    }

    public static ProductsController getInstance()
    {
        if(instance == null){
            instance = new ProductsController();
        }
        return instance;
    }


    public int CreateFile() {
        ProductPerSite file = new ProductPerSite(fileID_Counter);
        fileID_Counter++;
        files.put(file.getFileID(), file);
        return file.getFileID();
    }

    public void CreateProduct(String productName, int productWeight, int fileID, int quantity) {
        Product p = new Product(productID_Counter, productName, productWeight);
        productID_Counter++;
        products.put(p.getID(), p);
        files.get(fileID).addProduct(p, quantity);
    }

    public ProductPerSite getFileByID(int fileID){
        return files.get(fileID);
    }

    public int getTotalWeight(HashMap<Integer, Integer> destFiles) {
        int total = 0;
        for (int id: destFiles.keySet()) {
            total += files.get(destFiles.get(id)).getTotalWeight();
        }
        return total;
    }
}
