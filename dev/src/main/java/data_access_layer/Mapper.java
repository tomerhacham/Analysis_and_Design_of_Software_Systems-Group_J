package data_access_layer;

import bussines_layer.Branch;
import bussines_layer.SupplierCard;
import bussines_layer.employees_module.Driver;
import bussines_layer.employees_module.Shift;
import bussines_layer.employees_module.Worker;
import bussines_layer.inventory_module.*;
import bussines_layer.supplier_module.Contract;
import bussines_layer.supplier_module.CostEngineering;
import bussines_layer.supplier_module.Order;
import bussines_layer.transport_module.Transport;
import bussines_layer.transport_module.Truck;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import data_access_layer.DAO.*;
import data_access_layer.DTO.*;

import java.sql.SQLException;
import java.util.*;

/**
 * Singleton class to communicate with the DB
 */
public class Mapper {
    //fields:
    public ConnectionSource conn;
    public CatalogProductDAO catalog_product_dao;
    public GeneralProductDAO general_product_dao;
    public SpecificProductDAO specific_product_dao;
    public CategoryDAO category_dao;
    public SaleDAO sale_dao;
    public OrderDAO order_dao;
    public ContractDAO contract_dao;
    public SupplierDAO supplier_dao;
    public BranchDAO branch_dao;
    public CostEngineeringDAO cost_engineering_dao;
    public EmployeesDAO employees_dao;
    public ShiftsDAO shifts_dao;
    public TransportDAO transport_dao;
    public TruckDAO truck_dao;
    public Dao<IDsDTO, Integer> ids_dao;

    private static Mapper instance = null;

