package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "DestFile")
public class DestFile_DTO {

    @DatabaseField(columnName = "transportID", id = true, foreign = true, foreignColumnName = "transportID")
    private int transportID;

    @DatabaseField(columnName = "productFileID", id = true, foreign = true, foreignColumnName = "fileID")
    private int productFileID;

    @DatabaseField(columnName = "siteID", id = true, foreign = true, foreignColumnName = "siteID")
    private int siteID;

    public DestFile_DTO(){}
}
