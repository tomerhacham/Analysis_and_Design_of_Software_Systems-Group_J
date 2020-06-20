package bussines_layer;

import bussines_layer.employees_module.models.ModelShift;
import bussines_layer.employees_module.models.ModelWorker;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.Report;
import bussines_layer.inventory_module.Sale;
import bussines_layer.transport_module.TransportModule;
import data_access_layer.Mapper;
import javafx.util.Pair;

import java.util.*;

public class BranchController {

    private HashMap<Integer, String> branches;      //<Id,Branch>
    private SupplierController supplierController;
    private Branch currBranch;        //current branch active
    private Integer next_id;    //next id available for new branch
    public static Date system_curr_date;
    private Mapper mapper;

    public BranchController (Boolean external_initialize){
        system_curr_date = new Date();
        mapper=Mapper.getInstance();
        if(!external_initialize){next_id = mapper.loadID("branch");}
        supplierController = SupplierController.getInstance();
        branches= mapper.loadBranches();
        currBranch = null;
    }

    //region Supplier Controller

    public Result createSupplierCard (String supplierName, String address, String email, String phoneNumber, Integer id, String bankAccountNum, String payment, LinkedList<String> contactsName, String type) {
        return supplierController.createSupplierCard(supplierName , address , email , phoneNumber , id , bankAccountNum , payment , contactsName, type);
    }
    public Result createSupplierCard (String supplierName, String address, String email, String phoneNumber, Integer id, String bankAccountNum, String payment, LinkedList<String> contactsName, String type,Integer fix_day) {
        return supplierController.createSupplierCard(supplierName , address , email , phoneNumber , id , bankAccountNum , payment , contactsName, type, fix_day);
    }

    public Result changeSupplierName(Integer supid, String supplierName) {
        return supplierController.ChangeSupplierName(supid , supplierName);
    }

    public Result changeAddress(Integer supid, String address) {
        return supplierController.ChangeAddress(supid , address);
    }

    public Result ChangeEmail(Integer supid, String email) {
        return supplierController.ChangeEmail(supid , email);
    }

    public Result ChangePhoneNumber(Integer supid, String phoneNumber) {
        return supplierController.ChangePhoneNumber(supid , phoneNumber);
    }

    public Result ChangeBankAccount(Integer supid, String bankAccountNum) {
        return supplierController.ChangeBankAccount(supid , bankAccountNum);
    }

    public Result ChangePayment(Integer supid, String payment) {
        return supplierController.ChangePayment(supid , payment);
    }

    public Result AddContactName(Integer supid, LinkedList<String> contactsName) {
        return supplierController.AddContactName(supid , contactsName);
    }

    public Result DeleteContactName(Integer supid, String contactName) {
        return supplierController.DeleteContactName(supid , contactName);
    }

    public Result ChangeSupplierType(Integer supid, String type) {
        return supplierController.ChangeSupplierKind(supid , type);
    }

    public Result isExistSupplier(Integer supid) {
        return supplierController.isExist(supid);
    }

    public Result printallsuppliers() {
        return supplierController.printallsuppliers();
    }


    //endregion

    //region Branch
    public Result<Branch> createNewBranch(String name){
        if(!branches.values().contains(name)) {
            Branch toAdd = new Branch(getNextId(), name);
            branches.put(toAdd.getBranchId(), name);
            mapper.create(toAdd);
            return new Result<>(true, toAdd, String.format("New branch (ID: %d) created successfully", toAdd.getBranchId()));
        }
        return new Result<>(false,null,"this name is already taken...");
    }

    public Result switchBranch(Integer branch_id){
        if (checkBranchExists(branch_id)){
            mapper.clearCache();
            if (currBranch == null || !currBranch.getBranchId().equals(branch_id)) {
                currBranch = mapper.loadBranch(branch_id);
            }
            return new Result<>(true, currBranch, String.format("Switched to Branch %s (ID: %d) successfully", currBranch.getName(),currBranch.getBranchId()));
        }
        return new Result<>(false, null, String.format("Branch with ID %d not found", branch_id));
    }

    public Result removeBranch(Integer branch_id) {
        if (checkBranchExists(branch_id)) {
            if (currBranch!=null && currBranch.getBranchId().equals(branch_id)) {
                return new Result<>(false, null, String.format("Can not delete current branch (ID: %d), switch and try again.", branch_id));
            }
            branches.remove(branch_id);
            mapper.delete(branch_id);
            return new Result<>(true, branch_id, String.format("Branch (ID: %d) removed successfully", branch_id));
        }
        return new Result<>(false, null, String.format("Branch with ID %d not found", branch_id));
    }

