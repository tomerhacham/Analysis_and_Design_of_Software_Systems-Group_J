package bussines_layer;

import bussines_layer.employees_module.*;
import bussines_layer.employees_module.models.ModelShift;
import bussines_layer.employees_module.models.ModelWorker;
import bussines_layer.enums.OrderStatus;
import bussines_layer.enums.OrderType;
import bussines_layer.enums.supplierType;
import bussines_layer.inventory_module.*;
import bussines_layer.supplier_module.Order;
import bussines_layer.supplier_module.OrdersController;
import bussines_layer.supplier_module.SupplierModule;
import bussines_layer.transport_module.TransportModule;
import data_access_layer.DTO.BranchDTO;
import javafx.util.Pair;

import java.util.*;

import static bussines_layer.BranchController.system_curr_date;

public class Branch {
    //fields:
    private String name;
    private Integer branch_id;
    private Inventory inventory;
    private SupplierModule supplierModule;
    private TransportModule transportModule;
    private EmployeesModule employeesModule;

    //Constructor
    public Branch(Integer branch_id, String name) {
        this.branch_id = branch_id;
        inventory = new Inventory(branch_id);
        supplierModule = new SupplierModule(branch_id);
        transportModule = new TransportModule(branch_id);
        employeesModule=new EmployeesModule(branch_id);
        transportModule.setEmployeesModule(employeesModule);
        employeesModule.setTransportModule(transportModule);
        this.name = name;
    }
    public Branch(BranchDTO branchDTO){
        this.branch_id=branchDTO.getBranch_id();
        this.name=branchDTO.getName();
    }

    //region Inventory Module
    //region Categories
    public Result addMainCategory(String name){
        return inventory.addMainCategory(name);
    }
    public Result addSubCategory(Integer predecessor_cat_id, String name){
        return inventory.addSubCategory(predecessor_cat_id, name);
    }
    public Result removeCategory(Integer category_id){
        return inventory.removeCategory(category_id);
    }
    public Result editCategoryName(Integer category_id, String name){
        return inventory.editCategoryName(category_id, name);
    }
    //endregion

    //region General Products
    public Result addGeneralProduct(Integer category_id, String manufacture, String name, Float supplier_price, Float retail_price,
                                    Integer min_quantity, Integer catalogID, Integer gpID, Integer supplier_id, String supplier_category , Float weight){
        return inventory.addGeneralProduct(category_id, manufacture, name, supplier_price, retail_price, min_quantity, catalogID, gpID, supplier_id, supplier_category , weight);
    }
    public Result removeGeneralProduct(Integer category_id, Integer gpID)
    {
        return inventory.removeGeneralProduct(category_id, gpID);    }
    public Result editGeneralProductName(Integer gpID, String new_name){
        return inventory.editGeneralProductName(gpID, new_name);
    }
    public Result editGeneralProductSupplierPrice(Integer gpID, Float new_supplier_price, Integer supplier_id){
        return inventory.editGeneralProductSupplierPrice(gpID, new_supplier_price, supplier_id);
    }
    public Result editGeneralProductRetailPrice(Integer gpID, Float new_retail_price){
        return inventory.editGeneralProductRetailPrice(gpID, new_retail_price);

    }
    public Result editGeneralProductQuantity(Integer gpID, Integer new_quantity){
        return inventory.editGeneralProductQuantity(gpID, new_quantity);
    }
    public Result editGeneralProductMinQuantity(Integer gpID, Integer new_min_quantity){
        return inventory.editGeneralProductMinQuantity(gpID, new_min_quantity);
    }
    private Result<GeneralProduct> searchGeneralProductByGpID(Integer gpID){
        return inventory.searchGeneralProductByGpID(gpID);
    }
    //endregion

    //region Specific Products
    public Result addSpecificProduct(Integer gpID, Date expiration_date, Integer quantity){
        return inventory.addSpecificProduct(gpID, expiration_date, quantity);
    }
    public Result removeSpecificProduct(Integer specific_product_id){
        return inventory.removeSpecificProduct(specific_product_id);
    }
    public Result markAsFlaw(Integer specific_product_id){
        return inventory.markAsFlaw(specific_product_id);
    }
    public Result moveLocation(Integer specific_product_id){
        return inventory.moveLocation(specific_product_id);
    }
    //endregion

