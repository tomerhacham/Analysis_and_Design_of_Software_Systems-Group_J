package bussines_layer.transport_module;

import bussines_layer.supplier_module.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Transport {
    private int ID;
    private Date Date;
    private boolean Shift; //in which shift is the transport
    private Truck Truck;
    private String driverId;
    private String driverName;
    private Order order;
    private float TotalWeight;
    private ArrayList<String> log;
    private int branch_id;

    public Transport(int id, Date date, boolean partOfDay, Truck truck, String driverId, String driverName, float totalWeight, Order order, int branch_id)
    {
        ID=id;
        Date=date;
        Shift=partOfDay;
        Truck=truck;
        this.driverId =driverId;
        this.driverName=driverName;
        TotalWeight=totalWeight;
        log =new ArrayList<>();
        this.order=order;
        this.branch_id=branch_id;
    }

    public int getID() { return ID;}

    public Date getDate() { return Date;}

    public Truck getTruck() {return Truck; }

    public void setTruck(Truck truck) {Truck = truck;}

    public String getDriverId() { return driverId; }

    public void setDriver(String driver_Id, String driver_Name) {
        driverId = driver_Id;
        driverName = driver_Name;
    }

    public void addToLog (String s)
    {
        log.add(s);
    }

    public String getLogMessages(){
        String s = "";
        for (int i=0; i<log.size();i++)
        {
            s=s+ "\t" + (i+1) +". "+log.get(i)+"\n";
        }
        return s;
    }

    //doing first the setWeight method to validate the data
    public float getTotalWeight() {
        return TotalWeight;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public String toString() { //TODO: print all product details
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String s = "Transport Details:\n" + "\tid: " + ID + "\tDate: " + formatter.format(Date) +
                "\tTruckNumber: " + Truck.getLicense_plate() +
                "\tDriver: " + driverName + "\n" +
                "\tSupplier details:\n\t\t" + order.getSupplier().getSupplierName() + "\n";
        s = s + "\tTotalWeight: " + TotalWeight +"\n";
        if(log.size()>0) {
            s = s + "\tLog messages:\n" + getLogMessages();
        }
        else {
            s = s + "\tLog messages: none";
        }
        return s;
    }

    public boolean getShift(){return Shift;}

    public ArrayList<String> getLog() {
        return log;
    }

    public String getDriverName() {
        return driverName;
    }

    public int getBranchID() {
        return branch_id;
    }
}
