package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Driver")
public class Driver_DTO {

    @DatabaseField(columnName = "driverID", foreign = true, foreignColumnName = "workerID", canBeNull = false, foreignAutoRefresh = true)
    private Worker_DTO driverID;

    @DatabaseField(columnName = "license", canBeNull = false)
    private String license;

    public Driver_DTO(Worker_DTO driverID, String license){
        this.driverID=driverID;
        this.license=license;
    }

    public Driver_DTO(){}

    public String getLicense() {
        return license;
    }

    public Worker_DTO getDriverID() {
        return driverID;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setDriverID(Worker_DTO driverID) {
        this.driverID = driverID;
    }
}
