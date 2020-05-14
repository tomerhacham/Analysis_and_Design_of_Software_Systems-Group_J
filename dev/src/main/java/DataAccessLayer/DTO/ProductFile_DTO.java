package DataAccessLayer.DTO;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "ProductFile")
public class ProductFile_DTO {

    @DatabaseField(columnName = "fileID", id = true, canBeNull = false)
    private int fileID;

    @DatabaseField(columnName = "totalWeight", canBeNull = false)
    private float totalWeight;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Product_DTO> products;

    public ProductFile_DTO(int fileID, float totalWeight){
        this.fileID=fileID;
        this.totalWeight=totalWeight;
    }

    public ProductFile_DTO(int fileID, float totalWeight, ForeignCollection<Product_DTO> products){
        this.fileID=fileID;
        this.totalWeight=totalWeight;
        this.products=products;
    }

    public ProductFile_DTO(){}

    public int getFileID() {
        return fileID;
    }

    public float getTotalWeight() {
        return totalWeight;
    }

    public ForeignCollection<Product_DTO> getProducts() {
        return products;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public void setProducts(ForeignCollection<Product_DTO> products) {
        this.products = products;
    }

    public void setTotalWeight(float totalWeight) {
        this.totalWeight = totalWeight;
    }
}
