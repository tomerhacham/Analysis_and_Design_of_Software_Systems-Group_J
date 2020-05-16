package BusinessLayer.Transport;


import DataAccessLayer.Mapper;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

//singleton
public class ProductsController {
    private static  ProductsController instance = null;
    private static Mapper mapper = Mapper.getInstance();

    private int productID_Counter;
    private int fileID_Counter;
    private Hashtable<Integer, ProductFile> files;
    private Hashtable<Integer,Product> products;

    private ProductsController(){
        productID_Counter = (int)mapper.MaxIdProductsFile() + 1;
        fileID_Counter = (int)mapper.MaxIdProducts() + 1;
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

    public boolean getProductFileFromDB(int fileID)
    {
        ProductFile pf = mapper.getProductFile(fileID);
        if(pf!=null) {
            files.put(fileID, pf);
            return true;
        }
        return false;
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
        if(files.containsKey(fileID) || getProductFileFromDB(fileID)) {
            return files.get(fileID);
        }
        return null;
    }

    //if a File exist in the system return its details, else return empty string
    public String geFileDetails(int file_id)
    {
        if(files.containsKey(file_id)||getProductFileFromDB(file_id))
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
        if(files.containsKey(fileID)||getProductFileFromDB(fileID)) {
            return files.get(fileID).getTotalWeight();
        }
        else
            return 0;
    }
}
