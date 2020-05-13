package DataAccessLayer;

import BusinessLayer.Transport.*;
import BusinessLayer.Workers.*;
import DataAccessLayer.DTO.*;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import java.util.Date;


//singleton
public class Mapper {
    private static Mapper instance = null;
    private String databaseUrl = "jdbc:sqlite:SuperLi.db";
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
    private Dao<Worker_DTO, Integer> worker_DAO;



    private Mapper() throws Exception{
        try {
            Connecting_to_DB();
        }
        catch (Exception e)
        {
            throw new Exception("error accrued when trying to connect to DB: "+e.getMessage());
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
                return null;
            }
        }
        return instance;
    }

    private void Connecting_to_DB()throws Exception{
        conn = new JdbcConnectionSource(databaseUrl);

        DestFile_DAO = DaoManager.createDao(conn, DestFile_DTO.class);
        Driver_DAO = DaoManager.createDao(conn, Driver_DTO.class);
        log_DAO = DaoManager.createDao(conn, log_DTO.class);
        morning_shifts_DAO = DaoManager.createDao(conn, morning_shifts_DTO.class);
        night_shifts_DAO = DaoManager.createDao(conn, night_shifts_DTO.class);
        Occupation_DAO = DaoManager.createDao(conn, Occupation_DTO.class);
        position_DAO = DaoManager.createDao(conn, Position_DTO.class);
        product_DAO = DaoManager.createDao(conn, Product_DTO.class);
        productFile_DAO  = DaoManager.createDao(conn, ProductFile_DTO.class);
        Shift_availableWorkers_DAO = DaoManager.createDao(conn, Shift_availableWorkers_DTO.class);
        Shift_DAO  = DaoManager.createDao(conn, Shift_DTO.class);
        Shift_Driver_DAO  = DaoManager.createDao(conn, ShiftDriver_DTO.class);
        site_DAO = DaoManager.createDao(conn, Site_DTO.class);
        transport_DAO = DaoManager.createDao(conn, Transport_DTO.class);
        truck_DAO = DaoManager.createDao(conn, Truck_DTO.class);
        worker_DAO = DaoManager.createDao(conn, Worker_DTO.class);
    }

    public void CloseConnetion() throws Exception{
        try {
            conn.close();
        }catch (Exception e)
        {
            throw new Exception("error accrued when trying to close connection: "+e.getMessage());
        }
    }

    //TODO
    public void addDestFile(int site_id, int productFile_id)
    {
//        Site_DTO site_dto = site_DAO.queryForId(site_id);
//        ProductFile_DTO productFile_dto= productFile_DAO.queryForId(productFile_id);
    }

    //TODO
    public void add_to_log(int transport_ID, String string)
    {

    }

    //TODO
    public void addMorningShift(int truckID,Date date)
    {

    }

    //TODO
    public void addNightShift(int truckID,Date date)
    {

    }

    //TODO
    public void addOccupation(Shift shift, String position, int WorkerID )
    {

    }

    //TODO
    public void addPosition()
    {

    }
    //TODO
    public void addProduct()
    {

    }
    //TODO
    public void addProductFile()
    {

    }
    //TODO
    public void addShiftAvailableWorkers()
    {

    }
    //TODO
    public void addShift(Shift shift, String position, int WorkerID )
    {

    }
    //TODO
    public void addShiftDriver(Shift shift, String position, int WorkerID )
    {

    }

    //TODO
    public void addSite(Shift shift, String position, int WorkerID )
    {

    }
    //TODO
    public void addTransport(Shift shift, String position, int WorkerID )
    {

    }
    //TODO
    public void addTruck(Shift shift, String position, int WorkerID )
    {

    }
    //TODO
    public void addWorker(Shift shift, String position, int WorkerID )
    {

    }


    //TODO
    public void deleteDestFile()
    {
    }

    //TODO
    public void delete_from_log()
    {

    }

    //TODO
    public void delete_MorningShift()
    {

    }

    //TODO
    public void deleteNightShift()
    {

    }

    //TODO
    public void deleteOccupation()
    {

    }

    //TODO
    public void deletePosition()
    {

    }
    //TODO
    public void deleteProduct()
    {

    }
    //TODO
    public void deleteProductFile()
    {

    }
    //TODO
    public void deleteShiftAvailableWorkers()
    {

    }
    //TODO
    public void deleteShift()
    {

    }
    //TODO
    public void deleteShiftDriver()
    {

    }

    //TODO
    public void deleteSite( )
    {

    }
    //TODO
    public void deleteTransport()
    {

    }
    //TODO
    public void deleteTruck()
    {

    }
    //TODO
    public void deleteWorker()
    {

    }
}
