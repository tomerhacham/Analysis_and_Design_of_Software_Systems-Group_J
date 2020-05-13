package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Driver")
public class night_shifts_DTO {

    @DatabaseField(columnName = "date", id = true)
    private Date date;

    @DatabaseField(columnName = "truckID", id = true, foreign = true, foreignColumnName = "truckID")
    private int truckID;

    public night_shifts_DTO(Date date, int truckID){
        this.date=date;
        this.truckID=truckID;
    }

    public int getTruckID() {
        return truckID;
    }

    public Date getDate() {
        return date;
    }

    public void setTruckID(int truckID) {
        this.truckID = truckID;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