    public Result editBranchName(Integer branch_id, String newName){
        if (checkBranchExists(branch_id)){
            Branch b =mapper.find_Branch(branch_id);
            b.setName(newName);
            mapper.update(b);
            return new Result<>(true, b, String.format("Branch (ID: %d) name changed successfully to: %s", branch_id, newName));
        }
        return new Result<>(false, null, String.format("Branch with ID %d not found", branch_id));
    }

    //endregion

    //region Inventory Module
    //region Categories
    public Result addMainCategory(String name){
        return currBranch.addMainCategory(name);
    }

    public Result addSubCategory(Integer predecessor_cat_id, String name){
        return currBranch.addSubCategory(predecessor_cat_id, name);
    }

    public Result removeCategory(Integer category_id){
        return currBranch.removeCategory(category_id);
    }

    public Result editCategoryName(Integer category_id, String name){
        return currBranch.editCategoryName(category_id, name);
    }
    //endregion

    //region General Products
    public Result addGeneralProduct(Integer category_id, String manufacture, String name, Float supplier_price, Float retail_price,
                                    Integer min_quantity, Integer catalogID, Integer gpID, Integer supplier_id, String supplier_category, Float weight){
        return currBranch.addGeneralProduct(category_id, manufacture, name, supplier_price, retail_price, min_quantity, catalogID, gpID, supplier_id, supplier_category , weight);
    }
    //    public Result removeGeneralProduct(Integer category_id, Integer gpID) {
//        return currBranch.removeGeneralProduct(category_id, gpID);
//    }
    public Result editGeneralProductName(Integer gpID, String new_name){
        return currBranch.editGeneralProductName(gpID, new_name);
    }
    public Result editGeneralProductSupplierPrice(Integer gpID, Float new_supplier_price, Integer supplier_id){
        return currBranch.editGeneralProductSupplierPrice(gpID, new_supplier_price, supplier_id);
    }
    public Result editGeneralProductRetailPrice(Integer gpID, Float new_retail_price){
        return currBranch.editGeneralProductRetailPrice(gpID, new_retail_price);

    }
    public Result editGeneralProductQuantity(Integer gpID, Integer new_quantity){
        return currBranch.editGeneralProductQuantity(gpID, new_quantity);
    }
    public Result editGeneralProductMinQuantity(Integer gpID, Integer new_min_quantity){
        return currBranch.editGeneralProductMinQuantity(gpID, new_min_quantity);
    }
    //endregion

    //region Specific Products
    public Result addSpecificProduct(Integer gpID, Date expiration_date, Integer quantity){
        return currBranch.addSpecificProduct(gpID, expiration_date, quantity);
    }
    public Result removeSpecificProduct(Integer specific_product_id){
        return currBranch.removeSpecificProduct(specific_product_id);
    }
    public Result markAsFlaw(Integer specific_product_id){
        return currBranch.markAsFlaw(specific_product_id);
    }
    public Result moveLocation(Integer specific_product_id){
        return currBranch.moveLocation(specific_product_id);
    }
    //endregion

    //region Reports
    public Result<Report> makeReportByGeneralProduct(Integer gpID, String type){
        return currBranch.makeReportByGeneralProduct(gpID, type);
    }
    public Result<Report> makeReportByCategory(Integer category_id, String type){
        return currBranch.makeReportByCategory(category_id, type);
    }
    //endregion

    //region Sales
    public Result addSaleByGeneralProduct(Integer gpID, String stype, Float amount){
        return currBranch.addSaleByGeneralProduct(gpID, stype, amount);
    }
    public Result addSaleByGeneralProduct(Integer gpID, String stype, Float amount, Date start, Date end){
        return currBranch.addSaleByGeneralProduct(gpID, stype, amount, start, end);
    }
    public Result addSaleByCategory(Integer category_id, String stype, Float amount){
        return currBranch.addSaleByCategory(category_id, stype, amount);
    }
    public Result addSaleByCategory(Integer category_id, String stype, Float amount, Date start, Date end){
        return currBranch.addSaleByCategory(category_id, stype, amount, start, end);
    }
    public Result removeSale(Integer sale_id){
        return currBranch.removeSale(sale_id);
    }
    public Result<List<Sale>> CheckSalesStatus(){
        return currBranch.CheckSalesStatus();
    }
    //endregion

