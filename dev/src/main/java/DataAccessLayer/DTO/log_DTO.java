package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Log")
public class log_DTO {

    @DatabaseField(columnName = "message", id = true)
    private String message;

    @DatabaseField(columnName = "transportID", id = true, foreign = true, foreignColumnName = "transportID")
    private int transportID;

    public log_DTO(){}
}
