package DataAccessLayer;

import BusinessLayer.Transport.*;
import BusinessLayer.Workers.*;
import DataAccessLayer.DTO.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import javafx.concurrent.WorkerStateEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


//singleton
public class Mapper {
    private static Mapper instance = null;
    private String databaseUrl = "jdbc:sqlite:dev\\src\\main\\java\\DataAccessLayer\\SuperLi.db";
    private ConnectionSource conn;
    private Dao<DestFile_DTO,Integer> DestFile_DAO;
    private Dao<Driver_DTO, Integer> Driver_DAO;
    private Dao<log_DTO,Integer> log_DAO;
    private Dao<morning_shifts_DTO, Integer> morning_shifts_DAO;
    private Dao<night_shifts_DTO, Integer> night_shifts_DAO;
    private Dao<Occupation_DTO,Integer> Occupation_DAO;
    private Dao<Position_DTO,Integer> position_DAO;
    private Dao<Product_DTO, Integer> product_DAO;
    private Dao<ProductFile_DTO,Integer> productFile_DAO;
    private Dao<Shift_availableWorkers_DTO,Integer> Shift_availableWorkers_DAO;
    private Dao<Shift_DTO,Integer> Shift_DAO;
    private Dao<ShiftDriver_DTO,Integer> Shift_Driver_DAO;
    private Dao<Site_DTO,Integer> site_DAO;
    private Dao<Transport_DTO,Integer> transport_DAO;
    private Dao<Truck_DTO,Integer> truck_DAO;
    private Dao<Worker_DTO, String> worker_DAO;