    //region Debug tools
    public String mapAllCategories(){
        return currBranch.mapAllCategories();
    }
    public String mapAllGeneralProducts(){
        return currBranch.mapAllGeneralProducts();
    }
    public String mapAllSales(){
        return currBranch.mapAllSales();
    }
    public String mapAllSuppliers() {
        return supplierController.toString();
    }
    //endregion

    //endregion

    //region Supplier Module
    public Result addContract(Integer supplier_id , LinkedList<String> categories){
        Result<SupplierCard> result = supplierController.getSupplierCardByID(supplier_id);
        if(!result.isOK()) {
            return result;
        }
        return currBranch.addContract(result.getData() , categories);
    }
    public Result removeContract(Integer supplier_id){
        Result<SupplierCard> result = supplierController.getSupplierCardByID(supplier_id);
        if(!result.isOK()) {
            return result;
        }
        return currBranch.removeContract(result.getData());
    }
    public Result addProductToContract(Integer supplierID, Integer catalogID, Integer gpID, Float supplier_price, String supplier_category){
        Result result = isExistSupplier(supplierID);
        if (!result.isOK()){        //No supplier found with ID
            return result;
        }
        return currBranch.addProductToContract(supplierID, catalogID, gpID, supplier_price, supplier_category);
    }
    public Result removeProductFromContract(Integer supplierID, Integer gpID){
        return currBranch.removeProductFromContract(supplierID, gpID);
    }
    public Result addCategory(Integer supplierID, String category){
        return currBranch.addCategory(supplierID, category);
    }
    public Result removeCategory (Integer supplierID, String category){
        return currBranch.removeCategory(supplierID, category);
    }
    //endregion

    //region Cost Engineering
    public Result addProductToCostEng(Integer supid, Integer catalogid, Integer minQuantity, Float price) {
        return currBranch.addProductToCostEng(supid , catalogid , minQuantity , price);
    }
    public Result removeProductCostEng(Integer supid, Integer catalogid2delete) {
        return currBranch.removeProductCostEng(supid, catalogid2delete);
    }
    public Result updateMinQuantity(Integer supid, Integer catalogid, Integer minQuantity) {
        return currBranch.updateMinQuantity(supid , catalogid , minQuantity);
    }
    public Result updatePriceAfterSale(Integer supid, Integer catalogid, Float price) {
        return currBranch.updatePriceAfterSale(supid , catalogid , price);
    }
    public Result<LinkedList<CatalogProduct>> getAllSupplierProducts(Integer supId) {
        return currBranch.getAllSupplierProducts(supId);
    }
    public Result addCostEng(Integer supid) {
        return currBranch.addCostEng(supid);
    }
    public Result removeCostEng(Integer supid) {
        return currBranch.removeCostEng(supid);
    }
    //endregion

    //region Orders

    public Result acceptOrder (Integer orderID){
        return currBranch.acceptOrder(orderID);
    }

    public Result createPeriodicOrder(Integer supplierID , LinkedList<Pair<Integer, Integer>> productsAndQuantity , Integer day){
        return currBranch.createPeriodicOrder(supplierID, productsAndQuantity,day);
    }

    public Result<String> createOutOfStockOrder(Report report){

        return currBranch.createOutOfStockOrder(report);
    }

    public Result removePeriodicOrder(Integer orderId){
        return currBranch.removePeriodicOrder((orderId));
    }

    public Result addProductToPeriodicOrder(Integer orderId , Integer gpID , Integer quantity){
        return currBranch.addProductToPeriodicOrder(orderId,gpID,quantity);
    }

    public Result updateProductQuantityInPeriodicOrder(Integer orderId , Integer gpID , Integer newQuantity){
        return currBranch.updateProductQuantityInPeriodicOrder(orderId,gpID,newQuantity);
    }

    public Result removeProductFromPeriodicOrder(Integer orderId , Integer gpID){
        return currBranch.removeProductFromPeriodicOrder(orderId , gpID);
    }

    public Result updateSupplierToPeriodicOrder (Integer orderID, Integer supplierID){
        return currBranch.updateSupplierToPeriodicOrder(orderID,supplierID);
    }

    public Result<LinkedList<String>> displayAllOrders(){
        return currBranch.displayAllOrders();
    }

