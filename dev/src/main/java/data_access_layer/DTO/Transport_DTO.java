package data_access_layer.DTO;

import bussines_layer.Branch;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalTime;
import java.util.Date;

@DatabaseTable(tableName = "Transport")
public class Transport_DTO {

    @DatabaseField(columnName = "transportID", id = true, canBeNull = false)
    private int transportID;

    @DatabaseField(columnName = "truckID",foreign = true, foreignColumnName = "truckID", canBeNull = false)
    private Truck_DTO Truck;

    //driverId==WorkerID
    @DatabaseField(columnName = "driverID", foreign = true, foreignColumnName = "workerID", canBeNull = false)
    private Worker_DTO driverId;

    @DatabaseField(columnName = "driverName", canBeNull = false)
    private String driverName;

    @DatabaseField(columnName = "Date",  dataType = DataType.DATE_STRING, format = "dd/MM/yyy", canBeNull = false)
    private Date Date;

    @DatabaseField(columnName = "Shift", canBeNull = false)
    private int Shift; //in which shift is the transport

    @DatabaseField(columnName = "TotalWeight", canBeNull = false)
    private float TotalWeight;

    @DatabaseField(columnName = "BranchID", canBeNull = false)
    Integer branch;

    @DatabaseField(columnName = "orderID", canBeNull = false)
    private Integer orderId;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<log_DTO> log;

    public Transport_DTO(int id, Date date, int partOfDay,Truck_DTO truck, Worker_DTO driver_id, String driver_name,
                         float totalWeight, Integer order, Integer branch){
        transportID = id;
        Date = date;
        Shift = partOfDay;
        Truck = truck;
        driverId = driver_id;
        driverName = driver_name;
        TotalWeight = totalWeight;
        orderId=order;
        this.branch=branch;
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

    public Truck_DTO getTruck() {
        return Truck;
    }

    public Worker_DTO getDriverId() {
        return driverId;
    }

    public ForeignCollection<log_DTO> getLog() {
        return log;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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

    public void setTotalWeight(float totalWeight) {
        TotalWeight = totalWeight;
    }

    public void setTruck(Truck_DTO truck) {
        Truck = truck;
    }

    public Integer getBranch_id() {
        return branch;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setBranch_id(Integer branch_id) {
        this.branch = branch_id;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
