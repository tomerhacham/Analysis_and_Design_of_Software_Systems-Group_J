package DataAccessLayer.DTO;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Worker")
public class Truck_DTO {

    @DatabaseField(columnName = "truckID", id = true)
    private Integer id;

    @DatabaseField(columnName = "licensePlate")
    private String license_plate;

    @DatabaseField(columnName = "model")
    private String model;

    @DatabaseField(columnName = "netWeight")
    private float net_weight;

    @DatabaseField(columnName = "maxWeight")
    private float max_weight;

    @DatabaseField(columnName = "driversLicense")
    private String drivers_license;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<night_shifts_DTO> night_shifts;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<morning_shifts_DTO> morning_shifts;

    public Truck_DTO(){}
}
