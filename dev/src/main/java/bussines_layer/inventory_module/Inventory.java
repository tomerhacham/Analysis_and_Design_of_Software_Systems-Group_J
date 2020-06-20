package bussines_layer.inventory_module;

import bussines_layer.BranchController;
import bussines_layer.Result;
import bussines_layer.enums.ReportType;
import bussines_layer.enums.discountType;

import java.util.*;

public class Inventory {
    //fields
    private Integer branch_id;
    private CategoryController categoryController;
    private ProductController productController;
    private ReportController reportController;
    private SaleController saleController;


    //Constructors
    public Inventory(Integer branchId) {
        this.branch_id=branchId;
        this.categoryController=new CategoryController(branchId);
        this.productController = new ProductController(branchId);
        this.reportController = new ReportController(branchId);
        this.saleController=new SaleController(branchId);
    }
    //region Methods

    public Result updateInventory(HashMap<CatalogProduct, Integer> product_received){
        String error_msg="";
        for (CatalogProduct catalogProduct: product_received.keySet()) {
            GeneralProduct generalProduct = productController.searchGeneralProductByGpID(catalogProduct.getGpID());
            Date expiration_date = simulateExpirationDate();
            Result res = addSpecificProduct(generalProduct.getGpID(),expiration_date,product_received.get(catalogProduct));
            error_msg=error_msg.concat(res.getMessage().concat("\n"));
            if (!res.isOK()){
                return new Result<>(false, null, error_msg);
            }
        }
        String success = String.format("Order accepted. %d general products quantities were successfully updated in inventory", product_received.keySet().size());
        return new Result<>(true,product_received.keySet(), success);

    }

    //region Categories Management
    public Result addMainCategory(String name){
        return categoryController.addNewCategory(name);
    }
    public Result addSubCategory(Integer predecessor_cat_id, String name){return categoryController.addNewCategory(predecessor_cat_id, name);}
    public Result removeCategory(Integer category_id){
        return categoryController.removeCategory(category_id);
    }
    public Result editCategoryName(Integer category_id, String name){
        return categoryController.editCategoryName(category_id,name);
    }
    //endregion

    //region General Product Management
    public Result addGeneralProduct(Integer category_id, String manufacture, String name, Float supplier_price, Float retail_price,
                                    Integer min_quantity, Integer catalogID, Integer gpID, Integer supplier_id, String supplier_category , Float weight){
        Category category = categoryController.searchCategorybyId(category_id);
        Result result;
        if (category!=null){
            result= productController.addGeneralProduct(category,manufacture, name, supplier_price, retail_price,
                    min_quantity, catalogID, gpID, supplier_id, supplier_category , weight);
        }
        else{
            result=new Result<>(false,category_id,"Could not find category");
        }
        return result;
    }

    public Result removeGeneralProduct(Integer category_id, Integer gpID)
    {
        Category category = categoryController.searchCategorybyId(category_id);
        Result result;
        if (category!=null){
            result= productController.removeGeneralProduct(category,gpID);
        }
        else{
            result=new Result<Integer>(false,category_id,"Could not find category");
        }
        return result;    }
    public Result editGeneralProductName(Integer gpID, String new_name){
        return productController.editGeneralProductName(gpID, new_name);
    }
    public Result editGeneralProductSupplierPrice(Integer gpID, Float new_supplier_price, Integer supplier_id){
        return productController.editGeneralProductSupplierPrice(gpID, new_supplier_price, supplier_id);
    }
    public Result editGeneralProductRetailPrice(Integer gpID, Float new_retail_price){
        return productController.editGeneralProductRetailPrice(gpID, new_retail_price);

    }
    public Result editGeneralProductQuantity(Integer gpID, Integer new_quantity){
        return productController.editGeneralProductQuantity(gpID, new_quantity);
    }
    public Result editGeneralProductMinQuantity(Integer gpID, Integer new_min_quantity){
        return productController.editGeneralProductMinQuantity(gpID, new_min_quantity);

    }
    public Result<GeneralProduct> searchGeneralProductByGpID(Integer gpID){
        GeneralProduct gp = productController.searchGeneralProductByGpID(gpID);
        if (gp == null){
            return new Result<>(false, null, String.format("General Product with ID %d not found", gpID));
        }
        return new Result<>(true, gp, String.format("General Product with ID %d found", gpID));
    }
    //endregion

