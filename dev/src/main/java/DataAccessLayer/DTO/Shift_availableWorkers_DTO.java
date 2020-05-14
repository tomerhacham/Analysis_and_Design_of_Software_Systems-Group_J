package DataAccessLayer.DTO;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Shift_availableWorkers")
public class Shift_availableWorkers_DTO {

    @DatabaseField(columnName = "workerID", foreign = true, foreignColumnName = "workerID", uniqueCombo = true, canBeNull = false)
    private Worker_DTO workerID;

    @DatabaseField(columnName = "shiftDate", uniqueCombo = true, index = true, dataType = DataType.DATE_STRING, format = "dd/MM/yyy", canBeNull = false)
    private Date date;

    @DatabaseField(columnName = "partOfDay", uniqueCombo = true, index = true, canBeNull = false)
    private int timeOfDay;

    public Shift_availableWorkers_DTO(Worker_DTO workerID, Date date, int timeOfDay ) {
        this.workerID = workerID;
        this.date=date;
        this.timeOfDay=timeOfDay;

    }

    public Shift_availableWorkers_DTO() {
    }

    public Worker_DTO getWorkerID() {
        return workerID;
    }

    public void setWorkerID(Worker_DTO workerID) {
        this.workerID = workerID;
    }
}
