package data_access_layer.DTO;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Site")
public class Site_DTO {

    @DatabaseField(columnName = "siteID", id = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "address", canBeNull = false)
    private String address;

    @DatabaseField(columnName = "phoneNumber", canBeNull = false)
    private String phone_number;

    @DatabaseField(columnName = "contact", canBeNull = false)
    private String contact;

    @DatabaseField(columnName = "shippingArea", canBeNull = false)
    private int shipping_area;

    public Site_DTO(int ID, String Address, String phoneNumber, String Contact, int ShippingArea){
        id = ID;
        address = Address;
        phone_number = phoneNumber;
        contact = Contact;
        shipping_area = ShippingArea;
    }

    public Site_DTO(){}

    public int getId() {
        return id;
    }

    public int getShipping_area() {
        return shipping_area;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setShipping_area(int shipping_area) {
        this.shipping_area = shipping_area;
    }
}
