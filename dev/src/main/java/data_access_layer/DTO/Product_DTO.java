package data_access_layer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Product")
public class Product_DTO {

    @DatabaseField(columnName = "productID", id = true, canBeNull = false)
    private int ID;

    @DatabaseField(columnName = "fileID", foreign = true, foreignColumnName = "fileID", canBeNull = false, foreignAutoRefresh = true)
    private ProductFile_DTO fileID;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "weight", canBeNull = false)
    private float weight;

    @DatabaseField(columnName = "quantity", canBeNull = false)
    private int quantity;

    public Product_DTO(int id, ProductFile_DTO fileID, String name, float weight, int quantity){
        this.ID=id;
        this.fileID=fileID;
        this.name=name;
        this.weight=weight;
        this.quantity=quantity;
    }

    public Product_DTO(){}

    public String getName() {
        return name;
    }

    public float getWeight() {
        return weight;
    }

    public ProductFile_DTO getFileID() {
        return fileID;
    }

    public int getID() {
        return ID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setFileID(ProductFile_DTO fileID) {
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