    //region Specific Products Management
    public Result addSpecificProduct(Integer gpID, Date expiration_date,Integer quantity){
        return productController.addSpecificProduct(gpID, expiration_date, quantity);
    }
    public Result removeSpecificProduct(Integer specific_product_id){
        return productController.removeSpecificProduct(specific_product_id);
    }
    public Result markAsFlaw(Integer specific_product_id){
        return productController.markAsFlaw(specific_product_id);
    }
    public Result moveLocation(Integer specific_product_id){
        return productController.moveLocation(specific_product_id);
    }
    //endregion

    //region Report Management
    public Result<Report> makeReportByGeneralProduct(Integer gpID, String type){

        GeneralProduct generalProduct = productController.searchGeneralProductByGpID(gpID);
        if(generalProduct!=null) {
            List<GeneralProduct> dummy_list = new LinkedList<>();
            dummy_list.add(generalProduct);
            return reportController.makeReport(dummy_list, convertStringToReportType(type));
        }
        else{
            return new Result<>(false,null, String.format("No such general product ID:%d", gpID));
        }
    }
    public Result<Report> makeReportByCategory(Integer category_id, String type){
        Category category;
        if(category_id==0){
            category=categoryController.superCategory();
        }
        else{
            category = categoryController.searchCategorybyId(category_id);
        }
        if(category!=null) {
            List<GeneralProduct> all_general_products = category.getAllGeneralProduct();
            if (type.equals("outofstock")) {
                List<GeneralProduct> toReport = new LinkedList<>();
                for (GeneralProduct gp : all_general_products) {
                    if (gp.getQuantity() < gp.getMinQuantity()) {
                        toReport.add(gp);
                    }
                }
                return reportController.makeReport(toReport, convertStringToReportType(type));
            }
            return reportController.makeReport(all_general_products, convertStringToReportType(type));
        }
        else{return new Result<>(false,null, String.format("No such category ID:%d", category_id));}
    }
    private ReportType convertStringToReportType(String stype){
        switch(stype){
            case ("outofstock"):
                return ReportType.OutOfStock;
            case ("instock"):
                return ReportType.InStock;
            case ("dne"):
                return ReportType.ExpiredDamaged;
            default:
                return null;
        }
    }
    //endregion

    //region Sales Management
    public Result addSaleByGeneralProduct(Integer gpID, String stype, Float amount){
        GeneralProduct generalProduct = productController.searchGeneralProductByGpID(gpID);
        return saleController.addSale(generalProduct,convertStringToDiscountType(stype),amount);
    }
    public Result addSaleByGeneralProduct(Integer gpID, String stype, Float amount, Date start, Date end){
        GeneralProduct generalProduct = productController.searchGeneralProductByGpID(gpID);
        return saleController.addSale(generalProduct,convertStringToDiscountType(stype),amount,start,end);
    }
    public Result addSaleByCategory(Integer category_id, String stype, Float amount){
        Category _category = categoryController.searchCategorybyId(category_id);
        return saleController.addSale(_category,convertStringToDiscountType(stype),amount);
    }
    public Result addSaleByCategory(Integer category_id, String stype, Float amount, Date start, Date end){
        Category _category = categoryController.searchCategorybyId(category_id);
        return saleController.addSale(_category,convertStringToDiscountType(stype),amount,start,end);
    }
    public Result removeSale(Integer sale_id){
        return saleController.removeSale(sale_id);
    }
    public Result<List<Sale>> CheckSalesStatus(){
        return saleController.CheckSalesStatus();
    }
    private discountType convertStringToDiscountType(String stype){
        switch(stype){
            case ("fix"):
                return discountType.fix;
            case ("percentage"):
                return discountType.precentage;
            default:
                return null;
        }
    }
    //endregion

    //region Debug tools
    public String mapAllCategories(){
        return categoryController.toString();
    }
    public String mapAllGeneralProducts(){
        return productController.toString();
    }
    public String mapAllSales(){
        return saleController.toString();
    }
    public Date simulateExpirationDate(){
        Random ran = new Random();
        Integer days = ran.nextInt(7)+1;
        Calendar cal = Calendar.getInstance();
        cal.setTime(BranchController.system_curr_date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    //endregion

    //endregion
}
