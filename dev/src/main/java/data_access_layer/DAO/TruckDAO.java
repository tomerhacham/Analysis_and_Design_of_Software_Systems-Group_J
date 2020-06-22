package data_access_layer.DAO;

import bussines_layer.transport_module.Truck;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import data_access_layer.DTO.BranchDTO;
import data_access_layer.DTO.Truck_DTO;
import data_access_layer.DTO.morning_shifts_DTO;
import data_access_layer.DTO.night_shifts_DTO;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TruckDAO {
    HashMap<Integer, Truck> identityMap;
    public Dao<Truck_DTO, Integer> truck_DAO;
    public Dao<morning_shifts_DTO, Integer> morning_shifts_DAO;
    public Dao<night_shifts_DTO, Integer> night_shifts_DAO;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public TruckDAO(ConnectionSource conn) {
        try {
            identityMap = new HashMap<>();
            morning_shifts_DAO = DaoManager.createDao(conn, morning_shifts_DTO.class);
            night_shifts_DAO = DaoManager.createDao(conn, night_shifts_DTO.class);
            truck_DAO = DaoManager.createDao(conn, Truck_DTO.class);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addTruck(Truck truck) {
        identityMap.put(truck.getId(),truck);
        Truck_DTO truck_dto = new Truck_DTO(truck.getId(), truck.getLicense_plate(), truck.getModel(),
                truck.getNet_weight(), truck.getMax_weight(), truck.getDrivers_license(),truck.getBranchID());
        try {
            truck_DAO.create(truck_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean deleteTruck(int truckID) {
        try {
            if(identityMap.containsKey(truckID))
            {
                identityMap.remove(truckID);
            }
            Truck_DTO truck_dto = truck_DAO.queryForId(truckID);
            if(truck_dto!=null) {
                ForeignCollection<morning_shifts_DTO> morning_shifts_dtos = truck_dto.getMorning_shifts();
                for (morning_shifts_DTO ms : morning_shifts_dtos) {
                    delete_MorningShift(ms.getDate(), truckID);
                }
                ForeignCollection<night_shifts_DTO> night_shifts_dtos = truck_dto.getNight_shifts();
                for (night_shifts_DTO ns : night_shifts_dtos) {
                    deleteNightShift(ns.getDate(), truckID);
                }
                truck_DAO.delete(truck_dto);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    //Morning Shifts - Truck
    public void addMorningShift(int truckID, Date date) {
        try {
            Truck_DTO truck_dto = truck_DAO.queryForId(truckID);
            morning_shifts_DTO morningShiftsDto = new morning_shifts_DTO(date, truck_dto);
            morning_shifts_DAO.create(morningShiftsDto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete_MorningShift(Date date, int truckID) {
        try {
            morning_shifts_DAO.executeRaw("DELETE FROM morningShifts WHERE date='" + formatter.format(date) + "' AND truckID=" + truckID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Night Shifts - Truck
    public void addNightShift(int truckID, Date date) {
        try {
            Truck_DTO truck_dto = truck_DAO.queryForId(truckID);
            night_shifts_DTO night_shifts_dto = new night_shifts_DTO(date, truck_dto);
            night_shifts_DAO.create(night_shifts_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteNightShift(Date date, int truckID) {
        try {
            night_shifts_DAO.executeRaw("DELETE FROM nightShifts WHERE date='" + formatter.format(date) + "' AND truckID=" + truckID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public long MaxIdTrucks()
    {
        try {
            long max = truck_DAO.queryRawValue("SELECT MAX(truckID) FROM Truck");
            return max;
        }catch (Exception e)
        {
            return 0;
        }
    }

    public Truck getTruck(int id)
    {
        if(identityMap.containsKey(id))
        {
            return identityMap.get(id);
        }
        //return a truck, if there are no matches or there is an error- return null
        try {
            Truck_DTO truck_dto = truck_DAO.queryForId(id);
            if (truck_dto == null)
                return null;
            return  makeTRUCK(truck_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    private Truck makeTRUCK  (Truck_DTO truck_dto) {
        if(identityMap.containsKey(truck_dto.getId())){return identityMap.get(truck_dto.getId());}
        Truck truck = new Truck(truck_dto.getId(),truck_dto.getLicense_plate(),truck_dto.getModel(),truck_dto.getNet_weight(),
                truck_dto.getMax_weight(),truck_dto.getDrivers_license(),truck_dto.getBranch_id());
        ForeignCollection<morning_shifts_DTO> morning_shifts_dtos = truck_dto.getMorning_shifts();
        for (morning_shifts_DTO morningShiftsDto : morning_shifts_dtos) {
            truck.addDate(morningShiftsDto.getDate(),true);
        }
        ForeignCollection<night_shifts_DTO> night_shifts_dtos = truck_dto.getNight_shifts();
        for (night_shifts_DTO night_shifts_dto : night_shifts_dtos) {
            truck.addDate(night_shifts_dto.getDate(),false);
        }
        identityMap.put(truck.getId(), truck);
        return truck;
    }

    public List<Truck> getAllTrucks()
    {
        //return list of all trucks in the system if there are no matches - empty list if there is an error- return null
        try {
            List<Truck_DTO> truck_dtos = truck_DAO.queryForAll();
            List<Truck> trucks = new ArrayList<>();
            for (Truck_DTO t:truck_dtos ) {
                if(identityMap.containsKey(t.getId()))
                {
                    trucks.add(identityMap.get(t.getId()));
                }
                else {
                    trucks.add(makeTRUCK(t));
                }
            }
            return trucks;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //todo:check if the queries works!
    public boolean checkIfTrucksAvailableByDate(Date date, boolean partOfDay ,int branch_id)
    {
        try{
            long numOfTrucks = truck_DAO.queryRawValue("SELECT count(truckID) FROM Truck WHERE BranchID="+branch_id);
            long numOfOccupaied;
            if(partOfDay)//morning
            {
                numOfOccupaied = morning_shifts_DAO.queryRawValue("SELECT count(truckID) FROM morningShifts WHERE date='"+formatter.format(date) + "'");
            }
            else //night
            {
                numOfOccupaied = night_shifts_DAO.queryRawValue("SELECT count(truckID) FROM nightShifts WHERE date='"+formatter.format(date) + "'");
            }
            return numOfTrucks>numOfOccupaied;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<Truck> getAvailableTrucks(Date date, boolean partOfDay, float Weight,int branch)
    {
        try{
            List<Truck_DTO> truck_dtos = truck_DAO.queryForAll();
            List<Truck> trucks = new ArrayList<>();
            for (Truck_DTO truck_dto:truck_dtos) {
                if(truck_dto.getNet_weight()+ Weight < truck_dto.getMax_weight()&&truck_dto.getBranch_id()==branch)
                {
                    if(partOfDay && TruckNotInMorningShift(date,truck_dto.getId()))
                    {
                        if(identityMap.containsKey(truck_dto.getId()))
                        {
                            trucks.add(identityMap.get(truck_dto.getId()));
                        }
                        else {
                            trucks.add(makeTRUCK(truck_dto));
                        }
                    }
                    else if(!partOfDay && TruckNotInNightShift(date,truck_dto.getId()))
                    {
                        if(identityMap.containsKey(truck_dto.getId()))
                        {
                            trucks.add(identityMap.get(truck_dto.getId()));
                        }
                        else {
                            trucks.add(makeTRUCK(truck_dto));
                        }                    }
                }
            }
            return trucks;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean TruckNotInMorningShift(Date date , int truckID){
        try{
            HashMap<String, Object> args = new HashMap<>();
            args.put("date", date);
            args.put("truckID", truckID);
            List<morning_shifts_DTO> morning_shifts_dtos = morning_shifts_DAO.queryForFieldValues(args);
            if(morning_shifts_dtos.isEmpty())
                return true;
            return false;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean TruckNotInNightShift(Date date, int truckID){
        try{
            HashMap<String, Object> args = new HashMap<>();
            args.put("date", date);
            args.put("truckID", truckID);
            List<night_shifts_DTO> night_shifts_dtos = night_shifts_DAO.queryForFieldValues(args);
            if(night_shifts_dtos.isEmpty())
                return true;
            return false;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Hashtable<Integer,Truck> getAllTrucksByBranch(int branch_id)
    {
        //return list of all trucks in the system if there are no matches - empty list if there is an error- return null
        try {
            List<Truck_DTO> truck_dtos = truck_DAO.queryForAll();
            Hashtable<Integer,Truck> trucks = new Hashtable<>();
            for (Truck_DTO t:truck_dtos ) {
               if(t.getBranch_id()==branch_id) {
                   if (identityMap.containsKey(t.getId())) {
                       trucks.put(t.getId(),identityMap.get(t.getId()));
                   } else {
                       trucks.put(t.getId(),makeTRUCK(t));
                   }
               }
            }
            return trucks;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public void clearCache() {
        this.identityMap.clear();
    }
}
