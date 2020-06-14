package bussines_layer.employees_module;
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


    public String getLicense() {
        return license;
    }

    public Boolean checkIfAvailableByLicence(String needed_licence)
{
    return license.equals(needed_licence);
}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return getId().equals(worker.getId());
    }
}
