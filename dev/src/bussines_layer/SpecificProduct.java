package bussines_layer;
import java.util.Date;

enum Location
{warehouse,store;}

public class SpecificProduct {
    //fields:
    private final Integer id;
    private Enum<Location> location;
    private Date expiration_date;
    private Boolean flaw_flag;

    //Constructors
    public SpecificProduct(Integer id, Enum<Location> location, Date expiration_date) {
        this.id = id;
        this.location = location;
        this.expiration_date = expiration_date;
        this.flaw_flag=false;
    }
    //region Getter-Setters
    public Integer getId() {
        return id;
    }

    public Enum<Location> getLocation() {
        return location;
    }

    public void setLocation(Enum<Location> location) {
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
    //endregion

    @Override
    public String toString() {
        return "SpecificProduct{" +
                "id=" + id +
                ", location=" + location +
                ", expiration_date=" + expiration_date +
                ", flaw_flag=" + flaw_flag +
                '}';
    }
}