    public Result<LinkedList<String>> displayAllSupplierOrders(Integer supId){
        return currBranch.displayAllSupplierOrders(supId);
    }

    public Result<LinkedList<String>> issuePeriodicOrder(){
        LinkedList<String> allPeriodicOrdersFromAllBranches = new LinkedList<>();
        for (Integer branchid : branches.keySet()) {
            Branch branch;
            if(currBranch==null || !branchid.equals(currBranch.getBranchId())){
                mapper.clearCache();
                branch=mapper.find_Branch(branchid);
                if (currBranch==null){currBranch=branch;}
            }
            else{
                branch=currBranch;
            }
            LinkedList<String> periodicOrderFromBranch = branch.issuePeriodicOrder().getData();
            if(periodicOrderFromBranch!=null){ // the list can be null only if for this branch there are no periodic orders to send at this day
                allPeriodicOrdersFromAllBranches.addAll(periodicOrderFromBranch);
            }
        }

        if(allPeriodicOrdersFromAllBranches.size()>0){
            return new Result<>(true,allPeriodicOrdersFromAllBranches, String.format("All periodic orders with %d as their delivery day had been sent to order", system_curr_date.getDay()+1));
        }
        return new Result<>(false,null, String.format("There are no periodic orders to be sent today (%d)",system_curr_date.getDay()+1));
    }

    public Result<LinkedList<String>> getAllwaitingOrders( ){
        LinkedList<String> orderToDisplay = new LinkedList<>();
        for(Integer branch_id:branches.keySet()){
            Branch branch;
            if(currBranch==null || !branch_id.equals(currBranch.getBranchId()))
            {
                mapper.clearCache();
                branch = mapper.find_Branch(branch_id);
                if (currBranch==null){currBranch=branch;}
            }
            else{branch=currBranch;}
            if(branch!=null){
                Result<LinkedList<String>> result = branch.getAllwaitingOrders();
                if (result.isOK()){
                    orderToDisplay.addAll(result.getData());
                }
            }
        }
        return new Result<LinkedList<String>>(true, orderToDisplay, "List of all the order awaiting to be accepted today\n");
    }

    //endregion

    //region Getters & Setters
    public HashMap<Integer, String> getBranches() {
        return branches;
    }

    public Branch getCurrBranch() {
        return currBranch;
    }
    //endregion

    //region Utilities

    /**
     * Get next available ID for next new Branch
     * @return ID for next Branch
     */
    private Integer getNextId() {
        Integer next = next_id;
        this.next_id++;
        mapper.writeID("branch",next_id);
        return next;
    }
    private boolean checkBranchExists(Integer branch_id){
        return branches.containsKey(branch_id);
    }

    public Result<LinkedList<String>> simulateNextDay(){
        Result result;
        Calendar cal = Calendar.getInstance();
        cal.setTime(system_curr_date);
        cal.add(Calendar.DATE, 1);
        system_curr_date = cal.getTime();
        //after changing the day - check if there are periodic orders to send
        Result<LinkedList<String>> awatingtobeaccepted = getAllwaitingOrders();
        Result<LinkedList<String>> periodicorders = issuePeriodicOrder();
        LinkedList<String> allOrdersToDisplay = new LinkedList<>();
        //can be empty list - the false in Result need to be assign only if there was a problem in one of the fields
        if (awatingtobeaccepted.isOK()) {
            allOrdersToDisplay.addAll(awatingtobeaccepted.getData());
        }
        if(periodicorders.isOK()) {
            allOrdersToDisplay.addAll(periodicorders.getData());
        }
        result = new Result<>(true, allOrdersToDisplay,"Orders that has to be accepted and delivered today");
        if(!awatingtobeaccepted.isOK() && !periodicorders.isOK()){
            result = new Result<>(false, null,"There are no orders waiting to be accepted or delivered today");
        }
        return result;
    }
    public static void clearDB(){
        Mapper.getInstance().clearDatabase();
    }

    @Override
    public String toString() {
        String toReturn = "";
        for (Integer id : branches.keySet()){
            toReturn = toReturn.concat(String.format("ID:%d Name:%s\n",id,branches.get(id)));
        }
        return toReturn;
    }

    public void loadID() {
        this.next_id=next_id = mapper.loadID("branch");
    }

    //endregion

    //region Transport Module
    public String getAllTransportsDetails() {
        return currBranch.getAllTransportsDetails();
    }

