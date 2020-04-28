package BusinessLayer.Transport;


import java.util.ArrayList;
import java.util.Hashtable;

//singleton
public class ProductsController {
    private static  ProductsController instance = null;

    private int productID_Counter;
    private int fileID_Counter;
    private Hashtable<Integer, ProductFile> files;
    private Hashtable<Integer,Product> products;

    private ProductsController(){
        productID_Counter = 0;
        fileID_Counter = 0;
        files = new Hashtable<>();
        products = new Hashtable<>();
    }

    public static ProductsController getInstance()
    {
        if(instance == null){
            instance = new ProductsController();
        }
        return instance;
    }

    //creating a new Product-file
    public int CreateFile() {
        ProductFile file = new ProductFile(fileID_Counter);
        fileID_Counter++;
        files.put(file.getFileID(), file);
        return file.getFileID();
    }

    //creating a new Product
    public void CreateProduct(String productName, float productWeight, int fileID, int quantity) {
        Product p = new Product(productID_Counter, productName, productWeight);
        productID_Counter++;
        products.put(p.getID(), p);
        files.get(fileID).addProduct(p, quantity);
    }

    //if a File exist in the system return it, else return null
    public ProductFile getFileByID(int fileID){
        if(files.containsKey(fileID)) {
            return files.get(fileID);
        }
        return null;
    }

    //if a File exist in the system return its details, else return empty string
    public String geFileDetails(int file_id)
    {
        if(files.containsKey(file_id))
        {
            return files.get(file_id).toString();
        }

        return "";
    }

    //remove a list of Products from a specific file (if it exist in the system)
    //if one of the products is not in the system return false,
    //else call the removeProduct function in the file
    public boolean removeProducts(String[] products_id, int file_id){
        if(files.containsKey(file_id)) {
            ArrayList<Product>  productsToDelete = new ArrayList<>();
            for (String s:products_id) {
                int p_id=Integer.parseInt(s);
                if(products.containsKey(p_id))
                {
                    productsToDelete.add(products.get(p_id));
                }
                else {
                    return false;
                }
            }
            return files.get(file_id).removeProducts(productsToDelete);
        }
        return false;
    }

    //get details of a list of product
    public String getProductsDetails(String[] productsID){
        String s = "";
        for (int i = 0; i < productsID.length; i++){
            int productID = Integer.parseInt(productsID[i]);
            s += products.get(productID).toString() + "\n";
        }
        return s;
    }

    //if a file exist in the system, get its total weight
    public float getFileWeight(int fileID)
    {
        if(files.containsKey(fileID))
            return files.get(fileID).getTotalWeight();
        else
            return 0;
    }
}
