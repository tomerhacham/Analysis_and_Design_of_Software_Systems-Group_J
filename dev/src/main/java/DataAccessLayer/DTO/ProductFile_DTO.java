package DataAccessLayer.DTO;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "ProductFile")
public class ProductFile_DTO {

    @DatabaseField(columnName = "fileID", id = true)
    private int fileID;

    @DatabaseField(columnName = "totalWeight")
    private float totalWeight;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Product_DTO> products;

    public ProductFile_DTO(){}
}
