package bussines_layer;

import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import javafx.util.Pair;

import java.util.*;

public class BranchController {

    private HashMap<Integer, Branch> branches;      //<Id,Branch>
    private SupplierController supplierController;
    private Branch curr;        //current branch active
    private Integer next_id;    //next id available for new branch
    public static Date system_curr_date;

    public BranchController (){
        branches= new HashMap<>();
        supplierController = SupplierController.getInstance();
        curr = null;
        next_id = 1;
        system_curr_date = new Date();
    }

    //region Supplier Controller

    public Result createSupplierCard (String supplierName, String address, String email, String phoneNumber, int id, String bankAccountNum, String payment, LinkedList<String> contactsName, supplierType type) {
        return supplierController.createSupplierCard(supplierName , address , email , phoneNumber , id , bankAccountNum , payment , contactsName, type);
    }

    public Result ChangeSupplierName(Integer supid, String supplierName) {
        return supplierController.ChangeSupplierName(supid , supplierName);
    }

    public Result ChangeAddress(Integer supid, String address) {
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

    public Result ChangeSupplierType(Integer supid, supplierType type) {
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
    public Result createNewBranch(String name){
        Branch toAdd = new Branch(getNextId(), name);
        branches.put(toAdd.getBranchId(), toAdd);
        return new Result<>(true, toAdd, String.format("New branch (ID: %d) created successfully", toAdd.getBranchId()));
    }

    public Result switchBranch(Integer branch_id){
        if (checkBranchExists(branch_id)){
            curr = branches.get(branch_id);
            return new Result<>(true, curr, String.format("Switched to Branch %d successfully", curr.getBranchId()));
        }
        return new Result<>(false, null, String.format("Branch with ID %d not found", branch_id));
    }

    public Result removeBranch(Integer branch_id){
        if (curr.getBranchId().equals(branch_id)){
            return new Result<>(false, null, String.format("Can not delete current branch (ID: %d), switch and try again.", branch_id));
        }
        if (!checkBranchExists(branch_id)){
            return new Result<>(false, null, String.format("Branch with ID %d not found", branch_id));
        }
        branches.remove(branch_id);
        return new Result<>(true, branch_id, String.format("Branch (ID: %d) removed successfully", branch_id));
    }

    public Result editName(Integer branch_id, String newName){
        if (checkBranchExists(branch_id)){
            Branch b = branches.get(branch_id);
            b.setName(newName);
            return new Result<>(true, b, String.format("Branch (ID: %d) name changed successfully to: %s", branch_id, newName));
        }
        return new Result<>(false, null, String.format("Branch with ID %d not found", branch_id));
    }

    //endregion

    //region Inventory Module
    //region Categories
    public Result addMainCategory(String name){
        return curr.addMainCategory(name);
    }

    public Result addSubCategory(Integer predecessor_cat_id, String name){
        return curr.addSubCategory(predecessor_cat_id, name);
    }

    public Result removeCategory(Integer category_id){
        return curr.removeCategory(category_id);
    }

    public Result editCategoryName(Integer category_id, String name){
        return curr.editCategoryName(category_id, name);
    }
    //endregion

    //region General Products
    public Result addGeneralProduct(Integer category_id, String manufacture, String name, Float supplier_price, Float retail_price,
                                    Integer min_quantity, Integer catalogID, Integer gpID, Integer supplier_id, String supplier_category){
        return curr.addGeneralProduct(category_id, manufacture, name, supplier_price, retail_price, min_quantity, catalogID, gpID, supplier_id, supplier_category);
    }
    public Result removeGeneralProduct(Integer category_id, Integer gpID) {
        return curr.removeGeneralProduct(category_id, gpID);
    }
    public Result editGeneralProductName(Integer gpID, String new_name){
        return curr.editGeneralProductName(gpID, new_name);
    }
    public Result editGeneralProductSupplierPrice(Integer gpID, Float new_supplier_price, Integer supplier_id){
        return curr.editGeneralProductSupplierPrice(gpID, new_supplier_price, supplier_id);
    }
    public Result editGeneralProductRetailPrice(Integer gpID, Float new_retail_price){
        return curr.editGeneralProductRetailPrice(gpID, new_retail_price);

    }
    public Result editGeneralProductQuantity(Integer gpID, Integer new_quantity){
        return curr.editGeneralProductQuantity(gpID, new_quantity);
    }
    public Result editGeneralProductMinQuantity(Integer gpID, Integer new_min_quantity){
        return curr.editGeneralProductMinQuantity(gpID, new_min_quantity);
    }
    //endregion

    //region Specific Products
    public Result addSpecificProduct(Integer gpID, Date expiration_date, Integer quantity){
        return curr.addSpecificProduct(gpID, expiration_date, quantity);
    }
    public Result removeSpecificProduct(Integer specific_product_id){
        return curr.removeSpecificProduct(specific_product_id);
    }
    public Result markAsFlaw(Integer specific_product_id){
        return curr.markAsFlaw(specific_product_id);
    }
    public Result moveLocation(Integer specific_product_id){
        return curr.moveLocation(specific_product_id);
    }
    //endregion

    //region Sales
    public Result addSaleByGeneralProduct(Integer gpID, String stype, Float amount){
        return curr.addSaleByGeneralProduct(gpID, stype, amount);
    }
    public Result addSaleByGeneralProduct(Integer gpID, String stype, Float amount, Date start, Date end){
        return curr.addSaleByGeneralProduct(gpID, stype, amount, start, end);
    }
    public Result addSaleByCategory(Integer category_id, String stype, Float amount){
        return curr.addSaleByCategory(category_id, stype, amount);
    }
    public Result addSaleByCategory(Integer category_id, String stype, Float amount, Date start, Date end){
        return curr.addSaleByCategory(category_id, stype, amount, start, end);
    }
    public Result removeSale(Integer sale_id){
        return curr.removeSale(sale_id);
    }
    public Result CheckSalesStatus(){
        return curr.CheckSalesStatus();
    }
    //endregion

    //region Debug tools
    public String mapAllCategories(){
        return curr.mapAllCategories();
    }
    public String mapAllGeneralProducts(){
        return curr.mapAllGeneralProducts();
    }
    public String mapAllSales(){
        return curr.mapAllSales();
    }
    //endregion

    //endregion

    //region Supplier Module
    public Result addContract(SupplierCard supplier){
        return curr.addContract(supplier);
    }
    public Result removeContract(SupplierCard supplier){
        return curr.removeContract(supplier);
    }
    public Result addProductToContract(Integer supplierID, Integer catalogID, Integer gpID, Float supplier_price, Integer supplier_id, String supplier_category , String name){
        return curr.addProductToContract(supplierID, catalogID, gpID, supplier_price, supplier_id, supplier_category, name);
    }
    public Result removeProductFromContract(Integer supplierID, Integer gpID){
        return curr.removeProductFromContract(supplierID, gpID);
    }
    public Result addCategory(Integer supplierID, String category){
        return curr.addCategory(supplierID, category);
    }
    public Result removeCategory (Integer supplierID, String category){
        return curr.removeCategory(supplierID, category);
    }
    //endregion

    //region Cost Engineering
    public Result addProductToCostEng(Integer supid, Integer catalogid, Integer minQuantity, Float price) {
        return curr.addProductToCostEng(supid , catalogid , minQuantity , price);
    }
    public Result removeProductCostEng(Integer supid, Integer catalogid2delete) {
        return curr.removeProductCostEng(supid, catalogid2delete);
    }
    public Result updateMinQuantity(Integer supid, Integer catalogid, Integer minQuantity) {
        return curr.updateMinQuantity(supid , catalogid , minQuantity);
    }
    public Result updatePriceAfterSale(Integer supid, Integer catalogid, Float price) {
        return curr.updatePriceAfterSale(supid , catalogid , price);
    }
    public Result<LinkedList<CatalogProduct>> getAllSupplierProducts(Integer supId) {
        return curr.getAllSupplierProducts(supId);
    }
    public Result addCostEng(Integer supid) {
        return curr.addCostEng(supid);
    }
    public Result removeCostEng(Integer supid) {
        return curr.removeCostEng(supid);
    }
    //endregion

    //region Orders

    public Result acceptOrder (Integer orderID){
        return curr.acceptOrder(orderID);
    }

    public Result makeOutOfStockReportByCategory(Integer categoryID , String type){
        return curr.makeOutOfStockReportByCategory(categoryID,type);
    }

    public Result makeOutOfStockReportByGeneralProduct(Integer gpID , String type){
        return curr.makeOutOfStockReportByGeneralProduct(gpID, type);
    }

    public Result createPeriodicOrder(Integer supplierID , LinkedList<Pair<GeneralProduct, Integer>> productsAndQuantity , Integer date){
        return curr.createPeriodicOrder(supplierID, productsAndQuantity,date);
    }

    public Result removePeriodicOrder(Integer orderId){
        return curr.removePeriodicOrder((orderId));
    }

    public Result<Float> addProductToPeriodicOrder(Integer orderId , CatalogProduct product , Integer quantity){
        return curr.addProductToPeriodicOrder(orderId,product,quantity);
    }

    public Result updateProductQuantityInPeriodicOrder(Integer orderId , CatalogProduct product , Integer newQuantity){
        return curr.updateProductQuantityInPeriodicOrder(orderId,product,newQuantity);
    }

    public Result removeProductFromPeriodicOrder(Integer orderId , CatalogProduct product){
        return curr.removeProductFromPeriodicOrder(orderId , product);
    }

    public Result updateSupplierToPeriodicOrder (Integer orderID, Integer supplierID){
        return curr.updateSupplierToPeriodicOrder(orderID,supplierID);
    }

    public Result<LinkedList<String>> displayAllOrders(){
        return curr.displayAllOrders();
    }

    public Result<LinkedList<String>> displayAllSupplierOrders(Integer supId){
        return curr.displayAllSupplierOrders(supId);
    }

    public Result<LinkedList<String>> issuePeriodicOrder(){
        LinkedList<String> allPeriodicOrdersFromAllBranches = new LinkedList<>();
        for (Integer branchid : branches.keySet()) {
            LinkedList<String> periodicOrderFromBranch = branches.get(branchid).issuePeriodicOrder().getData();
            if(periodicOrderFromBranch!=null){ // the list can be null only if for this branch there are no periodic orders to send at this day
                for (String periodicOrder :periodicOrderFromBranch) {
                    allPeriodicOrdersFromAllBranches.add(periodicOrder);
                }
            }
        }

        if(allPeriodicOrdersFromAllBranches.size()>0){
            return new Result(true,allPeriodicOrdersFromAllBranches, String.format("All periodic orders with %d as their delivery day had been sent to order", system_curr_date.getDay()));
        }
        return new Result(false,null, String.format("There are no periodic orders with %d as their delivery day ",system_curr_date.getDay()));
    }

    //endregion

    //region Getters
    public HashMap<Integer, Branch> getBranches() {
        return branches;
    }

    public Branch getCurr() {
        return curr;
    }
    //endregion

    //region Utilities

    /**
     * Get next available ID for next new Branch
     * @return ID for next Branch
     */
    private Integer getNextId() {
        return next_id++;
    }
    private boolean checkBranchExists(Integer branch_id){
        return branches.containsKey(branch_id);
    }
    private void simulateNextDay(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(system_curr_date);
        cal.add(Calendar.DATE, 1);
        system_curr_date = cal.getTime();

        //after changing the day - check if there are periodic orders to send
        issuePeriodicOrder();
    }
    //endregion
}