    //region Reports
    public Result<Report> makeReportByGeneralProduct(Integer gpID, String type){
        return inventory.makeReportByGeneralProduct(gpID,type);
    }
    public Result<Report> makeReportByCategory(Integer category_id, String type){
        return inventory.makeReportByCategory(category_id,type);
    }
    //endregion

    //region Sales
    public Result addSaleByGeneralProduct(Integer gpID, String stype, Float amount){
        return inventory.addSaleByGeneralProduct(gpID, stype, amount);
    }
    public Result addSaleByGeneralProduct(Integer gpID, String stype, Float amount, Date start, Date end){
        return inventory.addSaleByGeneralProduct(gpID, stype, amount, start, end);
    }
    public Result addSaleByCategory(Integer category_id, String stype, Float amount){
        return inventory.addSaleByCategory(category_id, stype, amount);
    }
    public Result addSaleByCategory(Integer category_id, String stype, Float amount, Date start, Date end){
        return inventory.addSaleByCategory(category_id, stype, amount, start, end);
    }
    public Result removeSale(Integer sale_id){
        return inventory.removeSale(sale_id);
    }
    public Result<List<Sale>> CheckSalesStatus(){
        return inventory.CheckSalesStatus();
    }
    //endregion

    //region Debug tools
    public String mapAllCategories(){
        return inventory.mapAllCategories();
    }
    public String mapAllGeneralProducts(){
        return inventory.mapAllGeneralProducts();
    }
    public String mapAllSales(){
        return inventory.mapAllSales();
    }
    //endregion

    //endregion

    //region Supplier Module

    //region Contracts
    public Result addContract(SupplierCard supplier , LinkedList<String> categories){
        return supplierModule.addContract(supplier, categories);
    }
    public Result removeContract(SupplierCard supplier){
        return supplierModule.removeContract(supplier);
    }

    public Result addProductToContract(Integer supplierID, Integer catalogID, Integer gpID, Float supplier_price, String supplier_category){
        GeneralProduct gp = searchGeneralProductByGpID(gpID).getData();
        if (gp == null){
            return new Result<>(false, null, String.format("General Product with ID %d does not exist", gpID));
        }
        CatalogProduct cp = gp.getSupplierCatalogProduct(supplierID);
        if (cp == null){
            cp = gp.addCatalogProduct(catalogID, gpID, supplier_price, supplierID, supplier_category, gp.getName() , gp.getWeight()).getData();
        }
        return supplierModule.addProductToContract(supplierID, cp);
    }
    public Result removeProductFromContract(Integer supplierID, Integer gpID){
        GeneralProduct gp = searchGeneralProductByGpID(gpID).getData();
        CatalogProduct toRemove = gp.getSupplierCatalogProduct(supplierID);
        if (toRemove == null){
            return new Result<>(false, null, String.format("Catalog Product of gpID: %d and supplier ID: %d not found", gpID, supplierID));
        }
        return supplierModule.removeProductFromContract(supplierID, toRemove);
    }
    public Result addCategory(Integer supplierID, String category){
        return supplierModule.addCategory(supplierID, category);
    }
    public Result removeCategory (Integer supplierID, String category){
        return supplierModule.removeCategory(supplierID, category);
    }
    //endregion

    //region Cost Engineering
    public Result addProductToCostEng(Integer supid, Integer catalogid, Integer minQuantity, Float price) {
        return supplierModule.addProductToCostEng(supid , catalogid , minQuantity , price);
    }
    public Result removeProductCostEng(Integer supid, Integer catalogid2delete) {
        return supplierModule.removeProductCostEng(supid, catalogid2delete);
    }
    public Result updateMinQuantity(Integer supid, Integer catalogid, Integer minQuantity) {
        return supplierModule.updateMinQuantity(supid , catalogid , minQuantity);
    }
    public Result updatePriceAfterSale(Integer supid, Integer catalogid, Float price) {
        return supplierModule.updatePriceAfterSale(supid , catalogid , price);
    }
    public Result<LinkedList<CatalogProduct>> getAllSupplierProducts(Integer supId) {
        return supplierModule.getAllSupplierProducts(supId);
    }
    public Result addCostEng(Integer supid) {
        return supplierModule.addCostEng(supid);
    }
    public Result removeCostEng(Integer supid) {
        return supplierModule.removeCostEng(supid);
    }
    //endregion

