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

    public Boolean checkIfAvailableByLicence(String needed_licence)
{
    return license.equals(needed_licence);
}

}
