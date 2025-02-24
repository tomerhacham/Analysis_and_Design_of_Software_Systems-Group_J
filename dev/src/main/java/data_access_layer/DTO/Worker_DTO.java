package data_access_layer.DTO;
import bussines_layer.Branch;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Worker")
public class Worker_DTO {

    @DatabaseField(columnName = "workerID", id = true, canBeNull = false)
    private String workerID;

    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    @DatabaseField(columnName = "startDate", dataType = DataType.DATE_STRING, format = "dd/MM/yyy", canBeNull = false)
    private Date start_Date;

    @DatabaseField(columnName = "salary", canBeNull = false)
    private double salary;

    @DatabaseField(columnName = "BranchID", canBeNull = false)
    Integer branch;


    @ForeignCollectionField(eager = false)
    private ForeignCollection<Position_DTO> positions;

    public Worker_DTO(String id, String Name, Date startDate, Double Salary, Integer branch){
        workerID = id;
        name = Name;
        start_Date = startDate;
        salary = Salary;
        this.branch=branch;
    }



    public Worker_DTO(){}

    public String getName() {
        return name;
    }

    public Date getStart_Date() {
        return start_Date;
    }

    public double getSalary() {
        return salary;
    }

    public ForeignCollection<Position_DTO> getPositions() {
        return positions;
    }

    public String getWorkerID() {
        return workerID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart_Date(Date start_Date) {
        this.start_Date = start_Date;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setPositions(ForeignCollection<Position_DTO> positions) {
        this.positions = positions;
    }

    public Integer getBranch_id() {
        return branch;
    }

    public void setBranch(Integer branch) {
        this.branch = branch;
    }
}