    //region Orders

    public Result acceptOrder (Integer orderID){
        if(!isOrderExist(orderID)){
            return new Result(false,false, String.format("There is not order with ID:%d", orderID));
        }
        else if(isOrderAccepted(orderID)){
            return new Result(false,false, String.format("Order %d has been already accepted", orderID));
        }
        else if(validAcceptOrdervalidAcceptOrder(orderID)) {
            Result<HashMap<CatalogProduct, Integer>> productsResult = supplierModule.getProductsToAcceptOrder(orderID);
            if (!productsResult.isOK()) {
                return productsResult;
            }
            if(supplierModule.getOrderById(orderID).getData().getType().equals(OrderType.PeriodicOrder))
            {
                supplierModule.getOrderById(orderID).getData().setStatus(OrderStatus.inProcess);}
            HashMap<CatalogProduct, Integer> products = productsResult.getData();
            return inventory.updateInventory(products);
        }
        else {
            return new Result(false, false, String.format("Order %d has not been arrived yet, be patient...", orderID));
        }
    }

    private boolean isOrderAccepted(Integer orderID) {
        Result<Order> orderResult = supplierModule.getOrderById(orderID);
        boolean toReturn=false;
        if (orderResult.isOK()){
            if(orderResult.getData().getStatus().equals(OrderStatus.received)){
                toReturn=true;
            }
            else{
                toReturn=false;
            }
        }
        return toReturn;
    }

    public Result<String> bookTransportOrder (Order order){
        if(order.getSupplier().getType()== supplierType.selfDelivery){
            String toprint = transportModule.BookTransport(order);
            return new Result(true , toprint , "");
        }
        return new Result(false , new String(" ") , "");
    }

    public Result<String> createOutOfStockOrder(Report report){
        Result<LinkedList<Order>> resultOrder = supplierModule.createOutOfStockOrder(report);
        LinkedList<Order> orders = resultOrder.getData();
        if(orders!=null){
            String transportString = "";
            String toprint = "---------------------------------------------------------------\n";
            for (Order or : orders) {
                transportString = bookTransportOrder(or).getData().concat("\n");
                toprint = toprint.concat(or.display().getData().concat(transportString)).concat("---------------------------------------------------------------\n");
            }
            return new Result( true , toprint , "");
        }
        return new Result( false , resultOrder.getMessage() , "");
    }

    public Result createPeriodicOrder(Integer supplierID , LinkedList<Pair<Integer , Integer>> productsAndQuantity , Integer day){
        LinkedList<Pair<GeneralProduct,Integer>> products = new LinkedList<>();
        for (Pair<Integer,Integer> p : productsAndQuantity){
            Result<GeneralProduct> result = inventory.searchGeneralProductByGpID(p.getKey());
            if (!result.isOK()){
                return result;
            }
            products.add(new Pair<>(result.getData(),p.getValue()));
        }
        return supplierModule.createPeriodicOrder(supplierID, products, day);
    }

    public Result removePeriodicOrder(Integer orderId){
        return supplierModule.removePeriodicOrder((orderId));
    }

    public Result addProductToPeriodicOrder(Integer orderId , Integer gpID , Integer quantity){
        Result<GeneralProduct> result = searchGeneralProductByGpID(gpID);
        if (!result.isOK()){
            return result;
        }
        return supplierModule.addProductToPeriodicOrder(orderId, result.getData(),quantity);
    }

    public Result updateProductQuantityInPeriodicOrder(Integer orderId , Integer gpID , Integer newQuantity){
        Result<GeneralProduct> result = searchGeneralProductByGpID(gpID);
        if (!result.isOK()){
            return result;
        }
        return supplierModule.updateProductQuantityInPeriodicOrder(orderId, result.getData(),newQuantity);
    }

    public Result removeProductFromPeriodicOrder(Integer orderId , Integer gpID){
        Result<GeneralProduct> result = searchGeneralProductByGpID(gpID);
        if (!result.isOK()){
            return result;
        }
        return supplierModule.removeProductFromPeriodicOrder(orderId , result.getData());
    }

    public Result updateSupplierToPeriodicOrder (Integer orderID, Integer supplierID){
        return supplierModule.updateSupplierToPeriodicOrder(orderID,supplierID);
    }

