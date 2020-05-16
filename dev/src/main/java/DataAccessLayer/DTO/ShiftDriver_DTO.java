package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Shift_Driver")
public class ShiftDriver_DTO {

    //driverId==WorkerID
    @DatabaseField(columnName = "driverID", foreign = true, foreignColumnName = "WorkerID", uniqueCombo = true, canBeNull = false, foreignAutoRefresh = true)
    private Worker_DTO driverID;

    @DatabaseField(columnName = "ShiftID", foreign = true, foreignColumnName = "ShiftID", uniqueCombo = true, canBeNull = false, foreignAutoRefresh = true)
    private Shift_DTO shiftID;


    public ShiftDriver_DTO(Worker_DTO driver_id,Shift_DTO shift_ID){
        driverID = driver_id;
        shiftID=shift_ID;
    }

    public ShiftDriver_DTO(){}

    public Shift_DTO getShiftID() {
        return shiftID;
    }

    public Worker_DTO getDriverID() {
        return driverID;
    }


    public void setDriverID(Worker_DTO driverID) {
        this.driverID = driverID;
    }

    public void setShiftID(Shift_DTO shiftID) {
        this.shiftID = shiftID;
    }
}
