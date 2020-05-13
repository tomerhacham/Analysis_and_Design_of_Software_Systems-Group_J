package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class ShiftDriver_DTO {

    @DatabaseField(columnName = "driverID", id = true, foreign = true, foreignColumnName = "driverID")
    private String driverID;

    @DatabaseField(columnName = "shiftDate", id = true, foreign = true, foreignColumnName = "driverID")
    private Date shiftDate;

    @DatabaseField(columnName = "partOfDay", id = true, foreign = true, foreignColumnName = "driverID")
    private int partOfDay;

    public ShiftDriver_DTO(String driver_id, Date date, int part_of_day){
        driverID = driver_id;
        shiftDate = date;
        partOfDay = part_of_day;
    }

    public Date getShiftDate() {
        return shiftDate;
    }

    public int getPartOfDay() {
        return partOfDay;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setPartOfDay(int partOfDay) {
        this.partOfDay = partOfDay;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
    }
}
