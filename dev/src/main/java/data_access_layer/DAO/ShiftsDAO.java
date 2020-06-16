package data_access_layer.DAO;

import bussines_layer.employees_module.Driver;
import bussines_layer.employees_module.FixedSizeList;
import bussines_layer.employees_module.Shift;
import bussines_layer.employees_module.Worker;
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

public class ShiftsDAO {
    HashMap<String, Shift> identityMap;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    public Dao<Shift_availableWorkers_DTO, Integer> Shift_availableWorkers_DAO;
    public Dao<Shift_DTO, String> Shift_DAO;
    public Dao<ShiftDriver_DTO, Integer> Shift_Driver_DAO;
    public Dao<Occupation_DTO, Integer> Occupation_DAO;

    public ShiftsDAO(ConnectionSource conn) {
        try {
            identityMap = new HashMap<>();
            Shift_DAO = DaoManager.createDao(conn, Shift_DTO.class);
            Occupation_DAO = DaoManager.createDao(conn, Occupation_DTO.class);
            Shift_Driver_DAO = DaoManager.createDao(conn, ShiftDriver_DTO.class);
            Shift_availableWorkers_DAO = DaoManager.createDao(conn, Shift_availableWorkers_DTO.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //TODO:: add in business layer getter fo branch id
    public void addShift(Shift shift) {
        try {
            //creating the shifts in the DB
            int part_of_day=0;
            if (shift.getTimeOfDay()) {
                part_of_day = 1;
            }
            Shift_DTO shift_dto = new Shift_DTO(shift.getId(), shift.getDate(), part_of_day, shift.getBranchID());
            Shift_DAO.create(shift_dto);

            //creating the shift available workers in the DB
            List<Driver> scheduledDrivers = shift.getScheduledDrivers();
            for (Driver d : scheduledDrivers) {
                addShiftDriver(d.getId(), shift.getId());
            }

            //creating the occupation workers in the DB
            HashMap<String, FixedSizeList<Worker>> Occupation = shift.getOccupation();
            for (String position : Occupation.keySet()) {
                for (Worker w : Occupation.get(position)) {
                    addOccupation(shift.getId(), position, );
                }
            }

            //update the Shift ->  set the foreign field of product
            Shift_DAO.update(shift_dto);
            identityMap.put(shift.getId(),shift);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteShift(String shiftID) {
        try {
            Shift_DTO shift_dto = Shift_DAO.queryForId(shiftID);
            if(shift_dto!=null) {
                ForeignCollection<ShiftDriver_DTO> scheduledDrivers = shift_dto.getDrivers_in_shift();
                for (ShiftDriver_DTO shiftDriver_dto : scheduledDrivers) {
                    deleteShiftDriver(shiftDriver_dto.getDriverID().getWorkerID(), shiftID);
                }

                ForeignCollection<Occupation_DTO> occupation = shift_dto.getOccupation();
                for (Occupation_DTO occupation_dto : occupation) {
                    deleteOccupation(shiftID, occupation_dto.getPosition(), occupation_dto.getWorkerID().getWorkerID());
                }

                Shift_DAO.delete(shift_dto);
                if(identityMap.containsKey(shiftID)){
                    identityMap.remove(shiftID);}
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Occupation
    public void addOccupation(String shiftId, String position, String worker_id) {
        try {
            Shift_DTO shift_dto = Shift_DAO.queryForId(shiftId);
            Worker_DTO worker = Mapper.getInstance().employees_dao.worker_DAO.queryForId(worker_id);
            Occupation_DTO occupation_dto = new Occupation_DTO(position, worker, shift_dto);
            Occupation_DAO.create(occupation_dto);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteOccupation(String shiftId, String position, String WorkerID) {
        try {
            Occupation_DAO.executeRaw("DELETE FROM Occupation WHERE position='" + position + "' AND workerID='" + WorkerID + "' AND ShiftID='" + shiftId + "'");
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Shift Available Workers
    public void addShiftAvailableWorkers(String workerID, Date date, boolean timeOfDay) {
        int part_of_day=0;
        if (timeOfDay) {
            part_of_day = 1;
        }
        try {
            Worker_DTO worker_dto = Mapper.getInstance().employees_dao.worker_DAO.queryForId(workerID);
            Shift_availableWorkers_DTO shift_availableWorkers_dto = new Shift_availableWorkers_DTO(worker_dto, date, part_of_day);
            Shift_availableWorkers_DAO.create(shift_availableWorkers_dto);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // delete specific row
    public boolean deleteShiftAvailableWorkers(String WorkerId, Date date, boolean timeOfDay) {
        int part_of_day=0;
        if (timeOfDay ) {
            part_of_day = 1;
        }
        try {
            int deleted = Shift_availableWorkers_DAO.executeRaw("DELETE FROM Shift_availableWorkers WHERE workerID='" + WorkerId + "' AND ShiftDate='" + formatter.format(date) + "' And partOfDay=" + part_of_day);
            if(deleted==1)
            {
                return true;
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    // delete all rows with specified ID
    public void deleteShiftAvailableWorkers(String workerId) {
        try {
            Shift_availableWorkers_DAO.executeRaw("DELETE FROM Shift_availableWorkers WHERE workerID='" + workerId + "'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Shift Drivers
    public void addShiftDriver(String driverID, String shiftID) {
        try {
            Shift_DTO shift_dto = Shift_DAO.queryForId(shiftID);
            Worker_DTO driver = Mapper.getInstance().employees_dao.worker_DAO.queryForId(driverID);
            ShiftDriver_DTO shiftDriver_dto = new ShiftDriver_DTO(driver, shift_dto);
            Shift_Driver_DAO.create(shiftDriver_dto);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteShiftDriver(String driverID, String shiftID) {
        try {
            Shift_Driver_DAO.executeRaw("DELETE FROM Shift_Driver WHERE driverID='" + driverID + "' AND ShiftID='" + shiftID + "'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateShift(Shift shift) {
        try{//update only the "simple" fields
            Shift_DTO shift_dto = Shift_DAO.queryForId(shift.getId());
            if(shift_dto==null) {
                System.out.println("there is no such shift in the system");
                return;
            }
            shift_dto.setDate(shift.getDate());
            int partOfDay=0;
            if(shift.getTimeOfDay()){
                partOfDay=1;
            }
            shift_dto.setTimeOfDay(partOfDay);
            Shift_DAO.update(shift_dto);
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Shift getShift(Date date, boolean partOfDay, int branch_id) {
        //return a shift, if there are no matches or there is an error- return null
        try {
            HashMap<String, Object> args = new HashMap<>();
            args.put("date", date);
            args.put("partOfDay", partOfDay);
            List<Shift_DTO> shift_dtos = Shift_DAO.queryForFieldValues(args);
            for (Shift_DTO s:shift_dtos) {
                if(s.getBranch_id()==branch_id)
                {
                    if(identityMap.containsKey(s.getShiftID()))
                        return identityMap.get(s.getShiftID());
                    else {
                        return makeSHIFT(s);
                    }
                }
            }
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    private Shift makeSHIFT(Shift_DTO shift_dto) {
        //make a Shift from table
        if(identityMap.containsKey(shift_dto.getShiftID())) {
            return identityMap.get(shift_dto.getShiftID());
        }
        boolean partOfDay = false;
        if (shift_dto.getTimeOfDay() == 1) {
            partOfDay = true;
        }
        Shift shift = new Shift(shift_dto.getDate(), partOfDay, shift_dto.getShiftID());
        ForeignCollection<Occupation_DTO> occupation_dtos = shift_dto.getOccupation();
        HashMap<String, ArrayList<Worker>> occupation = new HashMap<>();
        for (Occupation_DTO o : occupation_dtos) {
            if(occupation.containsKey(o.getPosition())){
                occupation.get(o.getPosition()).add(Mapper.getInstance().getWorker(o.getWorkerID().getWorkerID()));
            }
            else
            {
                occupation.put(o.getPosition(), new ArrayList<>());
                occupation.get(o.getPosition()).add(Mapper.getInstance().getWorker(o.getWorkerID().getWorkerID()));
            }
        }
        for (String position: occupation.keySet()) {
            FixedSizeList<Worker> list = new FixedSizeList<>(occupation.get(position).size());
            list.addAll(occupation.get(position));
            shift.addARowToOcuupation(position,list);
        }
        ForeignCollection<ShiftDriver_DTO> drivers_in_shift = shift_dto.getDrivers_in_shift();
        for (ShiftDriver_DTO sd : drivers_in_shift) {
            shift.addDriverToShift(Mapper.getInstance().getDriver(sd.getDriverID().getWorkerID()));
        }
        identityMap.put(shift.getId(),shift);
        return shift;
    }

}
