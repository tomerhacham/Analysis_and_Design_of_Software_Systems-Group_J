package bussines_layer.transport_module;

public class Product {
    private int ID;
    private String name;
    private float weight;

    public Product(int id, String productName, float productWeight) {
        ID = id;
        name = productName;
        weight = productWeight;
    }

    public int getID() {
        return ID;
    }

    public float getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "product id: "+ID+ " product name: "+name+" product weight: "+weight;
    }
}
