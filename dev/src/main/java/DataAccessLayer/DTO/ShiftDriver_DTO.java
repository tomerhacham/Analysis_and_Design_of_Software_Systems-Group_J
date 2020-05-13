package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class ShiftDriver_DTO {

    @DatabaseField(columnName = "driverID", id = true, foreign = true, foreignColumnName = "driverID")
    private String driverID;

    @DatabaseField(columnName = "shiftDate", id = true, foreign = true, foreignColumnName = "driverID")
    private Date shiftDate;

    @DatabaseField(columnName = "partOfDay", id = true, foreign = true, foreignColumnName = "driverID")
    private int partOfDay;

    public ShiftDriver_DTO(){}
}
