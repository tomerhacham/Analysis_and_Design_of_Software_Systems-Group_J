package DataAccessLayer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Site")
public class Site_DTO {

    @DatabaseField(columnName = "siteID", id = true)
    private int id;

    @DatabaseField(columnName = "address")
    private String address;

    @DatabaseField(columnName = "phoneNumber")
    private String phone_number;

    @DatabaseField(columnName = "contact")
    private String contact;

    @DatabaseField(columnName = "shippingArea")
    private Integer shipping_area;

    public Site_DTO(){}
}
