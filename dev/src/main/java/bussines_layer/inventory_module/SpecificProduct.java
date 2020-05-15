package bussines_layer.inventory_module;
import bussines_layer.BranchController;
import bussines_layer.enums.Location;
import data_access_layer.DTO.SpecificProductDTO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SpecificProduct {
    //fields:
    private final Integer id;
    private Location location;
    private Date expiration_date;
    private Boolean flaw_flag;

    //Constructors
    public SpecificProduct(Integer id, Location location, Date expiration_date) {
        this.id = id;
        this.location = location;
        this.expiration_date = expiration_date;
        this.flaw_flag=false;
    }
    public SpecificProduct(SpecificProductDTO specificProductDTO){
        this.id=specificProductDTO.getId();
        this.location=specificProductDTO.getLocation();
        this.expiration_date=specificProductDTO.getExpiration_date();
        this.flaw_flag=specificProductDTO.getFlaw_flag();
    }
    //region Getter-Setters
    public Integer getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Date expiration_date) {
        this.expiration_date = expiration_date;
    }

    public Boolean getFlaw_flag() {
        return flaw_flag;
    }

    public void setFlaw_flag(Boolean flaw_flag) {
        this.flaw_flag = flaw_flag;
    }

    public void shiftLocation(){
        if (location.equals(Location.store)){
            location=Location.warehouse;
        }
        else{
            location=Location.store;
        }
    }

    public boolean isExpired(){
        return expiration_date.before(BranchController.system_curr_date);

    }
    public boolean isFlaw(){
        return getFlaw_flag();
    }
    //endregion

    @Override
    public String toString() {
        SimpleDateFormat date_formater = new SimpleDateFormat("dd/MM/yyyy");
        return  "product id:" + id +
                ", location:" + location +
                ", expiration date:" + date_formater.format(expiration_date).toString();
    }
}
