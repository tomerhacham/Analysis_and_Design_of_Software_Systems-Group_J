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

    public Truck_DTO(int ID, String licensePlate, String Model, float netWeight, float maxWeight, String driversLicense){
        id = ID;
        license_plate = licensePlate;
        model = Model;
        net_weight = netWeight;
        max_weight = maxWeight;
        drivers_license = driversLicense;
    }

    public Integer getId() {
        return id;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public String getModel() {
        return model;
    }

    public float getMax_weight() {
        return max_weight;
    }

    public float getNet_weight() {
        return net_weight;
    }

    public ForeignCollection<morning_shifts_DTO> getMorning_shifts() {
        return morning_shifts;
    }

    public ForeignCollection<night_shifts_DTO> getNight_shifts() {
        return night_shifts;
    }

    public String getDrivers_license() {
        return drivers_license;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public void setMax_weight(float max_weight) {
        this.max_weight = max_weight;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setNet_weight(float net_weight) {
        this.net_weight = net_weight;
    }

    public void setDrivers_license(String drivers_license) {
        this.drivers_license = drivers_license;
    }

    public void setMorning_shifts(ForeignCollection<morning_shifts_DTO> morning_shifts) {
        this.morning_shifts = morning_shifts;
    }

    public void setNight_shifts(ForeignCollection<night_shifts_DTO> night_shifts) {
        this.night_shifts = night_shifts;
    }
}
