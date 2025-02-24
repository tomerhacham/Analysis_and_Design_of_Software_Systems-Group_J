package data_access_layer.DTO;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Shift_availableWorkers")
public class Shift_availableWorkers_DTO {

    @DatabaseField(columnName = "workerID", foreign = true, foreignColumnName = "workerID", uniqueCombo = true, canBeNull = false, foreignAutoRefresh = true)
    private Worker_DTO workerID;

    @DatabaseField(columnName = "shiftDate", uniqueCombo = true, index = true, dataType = DataType.DATE_STRING, format = "dd/MM/yyy", canBeNull = false)
    private Date shiftDate;

    @DatabaseField(columnName = "partOfDay", uniqueCombo = true, index = true, canBeNull = false)
    private int partOfDay;

    public Shift_availableWorkers_DTO(Worker_DTO workerID, Date date, int timeOfDay ) {
        this.workerID = workerID;
        this.shiftDate=date;
        this.partOfDay=timeOfDay;

    }

    public Shift_availableWorkers_DTO() {
    }

    public Worker_DTO getWorkerID() {
        return workerID;
    }

    public void setWorkerID(Worker_DTO workerID) {
        this.workerID = workerID;
    }

    public Date getShiftDate() {
        return shiftDate;
    }

    public int getTimeOfDay() {
        return partOfDay;
    }

    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
    }

    public void setTimeOfDay(int timeOfDay) {
        this.partOfDay = timeOfDay;
    }
}
