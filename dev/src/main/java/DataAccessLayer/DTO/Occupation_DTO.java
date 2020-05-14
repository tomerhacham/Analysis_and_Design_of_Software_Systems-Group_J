package DataAccessLayer.DTO;

import BusinessLayer.Workers.Shift;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Occupation")
public class Occupation_DTO {

    @DatabaseField(columnName = "position", uniqueCombo = true)
    private String position;

    @DatabaseField(columnName = "workerID", foreign = true, foreignColumnName = "workerID", uniqueCombo = true)
    private Worker_DTO workerID;

    @DatabaseField(columnName = "ShiftID", foreign = true, foreignColumnName = "ShiftID", uniqueCombo = true)
    private Shift_DTO ShiftID;


    public Occupation_DTO(String position, Worker_DTO workerID, Shift_DTO ShiftID){
        this.position=position;
        this.workerID=workerID;
        this.ShiftID=ShiftID;
    }

    public Occupation_DTO(){}

    public Shift_DTO getShiftID() {
        return ShiftID;
    }

    public String getPosition() {
        return position;
    }

    public Worker_DTO getWorkerID() {
        return workerID;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setWorkerID(Worker_DTO workerID) {
        this.workerID = workerID;
    }

    public void setShiftID(Shift_DTO shiftID) {
        ShiftID = shiftID;
    }
}
