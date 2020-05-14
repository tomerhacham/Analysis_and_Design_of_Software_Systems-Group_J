package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Shift_availableWorkers")
public class Shift_availableWorkers_DTO {

    @DatabaseField(columnName = "workerID", foreign = true, foreignColumnName = "workerID", uniqueCombo = true)
    private Worker_DTO workerID;

    @DatabaseField(columnName = "ShiftID", foreign = true, foreignColumnName = "ShiftID", uniqueCombo = true)
    private Shift_DTO shiftID;

    public Shift_availableWorkers_DTO(Worker_DTO workerID, Shift_DTO Shift_id) {
        this.workerID = workerID;
        this.shiftID = Shift_id;
    }

    public Shift_availableWorkers_DTO() {
    }

    public Shift_DTO getShiftID() {
        return shiftID;
    }

    public Worker_DTO getWorkerID() {
        return workerID;
    }

    public void setShiftID(Shift_DTO shiftID) {
        this.shiftID = shiftID;
    }

    public void setWorkerID(Worker_DTO workerID) {
        this.workerID = workerID;
    }
}
