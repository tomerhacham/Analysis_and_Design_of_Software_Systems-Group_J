package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Occupation")
public class Occupation_DTO {

    @DatabaseField(columnName = "position", id = true)
    private String position;

    @DatabaseField(columnName = "workerID", id = true, foreign = true, foreignColumnName = "workerID")
    private String workerID;

    @DatabaseField(columnName = "shiftDate", id = true, foreign = true, foreignColumnName = "date")
    private Date shiftDate;

    @DatabaseField(columnName = "partOfDay", id = true, foreign = true, foreignColumnName = "partOfDay")
    private int partOfDay;

    public Occupation_DTO(String position, String workerID, Date shiftDate, int partOfDay){
        this.position=position;
        this.workerID=workerID;
        this.shiftDate=shiftDate;
        this.partOfDay=partOfDay;
    }

    public Date getShiftDate() {
        return shiftDate;
    }

    public int getPartOfDay() {
        return partOfDay;
    }

    public String getPosition() {
        return position;
    }

    public String getWorkerID() {
        return workerID;
    }

    public void setPartOfDay(int partOfDay) {
        this.partOfDay = partOfDay;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }
}