    public Result<LinkedList<String>> issuePeriodicOrder(){
        Result<LinkedList<Order>> resultOrdersToIssue = supplierModule.issuePeriodicOrder();
            String str = "";
        if(!resultOrdersToIssue.isOK()) {
            LinkedList<String> toreturn = new LinkedList<>();
            toreturn.add(resultOrdersToIssue.getMessage());
            return new Result<>(false ,  toreturn, String.format("There are no periodic orders to issue in branch %d", branch_id));
        }

        for (Order order:resultOrdersToIssue.getData()) {

            str = str.concat(order.display().getData());
            if(order.getSupplier().getType().equals(supplierType.selfDelivery)){
                str = str.concat(bookTransportOrder(order).getData());
            }
        }
        LinkedList<String> branchPeriodicOrders = new LinkedList<>();
        branchPeriodicOrders.add("-------------------Branch : "+name+"-------------------\n");
        branchPeriodicOrders.add("Last Notice ! Don't forget to send those orders to the supplier !\n");
        branchPeriodicOrders.add(str);
        branchPeriodicOrders.add("-------------------------------------------------------\n");
        return new Result<>(true,branchPeriodicOrders, String.format("All periodic orders with %d as their delivery day had been sent to order", BranchController.system_curr_date.getDay()+1));
    }

    public Result<LinkedList<String>> displayAllOrders(){
        return supplierModule.displayAllOrders();
    }

    public Result<LinkedList<String>> displayAllSupplierOrders(Integer supId){
        return supplierModule.displayAllSupplierOrders(supId);
    }

    public Result<LinkedList<String>> getAllwaitingOrders(){
        return supplierModule.getAllwaitingOrders();
    }

    //endregion

    //endregion

    //region Transport Module
    public String getAllTransportsDetails() {
        return transportModule.getAllTransportsDetails();
    }

    public String getAllTrucksDetails() {
        return transportModule.getAllTrucksDetails();
    }

    public boolean deleteTruck(int truckToDelete) {
        return transportModule.deleteTruck(truckToDelete);
    }

    public boolean createTruck(String license_plate, String model, float netWeight, float maxWeight, String drivers_license) {
        return transportModule.createTruck(license_plate, model, netWeight, maxWeight, drivers_license);
    }

    public String getPendingOrdersDetails() {
        return transportModule.getPendingOrdersDetails();
    }

    public boolean isOrderIdInPendingOrders(int orderID) {
        return transportModule.isOrderIdInPendingOrders(orderID);
    }

    public String BookTransportForPendingOrders(int orderID) {
        return transportModule.BookTransportForPendingOrders(orderID);
    }

    public void updatePendingOrders() {
        transportModule.updatePendingOrders();
    }
    //endregion

    //region Employee Module
    private static final boolean morning=true;
    private static final boolean night=false;

    public void setTransportModule(TransportModule transportModule) {
        employeesModule.setTransportModule(transportModule);
    }

    public String chooseDriverForTransport(Date date, boolean shift, String drivers_license) {
        return employeesModule.chooseDriverForTransport(date,shift,drivers_license);
    }

    public String getDriverName(String driverId) {
        return employeesModule.getDriverName(driverId);
    }

    public boolean StorageManInShift(Date d, boolean b) {
        return employeesModule.StorageManInShift(d,b);
    }

    public boolean DriversAvailability(Date date, boolean shift) {
        return employeesModule.DriversAvailability(date,shift);
    }

    public void removeDriverFromTransport(Date d, boolean shift, String driverId) {
        employeesModule.removeDriverFromTransport(d,shift,driverId);
    }



