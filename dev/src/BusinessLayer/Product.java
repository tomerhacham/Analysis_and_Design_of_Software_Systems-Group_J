package BusinessLayer;

public class Product {

    private int productID;
    private String name;
    private int price;
    private String producer;
    private String category;
    private int catalogID;

    public Product(int productID , String name , int price , String producer , String category , int catalogID){
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.producer = producer;
        this.category = category;
        this.catalogID = catalogID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(int catalogID) {
        this.catalogID = catalogID;
    }
}
