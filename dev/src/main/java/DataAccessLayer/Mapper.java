package DataAccessLayer;

import BusinessLayer.Transport.*;
import BusinessLayer.Workers.*;
import DataAccessLayer.DTO.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.text.SimpleDateFormat;
import java.util.*;


//singleton
public class Mapper {
    private static Mapper instance = null;
    private String databaseUrl = "jdbc:sqlite:dev\\src\\main\\java\\DataAccessLayer\\SuperLi.db";
    private static ConnectionSource conn;
    private Dao<DestFile_DTO, Integer> DestFile_DAO;
    private Dao<Driver_DTO, Integer> Driver_DAO;
    private Dao<log_DTO, Integer> log_DAO;
    private Dao<morning_shifts_DTO, Integer> morning_shifts_DAO;
    private Dao<night_shifts_DTO, Integer> night_shifts_DAO;
    private Dao<Occupation_DTO, Integer> Occupation_DAO;
    private Dao<Position_DTO, Integer> position_DAO;
    private Dao<Product_DTO, Integer> product_DAO;
    private Dao<ProductFile_DTO, Integer> productFile_DAO;
    private Dao<Shift_availableWorkers_DTO, Integer> Shift_availableWorkers_DAO;
    private Dao<Shift_DTO, String> Shift_DAO;
    private Dao<ShiftDriver_DTO, Integer> Shift_Driver_DAO;
    private Dao<Site_DTO, Integer> site_DAO;
    private Dao<Transport_DTO, Integer> transport_DAO;
    private Dao<Truck_DTO, Integer> truck_DAO;
    private Dao<Worker_DTO, String> worker_DAO;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    private Mapper() throws Exception {
        try {
            conn = new JdbcConnectionSource(databaseUrl);
            Connecting_to_DB();
        } catch (Exception e) {
            throw e;
        }
    }

