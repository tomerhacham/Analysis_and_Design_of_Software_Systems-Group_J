package bussines_layer;

import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.Report;
import bussines_layer.inventory_module.Sale;
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
        mapper=Mapper.getInstance();
        branches= new HashMap<>();
        supplierController = SupplierController.getInstance();
        currBranch = null;
        if(!external_initialize){next_id = mapper.loadID("branch");}
        system_curr_date = new Date();
    }

    //region Supplier Controller

    public Result createSupplierCard (String supplierName, String address, String email, String phoneNumber, Integer id, String bankAccountNum, String payment, LinkedList<String> contactsName, String type) {
        return supplierController.createSupplierCard(supplierName , address , email , phoneNumber , id , bankAccountNum , payment , contactsName, type);
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
        Branch toAdd = new Branch(getNextId(), name);
        branches.put(toAdd.getBranchId(), name);
        mapper.create(toAdd);
        return new Result<>(true, toAdd, String.format("New branch (ID: %d) created successfully", toAdd.getBranchId()));
    }

    public Result switchBranch(Integer branch_id){
        if (checkBranchExists(branch_id)){
            mapper.clearCache();
            currBranch = mapper.find_Branch(branch_id);
            currBranch.loadData();
            return new Result<>(true, currBranch, String.format("Switched to Branch %d successfully", currBranch.getBranchId()));
        }
        return new Result<>(false, null, String.format("Branch with ID %d not found", branch_id));
    }

    public Result removeBranch(Integer branch_id){
        if (currBranch.getBranchId().equals(branch_id)){
            return new Result<>(false, null, String.format("Can not delete current branch (ID: %d), switch and try again.", branch_id));
        }
        if (!checkBranchExists(branch_id)){
            return new Result<>(false, null, String.format("Branch with ID %d not found", branch_id));
        }
        branches.remove(branch_id);
        mapper.delete(mapper.find_Branch(branch_id));
        return new Result<>(true, branch_id, String.format("Branch (ID: %d) removed successfully", branch_id));
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
                                    Integer min_quantity, Integer catalogID, Integer gpID, Integer supplier_id, String supplier_category){
        return currBranch.addGeneralProduct(category_id, manufacture, name, supplier_price, retail_price, min_quantity, catalogID, gpID, supplier_id, supplier_category);
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

    public Result createPeriodicOrder(Integer supplierID , LinkedList<Pair<Integer, Integer>> productsAndQuantity , Integer date){
        return currBranch.createPeriodicOrder(supplierID, productsAndQuantity,date);
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
            if(!branchid.equals(currBranch.getBranchId())){
                mapper.clearCache();
                branch=mapper.find_Branch(branchid);
                branch.loadData();
            }
            else{branch=currBranch;}
            LinkedList<String> periodicOrderFromBranch = branch.issuePeriodicOrder().getData();
            if(periodicOrderFromBranch!=null){ // the list can be null only if for this branch there are no periodic orders to send at this day
                allPeriodicOrdersFromAllBranches.addAll(periodicOrderFromBranch);
            }
        }

        if(allPeriodicOrdersFromAllBranches.size()>0){
            return new Result<>(true,allPeriodicOrdersFromAllBranches, String.format("All periodic orders with %d as their delivery day had been sent to order", system_curr_date.getDay()));
        }
        return new Result<>(false,null, String.format("There are no periodic orders with %d as their delivery day ",system_curr_date.getDay()));
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
    public Result<LinkedList<String>> simulateNextDay(Integer numOfDays){
        String msg = String.format("Old date: %s. ", system_curr_date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(system_curr_date);
        cal.add(Calendar.DATE, numOfDays);
        system_curr_date = cal.getTime();
        msg=msg.concat(String.format("New date: %s", system_curr_date));
        //after changing the day - check if there are periodic orders to send
        return issuePeriodicOrder();
    }
    public void clearDB(){
        mapper.clearDatabase();
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
}
