package bussines_layer.transport_module;

import bussines_layer.supplier_module.Order;
import data_access_layer.Mapper;

import java.util.*;
public class TransportController {
    private static Mapper mapper = Mapper.getInstance();
    private Hashtable<Integer, Transport> transports;
    private List<Order> pending_Orders;

    private int Id_Counter;
    private int branch_id;

    public TransportController(int branchId) {
        Id_Counter = (int)mapper.MaxIDTransport() + 1;
        branch_id = branchId;
        transports=mapper.getAllTransportsByBranchID(branch_id);
        pending_Orders=mapper.getAllPendingOrdersForBranch(branchId);
    }


    public String BookTransport(Date date, boolean partOfDay, Truck truck, String driverId, String driverName, float totalWeight, Order order)
    {
        Transport transport = new Transport(Id_Counter,date,partOfDay,truck,driverId,driverName,totalWeight,order,branch_id);
        transports.put(transport.getID(),transport);
        mapper.addTransport(transport);
        return transport.toString();
    }

    public void addToPendingOrder(Order order)
    {
        pending_Orders.add(order);
        mapper.add_to_pending_orders(order.getOrderID(),branch_id);
    }

    //if a transport exist in the system, delete it - not remove from DB
    public boolean DeleteTransport(Integer id) {
        if (transports.containsKey(id)) {
            transports.remove(id);
            return mapper.deleteTransport(id);
        }
        return false;
    }

    //return the details of all transports in the system
    public String getAllTransportsDetails() {
        String details = "";
        int count = 1;
        for (Transport t : transports.values()) {
            details = details + count + ". " + t.toString() + "\n";
            count++;
        }
        return details;
    }

    //if a transport exist in the system return its date, else null
    public Date getTransportDate(int id) {
        if(transports.containsKey(id)) {
            return transports.get(id).getDate();
        }
        else
        {
            Transport t = mapper.getTransport(id);
            if (t != null){
                return t.getDate();
            }
        }
        return null;
    }


    //if a transport exist in the system return its truck ID, -1 if truck is null
    public int getTransportTruck(int id) {

        if(transports.containsKey(id))
        {
            Truck t = transports.get(id).getTruck();
            if(t!=null)
                return t.getId();
        }
        return -1;
    }

    //add a string to a transport log
    public void addToLog(String s, Integer id) {
       if(transports.containsKey(id)) {
           transports.get(id).addToLog(s);
           mapper.add_to_log(id,s);
       }
    }


    public boolean getTransportShift(int transportID)
    {
        Transport t = mapper.getTransport(transportID);
        if (t != null){
            return t.getShift();
        }
        return transports.get(transportID).getShift();
    }

    public void changeDriverInTransport(String prevDriverId, String newDriverId, Date date, Boolean shift, String newDriverName)
    {
        Transport t = mapper.getTransportToUpdate(prevDriverId, date, shift,branch_id);
        transports.get(t.getID()).setDriver(newDriverId,newDriverName);
        mapper.updateTransportDriver(t.getID(), newDriverId, newDriverName);
    }

    public Boolean isTransportExist(Date d, Boolean partOfDay)
    {
        return mapper.getTransportByShift(d, partOfDay, branch_id) != null;
    }

    public String getTransportDriverID(int TransportID)
    {
        Transport t = mapper.getTransport(TransportID);
        if(transports.containsKey(TransportID))
            return transports.get(TransportID).getDriverId();
        else if (t != null)
            return t.getDriverId();
        return "";
    }

    public boolean checkIfTransportExist(int transportID){
        if (transports.containsKey(transportID)){
            return true;
        }
        return false;
    }

    public String getPendingOrdersDetails()
    {
        String s="";
        for (Order o :pending_Orders) {
            //TODO: toString
        }
        return s;
    }

    public boolean isOrderIdInPendingOrders(int order_id)
    {
        for (Order o:pending_Orders) {
            if(o.getOrderID()==order_id)
                return true;
        }
        return false;
    }

    public Order getFromPending(int order_id)
    {
        for (Order o:pending_Orders) {
            if(o.getOrderID()==order_id)
                return o;
        }
        return null;
    }

    //TODO::daily function that delete non relevant orders

}