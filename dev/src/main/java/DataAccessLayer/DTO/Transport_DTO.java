package DataAccessLayer.DTO;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalTime;
import java.util.Date;

@DatabaseTable(tableName = "Transport")
public class Transport_DTO {

    @DatabaseField(columnName = "transportID", id = true)
    private int transportID;

    @DatabaseField(columnName = "Date")
    private Date Date;

    @DatabaseField(columnName = "Time")
    private LocalTime Time;

    @DatabaseField(columnName = "Shift")
    private int Shift; //in which shift is the transport

    @DatabaseField(columnName = "truckID",foreign = true, foreignColumnName = "truckID")
    private Truck_DTO Truck;

    @DatabaseField(columnName = "driverID", foreign = true, foreignColumnName = "driverID")
    private String driverId;

    @DatabaseField(columnName = "driverName")
    private String driverName;

    @DatabaseField(columnName = "sourceID",foreign = true, foreignColumnName = "siteID")
    private Site_DTO Source;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<DestFile_DTO> DestFiles;

    @DatabaseField(columnName = "TotalWeight")
    private float TotalWeight;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<log_DTO> log;

    public Transport_DTO(){}
}
