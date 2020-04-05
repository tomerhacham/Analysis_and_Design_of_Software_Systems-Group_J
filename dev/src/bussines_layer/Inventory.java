package bussines_layer;

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
}
