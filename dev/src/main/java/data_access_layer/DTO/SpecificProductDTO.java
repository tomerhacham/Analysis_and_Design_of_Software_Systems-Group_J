package data_access_layer.DTO;


import bussines_layer.enums.Location;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.SpecificProduct;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

@DatabaseTable(tableName ="SpecificProduct" )
public class SpecificProductDTO {
    //fields:
    @DatabaseField(columnName = "specific_product_id")
    private Integer id;
    @DatabaseField(columnName = "location",dataType = DataType.ENUM_TO_STRING)
    private Location location;
    @DatabaseField(columnName = "expiration_date",dataType = DataType.DATE_STRING)
    private Date expiration_date;
    @DatabaseField(columnName = "flaw_flag")
    private Boolean flaw_flag;
    @DatabaseField(columnName = "GPID")
    private Integer generalProduct;
    @DatabaseField(foreign = true,foreignAutoRefresh = true,foreignColumnName = "branch_id",columnName = "branch_id")
    private BranchDTO branch_id;

    //Constructors
    public SpecificProductDTO(SpecificProduct specificProduct){
        this.generalProduct=specificProduct.getGpId();
        this.id = specificProduct.getId();
        this.location = specificProduct.getLocation();
        this.expiration_date = specificProduct.getExpiration_date();
        this.flaw_flag=specificProduct.getFlaw_flag();
        this.branch_id=new BranchDTO(specificProduct.getBranch_id());
    }

    public SpecificProductDTO() {
    }

    //region Methods

    public Integer getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public Date getExpiration_date() {
        return expiration_date;
    }

    public Boolean getFlaw_flag() {
        return flaw_flag;
    }

    public Integer getGeneralProduct() {
        return generalProduct;
    }

    public BranchDTO getBranch_id() {
        return branch_id;
    }

    @Override
    public String toString() {
        return "SpecificProductDTO{" +
                "id=" + id +
                ", location=" + location +
                ", expiration_date=" + expiration_date +
                ", flaw_flag=" + flaw_flag +
                '}';
    }

    public Location convertStringTOEnum(String location){
        if (location.equals("store")){
            return Location.store;
        }
        else{
            return Location.warehouse;
        }
    }
    //endregion
}
