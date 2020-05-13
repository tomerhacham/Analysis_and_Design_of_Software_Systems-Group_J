package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Driver")
public class Driver_DTO {

    @DatabaseField(columnName = "driverID", id = true, foreign = true, foreignColumnName = "workerID")
    private String driverID;

    @DatabaseField(columnName = "license")
    private String license;

    public Driver_DTO(){}
}
