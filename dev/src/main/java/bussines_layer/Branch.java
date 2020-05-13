package bussines_layer;

import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.Inventory;
import bussines_layer.inventory_module.Report;
import bussines_layer.supplier_module.SupplierModule;

import java.util.Date;
import java.util.LinkedList;

public class Branch {
    //fields:
    private Integer branch_id;
    private Inventory inventory;
    private SupplierModule supplierModule;

    //Constructor
    public Branch(Integer branch_id) {
        this.branch_id = branch_id;
        inventory = new Inventory();
        supplierModule = SupplierModule.getInstance(branch_id);
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
            //TODO check where GP will be created
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
    public Result addProductToCostEng(Integer supid, Integer catalogid, Integer minQuantity, int price) {
        return supplierModule.addProductToCostEng(supid , catalogid , minQuantity , price);
    }
    public Result removeProductCostEng(Integer supid, Integer catalogid2delete) {
        return supplierModule.removeProductCostEng(supid, catalogid2delete);
    }
    public Result updateMinQuantity(Integer supid, Integer catalogid, Integer minQuantity) {
        return supplierModule.updateMinQuantity(supid , catalogid , minQuantity);
    }
    public Result updatePriceAfterSale(Integer supid, Integer catalogid, Integer price) {
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
    public Result generateOrderFromReport(Report report){
        return supplierModule.generateOrdersFromReport(report);
    }
    //endregion

    //endregion
}


