package BusinessLayer;

public class Product {

    private int ID;
    private String name;
    private int weight;

    public Product(int id, String productName, int productWeight) {
        ID = id;
        name = productName;
        weight = productWeight;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "product id: "+ID+ " product name: "+name+" product weight: "+weight;
    }
}
