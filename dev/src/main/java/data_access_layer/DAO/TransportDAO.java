package data_access_layer.DAO;

import bussines_layer.supplier_module.Order;
import bussines_layer.transport_module.Transport;
import bussines_layer.transport_module.Truck;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.support.ConnectionSource;
import com.sun.jmx.remote.util.OrderClassLoaders;
import data_access_layer.DTO.*;
import data_access_layer.Mapper;

import java.sql.SQLException;
import java.util.*;

public class TransportDAO {
    HashMap<Integer, Transport> identityMap;
    public Dao<Transport_DTO, Integer> transport_DAO;
    public Dao<log_DTO, Integer> log_DAO;
    public Dao<pending_orders_dto,Void> pending_orders_dao;

    public TransportDAO(ConnectionSource conn) {
        try{
            transport_DAO = DaoManager.createDao(conn, Transport_DTO.class);
            log_DAO = DaoManager.createDao(conn, log_DTO.class);
            identityMap = new HashMap<>();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateTransportDriver(int transport, String newDriverID, String newDriverName) {
        try {
            Transport_DTO transport_dto = transport_DAO.queryForId(transport);
            transport_dto.setDriverName(newDriverName);
            Worker_DTO driver = Mapper.getInstance().employees_dao.worker_DAO.queryForId(newDriverID);
            transport_dto.setDriverId(driver);
            transport_DAO.update(transport_dto);
        } catch  (SQLException throwables) {
            throwables.printStackTrace();

        }
    }

    public void addTransport(Transport transport) {
        try {
            //creating the transport in the DB
            int part_of_day=0;
            if (transport.getShift()) {
                part_of_day = 1;
            }
            Worker_DTO driver = Mapper.getInstance().employees_dao.worker_DAO.queryForId(transport.getDriverId());
            Truck_DTO truck_dto = Mapper.getInstance().truck_dao.truck_DAO.queryForId(transport.getTruck().getId());
            Transport_DTO transport_dto = new Transport_DTO(transport.getID(), transport.getDate(),
                    part_of_day, truck_dto, driver, transport.getDriverName(), transport.getTotalWeight(),
                    transport.getOrder().getOrderID(),transport.getBranchID());
            transport_DAO.create(transport_dto);

            //creating the transport log
            ArrayList<String> log = transport.getLog();
            for (String message : log) {
                add_to_log(transport.getID(), message);
            }

            //update the Transport ->  set the foreign field of product
            transport_DAO.update(transport_dto);
            identityMap.put(transport.getID(),transport);
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean deleteTransport(int transportID) {
        try {
            Transport_DTO transport_dto = transport_DAO.queryForId(transportID);
            if(transport_dto!=null) {
                delete_from_log(transportID);
                transport_DAO.delete(transport_dto);
                if(identityMap.containsKey(transportID))
                {
                    identityMap.remove(transportID);
                }
                return true;
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public long MaxIDTransport() {
        try {
            long max = transport_DAO.queryRawValue("SELECT MAX(transportID) FROM Transport");
            return max;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }
    }

    public List<Transport> getAllTransports() {
        //return list of all transports in the system if there are no matches -empty list if there is an error- return null
        try {
            List<Transport_DTO> transport_dtos = transport_DAO.queryForAll();
            List<Transport> Transports = new ArrayList<>();
            for (Transport_DTO transport_dto:transport_dtos) {
                if(identityMap.containsKey(transport_dto.getTransportID()))
                {
                    Transports.add(identityMap.get(transport_dto.getTransportID()));
                }
                else {
                    Transports.add(makeTRANSPORT(transport_dto));
                }
            }
            return Transports;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    private Transport makeTRANSPORT(Transport_DTO transport_dto) {
        if(identityMap.containsKey(transport_dto.getTransportID()))
        {
            return identityMap.get(transport_dto.getTransportID());
        }
        boolean partOfDay= false;
        if(transport_dto.getShift()==1)
        {
            partOfDay=true;
        }
        Truck truck = Mapper.getInstance().getTruck(transport_dto.getTruck().getId());
        Order order = Mapper.getInstance().order_dao.find(transport_dto.getOrderId(),transport_dto.getBranch_id());
        Transport transport = new Transport(transport_dto.getTransportID(),transport_dto.getDate(),partOfDay,truck,
                transport_dto.getDriverId().getWorkerID(),transport_dto.getDriverName(), transport_dto.getTotalWeight(),
                order,transport_dto.getBranch_id());

        ForeignCollection<log_DTO> log_dtos = transport_dto.getLog();
        for (log_DTO log_dto : log_dtos) {
            transport.addToLog(log_dto.getMessage());
        }
        identityMap.put(transport.getID(),transport);
        return transport;
    }

    public Transport getTransport(int transportID) {
        //return a transport, if there are no matches or there is an error- return null
        try {
            if(identityMap.containsKey(transportID)){return identityMap.get(transportID);}
            Transport_DTO transport_dto = transport_DAO.queryForId(transportID);
            if (transport_dto == null)
                return null;
            return  makeTRANSPORT(transport_dto);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public Transport getTransportToUpdate(String prevDriverId, Date date, Boolean partOfDay,int branch_id) {
        try {
            List<Transport_DTO> transport_dtos = transport_DAO.queryForEq("Date",date);
            int shift=0;
            if(partOfDay)
                shift=1;
            for (Transport_DTO transport_dto:transport_dtos) {
                if(transport_dto.getDriverId().getWorkerID().equals(prevDriverId) && transport_dto.getShift()==shift && transport_dto.getBranch_id()==branch_id)
                    if(identityMap.containsKey(transport_dto.getTransportID()))
                    {
                        return identityMap.get(transport_dto.getTransportID());
                    }
                    else {
                        return makeTRANSPORT(transport_dto);
                    }
            }
            return null;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public Boolean getTransportByShift(Date d, Boolean partOfDay, int branch_id) {
        try {
            List<Transport_DTO> transport_dtos = transport_DAO.queryForEq("Date",d);
            int shift=0;
            if(partOfDay)
                shift=1;
            for (Transport_DTO transport_dto:transport_dtos) {
                if(transport_dto.getShift()==shift&&transport_dto.getBranch_id()==branch_id)
                    return true;
            }
            return false;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public void add_to_log(int transportID, String message) {
        try {
            Transport_DTO transport_dto = transport_DAO.queryForId(transportID);
            log_DTO log_dto = new log_DTO(message, transport_dto);
            log_DAO.create(log_dto);
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void delete_from_log(int transportID) {
        try {
            log_DAO.executeRaw("DELETE FROM Log WHERE transportID=" + transportID);
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Hashtable<Integer,Transport> getAllTransportsByBranchID(int branch_id) {
        //return list of all transports in the system if there are no matches -empty list if there is an error- return null
        try {
            List<Transport_DTO> transport_dtos = transport_DAO.queryForAll();
            Hashtable<Integer,Transport> Transports = new Hashtable<>();
            for (Transport_DTO transport_dto:transport_dtos) {
                if(transport_dto.getBranch_id()==branch_id) {
                    if (identityMap.containsKey(transport_dto.getTransportID())) {
                        Transports.put(transport_dto.getTransportID(),identityMap.get(transport_dto.getTransportID()));
                    } else {
                        Transports.put(transport_dto.getTransportID(),makeTRANSPORT(transport_dto));
                    }
                }
            }
            return Transports;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public void add_to_pending_orders(int order_id, int branch_id)
    {
        try {
            //creating the pending_order in the DB
            pending_orders_dto p_o = new pending_orders_dto(order_id,branch_id);
            pending_orders_dao.create(p_o);
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Order> getAllPendingOrdersForBranch(int branch)
    {
        try {
            List<Order> orderList = new LinkedList<>();
            List<pending_orders_dto> pending_orders_dtos = pending_orders_dao.queryForEq("branchID", branch);
            for (pending_orders_dto p:pending_orders_dtos) {
                orderList.add(Mapper.getInstance().order_dao.find(p.getOrderID(),branch));
            }
            return orderList;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public void removeFromPendingOrders(int order_id,int branch_id)
    {
        try {
            pending_orders_dao.executeRaw("DELETE FROM pending_orders WHERE orderID=" + order_id+" AND branchID="+branch_id);
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void clearCache() {
        this.identityMap.clear();
    }
}
