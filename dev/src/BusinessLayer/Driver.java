package BusinessLayer;

import java.util.ArrayList;
import java.util.Date;

public class Driver {
    private String name;
    private String license;
    private ArrayList<Transport> transports;
    private int id;

    public Driver(int id, String license, String name)
    {
        this.license = license;
        transports= new ArrayList<>();
        this.id=id;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList getTransports() {
        return transports;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Boolean checkIfAvailable(Date d, String needed_licence)
    {
        if(license != needed_licence)
        {
            return false;
        }
        //that the time isn't overlapping another transport
        for(int i=0; i<transports.size(); i++)
        {
            if(transports.get(i).getDate().equals(d))
            {
                return false;
            }
        }
        return true;
    }

    public void AddTransport(Transport t)
    {
        transports.add(t);
    }

    public String toString()
    {
        String s = "id: "+id+" name: "+name+" license: "+license+"\n";
        if(transports.size()>0) {
            s = s + "transports:\n";
            for (int i = 0; i < transports.size(); i++) {
                s = s + i + ". " + transports.get(i).toString();
            }
        }
        else {
            s=s + "transports: none\n";
        }
        return s;
    }
}
