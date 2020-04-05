package bussines_layer;

import com.sun.org.apache.regexp.internal.REUtil;

public class Inventory {
    //fields
    CategoryController categoryController;
    ProductController productController;
    ReportController reportController;

    //Constructors
    public Inventory() {
        this.categoryController=new CategoryController(new GIDAllocator());
        this.productController = new ProductController(new PIDAllocator());
        this.reportController = new ReportController();
    }

    //region Methods

    //region Categories Management
    public Result addMainCategory(){
        //TODO:implemets
    }

    public Result addSubCategory(){
        //TODO:implemets
    }
    public Result removeCategory(){
        //TODO:implements
    }
    public Result editCategoryname(){
        //TODO:implemets
    }
    //endregion

    //region General Product Management
    public Result addGeneralProduct(){
        //TODO:implements
    }

    public Result removeGeneralProduct(){
        //TODO:implements
    }

    public Result editGeneralProduct_name(){
        //TODO:implemets
    }
    public Result editGeneralProduct_supplier_price(){
        //TODO:implemets
    }
    public Result editGeneralProduct_retail_price(){
        //TODO:implemets
    }
    public Result editGeneralProduct_quantity(){
        //TODO:implemets
    }
    public Result editGeneralProduct_min_quantity(){
        //TODO:implemets
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
