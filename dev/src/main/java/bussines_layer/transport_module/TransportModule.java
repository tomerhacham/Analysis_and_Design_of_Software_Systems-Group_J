package bussines_layer.transport_module;
import bussines_layer.employees_module.EmployeesModule;
import bussines_layer.supplier_module.Order;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransportModule {

    private TransportController transportController ;
    private TruckController truckController ;
    private EmployeesModule employeesModule = null ;
    private Integer branch_id;

    public TransportModule(int branch_id)
    {
        transportController=new TransportController(branch_id);
        truckController=new TruckController(branch_id);
        this.branch_id=branch_id;
    }

    public void setEmployeesModule(EmployeesModule employeesModule) {
        this.employeesModule = employeesModule;
    }

    //region Book Transport Transport

    public String BookTransport(Order order)
    {
        Date date = order.getIssuedDate();
        boolean shift ;
        int totalWeight=0; //TODO::: ORDER / FUNCTION
        for(int i=1; i<=7; i++)
        {
            //check storage man
            if(checkIfStorageManInMorningShift(date))
                shift=true;
            else if (checkIfStorageManInNightShift(date))
                shift =false;
            else
                continue;
            //check driver availability
            if(!checkIfDriversAndTrucksAvailable(date,shift))
                continue;

            List<Truck> trucks =  truckController.getAvailableTrucks(date,shift, totalWeight);
            if(trucks.isEmpty())
                continue;
            for (Truck truck:trucks) {
                String DriverId = employeesModule.chooseDriverForTransport(date, shift, truck.getDrivers_license());
                if(DriverId == null)
                    continue;
                String DriverName = employeesModule.getDriverName(DriverId);
                truckController.addDate(date,shift, truck.getId());
                return transportController.BookTransport(date,shift,truck,DriverId,DriverName,totalWeight,order);
            }
            date = addDay(date);
        }
        transportController.addToPendingOrder(order);
        return "The transport book - Failed!\n The order moved to pending list. \n" +
                "please ensure that there are available storage man, driver and a truck for further treatment.";
    }

    //TODO: check if works
    public Date addDay(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    public boolean checkIfStorageManInMorningShift(Date d)
    {
        return employeesModule.StorageManInShift(d, true);
    }

    public boolean checkIfStorageManInNightShift(Date d)
    {
        return employeesModule.StorageManInShift(d, false);
    }

    public boolean checkIfDriversAndTrucksAvailable(Date date, boolean shift)
    {
        boolean trucksAvailable = truckController.checkIfTrucksAvailableByDate(date , shift);
        boolean driversAvailable = employeesModule.DriversAvailability(date , shift);
        return driversAvailable&&trucksAvailable;
    }

    //endregion

    //region Transports for IO
    public String getAllTransportsDetails() {
        return transportController.getAllTransportsDetails();
    }

    public boolean checkIfTransportExist(int transportID){
        return transportController.checkIfTransportExist(transportID);
    }

    public void removeDatesFromDriverAndTruck( int transportID) {
        String DriverId = transportController.getTransportDriverID(transportID);
        Date d =transportController.getTransportDate(transportID);
        boolean shift = transportController.getTransportShift(transportID);
        employeesModule.removeDriverFromTransport(d, shift, DriverId);
        truckController.removeDate(d,shift, transportController.getTransportTruck(transportID));
    }

    public boolean deleteTransport(int transportToDelete) {
        return transportController.DeleteTransport(transportToDelete);
    }

    public String getPendingOrdersDetails()
    {
        return transportController.getPendingOrdersDetails();
    }

    public boolean isOrderIdInPendingOrders(int orderId)
    {
        return transportController.isOrderIdInPendingOrders(orderId);
    }

    public String BookTransportForPendingOrders(int order_id)
    {
        Order order = transportController.getFromPending(order_id);
        if(order!=null)
        {
            return BookTransport(order);
        }
        return "The order with id:"+order_id+"is not in the pending orders list.";
    }
    //endregion

    //region Trucks for IO

    public String getAllTrucksDetails() {
        return truckController.getAllTrucksDetails();
    }

    public boolean createTruck(String license_plate, String model, float netWeight, float maxWeight, String drivers_license) {
        return truckController.CreateTruck(license_plate, model, netWeight, maxWeight, drivers_license);
    }


    public boolean deleteTruck(int truckToDelete) {
        return truckController.DeleteTruck(truckToDelete);
    }

    //endregion

    public void changeDriverInTransport(String prevDriverId, String newDriverId, Date date, Boolean shift)
    {
        String newDriverName = employeesModule.getDriverName(newDriverId);
        transportController.changeDriverInTransport(prevDriverId, newDriverId, date, shift, newDriverName);
    }

    public Boolean isTransportExist(Date date, Boolean shift)
    {
        return transportController.isTransportExist(date, shift);
    }

}
