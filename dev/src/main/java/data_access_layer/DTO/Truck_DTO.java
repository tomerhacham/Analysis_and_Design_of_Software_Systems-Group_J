package data_access_layer.DTO;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Truck")
public class Truck_DTO {

    @DatabaseField(columnName = "truckID", id = true, canBeNull = false)
    private Integer id;

    @DatabaseField(columnName = "licensePlate", canBeNull = false)
    private String license_plate;

    @DatabaseField(columnName = "model", canBeNull = false)
    private String model;

    @DatabaseField(columnName = "netWeight", canBeNull = false)
    private float net_weight;

    @DatabaseField(columnName = "maxWeight", canBeNull = false)
    private float max_weight;

    @DatabaseField(columnName = "driversLicense", canBeNull = false)
    private String drivers_license;

    @DatabaseField(columnName = "BranchID", canBeNull = false)
    Integer branch;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<night_shifts_DTO> night_shifts;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<morning_shifts_DTO> morning_shifts;

    public Truck_DTO(int ID, String licensePlate, String Model, float netWeight, float maxWeight, String driversLicense,
                     Integer branch){
        id = ID;
        license_plate = licensePlate;
        model = Model;
        net_weight = netWeight;
        max_weight = maxWeight;
        drivers_license = driversLicense;
        this.branch= branch;
    }
    public Truck_DTO(int ID, String licensePlate, String Model, float netWeight, float maxWeight, String driversLicense,
                     ForeignCollection<night_shifts_DTO> night_shifts, ForeignCollection<morning_shifts_DTO> morning_shifts
                    , Integer branch){
        id = ID;
        license_plate = licensePlate;
        model = Model;
        net_weight = netWeight;
        max_weight = maxWeight;
        drivers_license = driversLicense;
        this.night_shifts=night_shifts;
        this.morning_shifts=morning_shifts;
        this.branch=branch;
    }
    public Truck_DTO(){}

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

    public Integer getBranch_id() {
        return branch;
    }

    public void setBranch(Integer branch) {
        this.branch = branch;
    }
}