    public String removeShift(Date date,boolean timeOfDay){
        return employeesModule.removeShift(date,timeOfDay);
    }
    public String createShift(Date date, boolean timeOfDay)
    {
        return employeesModule.createShift(date,timeOfDay);
    }
    public String editShift(Date date, boolean timeOfDay)
    {
        return employeesModule.editShift(date, timeOfDay);
    }
    public ModelShift getCurrentEditedModelShift()
    {
        return employeesModule.getCurrentEditedModelShift();
    }
    public void cancelShift(){employeesModule.cancelShift();}
    public String submitShift(){
        return employeesModule.submitShift();
    }
    public String removePositionFromShift(String pos){
        return employeesModule.removePositionFromShift(pos);
    }
    public String addPositionToShift(String pos,int quantity)
    {
        return employeesModule.addPositionToShift(pos, quantity);

    }
    public String addWorkerToPositionInShift(String pos,String id){
        return employeesModule.addWorkerToPositionInShift(pos, id);
    }
    public String removeWorkerToPositionInShift(String pos,String id)
    {
        return employeesModule.removeWorkerToPositionInShift(pos, id);
    }
    public String addAvailableWorker(Date date, boolean partOfDay, String id){
        return employeesModule.addAvailableWorker(date, partOfDay, id);
    }
    public String removeAvailableWorker(Date date,boolean partOfDay,String id) {

        return employeesModule.removeAvailableWorker(date, partOfDay, id);
    }
    public List<ModelShift> getWeeklyShifts(Date date)
    {
        return employeesModule.getWeeklyShifts(date);
    }
    public String removeWorkerFromRoster(String id){
        return employeesModule.removeWorkerFromRoster(id);

    }
    public List<ModelWorker> displayWorkers()
    {
        return employeesModule.displayWorkers();
    }

    public ModelWorker displaySingleWorker(String id)
    {
        return employeesModule.displaySingleWorker(id);
    }

    public String editName(String newName,String id)
    {
        return employeesModule.editName(newName,id);
    }
    public String editSalary(double newSalary,String id)
    {
        return employeesModule.editSalary(newSalary,id);
    }
    public String addPosition(String pos,String id)
    {
        return employeesModule.addPosition(pos,id);
    }
    public String removePosition(String pos,String id)
    {
        return employeesModule.removePosition(pos,id);
    }
    public String addWorker(String name, double salary, Date startDate,List<String>positions)
    {
        return employeesModule.addWorker(name,salary,startDate,positions);
    }
    public String addDriver(String name, double salary, Date startDate,String license)
    {
        return employeesModule.addDriver(name,salary,startDate,license);
    }
    public String removeWorker(String id)
    {
        return employeesModule.removeWorker(id);
    }


    public String initAddWorker(String id, String name, double salary, Date startDate,List<String>positions){
        return employeesModule.initAddWorker(id, name, salary, startDate, positions);
    }

    public String initAddDriver(String id, String name, double salary, Date startDate,String license){
        return  employeesModule.initAddDriver(id, name, salary, startDate, license);
    }

//
//    public void removeExistingWorkers() {
//        employeesModule.removeExistingWorkers();
//    }

    public Scheduler testScheduler()
    {
        return employeesModule.testScheduler();
    }

    public Roster testRoster()
    {
        return employeesModule.testRoster();
    }



    //endregion

    //region Getters & setters

    public Integer getBranchId() {
        return branch_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setSupplierModule(SupplierModule supplierModule) {
        this.supplierModule = supplierModule;
    }
    //endregion
    public void loadData(){
        this.inventory=new Inventory(this.branch_id);
        this.supplierModule = new SupplierModule(branch_id);
        this.employeesModule=new EmployeesModule(branch_id);
        this.transportModule=new TransportModule(branch_id);
        transportModule.setEmployeesModule(employeesModule);
        employeesModule.setTransportModule(transportModule);
    }
    @Override
    public String toString() {
        return "" +
                "Name:'" + name +
                ", ID:" + branch_id;
    }
    private boolean validAcceptOrdervalidAcceptOrder(Integer order_id){
        Result<Order> orderResult = supplierModule.getOrderById(order_id);
        boolean toReturn=false;
        if (orderResult.isOK() && orderResult.getData().getStatus().equals(OrderStatus.sent)){
            SupplierCard supplier = orderResult.getData().getSupplier();
            if(orderResult.getData().getType().equals(OrderType.OutOfStockOrder)) {
                if (supplier.getType().equals(supplierType.fix_days)) {
                    if (supplier.getFix_day().equals(system_curr_date.getDay())) {
                        toReturn = true; }
                    else {toReturn = false;}
                }
                else { toReturn = true;}
            }
            else {
                if(orderResult.getData().getDayToDeliver().getData().equals(system_curr_date.getDay())){
                    toReturn=true;}
                    else{toReturn=false;
                    }
                }
            }
        return toReturn;
    }
    private boolean isOrderExist(Integer order_id){
        return supplierModule.getOrderById(order_id).isOK();
    }

}


