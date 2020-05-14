package DataAccessLayer.DTO;

import com.j256.ormlite.field.DataType;
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

    @DatabaseField(columnName = "Date",  dataType = DataType.DATE_STRING)
    private Date Date;

    @DatabaseField(columnName = "Time", dataType = DataType.DATE_TIME)
    private LocalTime Time;

    @DatabaseField(columnName = "Shift")
    private int Shift; //in which shift is the transport

    @DatabaseField(columnName = "truckID",foreign = true, foreignColumnName = "truckID")
    private Truck_DTO Truck;

    //driverId==WorkerID
    @DatabaseField(columnName = "driverID", foreign = true, foreignColumnName = "workerID")
    private Worker_DTO driverId;

    @DatabaseField(columnName = "driverName")
    private String driverName;

    @DatabaseField(columnName = "sourceID",foreign = true, foreignColumnName = "siteID")
    private Site_DTO Source;

    @DatabaseField(columnName = "TotalWeight")
    private float TotalWeight;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<DestFile_DTO> DestFiles;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<log_DTO> log;

    public Transport_DTO(int id, Date date, LocalTime time, int partOfDay, Worker_DTO driver_id, String driver_name, Site_DTO source,
                         float totalWeight){
        transportID = id;
        Date = date;
        Time = time;
        Shift = partOfDay;
        driverId = driver_id;
        driverName = driver_name;
        Source = source;
        TotalWeight = totalWeight;
    }

    public Transport_DTO(){}

    public int getTransportID() {
        return transportID;
    }

    public String getDriverName() {
        return driverName;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public float getTotalWeight() {
        return TotalWeight;
    }

    public int getShift() {
        return Shift;
    }

    public LocalTime getTime() {
        return Time;
    }

    public Truck_DTO getTruck() {
        return Truck;
    }

    public Worker_DTO getDriverId() {
        return driverId;
    }

    public Site_DTO getSource() {
        return Source;
    }

    public ForeignCollection<DestFile_DTO> getDestFiles() {
        return DestFiles;
    }

    public ForeignCollection<log_DTO> getLog() {
        return log;
    }

    public void setDestFiles(ForeignCollection<DestFile_DTO> destFiles) {
        DestFiles = destFiles;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setTime(LocalTime time) {
        Time = time;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public void setDriverId(Worker_DTO driverId) {
        this.driverId = driverId;
    }

    public void setShift(int shift) {
        Shift = shift;
    }

    public void setLog(ForeignCollection<log_DTO> log) {
        this.log = log;
    }

    public void setSource(Site_DTO source) {
        Source = source;
    }

    public void setTotalWeight(float totalWeight) {
        TotalWeight = totalWeight;
    }

    public void setTruck(Truck_DTO truck) {
        Truck = truck;
    }
}
