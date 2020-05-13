package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Product")
public class Product_DTO {

    @DatabaseField(columnName = "productID", id = true)
    private int ID;

    @DatabaseField(columnName = "fileID", foreign = true, foreignColumnName = "fileID")
    private int fileID;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "weight")
    private float weight;

    @DatabaseField(columnName = "quantity")
    private int quantity;

    public Product_DTO(int id, int fileID, String name, float weight, int quantity){
        this.ID=id;
        this.fileID=fileID;
        this.name=name;
        this.weight=weight;
        this.quantity=quantity;
    }

    public String getName() {
        return name;
    }

    public float getWeight() {
        return weight;
    }

    public int getFileID() {
        return fileID;
    }

    public int getID() {
        return ID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
