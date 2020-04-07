package BusinessLayer;

public class Site {
    private String address;
    private String phone_number;
    private String contact;
    private Integer shipping_area;
    private int id;

    public Site(int id, String add, String phone, String con, Integer area)
    {
        this.id=id;
        address=add;
        phone_number=phone;
        contact=con;
        shipping_area=area;
    }

    public int getId() {
        return id;
    }

    public Integer getShipping_area() {
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

    public void setShipping_area(Integer shipping_area) {
        this.shipping_area = shipping_area;
    }
}

