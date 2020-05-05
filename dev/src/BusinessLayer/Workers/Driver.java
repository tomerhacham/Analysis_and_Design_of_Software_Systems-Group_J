package BusinessLayer.Workers;

import InterfaceLayer.Workers.ModelDriver;
import InterfaceLayer.Workers.ModelWorker;

import java.util.Date;




public class Driver extends Worker {
    private String license;


    public Driver(String id,String license, String name,Date startDate,double salary)
    {
        super(name,id,startDate,salary);
        this.license = license;
        this.positions.add("driver");
    }
    @Override
    public ModelWorker getModel(){return new ModelDriver(this);}

    @Override
    public String addPosition(String pos)
    {
        return "Can not add another position to driver";
    }
    @Override
    public String removePosition(String pos)
    {
        return "cannot remove positions from driver";
    }

    public String setLicense(String license) {
        this.license = license;
        return null;
    }

    public String getLicense() {
        return license;
    }


 /*

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

  */
public Boolean checkIfAvailableByLicence(String needed_licence)
{
    return license.equals(needed_licence);
}

}
