package bussines_layer.employees_module;

import bussines_layer.transport_module.TransportModule;

import java.util.Date;
//TODO: merge both of the controllers (Roaster and Scheduler)

public class EmployeesModule {

    private TransportModule transportModule;

    public EmployeesModule(Integer branch_id) {
        //TODO:: implement
    }

    public String chooseDriverForTransport(Date date, boolean shift, String drivers_license) {
        //TODO implement
        return null;
    }

    public String getDriverName(String driverId) {
        //TODO implement
        return null;
    }

    public boolean StorageManInShift(Date d, boolean b) {
        //TODO implement
        return false;
    }

    public boolean DriversAvailability(Date date, boolean shift) {
        //TODO implement
        return false;
    }

    public void removeDriverFromTransport(Date d, boolean shift, String driverId) {
        //TODO implement
    }

    public void setTransportModule(TransportModule transportModule) {
        this.transportModule = transportModule;
    }
}
