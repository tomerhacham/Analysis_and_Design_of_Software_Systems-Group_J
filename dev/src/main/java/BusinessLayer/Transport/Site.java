package BusinessLayer.Transport;


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

    //check if a site has a specific shipping_area
    public Boolean checkIfAvailable(int shipping_area)
    {
        return this.shipping_area==shipping_area;
    }

    @Override
    public String toString() {
       String s="id: "+id+" address: "+address+" phone number: "+phone_number+" contact: "+contact+" shipping area: "+shipping_area;
       return s;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

}

