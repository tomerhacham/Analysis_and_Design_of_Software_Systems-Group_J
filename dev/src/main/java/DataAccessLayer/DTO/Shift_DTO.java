package DataAccessLayer.DTO;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Shift")
public class Shift_DTO {

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Occupation_DTO> occupation;

    @DatabaseField(columnName = "date")
    private Date date;

    @DatabaseField(columnName = "partOfDay")
    private int timeOfDay;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Driver_DTO> scheduledDrivers;

    public Shift_DTO(){}
}
