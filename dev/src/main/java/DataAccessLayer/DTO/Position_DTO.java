package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "positions")
public class Position_DTO {

    @DatabaseField(columnName = "workerID", id = true, foreign = true, foreignColumnName = "workerID")
    private String workerID;

    @DatabaseField(columnName = "position", id = true)
    private String position;

    public Position_DTO(String workerID, String position){
        this.workerID=workerID;
        this.position=position;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getWorkerID() {
        return workerID;
    }

    public String getPosition() {
        return position;
    }
}
