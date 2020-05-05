package InterfaceLayer.Workers;

import BusinessLayer.Workers.Driver;

public class ModelDriver extends ModelWorker {
    public String license;
    public ModelDriver(Driver driver) {
        super(driver);
        this.license=driver.getLicense();
    }

    @Override
    public String toString() {
        return super.toString()+"\tlicense="+license;
    }
}
