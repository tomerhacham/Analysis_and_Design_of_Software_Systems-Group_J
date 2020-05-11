package bussines_layer.inventory_module;

import bussines_layer.Result;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Inventory {
    //fields
    private CategoryController categoryController;
    private ProductController productController;
    private ReportController reportController;
    private SaleController saleController;

    //Constructors
    public Inventory() {
        this.categoryController=new CategoryController();
        this.productController = new ProductController();
        this.reportController = new ReportController();
        this.saleController=new SaleController();
    }

    //region Methods

    //region Categories Management
    public Result addMainCategory(String name){
        return categoryController.addNewCategory(name);
    }
    public Result addSubCategory(Integer predecessor_cat_id, String name){return categoryController.addNewCategory(predecessor_cat_id, name);}
    public Result removeCategory(Integer category_id){
        return categoryController.removeCategory(category_id);
    }
    public Result editCategoryname(Integer category_id,String name){
        return categoryController.editCategoryname(category_id,name);
    }
    //endregion

    //region General Product Management
    public Result addGeneralProduct(Integer category_id, String manufacture, String catalogID, String name, Float supplier_price, Float retail_price, Integer min_quantity)
    {
    Category category = categoryController.searchCategorybyId(category_id);
    Result result;
        if (category!=null){
            result= productController.addGeneralProduct(category,manufacture, catalogID, name, supplier_price, retail_price, min_quantity);
        }
        else{
            result=new Result<Integer>(false,category_id,"Could not find category");
        }
        return result;
    }

    public Result removeGeneralProduct(Integer category_id, String catalogID)
    {
        Category category = categoryController.searchCategorybyId(category_id);
        Result result;
        if (category!=null){
            result= productController.removeGeneralProduct(category,catalogID);
        }
        else{
            result=new Result<Integer>(false,category_id,"Could not find category");
        }
        return result;    }
    public Result editGeneralProduct_name(String catalogID,String new_name){
        return productController.editGeneralProduct_name(catalogID, new_name);
    }
    public Result editGeneralProduct_supplier_price(String catalogID, Float new_supplier_price){
        return productController.editGeneralProduct_supplier_price(catalogID, new_supplier_price);
    }
    public Result editGeneralProduct_retail_price(String catalogID, Float new_retail_price){
        return productController.editGeneralProduct_retail_price(catalogID, new_retail_price);

    }
    public Result editGeneralProduct_quantity(String catalogID, Integer new_quantity){
        return productController.editGeneralProduct_quantity(catalogID, new_quantity);
    }
    public Result editGeneralProduct_min_quantity(String catalogID, Integer new_min_quantity){
        return productController.editGeneralProduct_min_quantity(catalogID, new_min_quantity);

    }
    //endregion

    //region Specific Products Management
    public Result addSpecificProduct(String catalogID, Date expiration_date,Integer quantity){
        return productController.addSpecificProduct(catalogID, expiration_date, quantity);
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
    public Result makeReport(String catalogID,String stype){

        GeneralProduct generalProduct=productController.searchGeneralProductbyCatalogID(catalogID);
        List<GeneralProduct> dummy_list = new LinkedList<>();
        dummy_list.add(generalProduct);
        return reportController.makeReport(dummy_list, convertStringToReportType(stype));
    }
    public Result makeReport(Integer category_id,String stype){
        Category category;
        if(category_id==0){
            category=categoryController.superCategory();
        }
        else{
            category = categoryController.searchCategorybyId(category_id);
        }
        List general_products = category.getAllGeneralProduct();
        return reportController.makeReport(general_products, convertStringToReportType(stype));
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
    public Result addSale(String catalogID,String stype,Float amount){
        GeneralProduct generalProduct = productController.searchGeneralProductbyCatalogID(catalogID);
        return saleController.addSale(generalProduct,convertStringToDiscountType(stype),amount);
    }
    public Result addSale(String catalogID,String stype,Float amount, Date start, Date end){
        GeneralProduct generalProduct = productController.searchGeneralProductbyCatalogID(catalogID);
        return saleController.addSale(generalProduct,convertStringToDiscountType(stype),amount,start,end);
    }
    public Result addSale(Integer category_id,String stype,Float amount){
        Category _category = categoryController.searchCategorybyId(category_id);
        return saleController.addSale(_category,convertStringToDiscountType(stype),amount);
    }
    public Result addSale(Integer category_id,String stype,Float amount,Date start, Date end){
        Category _category = categoryController.searchCategorybyId(category_id);
        return saleController.addSale(_category,convertStringToDiscountType(stype),amount,start,end);
    }
    public Result removeSale(Integer sale_id){
        return saleController.removeSale(sale_id);
    }
    public Result CheckSalesStatus(){
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

    //endregion

    //endregion
}
