package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Occupation")
public class Occupation_DTO {

    @DatabaseField(columnName = "position", id = true)
    private String position;

    @DatabaseField(columnName = "workerID", id = true, foreign = true, foreignColumnName = "workerID")
    private String workerID;

    @DatabaseField(columnName = "shiftDate", id = true, foreign = true, foreignColumnName = "date")
    private Date shiftDate;

    @DatabaseField(columnName = "partOfDay", id = true, foreign = true, foreignColumnName = "partOfDay")
    private int partOfDay;

    public Occupation_DTO(){}
}
