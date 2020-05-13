package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Shift_availableWorkers")
public class Shift_availableWorkers_DTO {

    @DatabaseField(columnName = "workerID", id = true, foreign = true, foreignColumnName = "workerID")
    private String workerID;

    @DatabaseField(columnName = "shiftDate", id = true)
    private Date shiftDate;

    @DatabaseField(columnName = "partOfDay", id = true)
    private int partOfDay;

    public Shift_availableWorkers_DTO(){}
}
