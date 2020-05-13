package DataAccessLayer.DTO;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Worker")
public class Worker_DTO {

    @DatabaseField(columnName = "workerID", id = true)
    private String workerID;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "startDate")
    private Date start_Date;

    @DatabaseField(columnName = "salary")
    private double salary;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Position_DTO> positions;

    public Worker_DTO(){}
}
