package BusinessLayer.Transport;

import java.util.ArrayList;
import java.util.Date;

public class Driver {
    private String name;
    private String license;
    private ArrayList<Date> Dates;
    private int id;

    public Driver(int id, String license, String name)
    {
        this.license = license;
        Dates= new ArrayList<>();
        this.id=id;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //check if the driver available in a specific date
    public Boolean checkIfAvailableByDate(Date d)
    {
        for(int i=0; i<Dates.size(); i++)
        {
            if(Dates.get(i).equals(d))
            {
                return false;
            }
        }
        return true;
    }

    //check if the driver has a specific licence
    public Boolean checkIfAvailableByLicence(String needed_licence)
    {
        return license.equals(needed_licence);
    }

    public void addDate(Date d){Dates.add(d);}

    public void removeDate(Date d){
        for (int i = 0 ; i<Dates.size() ; i++)
        {
            if(Dates.get(i).equals(d))
                Dates.remove(i);
        }
    }

    public String toString()
    {
        String s = "id: "+id+" name: "+name+" license: "+license+"\n";
        if(Dates.size()>0) {
            s = s + "\tunavailable dates:\n";
            for (int i = 0; i < Dates.size(); i++) {
                s = s + "\t" + (i+1) + ". " + Dates.get(i).toString() + "\n";
            }
        }
        else {
            s=s + "\tunavailable dates: non\n";
        }
        return s;
    }
}
