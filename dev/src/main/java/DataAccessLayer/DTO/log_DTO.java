package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Log")
public class log_DTO {

    @DatabaseField(columnName = "message")
    private String message;

    @DatabaseField(columnName = "transportID",foreign = true, foreignColumnName = "transportID")
    private Transport_DTO transportID;

    public log_DTO(String message, Transport_DTO transportID){
        this.message=message;
        this.transportID = transportID;
    }

    public log_DTO(){}

    public Transport_DTO getTransportID() {
        return transportID;
    }

    public String getMessage() {
        return message;
    }

    public void setTransportID(Transport_DTO transportID) {
        this.transportID = transportID;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
