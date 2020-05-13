package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Driver")
public class morning_shifts_DTO {

    @DatabaseField(columnName = "date", id = true)
    private Date date;

    @DatabaseField(columnName = "truckID", id = true, foreign = true, foreignColumnName = "truckID")
    private int truckID;

    public morning_shifts_DTO(){}
}