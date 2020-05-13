package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Log")
public class log_DTO {

    @DatabaseField(columnName = "message", id = true)
    private String message;

    @DatabaseField(columnName = "transportID", id = true, foreign = true, foreignColumnName = "transportID")
    private int transportID;

    public log_DTO(String message, int transportID){
        this.message=message;
        this.transportID = transportID;
    }

    public int getTransportID() {
        return transportID;
    }

    public String getMessage() {
        return message;
    }

    public void setTransportID(int transportID) {
        this.transportID = transportID;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