    private Mapper() throws Exception{
        try {
            conn = new JdbcConnectionSource(databaseUrl);
            Connecting_to_DB();
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    //if there is a problem with creating the connection return null
    public static Mapper getInstance()
    {
        if(instance == null){
            try {
                instance = new Mapper();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return instance;
    }

    private void Connecting_to_DB()throws Exception{
        site_DAO = DaoManager.createDao(conn, Site_DTO.class);
        productFile_DAO  = DaoManager.createDao(conn, ProductFile_DTO.class);
        Driver_DAO = DaoManager.createDao(conn, Driver_DTO.class);
        position_DAO = DaoManager.createDao(conn, Position_DTO.class);
        worker_DAO = DaoManager.createDao(conn, Worker_DTO.class);
        morning_shifts_DAO = DaoManager.createDao(conn, morning_shifts_DTO.class);
        night_shifts_DAO = DaoManager.createDao(conn, night_shifts_DTO.class);
        truck_DAO = DaoManager.createDao(conn, Truck_DTO.class);
        product_DAO = DaoManager.createDao(conn, Product_DTO.class);
        Shift_DAO  = DaoManager.createDao(conn, Shift_DTO.class);
        Occupation_DAO = DaoManager.createDao(conn, Occupation_DTO.class);
        Shift_Driver_DAO  = DaoManager.createDao(conn, ShiftDriver_DTO.class);
        Shift_availableWorkers_DAO = DaoManager.createDao(conn, Shift_availableWorkers_DTO.class);
        log_DAO = DaoManager.createDao(conn, log_DTO.class);
        DestFile_DAO = DaoManager.createDao(conn, DestFile_DTO.class);
        transport_DAO = DaoManager.createDao(conn, Transport_DTO.class);
    }

    public void CloseConnection() throws Exception{
        try {
            conn.close();
        }catch (Exception e)
        {
            throw new Exception("error accrued when trying to close connection: "+e.getMessage());
        }
    }

    //TODO:: add destFile => need to run once
    public void addDestFile(int site_id, int productFile_id ,int transportID)
    {
        try {
            Site_DTO site_dto = site_DAO.queryForId(site_id);
            ProductFile_DTO productFile_dto = productFile_DAO.queryForId(productFile_id);
            Transport_DTO transport_dto = transport_DAO.queryForId(transportID);
            DestFile_DTO destFile_dto = new DestFile_DTO(transport_dto,productFile_dto,site_dto);
            DestFile_DAO.create(destFile_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //TODO::add_to_log=>need to run once
    public void add_to_log(int transportID, String message)
    {
        try {
            Transport_DTO transport_dto = transport_DAO.queryForId(transportID);
            log_DTO log_dto = new log_DTO(message, transport_dto);
            log_DAO.create(log_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //TODO::addMorningShift=>need to run once
    public void addMorningShift(int truckID,Date date)
    {
        try {
            Truck_DTO truck_dto = truck_DAO.queryForId(truckID);
            morning_shifts_DTO morningShiftsDto = new morning_shifts_DTO( date, truck_dto);
            morning_shifts_DAO.create(morningShiftsDto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //TODO::addNightShift=>need to run once
    public void addNightShift(int truckID,Date date)
    {
        try {
            Truck_DTO truck_dto = truck_DAO.queryForId(truckID);
            night_shifts_DTO night_shifts_dto = new night_shifts_DTO( date, truck_dto);
            night_shifts_DAO.create(night_shifts_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //TODO::addOccupation
    public void addOccupation(Shift shift, String position, int WorkerID )
    {

    }

    //TODO::addPosition =>need to run once
    private void addPosition(String position, Worker_DTO worker_dto)
    {
        try {
            Position_DTO position_dto = new Position_DTO(worker_dto, position);
            position_DAO.create(position_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //TODO::addShiftAvailableWorkers
    public void addShiftAvailableWorkers(String workerID, int shiftID)
    {
        try {
            Shift_DTO shift_dto = Shift_DAO.queryForId(shiftID);
            Worker_DTO driver = worker_DAO.queryForId(workerID);
            Shift_availableWorkers_DTO shift_availableWorkers_dto =new Shift_availableWorkers_DTO(driver,shift_dto);
            Shift_availableWorkers_DAO.create(shift_availableWorkers_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //TODO::addShift=>need to run once
    /*public void addShift(Shift shift)
    {
        try {
            //creating the shifts in the DB
            int part_of_day;
            if(shift.getTimeOfDay()==true)
            {
                part_of_day=1;
            }
            else {
                part_of_day = 0;
            }
            Shift_DTO shift_dto = new Shift_DTO(shift.getId(),shift.getDate(), part_of_day);
            Shift_DAO.create(shift_dto);

            //creating the shift available workers destFile in the DB
            List<Worker> availableWorkers =shift.getAvailableWorkers();
            for (Worker w : availableWorkers) {
                addShiftAvailableWorkers(w.getId(), shift.getId());
            }

            //creating the shift available workers destFile in the DB
            List<Driver> scheduledDrivers =shift.getScheduledDrivers();
            for (Driver d : scheduledDrivers) {
                addShiftDriver(d.getId(), shift.getId());
            }

            //update the Shift ->  set the foreign field of product
            Shift_DAO.update(shift_dto);

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //TODO::addShiftDriver=>need to run once
        public void addShiftDriver(String driverID, int shiftID)
    {
        try {
            Shift_DTO shift_dto = Shift_DAO.queryForId(shiftID);
            Worker_DTO driver = worker_DAO.queryForId(driverID);
            ShiftDriver_DTO shiftDriver_dto =new ShiftDriver_DTO(driver,shift_dto);
            Shift_Driver_DAO.create(shiftDriver_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
*/
    //TODO::addTransport=>need to run once
    public void addTransport(Transport transport)
    {
        try {
            //creating the transport in the DB
            int part_of_day;
            if(transport.getShift()==true)
            {
                part_of_day=1;
            }
            else {
                part_of_day = 0;
            }
            Worker_DTO driver_in_transport=worker_DAO.queryForId(transport.getDriverId());
            Site_DTO source = site_DAO.queryForId(transport.getSource().getId());
            Transport_DTO transport_dto = new Transport_DTO(transport.getID(),transport.getDate(),transport.getTime(),
                                                part_of_day,driver_in_transport,transport.getDriverName(),source,transport.getTotalWeight());
            transport_DAO.create(transport_dto);

            //creating the transport destFile in the DB
            HashMap<Site,ProductFile> destFiles =transport.getDestFiles();
            for (Site site : destFiles.keySet()) {
                addDestFile(site.getId(),destFiles.get(site).getFileID(),transport.getID());
            }

            //creating the transport log
            ArrayList<String> log =transport.getLog();
            for (String message : log) {
                add_to_log(transport.getID(), message);
            }

            //update the Transport ->  set the foreign field of product
            transport_DAO.update(transport_dto);

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }



    //TODO::deleteDestFile
    public void deleteDestFile()
    {
    }

    //TODO::delete_from_log
    public void delete_from_log()
    {

    }

    //TODO::delete_MorningShift
    public void delete_MorningShift()
    {

    }

    //TODO::deleteNightShift
    public void deleteNightShift()
    {

    }

    //TODO::deleteOccupation
    public void deleteOccupation()
    {

    }

    //TODO::deletePosition
    public void deletePosition()
    {

    }

    //TODO::deleteShiftAvailableWorkers
    public void deleteShiftAvailableWorkers()
    {

    }
    //TODO::deleteShift
    public void deleteShift()
    {

    }
    //TODO::deleteShiftDriver
    public void deleteShiftDriver()
    {

    }


    //TODO::deleteTransport
    public void deleteTransport()
    {

    }



    //Worker
    public void addWorker(Worker worker)
    {
        try {
            //creating the worker in the DB
            Worker_DTO worker_dto = new Worker_DTO(worker.getId(),worker.getName(),worker.getStart_Date(),worker.getSalary());
            worker_DAO.create(worker_dto);

            //creating the worker positions products in the DB
            List<String> positions =worker.getPositions();
            for (String position : positions) {
                addPosition(position,worker_dto);
            }

            //update the worker ->  set the foreign field of product
            worker_DAO.update(worker_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //TODO::deleteWorker
    public void deleteWorker(String workerId)
    {
        try {
            Worker_DTO worker_dto = worker_DAO.queryForId(workerId);
            worker_DAO.delete(worker_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }


    //Driver
    public void addDriver(Driver driver)
    {
        try {
            //creating the worker in the DB
            Worker_DTO worker_dto = new Worker_DTO(driver.getId(),driver.getName(),driver.getStart_Date(),driver.getSalary());
            worker_DAO.create(worker_dto);
            //adding License to Driver table

            //creating the worker positions products in the DB
            List<String> positions =driver.getPositions();
            for (String position : positions) {
                addPosition(position,worker_dto);
            }

            //update the worker ->  set the foreign field of product
            worker_DAO.update(worker_dto);

            Driver_DTO driver_dto = new Driver_DTO(worker_dto, driver.getLicense());
            Driver_DAO.create(driver_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //TODO::deleteWorker
    public void deleteDriver(String driverID)
    {
        try {
            Worker_DTO worker_dto = worker_DAO.queryForId(driverID);
            worker_DAO.delete(worker_dto);
            Driver_DAO.executeRaw("DELETE FROM Driver WHERE driverID = '"+driverID+"'");
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //Site
    public void addSite(Site site)
    {
        Site_DTO site_dto = new Site_DTO(site.getId(),site.getAddress(),site.getPhone_number(),site.getContact(),site.getShipping_area());
        try {
            site_DAO.create(site_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    public void deleteSite(int siteID)
    {
        try {
            Site_DTO site_dto = site_DAO.queryForId(siteID);
            site_DAO.delete(site_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //Truck
    public void addTruck(Truck truck)
    {
        Truck_DTO truck_dto = new Truck_DTO(truck.getId(),truck.getLicense_plate(),truck.getModel(),
                truck.getNet_weight(),truck.getMax_weight(),truck.getDrivers_license());
        try {
            truck_DAO.create(truck_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    public void deleteTruck(int truckID)
    {
        try {
            Truck_DTO truck_dto = truck_DAO.queryForId(truckID);
            truck_DAO.delete(truck_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    //products & productFile
    private void addProduct(Product product, ProductFile_DTO fileId, int quantity)
    {
        Product_DTO product_dto = new Product_DTO(product.getID(),fileId,product.getName(),  product.getWeight(), quantity);
        try {
            product_DAO.create(product_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void addProductFile(ProductFile productFile)
    {
        try {
            //creating the productFile in the DB
            ProductFile_DTO productFile_dto = new ProductFile_DTO(productFile.getFileID(),productFile.getTotalWeight());
            productFile_DAO.create(productFile_dto);

            //creating the productFile products in the DB
            HashMap<Product, Integer> product_quantity =productFile.getProducts();
            for (Product p:product_quantity.keySet()) {
                addProduct(p,productFile_dto,product_quantity.get(p));
            }

            //update the productFile ->  set the foreign field of product
            productFile_DAO.update(productFile_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void deleteProduct( ForeignCollection<Product_DTO> product_dtos)
    {
        try {
            product_DAO.delete(product_dtos);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void deleteProductFile(int fileID)
    {
        try {
            ProductFile_DTO productFile_dto = productFile_DAO.queryForId(fileID);
            //delete all the products in the product file
            deleteProduct(productFile_dto.getProducts());
            //delete the productFile
            productFile_DAO.delete(productFile_dto);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }
}
