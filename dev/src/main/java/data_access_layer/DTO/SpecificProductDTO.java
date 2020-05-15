package data_access_layer.DTO;


import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.SpecificProduct;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

enum Location
{warehouse,store;}

@DatabaseTable(tableName ="SpecificProduct" )
public class SpecificProductDTO {
    //fields:
    @DatabaseField(id=true, columnName = "specific_product_id")
    private Integer id;
    @DatabaseField(columnName = "location",dataType = DataType.ENUM_TO_STRING)
    private Location location;
    @DatabaseField(columnName = "expiration_date",dataType = DataType.DATE_STRING)
    private Date expiration_date;
    @DatabaseField(columnName = "flaw_flag")
    private Boolean flaw_flag;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "GPID")
    private GeneralProductDTO generalProduct;
    @DatabaseField(columnName = "branch_id")
    private Integer branch_id;

    //Constructors
    public SpecificProductDTO(GeneralProductDTO gp,Integer id, String location, Date expiration_date,Boolean flaw_flag) {
        this.generalProduct=gp;
        this.id = id;
        this.location = convertStringTOEnum(location);
        this.expiration_date = expiration_date;
        this.flaw_flag=flaw_flag;
        this.branch_id=gp.getBranch_id().branch_id;
    }
    public SpecificProductDTO(GeneralProductDTO generalProductDTO,SpecificProduct specificProduct){
        this.generalProduct=generalProductDTO;
        this.id = specificProduct.getId();
        this.location = convertStringTOEnum(specificProduct.getLocation().name());
        this.expiration_date = specificProduct.getExpiration_date();
        this.flaw_flag=specificProduct.getFlaw_flag();
        this.branch_id=generalProductDTO.getBranch_id().branch_id;
    }

    public SpecificProductDTO(GeneralProduct generalProduct, SpecificProduct specificProduct){
        this.generalProduct= new GeneralProductDTO(generalProduct);
        this.id = specificProduct.getId();
        this.location = convertStringTOEnum(specificProduct.getLocation().name());
        this.expiration_date = specificProduct.getExpiration_date();
        this.flaw_flag=specificProduct.getFlaw_flag();
        this.branch_id=this.generalProduct.getBranch_id().branch_id;
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

    public GeneralProductDTO getGeneralProduct() {
        return generalProduct;
    }

    public Integer getBranch_id() {
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
