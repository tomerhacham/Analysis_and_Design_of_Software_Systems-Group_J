package DataAccessLayer.DTO;

import DataAccessLayer.Mapper;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "morningShifts")
public class morning_shifts_DTO {

    @DatabaseField(columnName = "date", uniqueCombo = true, dataType = DataType.DATE_STRING, format = "dd/MM/yyy")
    private Date date;

    @DatabaseField(columnName = "truckID", foreign = true, foreignColumnName = "truckID", uniqueCombo = true)
    private Truck_DTO truckID;

    public morning_shifts_DTO(Date date, Truck_DTO truckID){
        this.date=date;
        this.truckID=truckID;
    }

    public morning_shifts_DTO(){}

    public Date getDate() {
        return date;
    }

    public Truck_DTO getTruckID() {
        return truckID;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTruckID(Truck_DTO truckID) {
        this.truckID = truckID;
    }
}