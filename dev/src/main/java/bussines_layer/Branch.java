package bussines_layer;

import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.Inventory;
import bussines_layer.inventory_module.Report;
import bussines_layer.supplier_module.SupplierModule;
import javafx.util.Pair;

import java.util.Date;
import java.util.LinkedList;
import java.util.HashMap;

public class Branch {
    //fields:
    private String name;
    private Integer branch_id;
    private Inventory inventory;
    private SupplierModule supplierModule;
    private Object HashMap;

    //Constructor
    public Branch(Integer branch_id, String name) {
        this.branch_id = branch_id;
        inventory = new Inventory();
        supplierModule = SupplierModule.getInstance(branch_id);
        this.name = name;
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
                                    Integer min_quantity, Integer catalogID, Integer gpID, Integer supplier_id, String supplier_category){
        return inventory.addGeneralProduct(category_id, manufacture, name, supplier_price, retail_price, min_quantity, catalogID, gpID, supplier_id, supplier_category);
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
    public Result CheckSalesStatus(){
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
    public Result addContract(SupplierCard supplier){
        return supplierModule.addContract(supplier);
    }
    public Result removeContract(SupplierCard supplier){
        return supplierModule.removeContract(supplier);
    }
    public Result addProductToContract(Integer supplierID, Integer catalogID, Integer gpID, Float supplier_price, Integer supplier_id, String supplier_category , String name){
        GeneralProduct gp = searchGeneralProductByGpID(gpID).getData();
        if (gp == null){
            return new Result<>(false, null, String.format("General Product with ID %d does not exist", gpID));
        }
        Result<CatalogProduct> res = gp.addCatalogProduct(catalogID, gpID, supplier_price, supplier_id, supplier_category, name);
        return supplierModule.addProductToContract(supplierID, res.getData());
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
        Result<HashMap<CatalogProduct, Integer>> productsResult = supplierModule.getProductsToAcceptOrder(orderID);
        if (!productsResult.isOK()) {return productsResult;}
        HashMap<CatalogProduct, Integer> products = productsResult.getData();

        return inventory.updateInventory(products);
    }

    public Result makeOutOfStockReportByCategory(Integer categoryID , String type){
        Result<Report> reportResult = inventory.makeReportByCategory(categoryID,type);
        if (!reportResult.isOK()) {return reportResult;}
        return supplierModule.createOutOfStockOrder(reportResult.getData());
    }

    public Result makeOutOfStockReportByGeneralProduct(Integer gpID , String type){
        Result<Report> reportResult = inventory.makeReportByGeneralProduct(gpID,type);
        if (!reportResult.isOK()) {return reportResult;}
        return supplierModule.createOutOfStockOrder(reportResult.getData());
    }

    public Result createPeriodicOrder(Integer supplierID , LinkedList<Pair<GeneralProduct , Integer>> productsAndQuantity , Integer date){
        return supplierModule.createPeriodicOrder(supplierID, productsAndQuantity, date);
    }

    public Result removePeriodicOrder(Integer orderId){
        return supplierModule.removePeriodicOrder((orderId));
    }

    public Result<Float> addProductToPeriodicOrder(Integer orderId , CatalogProduct product , Integer quantity){
        return supplierModule.addProductToPeriodicOrder(orderId,product,quantity);
    }
    public Result updateProductQuantityInPeriodicOrder(Integer orderId , CatalogProduct product , Integer newQuantity){
        return supplierModule.updateProductQuantityInPeriodicOrder(orderId,product,newQuantity);
    }

    public Result removeProductFromPeriodicOrder(Integer orderId , CatalogProduct product){
        return supplierModule.removeProductFromPeriodicOrder(orderId , product);
    }

    public Result updateSupplierToPeriodicOrder (Integer orderID, Integer supplierID){
        return supplierModule.updateSupplierToPeriodicOrder(orderID,supplierID);
    }

    public Result<LinkedList<String>> issuePeriodicOrder(){

        Result<LinkedList<String>> result = supplierModule.issuePeriodicOrder();

        if(!result.isOK()) {
            return result;
        }
        LinkedList<String> branchPeriodicOrders = new LinkedList<>();
        branchPeriodicOrders.add("-------------------Branch : "+name+"-------------------\n");
        branchPeriodicOrders.addAll(result.getData());
        branchPeriodicOrders.add("-------------------------------------------------------\n");
        return new Result(true,branchPeriodicOrders, String.format("All periodic orders with %d as their delivery day had been sent to order", BranchController.system_curr_date.getDay()));
    }

    public Result<LinkedList<String>> displayAllOrders(){
        return supplierModule.displayAllOrders();
    }

    public Result<LinkedList<String>> displayAllSupplierOrders(Integer supId){
        return supplierModule.displayAllSupplierOrders(supId);
    }

    //endregion

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
    //endregion
}