    //Constructor
    private Mapper() {
        String databaseUrl = "jdbc:sqlite:dev/src/main/java/data_access_layer/SuperLi.db";
        //String databaseUrl = "jdbc:sqlite:SuperLi.db";
        try (ConnectionSource conn = new JdbcConnectionSource(databaseUrl)) {
            this.conn = conn;
            this.branch_dao = new BranchDAO(conn);
            this.catalog_product_dao = new CatalogProductDAO(conn);
            this.general_product_dao = new GeneralProductDAO(conn);
            this.specific_product_dao = new SpecificProductDAO(conn);
            this.category_dao = new CategoryDAO(conn);
            this.sale_dao = new SaleDAO(conn);
            this.order_dao = new OrderDAO(conn);
            this.contract_dao = new ContractDAO(conn);
            this.supplier_dao = new SupplierDAO(conn);
            this.cost_engineering_dao = new CostEngineeringDAO(conn);
            this.employees_dao = new EmployeesDAO(conn);
            this.shifts_dao = new ShiftsDAO(conn);
            this.transport_dao = new TransportDAO(conn);
            this.truck_dao = new TruckDAO(conn);
            this.ids_dao = DaoManager.createDao(conn, IDsDTO.class);
            IDsDTO ids = new IDsDTO(1, 1, 1, 1, 1, 1, 1);
            ids_dao.createIfNotExists(ids);

            //endregion
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Mapper getInstance() {
        if (instance == null) {
            instance = new Mapper();
        }
        return instance;
    }

    //region Methods
    public Integer loadID(String controller_name) {
        Integer id = null;
        try {
            IDsDTO iDs = ids_dao.queryForId(1);
            switch (controller_name) {
                case "category":
                    id = iDs.getCategory_next_id();
                    break;
                case "product":
                    id = iDs.getProduct_next_id();
                    break;
                case "sale":
                    id = iDs.getSale_next_id();
                    break;
                case "contract":
                    id = iDs.getContract_next_id();
                    break;
                case "order":
                    id = iDs.getOrder_next_id();
                    break;
                case "branch":
                    id = iDs.getBranch_next_id();
                    break;
                case "supplier":
                    id = iDs.getSupplier_next_id();
                    break;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    public void writeID(String controller_name, Integer id) {
        try {
            IDsDTO iDs = ids_dao.queryForId(1);
            switch (controller_name) {
                case "category":
                    iDs.setCategory_next_id(id);
                    break;
                case "product":
                    iDs.setProduct_next_id(id);
                    break;
                case "sale":
                    iDs.setSale_next_id(id);
                    break;
                case "contract":
                    iDs.setContract_next_id(id);
                    break;
                case "order":
                    iDs.setOrder_next_id(id);
                    break;
                case "branch":
                    iDs.setBranch_next_id(id);
                    break;
                case "supplier":
                    iDs.setSupplier_next_id(id);
                    break;
            }
            ids_dao.createOrUpdate(iDs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public HashMap<Integer, SupplierCard> loadSuppliers() {
        HashMap<Integer, SupplierCard> supplierCards = new HashMap<>();
        try {
            List<SupplierDTO> supplierDTOS = supplier_dao.dao.queryForAll();
            if (supplierDTOS != null && !supplierDTOS.isEmpty()) {
                for (SupplierDTO dto : supplierDTOS) {
                    SupplierCard supplierCard = find_Supplier(dto.getSupplier_id());
                    if (supplierCard != null) {
                        supplierCards.put(supplierCard.getId(), supplierCard);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return supplierCards;
    }

    public HashMap<Integer, String> loadBranches() {
        HashMap<Integer, String> branches = new HashMap<>();
        try {
            List<BranchDTO> list = branch_dao.dao.queryForAll();
            if (list != null && !list.isEmpty()) {
                for (BranchDTO dto : list) {
                    branches.put(dto.getBranch_id(), dto.getName());
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return branches;
    }

    public Branch loadBranch(Integer branch_id) {
        return branch_dao.find(branch_id);
    }

    public LinkedList<Category> loadCategories(Integer branch_id) {
        LinkedList<Category> main_categories = new LinkedList<>();
        try {
            List<CategoryDTO> dto_main_categories = category_dao.dao.queryBuilder().where().eq("branch_id", branch_id).and().eq("super_category_id", 0).query();
            if (dto_main_categories != null && !dto_main_categories.isEmpty()) {
                for (CategoryDTO main_cat_dto : dto_main_categories) {
                    Category category = find_Category(main_cat_dto.getId(), branch_id);
                    if (category != null) {
                        main_categories.add(category);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return main_categories;
    }

    public LinkedList<Sale> loadSales(Integer branch_id) {
        LinkedList<Sale> sales = new LinkedList<>();
        try {
            List<SaleDTO> saleDTOS = sale_dao.dao.queryBuilder().where().eq("branch_id", branch_id).query();
            if (saleDTOS != null && !saleDTOS.isEmpty()) {
                for (SaleDTO saleDTO : saleDTOS) {
                    Sale sale = find_Sale(saleDTO.getSale_id(), branch_id);
                    sales.add(sale);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sales;
    }

    public LinkedList<Order> loadOrders(Integer branch_id) {
        LinkedList<Order> orders = new LinkedList<>();
        try {
            List<OrderDTO> orderDTOS = order_dao.dao.queryBuilder().where().eq("branch_id", branch_id).query();
            if (orderDTOS != null && !orderDTOS.isEmpty()) {
                for (OrderDTO orderDTO : orderDTOS) {
                    Order order = find_Order(orderDTO.getOrder_id(), branch_id);
                    if (order != null) {
                        orders.add(order);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return orders;
    }

    public LinkedList<Contract> loadContracts(Integer branch_id) {
        LinkedList<Contract> contracts = new LinkedList<>();
        try {
            List<ContractDTO> contractDTOS = contract_dao.dao.queryBuilder().where().eq("branch_id", branch_id).query();
            if (contractDTOS != null && !contractDTOS.isEmpty()) {
                for (ContractDTO contractDTO : contractDTOS) {
                    Contract contract = find_Contract(contractDTO.getContract_id(), branch_id);
                    if (contract != null) {
                        contracts.add(contract);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contracts;
    }


    //TODO: Warning! the CategoryController must be instate before the ProductController
    public LinkedList<GeneralProduct> loadGeneralProducts(Integer branch_id) {
        LinkedList<GeneralProduct> generalProducts = new LinkedList<>();
        try {
            List<GeneralProductDTO> generalProductDTOS = general_product_dao.dao.queryBuilder().where().eq("branch_id", branch_id).query();
            if (generalProductDTOS != null && !generalProductDTOS.isEmpty()) {
                for (GeneralProductDTO dto : generalProductDTOS) {
                    GeneralProduct generalProduct = find_GeneralProduct(dto.getGPID(), branch_id);
                    if (generalProduct != null) {
                        generalProducts.add(generalProduct);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return generalProducts;
    }

    //region CRUD section
    //region Branch Management

    /**
     * find and return Branch object
     *
     * @param branch_id
     * @return
     */
    public Branch find_Branch(Integer branch_id) {
        return branch_dao.find(branch_id);
    }

    /**
     * write Branch to the DB
     *
     * @param branch
     */
    public void create(Branch branch) {
        branch_dao.create(branch);
    }

    /**
     * change the Branch name and update the DB
     *
     * @param branch
     */
    public void update(Branch branch) {
        branch_dao.update(branch);
    }

    /**
     * delete Branch from the DB
     *
     * @param branch_id
     */
    public void delete(Integer branch_id) {
        branch_dao.delete(branch_id);
    }
    //endregion

    //region CatalogProduct Management
    public CatalogProduct find_CatalogProduct(Integer catalog_id, Integer branch_id) {
        return catalog_product_dao.find(catalog_id, branch_id);
    }

    /**
     * write the connection between a catalog product to its general product in the DB
     *
     * @param catalogProduct
     */
    public void create(CatalogProduct catalogProduct) {
        catalog_product_dao.create(catalogProduct);
    }

    /**
     * update the connection between a catalog product to its general product in the DB
     *
     * @param catalogProduct
     */
    public void update(CatalogProduct catalogProduct) {
        catalog_product_dao.update(catalogProduct);
    }

    /**
     * update the connection between a catalog product to its general product in the DB
     *
     * @param catalogProduct
     */
    public void delete(CatalogProduct catalogProduct) {
        catalog_product_dao.delete(catalogProduct);
    }
    //endregion

    //region Category Management
    public Category find_Category(Integer category_id, Integer branch_id) {
        return category_dao.find(category_id, branch_id);
    }

    /**
     * writing the provided category to the DB
     *
     * @param category
     */
    public void create(Category category) {
        category_dao.create(category);
    }

    /**
     * update the provided category from the DB
     *
     * @param category
     */
    public void update(Category category) {
        category_dao.update(category);
    }

    /**
     * delete the provided category from the DB
     *
     * @param category
     */
    public void delete(Category category) {
        category_dao.delete(category);
    }
    //endregion

    //region Contract Management
    public Contract find_Contract(Integer contract_id, Integer branch_id) {
        return contract_dao.find(contract_id, branch_id);
    }

    /**
     * write contract to the DB
     *
     * @param contract
     */
    public void create(Contract contract) {
        contract_dao.create(contract);
    }

    /**
     * delete contract from DB
     *
     * @param contract
     */
    public void delete(Contract contract) {
        contract_dao.delete(contract);
    }

    //region catalog products management

    /**
     * write the connection between a catalog product to its contract in the DB
     *
     * @param catalogProduct
     * @param contract
     */
    public void addCatalogProduct(Contract contract, CatalogProduct catalogProduct) {
        contract_dao.addCatalogProduct(contract, catalogProduct);
    }

    /**
     * delete the product from the contract and update the DB
     *
     * @param catalogProduct
     * @param contract
     */
    public void deleteCatalogProduct(Contract contract, CatalogProduct catalogProduct) {
        contract_dao.deleteCatalogProduct(contract, catalogProduct);
    }
    //endregion

    //region categories in contract

    /**
     * write the contracts category in the DB
     *
     * @param contract
     * @param category
     */
    public void addCategoryToContract(Contract contract, String category) {
        contract_dao.addCategoryToContract(contract, category);
    }

    /**
     * delete a contracts category in the DB
     *
     * @param contract
     * @param category
     */
    public void deleteCategoryFromContract(Contract contract, String category) {
        contract_dao.deleteCategoryFromContract(contract, category);
    }
    //endregion
    //endregion

    //region Cost Engineering Management
    public CostEngineering find_CostEngineering(Integer contract_id, Integer branch_id) {
        return cost_engineering_dao.find(contract_id, branch_id);
    }

    /**
     * write CostEngineering to the DB
     *
     * @param costEngineering
     */
    public void create(CostEngineering costEngineering) {
        cost_engineering_dao.create(costEngineering);
    }

    /**
     * update CostEngineering to the DB
     *
     * @param costEngineering
     */
    public void update(CostEngineering costEngineering) {
        cost_engineering_dao.update(costEngineering);
    }

    /**
     * delete the cost engineering from the DB
     *
     * @param costEngineering
     */
    public void delete(CostEngineering costEngineering) {
        cost_engineering_dao.delete(costEngineering);
    }
    //region product in cost engineering

    /**
     * add a product to CostEngineering and update the DB
     *
     * @param product
     * @param costEngineering
     */
    public void addProductToCostEngineering(CatalogProduct product, CostEngineering costEngineering) {
        cost_engineering_dao.addProductToCostEngineering(product, costEngineering);
    }

    /**
     * delete a product from cost engineering and update the DB
     *
     * @param product
     * @param costEngineering
     */
    public void deleteProductFromCostEngineering(CatalogProduct product, CostEngineering costEngineering) {
        cost_engineering_dao.deleteProductFromCostEngineering(product, costEngineering);
    }
    //endregion
    //endregion

    //region General Product Management
    public GeneralProduct find_GeneralProduct(Integer general_product_id, Integer branch_id) {
        return general_product_dao.find(general_product_id, branch_id);
    }
//    public GeneralProduct find_GeneralProductbyCategory(Integer general_product_id, Integer category_id,Integer branch_id){
//        return general_product_dao.find(general_product_id,category_id,branch_id);
//    }

    /**
     * write general product to the DB
     *
     * @param generalProduct
     */
    public void create(GeneralProduct generalProduct) {
        general_product_dao.create(generalProduct);

    }

    /**
     * update general product at the DB
     *
     * @param generalProduct
     */
    public void update(GeneralProduct generalProduct) {
        general_product_dao.update(generalProduct);
    }

    /**
     * delete general product from the DB
     *
     * @param generalProduct
     */
    public void delete(GeneralProduct generalProduct) {
        general_product_dao.delete(generalProduct);
    }
    //endregion

    //region Order Management
    public Order find_Order(Integer order_id, Integer branch_id) {
        return order_dao.find(order_id, branch_id);
    }

    /**
     * write single order and it associate classes to the DB
     *
     * @param order
     */
    public void create(Order order) {
        order_dao.create(order);
    }

    /**
     * update single order and all its products to the DB
     *
     * @param order
     */
    public void update(Order order) {
        order_dao.update(order);
    }

    /**
     * delete the order from the DB
     *
     * @param order
     */
    public void delete(Order order) {
        order_dao.delete(order);
    }

    /**
     * add a specific product to a single order ant update the DB
     *
     * @param product
     * @param order
     * @param quantity
     * @param price
     */
    public void addCatalogProductToOrder(CatalogProduct product, Order order, Integer quantity, Float price) {
        order_dao.addCatalogProductToOrder(product, order, quantity, price);
    }

    /**
     * delete a specific product from the order and update the DB
     *
     * @param product
     * @param order
     */
    public void deleteCatalogProductFromOrder(Order order, CatalogProduct product) {
        order_dao.delete(order, product);
    }
    //endregion

    //region Sale Management
    public Sale find_Sale(Integer sale_id, Integer branch_id) {
        return sale_dao.find(sale_id, branch_id);
    }

    /**
     * write Sale object to the DB and all its general product associate to it
     *
     * @param sale
     */
    public void create(Sale sale) {
        sale_dao.create(sale);
    }

    /**
     * update Sale object to the DB and all its general product associate to it
     *
     * @param sale
     */
    public void update(Sale sale) {
        sale_dao.update(sale);
    }

    /**
     * delete Sale object to the DB and all its general product associate to it
     *
     * @param sale
     */
    public void delete(Sale sale) {
        sale_dao.delete(sale);
    }
    //endregion

    //region Specific Product Management

    /**
     * find and return SpecificProduct object
     *
     * @param specific_product_id
     * @return
     */
    public SpecificProduct find_SpecificProduct(Integer specific_product_id, Integer branch_id) {
        return specific_product_dao.find(specific_product_id, branch_id);
    }

    /**
     * write specific product to the DB
     *
     * @param specificProduct
     */
    public void create(SpecificProduct specificProduct) {
        specific_product_dao.create(specificProduct);
    }

    /**
     * update specific product to the DB
     *
     * @param specificProduct
     */
    public void update(SpecificProduct specificProduct) {
        specific_product_dao.update(specificProduct);
    }

    /**
     * delete specific product to the DB
     *
     * @param specificProduct
     */
    public void delete(SpecificProduct specificProduct) {
        specific_product_dao.delete(specificProduct);
    }
    //endregion

    //region Supplier Management

    /**
     * find and returns the supplierCard object in the repository
     *
     * @param supplier_id
     * @return
     */
    public SupplierCard find_Supplier(Integer supplier_id) {
        return supplier_dao.find(supplier_id);
    }

    /**
     * writing supplierCard  and all its contactList to the DB
     *
     * @param supplier
     */
    public void create(SupplierCard supplier) {
        supplier_dao.create(supplier);
    }

    /**
     * update supplierCard  and all its contactList to the DB
     *
     * @param supplier
     */
    public void update(SupplierCard supplier) {
        supplier_dao.update(supplier);
    }

    /**
     * add a contact to the supplierCard and update the DB
     *
     * @param supplier
     * @param contactName
     */
    public void create_contact(String contactName, SupplierCard supplier) {
        supplier_dao.create_contact(contactName, supplier);
    }

    /**
     * delete a contact from the supplier card and update the DB
     *
     * @param supplier
     * @param contact
     */
    public void delete_contact(String contact, SupplierCard supplier) {
        supplier_dao.delete_contact(contact, supplier);
    }

    /**
     * delete a the supplier and update the DB
     *
     * @param supplier
     */
    public void delete(SupplierCard supplier) {
        supplier_dao.delete(supplier);
    }
    //endregion

    //region Employees Management
    //shift
    public void addShift(Shift shift){shifts_dao.addShift(shift);}

    public void deleteShift(String shiftID){shifts_dao.deleteShift(shiftID);}

    public void addOccupation(String shiftId, String position, String WorkerID){
              shifts_dao.addOccupation(shiftId, position, WorkerID);
    }

    public void deleteOccupation(String shiftId, String position, String WorkerID){
        shifts_dao.deleteOccupation(shiftId,position,WorkerID);
    }

    public void addShiftAvailableWorkers(String workerID, Date date, boolean timeOfDay){
        shifts_dao.addShiftAvailableWorkers(workerID,date,timeOfDay);
    }

    public boolean deleteShiftAvailableWorkers(String WorkerId, Date date, boolean timeOfDay)
    {
        return shifts_dao.deleteShiftAvailableWorkers(WorkerId,date,timeOfDay);
    }

    public void deleteShiftAvailableWorkers(String workerId) {
        shifts_dao.deleteShiftAvailableWorkers(workerId);
    }

    public void addShiftDriver(String driverID, String shiftID){
        shifts_dao.addShiftDriver(driverID,shiftID);
    }

    public void deleteShiftDriver(String driverID, String shiftID){
        shifts_dao.deleteShiftDriver(driverID,shiftID);
    }

    public void updateShift(Shift shift){shifts_dao.updateShift(shift);}

    public Shift getShift(Date date, boolean partOfDay, int branch){return shifts_dao.getShift(date,partOfDay,branch);}

    //Worker
    public void addWorker(Worker worker){employees_dao.addWorker(worker);}

    public void deleteWorker(String workerId){employees_dao.deleteWorker(workerId);}

    public void addPosition(String position, String workerID){employees_dao.addPosition(position,workerID);}

    public void deletePosition(String position, String WorkerId){employees_dao.deletePosition(position,WorkerId);}

    public void updateWorker(Worker worker){employees_dao.updateWorker(worker);}

    public Worker getWorker(String id){return employees_dao.getWorker(id);}

    public boolean isScheduled(String id){return employees_dao.isScheduled(id);}

    public List<Worker> getAllWorkers(){return employees_dao.getAllWorkers();}

    //TODO:change in business layer all usage of the functions below to receive branch_id
    public List<Worker> getAvailableWorkers(Date date, boolean partOfDay, int branch_id)
    {
        return employees_dao.getAvailableWorkers(date,partOfDay,branch_id);
    }

    public List<Worker> getAllWorkersByBranch(int branch_id){return employees_dao.getAllWorkersByBranch(branch_id);}
    //Driver
    public void addDriver(Driver driver){employees_dao.addDriver(driver);}

    public void deleteDriver(String driverID){employees_dao.deleteDriver(driverID);}

    public Driver getDriver(String driver_id){return employees_dao.getDriver(driver_id);}
    //endregion

    //region Transport Management
    public void updateTransportDriver(int transport, String newDriverID, String newDriverName) {
            transport_dao.updateTransportDriver(transport, newDriverID, newDriverName);
    }

    public void addTransport(Transport transport) {
            transport_dao.addTransport(transport);
    }

    public boolean deleteTransport(int transportID) {
        return transport_dao.deleteTransport(transportID);
    }

    public long MaxIDTransport() {
        return transport_dao.MaxIDTransport();
    }

    public List<Transport> getAllTransports() { return transport_dao.getAllTransports();}

    public Transport getTransport(int transportID) {return transport_dao.getTransport(transportID);    }

    public void add_to_log(int transportID, String message) {transport_dao.add_to_log(transportID,message);}

    public Transport getTransportToUpdate(String prevDriverId, Date date, Boolean partOfDay, int branch_id) {
        return transport_dao.getTransportToUpdate(prevDriverId,date,partOfDay,branch_id);
    }

    public Hashtable<Integer,Transport> getAllTransportsByBranchID(int branch_id) {
       return transport_dao.getAllTransportsByBranchID(branch_id);
    }

    public Boolean getTransportByShift(Date d, Boolean partOfDay, int branch_id) {return transport_dao.getTransportByShift(d,partOfDay,branch_id);}

    public void add_to_pending_orders(int order_id, int branch_id){transport_dao.add_to_pending_orders(order_id,branch_id);}

    public List<Order> getAllPendingOrdersForBranch(int branch){return transport_dao.getAllPendingOrdersForBranch(branch);}

    public void removeFromPendingOrders(int order_id,int branch_id){transport_dao.removeFromPendingOrders(order_id,branch_id);}
    //truck
    public void addTruck(Truck truck) {
            truck_dao.addTruck(truck);
    }

    public boolean deleteTruck(int truckID) {
        return truck_dao.deleteTruck(truckID);
    }

    public void addMorningShift(int truckID, Date date) {
        truck_dao.addMorningShift(truckID,date);
    }

    public void delete_MorningShift(Date date, int truckID) {
       truck_dao.delete_MorningShift(date,truckID);
    }

    public void addNightShift(int truckID, Date date) {truck_dao.addNightShift(truckID,date);}

    public void deleteNightShift(Date date, int truckID) {truck_dao.deleteNightShift(date,truckID);}

    public long MaxIdTrucks(){return truck_dao.MaxIdTrucks();}

    public Truck getTruck(int id){return truck_dao.getTruck(id);}

    public List<Truck> getAllTrucks(){return truck_dao.getAllTrucks();}

    public boolean TruckNotInMorningShift(Date date , int truckID){
        return truck_dao.TruckNotInMorningShift(date,truckID);}

    public boolean TruckNotInNightShift(Date date, int truckID){
        return truck_dao.TruckNotInNightShift(date,truckID);
    }

    public Hashtable<Integer,Truck> getAllTrucksByBranch(int branch_id){return truck_dao.getAllTrucksByBranch(branch_id);}

    public boolean checkIfTrucksAvailableByDate(Date date, boolean partOfDay, int branch_id){
        return truck_dao.checkIfTrucksAvailableByDate(date,partOfDay,branch_id);
    }

    public List<Truck> getAvailableTrucks(Date date, boolean partOfDay, float Weight, int branch_id){
        return truck_dao.getAvailableTrucks(date,partOfDay,Weight, branch_id);
    }
    //endregion

    //region Utilities
    public void clearCache(){
        catalog_product_dao.clearCache();
        general_product_dao.clearCache();
        specific_product_dao.clearCache();
        category_dao.clearCache();
        sale_dao.clearCache();
        order_dao.clearCache();
        contract_dao.clearCache();
        supplier_dao.clearCache();
        branch_dao.clearCache();
        cost_engineering_dao.clearCache();

        transport_dao.clearCache();
        truck_dao.clearCache();
        employees_dao.clearCache();
        shifts_dao.clearCache();



    }

    public void clearDatabase(){
        try {
            TableUtils.clearTable(this.conn,BranchDTO.class);
            TableUtils.clearTable(this.conn,SupplierDTO.class);
            TableUtils.clearTable(this.conn,catalog_product_in_contractDTO.class);
            TableUtils.clearTable(this.conn,catalog_product_in_general_productDTO.class);
            TableUtils.clearTable(this.conn,catalog_product_in_orderDTO.class);
            TableUtils.clearTable(this.conn,CatalogProductDTO.class);
            TableUtils.clearTable(this.conn,categories_in_contractDTO.class);
            TableUtils.clearTable(this.conn,CategoryDTO.class);
            TableUtils.clearTable(this.conn,contact_of_supplierDTO.class);
            TableUtils.clearTable(this.conn,ContractDTO.class);
            TableUtils.clearTable(this.conn,CostEngineeringDTO.class);
            TableUtils.clearTable(this.conn,general_product_on_saleDTO.class);
            TableUtils.clearTable(this.conn,GeneralProductDTO.class);
            TableUtils.clearTable(this.conn,IDsDTO.class);
            TableUtils.clearTable(this.conn,OrderDTO.class);
            TableUtils.clearTable(this.conn,SaleDTO.class);
            TableUtils.clearTable(this.conn,SpecificProductDTO.class);
            TableUtils.clearTable(this.conn,Driver_DTO.class);
            TableUtils.clearTable(this.conn,log_DTO.class);
            TableUtils.clearTable(this.conn,morning_shifts_DTO.class);
            TableUtils.clearTable(this.conn,night_shifts_DTO.class);
            TableUtils.clearTable(this.conn,Occupation_DTO.class);
            TableUtils.clearTable(this.conn,pending_orders_dto.class);
            TableUtils.clearTable(this.conn,Position_DTO.class);
            TableUtils.clearTable(this.conn,Shift_DTO.class);
            TableUtils.clearTable(this.conn,ShiftDriver_DTO.class);
            TableUtils.clearTable(this.conn,Shift_availableWorkers_DTO.class);
            TableUtils.clearTable(this.conn,Transport_DTO.class);
            TableUtils.clearTable(this.conn,Truck_DTO.class);
            TableUtils.clearTable(this.conn,Worker_DTO.class);
            IDsDTO ids = new IDsDTO(1,1,1,1,1,1,1);
            ids_dao.create(ids);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //endregion

}



