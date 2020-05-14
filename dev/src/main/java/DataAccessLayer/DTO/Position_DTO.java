package DataAccessLayer.DTO;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "positions")
public class Position_DTO {

    @DatabaseField(columnName = "workerID",foreign = true, foreignColumnName = "workerID", uniqueCombo = true, canBeNull = false)
    private Worker_DTO workerID;

    @DatabaseField(columnName = "position", uniqueCombo = true, canBeNull = false)
    private String position;

    public Position_DTO(Worker_DTO workerID, String position){
        this.workerID=workerID;
        this.position=position;
    }

    public Position_DTO(){}

    public Worker_DTO getWorkerID() {
        return workerID;
    }

    public String getPosition() {
        return position;
    }

    public void setWorkerID(Worker_DTO workerID) {
        this.workerID = workerID;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
