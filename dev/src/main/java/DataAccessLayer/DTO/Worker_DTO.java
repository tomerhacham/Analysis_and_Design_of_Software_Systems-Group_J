package DataAccessLayer.DTO;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Worker")
public class Worker_DTO {

    @DatabaseField(columnName = "workerID", id = true)
    String workerID;

    @DatabaseField(columnName = "name")
    String name;

    @DatabaseField(columnName = "startDate", dataType = DataType.DATE_STRING)
    Date start_Date;

    @DatabaseField(columnName = "salary")
    double salary;

    @ForeignCollectionField(eager = false)
    ForeignCollectionField<Position_DTO> positions;

    public Worker_DTO(){}
}