    //if there is a problem with creating the connection return null
    public static Mapper getInstance() {
        if (instance == null) {
            try {
                instance = new Mapper();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return instance;
    }

    private void Connecting_to_DB() throws Exception {
        site_DAO = DaoManager.createDao(conn, Site_DTO.class);
        productFile_DAO = DaoManager.createDao(conn, ProductFile_DTO.class);
        Driver_DAO = DaoManager.createDao(conn, Driver_DTO.class);
        position_DAO = DaoManager.createDao(conn, Position_DTO.class);
        worker_DAO = DaoManager.createDao(conn, Worker_DTO.class);
        morning_shifts_DAO = DaoManager.createDao(conn, morning_shifts_DTO.class);
        night_shifts_DAO = DaoManager.createDao(conn, night_shifts_DTO.class);
        truck_DAO = DaoManager.createDao(conn, Truck_DTO.class);
        product_DAO = DaoManager.createDao(conn, Product_DTO.class);
        Shift_DAO = DaoManager.createDao(conn, Shift_DTO.class);
        Occupation_DAO = DaoManager.createDao(conn, Occupation_DTO.class);
        Shift_Driver_DAO = DaoManager.createDao(conn, ShiftDriver_DTO.class);
        Shift_availableWorkers_DAO = DaoManager.createDao(conn, Shift_availableWorkers_DTO.class);
        log_DAO = DaoManager.createDao(conn, log_DTO.class);
        DestFile_DAO = DaoManager.createDao(conn, DestFile_DTO.class);
        transport_DAO = DaoManager.createDao(conn, Transport_DTO.class);
    }

    public static void CloseConnection() throws Exception {
        try {
            conn.close();
        } catch (Exception e) {
            throw new Exception("error accrued when trying to close connection: " + e.getMessage());
        }
    }

    //Worker
    public void addWorker(Worker worker) {
        try {
            //creating the worker in the DB
            Worker_DTO worker_dto = new Worker_DTO(worker.getId(), worker.getName(), worker.getStart_Date(), worker.getSalary());
            worker_DAO.create(worker_dto);

            //creating the worker positions products in the DB
            List<String> positions = worker.getPositions();
            for (String position : positions) {
                addPosition(position, worker_dto);
            }

            //update the worker ->  set the foreign field of product
            worker_DAO.update(worker_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteWorker(String workerId) {
        try {
            deleteShiftAvailableWorkers(workerId);
            Worker_DTO worker_dto = worker_DAO.queryForId(workerId);
            if(worker_dto!=null) {
                ForeignCollection<Position_DTO> position_dtos = worker_dto.getPositions();
                for (Position_DTO p : position_dtos) {
                    deletePosition(p.getPosition(), workerId);
                }
                worker_DAO.delete(worker_dto);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Driver
    public void addDriver(Driver driver) {
        try {
            //creating the worker in the DB
            Worker_DTO worker_dto = new Worker_DTO(driver.getId(), driver.getName(), driver.getStart_Date(), driver.getSalary());
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteDriver(String driverID) {
        try {
            deleteWorker(driverID);
            Driver_DAO.executeRaw("DELETE FROM Driver WHERE driverID = '" + driverID + "'");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Positions
    private void addPosition(String position, Worker_DTO worker_dto) {
        try {
            Position_DTO position_dto = new Position_DTO(worker_dto, position);
            position_DAO.create(position_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPosition(String position, String workerID) //TODO:rquires testing
    {
        try {
            Worker_DTO worker_dto = worker_DAO.queryForId(workerID);
            Position_DTO position_dto = new Position_DTO(worker_dto, position);
            position_DAO.create(position_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deletePosition(String position, String WorkerId) {
        try {
            position_DAO.executeRaw("DELETE FROM positions WHERE workerID='" + WorkerId + "' AND position='" + position + "'");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Shift
    public void addShift(Shift shift) {
        try {
            //creating the shifts in the DB
            int part_of_day=0;
            if (shift.getTimeOfDay()) {
                part_of_day = 1;
            }
            Shift_DTO shift_dto = new Shift_DTO(shift.getId(), shift.getDate(), part_of_day);
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
                    addOccupation(shift.getId(), position, w.getId());
                }
            }

            //update the Shift ->  set the foreign field of product
            Shift_DAO.update(shift_dto);

        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Occupation
    public void addOccupation(String shiftId, String position, String WorkerID) {
        try {
            Shift_DTO shift_dto = Shift_DAO.queryForId(shiftId);
            Worker_DTO worker_dto = worker_DAO.queryForId(WorkerID);
            Occupation_DTO occupation_dto = new Occupation_DTO(position, worker_dto, shift_dto);
            Occupation_DAO.create(occupation_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteOccupation(String shiftId, String position, String WorkerID) {
        try {
            Occupation_DAO.executeRaw("DELETE FROM Occupation WHERE position='" + position + "' AND workerID='" + WorkerID + "' AND ShiftID='" + shiftId + "'");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Shift Available Workers
    public void addShiftAvailableWorkers(String workerID, Date date, boolean timeOfDay) {
        int part_of_day=0;
        if (timeOfDay) {
            part_of_day = 1;
        }
        try {
            Worker_DTO worker_dto = worker_DAO.queryForId(workerID);
            Shift_availableWorkers_DTO shift_availableWorkers_dto = new Shift_availableWorkers_DTO(worker_dto, date, part_of_day);
            Shift_availableWorkers_DAO.create(shift_availableWorkers_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // delete specific row
    public void deleteShiftAvailableWorkers(String WorkerId, Date date, boolean timeOfDay) {
        int part_of_day=0;
        if (timeOfDay ) {
            part_of_day = 1;
        }
        try {
            Shift_availableWorkers_DAO.executeRaw("DELETE FROM Shift_availableWorkers WHERE workerID='" + WorkerId + "' AND ShiftDate='" + formatter.format(date) + "' And partOfDay=" + part_of_day);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // delete all rows with specified ID
    public void deleteShiftAvailableWorkers(String workerId) {
        try {
            Shift_availableWorkers_DAO.executeRaw("DELETE FROM Shift_availableWorkers WHERE workerID='" + workerId + "'");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Shift Drivers
    public void addShiftDriver(String driverID, String shiftID) {
        try {
            Shift_DTO shift_dto = Shift_DAO.queryForId(shiftID);
            Worker_DTO driver = worker_DAO.queryForId(driverID);
            ShiftDriver_DTO shiftDriver_dto = new ShiftDriver_DTO(driver, shift_dto);
            Shift_Driver_DAO.create(shiftDriver_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteShiftDriver(String driverID, String shiftID) {
        try {
            Shift_Driver_DAO.executeRaw("DELETE FROM Shift_Driver WHERE driverID='" + driverID + "' AND ShiftID='" + shiftID + "'");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    //Transport
    public void updateTransportDriver(int transport, String newDriverID, String newDriverName) {
        try {
            Transport_DTO transport_dto = transport_DAO.queryForId(transport);
            Worker_DTO driver = worker_DAO.queryForId(newDriverID);
            transport_dto.setDriverName(newDriverName);
            transport_dto.setDriverId(driver);
            transport_DAO.update(transport_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTransport(Transport transport) {
        try {
            //creating the transport in the DB
            int part_of_day=0;
            if (transport.getShift()) {
                part_of_day = 1;
            }
            Worker_DTO driver_in_transport = worker_DAO.queryForId(transport.getDriverId());
            Site_DTO source = site_DAO.queryForId(transport.getSource().getId());
            Truck_DTO truck = truck_DAO.queryForId(transport.getTruck().getId());
            Transport_DTO transport_dto = new Transport_DTO(transport.getID(), transport.getDate(),
                    part_of_day, truck, driver_in_transport, transport.getDriverName(), source, transport.getTotalWeight());
            transport_DAO.create(transport_dto);

            //creating the transport destFile in the DB
            HashMap<Site, ProductFile> destFiles = transport.getDestFiles();
            for (Site site : destFiles.keySet()) {
                addDestFile(site.getId(), destFiles.get(site).getFileID(), transport.getID());
            }

            //creating the transport log
            ArrayList<String> log = transport.getLog();
            for (String message : log) {
                add_to_log(transport.getID(), message);
            }

            //update the Transport ->  set the foreign field of product
            transport_DAO.update(transport_dto);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean deleteTransport(int transportID) {
        try {
            Transport_DTO transport_dto = transport_DAO.queryForId(transportID);
            if(transport_dto!=null) {
                ForeignCollection<DestFile_DTO> destFile_dtos = transport_dto.getDestFiles();
                for (DestFile_DTO df : destFile_dtos) {
                    deleteDestFile(transportID, df.getSiteID().getId(), df.getProductFileID().getFileID());
                }
                delete_from_log(transportID);
                transport_DAO.delete(transport_dto);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //Site
    public void addSite(Site site) {
        Site_DTO site_dto = new Site_DTO(site.getId(), site.getAddress(), site.getPhone_number(), site.getContact(), site.getShipping_area());
        try {
            site_DAO.create(site_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public boolean deleteSite(int siteID) {
        try {
            Site_DTO site_dto = site_DAO.queryForId(siteID);
            if(site_dto!=null) {
                site_DAO.delete(site_dto);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //Truck
    public void addTruck(Truck truck) {
        Truck_DTO truck_dto = new Truck_DTO(truck.getId(), truck.getLicense_plate(), truck.getModel(),
                truck.getNet_weight(), truck.getMax_weight(), truck.getDrivers_license());
        try {
            truck_DAO.create(truck_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public boolean deleteTruck(int truckID) {
        try {
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

    //products & productFile
    private void addProduct(Product product, ProductFile_DTO fileId, int quantity) {
        Product_DTO product_dto = new Product_DTO(product.getID(), fileId, product.getName(), product.getWeight(), quantity);
        try {
            product_DAO.create(product_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addProductFile(ProductFile productFile) {
        try {
            //creating the productFile in the DB
            ProductFile_DTO productFile_dto = new ProductFile_DTO(productFile.getFileID(), productFile.getTotalWeight());
            productFile_DAO.create(productFile_dto);

            //creating the productFile products in the DB
            HashMap<Product, Integer> product_quantity = productFile.getProducts();
            for (Product p : product_quantity.keySet()) {
                addProduct(p, productFile_dto, product_quantity.get(p));
            }

            //update the productFile ->  set the foreign field of product
            productFile_DAO.update(productFile_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteProduct(ForeignCollection<Product_DTO> product_dtos) {
        try {
            product_DAO.delete(product_dtos);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteProductFile(int fileID) {
        try {
            ProductFile_DTO productFile_dto = productFile_DAO.queryForId(fileID);
            if(productFile_dto!=null) {
                //delete all the products in the product file
                deleteProduct(productFile_dto.getProducts());
                //delete the productFile
                productFile_DAO.delete(productFile_dto);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    //DestFile
    public void addDestFile(int site_id, int productFile_id, int transportID) {
        try {
            Site_DTO site_dto = site_DAO.queryForId(site_id);
            ProductFile_DTO productFile_dto = productFile_DAO.queryForId(productFile_id);
            Transport_DTO transport_dto = transport_DAO.queryForId(transportID);
            DestFile_DTO destFile_dto = new DestFile_DTO(transport_dto, productFile_dto, site_dto);
            DestFile_DAO.create(destFile_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteDestFile(int transportID, int siteID, int productFileID) {
        try {
            deleteProductFile(productFileID);
            DestFile_DAO.executeRaw("DELETE FROM DestFile WHERE transportID=" + transportID + " AND siteID=" + siteID + " AND productFileID=" + productFileID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Log
    public void add_to_log(int transportID, String message) {
        try {
            Transport_DTO transport_dto = transport_DAO.queryForId(transportID);
            log_DTO log_dto = new log_DTO(message, transport_dto);
            log_DAO.create(log_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void delete_from_log(int transportID) {
        try {
            log_DAO.executeRaw("DELETE FROM Log WHERE transportID=" + transportID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    //-----------------------dummy methods------------------------------------------------//
    public List<Worker> getAvailableWorkers(Date date, boolean partOfDay) {
        //return a list with available workers, if there are no matches or there is an error null
        try {
            HashMap<String, Object> args = new HashMap<>();
            args.put("shiftDate", date);
            args.put("partOfDay", partOfDay);
            List<Shift_availableWorkers_DTO> availableWorkers_dtos = Shift_availableWorkers_DAO.queryForFieldValues(args);
            List<Worker> retList = new ArrayList<>();
            for (Shift_availableWorkers_DTO available : availableWorkers_dtos) {
                List<Driver_DTO> driver_dtos = Driver_DAO.queryForEq("driverID", available.getWorkerID());
                if(driver_dtos.size()==0) {
                    retList.add(makeWORKER(available.getWorkerID()));
                }
                else{
                    retList.add(makeDRIVER(available.getWorkerID()));
                }
            }
            if(retList.size()==0)
                return null;
            return retList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void updateWorker(Worker worker) {
        //update only the "simple" fields
        try{
            Worker_DTO worker_dto = worker_DAO.queryForId(worker.getId());
            if(worker_dto==null) {
                System.out.println("there is no such worker in the system");
                return;
            }
            worker_dto.setName(worker.getName());
            worker_dto.setSalary(worker.getSalary());
            worker_dto.setStart_Date(worker.getStart_Date());
            worker_DAO.update(worker_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public Worker getWorker(String id) {
        //return a worker, if there are no matches or there is an error- return null
        try {
            Worker_DTO worker_dto = worker_DAO.queryForId(id);
            if (worker_dto == null)
                return null;
            return  makeWORKER(worker_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;

        }
    }

    private Worker makeWORKER(Worker_DTO worker_dto) {
        Worker worker = new Worker(worker_dto.getName(), worker_dto.getWorkerID(), worker_dto.getStart_Date(), worker_dto.getSalary());
        ForeignCollection<Position_DTO> position_dtos = worker_dto.getPositions();
        for (Position_DTO p : position_dtos) {
            worker.addPosition(p.getPosition());
        }
        return worker;
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
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public Shift getShift(Date date, boolean partOfDay) {
        //return a shift, if there are no matches or there is an error- return null
        try {
            HashMap<String, Object> args = new HashMap<>();
            args.put("date", date);
            args.put("partOfDay", partOfDay);
            List<Shift_DTO> shift_dtos = Shift_DAO.queryForFieldValues(args);
            if (shift_dtos.size() == 1) {
               return makeSHIFT(shift_dtos.get(0));
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Shift makeSHIFT(Shift_DTO shift_dto) {
        //make a Shift from table
        boolean partOfDay = false;
        if (shift_dto.getTimeOfDay() == 1) {
            partOfDay = true;
        }
        Shift shift = new Shift(shift_dto.getDate(), partOfDay, shift_dto.getShiftID());
        ForeignCollection<Occupation_DTO> occupation_dtos = shift_dto.getOccupation();
        HashMap<String, ArrayList<Worker>> occupation = new HashMap<>();
        for (Occupation_DTO o : occupation_dtos) {
            if(occupation.containsKey(o.getPosition())){
                occupation.get(o.getPosition()).add(makeWORKER(o.getWorkerID()));
            }
            else
            {
                occupation.put(o.getPosition(), new ArrayList<>());
                occupation.get(o.getPosition()).add(makeWORKER(o.getWorkerID()));
            }
        }
        for (String position: occupation.keySet()) {
            FixedSizeList<Worker> list = new FixedSizeList<>(occupation.get(position).size());
            list.addAll(occupation.get(position));
            shift.addARowToOcuupation(position,list);
        }

        ForeignCollection<ShiftDriver_DTO> drivers_in_shift = shift_dto.getDrivers_in_shift();
        for (ShiftDriver_DTO sd : drivers_in_shift) {
            shift.addDriverToShift(makeDRIVER(sd.getDriverID()));
        }
        return shift;
    }

    private Driver makeDRIVER(Worker_DTO worker) {
        //make a Driver from table, if there is an error return null
        try {
            String License;
            List<Driver_DTO> driver_dtos = Driver_DAO.queryForEq("driverID", worker.getWorkerID());
            if (driver_dtos.size() == 1) {
                Driver_DTO driver_dto = driver_dtos.get(0);
                Driver driver = new Driver(worker.getWorkerID(), driver_dto.getLicense(), worker.getName(), worker.getStart_Date(), worker.getSalary());
                ForeignCollection<Position_DTO> position_dtos = worker.getPositions();
                for (Position_DTO p : position_dtos) {
                    driver.addPosition(p.getPosition());
                }
                return driver;
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
                    workers.add(makeWORKER(w));
                }
                else{
                    workers.add(makeDRIVER(w));
                }
            }
            if(workers.isEmpty())
            {
                return null;
            }
            return workers;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ProductFile getProductFile(int id){
        //return a ProductFile, if there are no matches or there is an error- return null
        try {
            ProductFile_DTO productFile_dto = productFile_DAO.queryForId(id);
            if (productFile_dto == null)
                return null;
            return  makePRODUCT_FILE(productFile_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;

        }
    }

    private ProductFile makePRODUCT_FILE(ProductFile_DTO productFile_dto) {
        ProductFile productFile = new ProductFile(productFile_dto.getFileID());
        productFile.setTotalWeight(productFile_dto.getTotalWeight());
        ForeignCollection<Product_DTO> product_dtos = productFile_dto.getProducts();
        HashMap<Product,Integer> products = new HashMap<>();
        for (Product_DTO p : product_dtos) {
            products.put(makePRODUCT(p),p.getQuantity());
        }
        productFile.setProducts(products);
        return productFile;
    }

    private Product makePRODUCT(Product_DTO product_dto) {
        return  new Product(product_dto.getID(),product_dto.getName(),product_dto.getWeight());
    }

    public long MaxIdProducts()
    {
        try {
            long max = product_DAO.queryRawValue("SELECT MAX(productID) FROM Product");
            return max;
        }catch (Exception e)
        {
            return 0;
        }
    }

    public long MaxIdProductsFile()
    {
        try {
            long max = product_DAO.queryRawValue("SELECT MAX(fileID) FROM ProductFile");
            return max;
        }catch (Exception e)
        {
            return 0;
        }
    }

    public long MaxIdSite()
    {
        try {
            long max = product_DAO.queryRawValue("SELECT MAX(siteID) FROM Site");
            return max;
        }catch (Exception e)
        {
            return 0;
        }
    }

    public long MaxIdTrucks()
    {
        try {
            long max = product_DAO.queryRawValue("SELECT MAX(truckID) FROM Truck");
            return max;
        }catch (Exception e)
        {
            return 0;
        }
    }

    public Truck getTruck(int id)
    {
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
        Truck truck = new Truck(truck_dto.getId(),truck_dto.getLicense_plate(),truck_dto.getModel(),truck_dto.getNet_weight(),
                                truck_dto.getMax_weight(),truck_dto.getDrivers_license());
        ForeignCollection<morning_shifts_DTO> morning_shifts_dtos = truck_dto.getMorning_shifts();
        for (morning_shifts_DTO morningShiftsDto : morning_shifts_dtos) {
            truck.addDate(morningShiftsDto.getDate(),true);
        }
        ForeignCollection<night_shifts_DTO> night_shifts_dtos = truck_dto.getNight_shifts();
        for (night_shifts_DTO night_shifts_dto : night_shifts_dtos) {
            truck.addDate(night_shifts_dto.getDate(),false);
        }
        return truck;
    }

    public List<Truck> getAllTrucks()
    {
        //return list of all trucks in the system if there are no matches - empty list if there is an error- return null
        try {
            List<Truck_DTO> truck_dtos = truck_DAO.queryForAll();
            List<Truck> trucks = new ArrayList<>();
            for (Truck_DTO t:truck_dtos ) {
               trucks.add(makeTRUCK(t));
            }
            return trucks;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean checkIfTrucksAvailableByDate(Date date, boolean partOfDay)
    {
        try{
            long numOfTrucks = truck_DAO.queryRawValue("SELECT count(truckID) FROM Truck");
            long numOfOccupaied;
            if(partOfDay)//morning
            {
                numOfOccupaied = morning_shifts_DAO.queryRawValue("SELECT count(truckID) FROM morningShifts WHERE date='"+formatter.format(date)+"'");
            }
            else //night
            {
                numOfOccupaied = morning_shifts_DAO.queryRawValue("SELECT count(truckID) FROM nightShifts WHERE date='"+formatter.format(date)+"'");
            }
            return numOfTrucks>numOfOccupaied;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<Truck> getAvailableTrucks(Date date, boolean partOfDay, float Weight)
    {
        try{
            List<Truck_DTO> truck_dtos = truck_DAO.queryForAll();
            List<Truck> trucks = new ArrayList<>();
            for (Truck_DTO truck_dto:truck_dtos) {
                if(truck_dto.getNet_weight()+ Weight < truck_dto.getMax_weight())
                {
                    if(partOfDay && TruckNotInMorningShift(date,truck_dto.getId()))
                    {
                        trucks.add(makeTRUCK(truck_dto));
                    }
                    else if(!partOfDay && TruckNotInNightShift(date,truck_dto.getId()))
                    {
                        trucks.add(makeTRUCK(truck_dto));
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

    public Site getSite(int siteID) {
        //return a site, if there are no matches or there is an error- return null
        try {
            Site_DTO site_dto = site_DAO.queryForId(siteID);
            if (site_dto == null)
                return null;
            return  makeSITE(site_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Site makeSITE  (Site_DTO site_dto) {
        return new Site(site_dto.getId(),site_dto.getAddress(),site_dto.getPhone_number(),site_dto.getContact(),site_dto.getShipping_area());
    }

    public List<Site> getAllSites() {
        //return list of all sites in the system if there are no matches -empty list if there is an error- return null
        try {
            List<Site_DTO> site_dtos = site_DAO.queryForAll();
            List<Site> sites = new ArrayList<>();
            for (Site_DTO site_dto:site_dtos) {
                sites.add(makeSITE(site_dto));
            }
            return sites;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Site> getAvailableSites(int otherSite_id) {
        try {
            Site_DTO Other_site_dto = site_DAO.queryForId(otherSite_id);
            List<Site_DTO> site_dtos = site_DAO.queryForEq("shippingArea",Other_site_dto.getShipping_area());
            List<Site> sites = new ArrayList<>();
            for (Site_DTO site_dto:site_dtos) {
                if(site_dto.getId()!=otherSite_id)
                    sites.add(makeSITE(site_dto));
            }
            return sites;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public long MaxIDTransport() {
        try {
            long max = product_DAO.queryRawValue("SELECT MAX(transportID) FROM Transport");
            return max;
        }catch (Exception e)
        {
            return 0;
        }
    }

    public List<Transport> getAllTransports() {
        //return list of all transports in the system if there are no matches -empty list if there is an error- return null
        try {
            List<Transport_DTO> transport_dtos = transport_DAO.queryForAll();
            List<Transport> Transports = new ArrayList<>();
            for (Transport_DTO transport_dto:transport_dtos) {
                Transports.add(makeTRANSPORT(transport_dto));
            }
            return Transports;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Transport makeTRANSPORT(Transport_DTO transport_dto) {
        boolean partOfDay= false;
        if(transport_dto.getShift()==1)
        {
            partOfDay=true;
        }
        Truck t = makeTRUCK(transport_dto.getTruck());
        Site s = makeSITE(transport_dto.getSource());
        Transport transport = new Transport(transport_dto.getTransportID(),transport_dto.getDate(),partOfDay,t,
                                            transport_dto.getDriverId().getWorkerID(),transport_dto.getDriverName(),
                                            s,transport_dto.getTotalWeight());
        ForeignCollection<DestFile_DTO> destFile_dtos = transport_dto.getDestFiles();
        HashMap<Site, ProductFile> destFile = new HashMap<>();
        for (DestFile_DTO destFile_dto : destFile_dtos) {
            destFile.put(makeSITE(destFile_dto.getSiteID()),makePRODUCT_FILE(destFile_dto.getProductFileID()));
        }
        transport.setDestFiles(destFile);
        ForeignCollection<log_DTO> log_dtos = transport_dto.getLog();
        for (log_DTO log_dto : log_dtos) {
            transport.addToLog(log_dto.getMessage());
        }
        return transport;
    }

    public Transport getTransport(int transportID) {
        //return a transport, if there are no matches or there is an error- return null
        try {
            Transport_DTO transport_dto = transport_DAO.queryForId(transportID);
            if (transport_dto == null)
                return null;
            return  makeTRANSPORT(transport_dto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Transport getTransportToUpdate(String prevDriverId, Date date, Boolean partOfDay) {
        try {
            List<Transport_DTO> transport_dtos = transport_DAO.queryForEq("Date",date);
            int shift=0;
            if(partOfDay)
                shift=1;
            for (Transport_DTO transport_dto:transport_dtos) {
                if(transport_dto.getDriverId().equals(prevDriverId) && transport_dto.getShift()==shift)
                    return makeTRANSPORT(transport_dto);
            }
            return null;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Boolean getTransportByShift(Date d, Boolean partOfDay) {
        try {
            List<Transport_DTO> transport_dtos = transport_DAO.queryForEq("Date",d);
            int shift=0;
            if(partOfDay)
                shift=1;
            for (Transport_DTO transport_dto:transport_dtos) {
                if(transport_dto.getShift()==shift)
                    return true;
            }
            return false;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
}