    public String getAllTrucksDetails() {
        return currBranch.getAllTrucksDetails();
    }

    public boolean deleteTruck(int truckToDelete) {
        return currBranch.deleteTruck(truckToDelete);
    }

    public boolean createTruck(String license_plate, String model, float netWeight, float maxWeight, String drivers_license) {
        return currBranch.createTruck(license_plate, model, netWeight, maxWeight, drivers_license);
    }

    public String getPendingOrdersDetails() {
        return currBranch.getPendingOrdersDetails();
    }

    public boolean isOrderIdInPendingOrders(int orderID) {
        return currBranch.isOrderIdInPendingOrders(orderID);
    }

    public String BookTransportForPendingOrders(int orderID) {
        return currBranch.BookTransportForPendingOrders(orderID);
    }
    //endregion

    //region Employee Module
    private static final boolean morning=true;
    private static final boolean night=false;

    public void setTransportModule(TransportModule transportModule) {
        currBranch.setTransportModule(transportModule);
    }

    public String chooseDriverForTransport(Date date, boolean shift, String drivers_license) {
        return currBranch.chooseDriverForTransport(date,shift,drivers_license);
    }

    public String getDriverName(String driverId) {
        return currBranch.getDriverName(driverId);
    }

    public boolean StorageManInShift(Date d, boolean b) {
        return currBranch.StorageManInShift(d,b);
    }

    public boolean DriversAvailability(Date date, boolean shift) {
        return currBranch.DriversAvailability(date,shift);
    }

    public void removeDriverFromTransport(Date d, boolean shift, String driverId) {
        currBranch.removeDriverFromTransport(d,shift,driverId);
    }



    public String removeShift(Date date,boolean timeOfDay){
        return currBranch.removeShift(date,timeOfDay);
    }
    public String createShift(Date date, boolean timeOfDay)
    {
        return currBranch.createShift(date,timeOfDay);
    }
    public String editShift(Date date, boolean timeOfDay)
    {
        return currBranch.editShift(date, timeOfDay);
    }
    public ModelShift getCurrentEditedModelShift()
    {
        return currBranch.getCurrentEditedModelShift();
    }
    public void cancelShift(){currBranch.cancelShift();}
    public String submitShift(){
        return currBranch.submitShift();
    }
    public String removePositionFromShift(String pos){
        return currBranch.removePositionFromShift(pos);
    }
    public String addPositionToShift(String pos,int quantity)
    {
        return currBranch.addPositionToShift(pos, quantity);

    }
    public String addWorkerToPositionInShift(String pos,String id){
        return currBranch.addWorkerToPositionInShift(pos, id);
    }
    public String removeWorkerToPositionInShift(String pos,String id)
    {
        return currBranch.removeWorkerToPositionInShift(pos, id);
    }
    public String addAvailableWorker(Date date, boolean partOfDay, String id){
        return currBranch.addAvailableWorker(date, partOfDay, id);
    }
    public String removeAvailableWorker(Date date,boolean partOfDay,String id) {

        return currBranch.removeAvailableWorker(date, partOfDay, id);
    }
    public List<ModelShift> getWeeklyShifts(Date date)
    {
        return currBranch.getWeeklyShifts(date);
    }
    public String removeWorkerFromRoster(String id){
        return currBranch.removeWorkerFromRoster(id);

    }
    public List<ModelWorker> displayWorkers()
    {
        return currBranch.displayWorkers();
    }

    public ModelWorker displaySingleWorker(String id)
    {
        return currBranch.displaySingleWorker(id);
    }

    public String editName(String newName,String id)
    {
        return currBranch.editName(newName,id);
    }
    public String editSalary(double newSalary,String id)
    {
        return currBranch.editSalary(newSalary,id);
    }
    public String addPosition(String pos,String id)
    {
        return currBranch.addPosition(pos,id);
    }
    public String removePosition(String pos,String id)
    {
        return currBranch.removePosition(pos,id);
    }
    public String addWorker(String name, double salary, Date startDate,List<String>positions)
    {
        return currBranch.addWorker(name,salary,startDate,positions);
    }
    public String addDriver(String name, double salary, Date startDate,String license)
    {
        return currBranch.addDriver(name,salary,startDate,license);
    }
    public String removeWorker(String id)
    {
        return currBranch.removeWorker(id);
    }


    //endregion

}
