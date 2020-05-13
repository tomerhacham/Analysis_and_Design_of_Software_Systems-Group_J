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
    private float quantity;

    public Product_DTO(){}
}
