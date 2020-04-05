package bussines_layer;

import com.sun.org.apache.regexp.internal.REUtil;

public class Inventory {
    //fields
    CategoryController categoryController;
    ProductController productController;
    ReportController reportController;

    //Constructors
    public Inventory() {
        this.categoryController=new CategoryController();
        this.productController = new ProductController();
        this.reportController = new ReportController();
    }

    //region Methods

    //region Categories Management
    public Result addMainCategory(String name){
        return categoryController.addNewCategory(name);
    }

    public Result addSubCategory(Integer predecessor_cat_id, String name){
        return categoryController.addNewCategory(predecessor_cat_id, name);
    }
    public Result removeCategory(Integer category_id){
        return categoryController.removeCategory(category_id);
    }
    public Result editCategoryname(Integer category_id,String name){
        return categoryController.editCategoryname(category_id,name);
    }
    //endregion

    //region General Product Management
    public Result addGeneralProduct(Integer category_id, String manufacture, String catalogID, String name, Float supplier_price, Float retail_price, Integer quantity, Integer min_quantity)
    {
    Category category = categoryController.searchCategorybyId(category_id);
    Result result;
        if (category!=null){
            result= productController.addGeneralProduct(category,manufacture, catalogID, name, supplier_price, retail_price, quantity, min_quantity);
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
    public Result addSpecificProduct(){
        //TODO:implemets
    }
    public Result removeSpecificProduct(){
        //TODO:implemets
    }
    public Result markAsFlaw(){
        //TODO:implemets
    }
    public Result moveLocation(){
        //TODO:implemets
    }
    //endregion

    //region Report Management
    public Result makeReport(){
        //TODO:implement
    }
    //endregion

    //region Sales Management
    public Result addSale(GeneralProduct generalProduct){
        //TODO:implemets
    }
    public Result addSale(Category category){
        //TODO:implemets
    }
    public Result removeSale(GeneralProduct generalProduct){
        //TODO:implement
    }
    public Result removeSale(Category category){
        //TODO:implement
    }

    //endregion

    //endregion
}
