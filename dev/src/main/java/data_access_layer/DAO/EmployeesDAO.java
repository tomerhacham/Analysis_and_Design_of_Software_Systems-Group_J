package data_access_layer.DAO;

import bussines_layer.employees_module.Driver;
import bussines_layer.employees_module.Worker;
import bussines_layer.transport_module.Transport;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.support.ConnectionSource;
import data_access_layer.DTO.*;
import data_access_layer.Mapper;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EmployeesDAO {
    public Dao<Driver_DTO, Integer> Driver_DAO;
    public Dao<Position_DTO, Integer> position_DAO;
    public Dao<Worker_DTO, String> worker_DAO;
    public SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    HashMap<String, Worker> identityMap_workers;
    HashMap<String, Driver> identityMap_drivers;

    public EmployeesDAO(ConnectionSource conn) {
        try {
            identityMap_drivers = new HashMap<>();
            identityMap_workers = new HashMap<>();
            Driver_DAO = DaoManager.createDao(conn, Driver_DTO.class);
            position_DAO = DaoManager.createDao(conn, Position_DTO.class);
            worker_DAO = DaoManager.createDao(conn, Worker_DTO.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //TODO:: add in business layer getter fo branch id
    public void addWorker(Worker worker) {
        try {
            //creating the worker in the DB
            Worker_DTO worker_dto = new Worker_DTO(worker.getId(), worker.getName(), worker.getStart_Date(), worker.getSalary(),worker.getBranchID());
            worker_DAO.create(worker_dto);

            //creating the worker positions products in the DB
            List<String> positions = worker.getPositions();
            for (String position : positions) {
                addPosition(position, worker_dto);
            }

            //update the worker ->  set the foreign field of product
            worker_DAO.update(worker_dto);
            identityMap_workers.put(worker.getId(),worker);
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteWorker(String workerId) {
        try {
            Mapper.getInstance().deleteShiftAvailableWorkers(workerId);
            Worker_DTO worker_dto = worker_DAO.queryForId(workerId);
            if(worker_dto!=null) {
                ForeignCollection<Position_DTO> position_dtos = worker_dto.getPositions();
                for (Position_DTO p : position_dtos) {
                    deletePosition(p.getPosition(), workerId);
                }
                worker_DAO.delete(worker_dto);
            }
            if(identityMap_workers.containsKey(workerId))
                identityMap_workers.remove(workerId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Driver
    //TODO:: add in business layer getter fo branch id
    public void addDriver(Driver driver) {
        try {
            //creating the worker in the DB
            Worker_DTO worker_dto = new Worker_DTO(driver.getId(), driver.getName(), driver.getStart_Date(), driver.getSalary(), driver.getBranchID());
            worker_DAO.create(worker_dto);
            //adding License to Driver table

            //creating the worker positions products in the DB
            List<String> positions = driver.getPositions();
            for (String position : positions) {
                addPosition(position, worker_dto);
            }

            //update the worker ->  set the foreign field of product
            worker_DAO.update(worker_dto);

            Driver_DTO driver_dto = new Driver_DTO(worker_dto, driver.getLicense());
            Driver_DAO.create(driver_dto);
            identityMap_drivers.put(driver.getId(),driver);
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteDriver(String driverID) {
        try {
            deleteWorker(driverID);
            Driver_DAO.executeRaw("DELETE FROM Driver WHERE driverID = '" + driverID + "'");
            if(identityMap_drivers.containsKey(driverID))
                identityMap_drivers.remove(driverID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Positions
    private void addPosition(String position, Worker_DTO worker_dto) {
        try {
            Position_DTO position_dto = new Position_DTO(worker_dto, position);
            position_DAO.create(position_dto);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addPosition(String position, String workerID)
    {
        try {
            Worker_DTO worker_dto = worker_DAO.queryForId(workerID);
            Position_DTO position_dto = new Position_DTO(worker_dto, position);
            position_DAO.create(position_dto);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deletePosition(String position, String WorkerId) {
        try {
            position_DAO.executeRaw("DELETE FROM positions WHERE workerID='" + WorkerId + "' AND position='" + position + "'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Worker> getAvailableWorkers(Date date, boolean partOfDay, int branch_id) {
        //return a list with available workers, if there are no matches or there is an error null
        try {
            HashMap<String, Object> args = new HashMap<>();
            args.put("shiftDate", date);
            args.put("partOfDay", partOfDay);
            List<Shift_availableWorkers_DTO> availableWorkers_dtos = Mapper.getInstance().shifts_dao.Shift_availableWorkers_DAO.queryForFieldValues(args);
            List<Worker> retList = new ArrayList<>();
            for (Shift_availableWorkers_DTO available : availableWorkers_dtos) {
                if(available.getWorkerID().getBranch_id()==branch_id) {
                    List<Driver_DTO> driver_dtos = Driver_DAO.queryForEq("driverID", available.getWorkerID());
                    if (driver_dtos.size() == 0) {
                        if(identityMap_workers.containsKey(available.getWorkerID().getWorkerID()))
                            retList.add(identityMap_workers.get(available.getWorkerID().getWorkerID()));
                        else
                            retList.add(makeWORKER(available.getWorkerID()));
                    } else {
                        if(identityMap_drivers.containsKey(available.getWorkerID().getWorkerID()))
                            retList.add(identityMap_drivers.get(available.getWorkerID().getWorkerID()));
                        else
                            retList.add(makeDRIVER(available.getWorkerID()));
                    }
                }
            }
            if(retList.size()==0)
                return null;
            return retList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public void updateWorker(Worker worker) {
        //update only the "simple" fields
        try{
            Worker_DTO worker_dto = worker_DAO.queryForId(worker.getId());
            if(worker_dto==null) {
                return;
            }
            worker_dto.setName(worker.getName());
            worker_dto.setSalary(worker.getSalary());
            worker_dto.setStart_Date(worker.getStart_Date());
            worker_DAO.update(worker_dto);
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Worker getWorker(String id) {
        //return a worker, if there are no matches or there is an error- return null
        try {
            if(identityMap_workers.containsKey(id))
                return identityMap_workers.get(id);
            Worker_DTO worker_dto = worker_DAO.queryForId(id);
            if (worker_dto == null)
                return null;
            return  makeWORKER(worker_dto);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    private Worker makeWORKER(Worker_DTO worker_dto) {
        if(identityMap_workers.containsKey(worker_dto.getWorkerID()))
            return identityMap_workers.get(worker_dto.getWorkerID());
        Worker worker = new Worker(worker_dto.getName(), worker_dto.getWorkerID(),worker_dto.getBranch_id(), worker_dto.getStart_Date(), worker_dto.getSalary());
        ForeignCollection<Position_DTO> position_dtos = worker_dto.getPositions();
        for (Position_DTO p : position_dtos) {
            worker.addPosition(p.getPosition());
        }
        identityMap_workers.put(worker.getId(),worker);
        return worker;
    }

    public boolean isScheduled(String id){
        //check if a worker or a driver is scheduled to a shift
        //return true - if it scheduled, false if not
        try{
            List<Occupation_DTO> occupation_dtos = Mapper.getInstance().shifts_dao.Occupation_DAO.queryForEq("workerID", id);
            if(!occupation_dtos.isEmpty())
            {
                return true;
            }
            List<ShiftDriver_DTO> shiftDriver_dtos = Mapper.getInstance().shifts_dao.Shift_Driver_DAO.queryForEq("driverID", id);
            if(!shiftDriver_dtos.isEmpty())
            {
                return true;
            }
            return false;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    private Driver makeDRIVER(Worker_DTO worker) {
        //make a Driver from table, if there is an error return null
        try {
            if(identityMap_drivers.containsKey(worker.getWorkerID()))
                return identityMap_drivers.get(worker.getWorkerID());
            String License;
            List<Driver_DTO> driver_dtos = Driver_DAO.queryForEq("driverID", worker.getWorkerID());
            if (driver_dtos.size() == 1) {
                Driver_DTO driver_dto = driver_dtos.get(0);
                Driver driver = new Driver(worker.getWorkerID(),worker.getBranch_id(), driver_dto.getLicense(), worker.getName(), worker.getStart_Date(), worker.getSalary());
                ForeignCollection<Position_DTO> position_dtos = worker.getPositions();
                for (Position_DTO p : position_dtos) {
                    driver.addPosition(p.getPosition());
                }
                identityMap_drivers.put(driver.getId(),driver);
                return driver;
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public List<Worker> getAllWorkers()
    {//return list of all workers in the system if there are no matches or there is an error- return null
        try {
            List<Worker_DTO> worker_dtos = worker_DAO.queryForAll();
            List<Worker> workers = new ArrayList<>();
            for (Worker_DTO w:worker_dtos ) {
                List<Driver_DTO> driver_dtos = Driver_DAO.queryForEq("driverID", w.getWorkerID());
                if(driver_dtos.size()==0) {
                    if(identityMap_workers.containsKey(w.getWorkerID()))
                        workers.add(identityMap_workers.get(w.getWorkerID()));
                    else
                        workers.add(makeWORKER(w));
                }
                else{
                    if(identityMap_drivers.containsKey(w.getWorkerID()))
                        workers.add(identityMap_drivers.get(w.getWorkerID()));
                    else
                        workers.add(makeDRIVER(w));
                }
            }
            if(workers.isEmpty())
            {
                return null;
            }
            return workers;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public List<Worker> getAllWorkersByBranch(int branch_id)
    {//return list of all workers in the system if there are no matches or there is an error- return null
        try {
            List<Worker_DTO> worker_dtos = worker_DAO.queryForAll();
            List<Worker> workers = new ArrayList<>();
            for (Worker_DTO w:worker_dtos ) {
                if(w.getBranch_id()==branch_id) {
                    List<Driver_DTO> driver_dtos = Driver_DAO.queryForEq("driverID", w.getWorkerID());
                    if (driver_dtos.size() == 0) {
                        if (identityMap_workers.containsKey(w.getWorkerID()))
                            workers.add(identityMap_workers.get(w.getWorkerID()));
                        else
                            workers.add(makeWORKER(w));
                    } else {
                        if (identityMap_drivers.containsKey(w.getWorkerID()))
                            workers.add(identityMap_drivers.get(w.getWorkerID()));
                        else
                            workers.add(makeDRIVER(w));
                    }
                }
            }
            if(workers.isEmpty())
            {
                return null;
            }
            return workers;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public Driver getDriver(String driver_id)
    {
        if(identityMap_drivers.containsKey(driver_id))
            return identityMap_drivers.get(driver_id);
        try {
            Worker_DTO driver_dto = worker_DAO.queryForId(driver_id);
            return makeDRIVER(driver_dto);
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}