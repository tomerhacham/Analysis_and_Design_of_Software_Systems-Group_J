package bussines_layer.supplier_module;
import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.GeneralProduct;

import java.util.LinkedList;

/**
 * Singleton FacadeController.
 * Represent the Interface layer of the system.
 *
 * No functionality.
 * Passing data from IO to SupplierController/ OrdersController
 * according to the functionality.
 *
 */

//Singleton
public class SupplierModule {

    // static variable single_instance of type Singleton
    private static SupplierModule instance = null;
    private Integer branchId;
    private ContractController contractController = new ContractController(branchId);
    private OrdersController ordersController = OrdersController.getInstance();

    private SupplierModule(Integer branchId){ this.branchId = branchId; }

    // static method to create instance of Singleton class
    public SupplierModule getInstance(Integer branchId)
    {
       if (instance == null)
            instance = new SupplierModule(branchId);

        return instance;
    }

//#region Contract Controller

//#region Contract

    public void addContract(SupplierCard supplier){
        contractController.addContract(supplier);
    }

    public void removeContract(SupplierCard supplier){
        contractController.removeContract(supplier);
    }

    public void addProductToContract(Integer supplierID, GeneralProduct product){
        contractController.addProductToContract(supplierID, product);
    }

    public void removeProductFromContract(Integer supplierID, GeneralProduct product){
        contractController.removeProductFromContract(supplierID, product);
    }

    public void addCategory (Integer supplierID, String category){
        contractController.addCategory(supplierID, category);
    }

    public void removeCategory (Integer supplierID, String category){
        contractController.removeCategory(supplierID, category);
    }


//#endregion

//#region CostEngineering

    public void addProductToCostEng(int supid, int catalogid, int minQuantity, int price) {
        contractController.addProductToCostEng(supid , catalogid , minQuantity , price);
    }

    public void removeProductCostEng(int supid, int catalogid2delete) {
        contractController.removeProductFromCostEng(supid , catalogid2delete);
    }

    public void updateMinQuantity(int supid, int catalogid, int minQuantity) {
        contractController.updateMinQuantity(supid , catalogid , minQuantity);
    }

    public void updatePriceAfterSale(int supid, int catalogid, int price) {
        contractController.updatePriceAfterSale(supid , catalogid , price);
    }

    public LinkedList<GeneralProduct> getAllSupplierProducts(int supId) {
        return contractController.getAllSupplierProducts(supId);
    }

    public void addCostEng(int supid) {
        contractController.addCostEngineering(supid);
    }

    public void removeCostEng(int supid) {
        contractController.removeCostEngineering(supid);
    }
//#endregion

//#endregion


//#region Order Controller

    public void createOrder() {
        ordersController.createOrder();
    }

    public void addProductToOrder(int supID, int productID , int quantity) {
        ordersController.addProductToOrder(supID, productID, quantity);
    }

  /*  public HashMap<GeneralProduct, Integer> endOrder() {
        return ordersController.endOrder();
    }*/

    public Double getTotalAmountLastOrder() {
        return  ordersController.getTotalAmountLastOrder();
    }

    public void removeFromOrder(int productID , int supid) {
        ordersController.removeFromOrder(productID , supid);
    }

    public void removeOrder() {
        ordersController.removeOrder();
    }

    public void updateProductQuantity(int productID, int quantity) {
        ordersController.updateProductQuantity(productID,quantity);
    }

    public LinkedList<String> displayAllOrders(){
        return ordersController.displayAllOrders();
    }

    public LinkedList<String> displayOrderBySupplier(int supId) {
        return ordersController.displayOrderBySupplier(supId);
    }

 /*   public GeneralProduct createNewProduct(int productID, String name, int price, String producer, String category, int catalogid) {
        return ordersController.createNewProduct(productID, name, price, producer, category, catalogid);
    }*/




//#endregion

}



