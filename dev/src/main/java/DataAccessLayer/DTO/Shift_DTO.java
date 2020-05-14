package DataAccessLayer.DTO;

import BusinessLayer.Workers.Shift;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Shift")
public class Shift_DTO {

    @DatabaseField (columnName = "ShiftID", id = true)
    String ShiftID;

    @DatabaseField(columnName = "date", uniqueCombo = true, index = true, dataType = DataType.DATE_STRING, format = "dd/MM/yyy")
    private Date date;

    @DatabaseField(columnName = "partOfDay", uniqueCombo = true, index = true)
    private int timeOfDay;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Occupation_DTO> occupation;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<ShiftDriver_DTO> drivers_in_shift;

    public Shift_DTO(String shiftID, Date Date, int partOfDay){
        ShiftID=shiftID;
        date = Date;
        timeOfDay = partOfDay;
    }

    public Shift_DTO(){}

    public Date getDate() {
        return date;
    }

    public ForeignCollection<Occupation_DTO> getOccupation() {
        return occupation;
    }

    public int getTimeOfDay() {
        return timeOfDay;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setOccupation(ForeignCollection<Occupation_DTO> occupation) {
        this.occupation = occupation;
    }

    public void setTimeOfDay(int timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getShiftID() {
        return ShiftID;
    }

    public void setShiftID(String shiftID) {
        ShiftID = shiftID;
    }

    public ForeignCollection<ShiftDriver_DTO> getDrivers_in_shift() {
        return drivers_in_shift;
    }

    public void setDrivers_in_shift(ForeignCollection<ShiftDriver_DTO> drivers_in_shift) {
        this.drivers_in_shift = drivers_in_shift;
    }
}
