package BusinessLayer;

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

    public Boolean checkIfAvailableByLicence(String needed_licence)
    {
        if(license != needed_licence)
        {
            return false;
        }

        return true;
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
                s = s + i + ". " + Dates.get(i).toString();
            }
        }
        else {
            s=s + "\tunavailable dates: none";
        }
        return s;
    }
}
