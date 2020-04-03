package bussines_layer;
import java.util.Date;

enum Location
{warehouse,store;}

public class SpecificProduct {
    //fields:
    private Integer id;
    private Enum<Location> location;
    private Date expiration_date;
    private Boolean flaw_flag;
}
