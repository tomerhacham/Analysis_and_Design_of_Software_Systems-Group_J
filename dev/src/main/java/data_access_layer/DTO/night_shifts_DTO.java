package data_access_layer.DTO;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "nightShifts")
public class night_shifts_DTO {

    @DatabaseField(columnName = "date", uniqueCombo = true, dataType = DataType.DATE_STRING, format = "dd/MM/yyy", canBeNull = false)
    private Date date;

    @DatabaseField(columnName = "truckID", foreign = true, foreignColumnName = "truckID",  uniqueCombo = true, canBeNull = false, foreignAutoRefresh = true)
    private Truck_DTO truckID;

    public night_shifts_DTO(Date date, Truck_DTO truckID){
        this.date=date;
        this.truckID=truckID;
    }

    public night_shifts_DTO(){}

    public Truck_DTO getTruckID() {
        return truckID;
    }

    public Date getDate() {
        return date;
    }

    public void setTruckID(Truck_DTO truckID) {
        this.truckID = truckID;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
