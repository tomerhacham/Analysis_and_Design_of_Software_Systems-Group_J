package DataAccessLayer.DTO;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Shift")
public class Shift_DTO {

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Occupation_DTO> occupation;

    @DatabaseField(columnName = "date")
    private Date date;

    @DatabaseField(columnName = "partOfDay")
    private int timeOfDay;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Driver_DTO> scheduledDrivers;

    public Shift_DTO(ForeignCollection<Occupation_DTO> Occupation, Date Date, int partOfDay, ForeignCollection<Driver_DTO> drivers){
        occupation = Occupation;
        date = Date;
        timeOfDay = partOfDay;
        scheduledDrivers = drivers;
    }

    public Date getDate() {
        return date;
    }

    public ForeignCollection<Driver_DTO> getScheduledDrivers() {
        return scheduledDrivers;
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

    public void setScheduledDrivers(ForeignCollection<Driver_DTO> scheduledDrivers) {
        this.scheduledDrivers = scheduledDrivers;
    }

    public void setTimeOfDay(int timeOfDay) {
        this.timeOfDay = timeOfDay;
    }
}
