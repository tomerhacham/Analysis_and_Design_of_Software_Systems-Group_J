package bussines_layer.supplier_module;
import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.Report;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;
import sun.awt.image.ImageWatched;

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

        public void addProductToContract(Integer supplierID, CatalogProduct product){
            contractController.addProductToContract(supplierID, product);
        }

        public void removeProductFromContract(Integer supplierID, CatalogProduct product){
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

        public LinkedList<CatalogProduct> getAllSupplierProducts(int supId) {
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

        public void generateOrdersFromReport(Report report){

            HashMap<Contract , LinkedList<Pair<CatalogProduct , Integer>>> productsForEachSupplier = new HashMap<>();

            for (GeneralProduct product: report.getProducts() ) {
                Pair<Contract , Integer> supplierAndPrice =contractController.getBestSupplierForProduct(product.getProductID() , product.quantityToOrder());
                Contract contract = supplierAndPrice.getKey();

                if(contract != null){
                    if(productsForEachSupplier.containsKey(contract)){
                        productsForEachSupplier.get(contract).add( new Pair(product.getSupplierCatalogProduct(contract.getSupplierID()) , supplierAndPrice.getValue()));
                    }
                    else{
                        LinkedList<Pair<CatalogProduct , Integer>> productsAndPrice = new LinkedList<>();
                        productsAndPrice.add(new Pair(product.getSupplierCatalogProduct(contract.getSupplierID()) , supplierAndPrice.getValue()));
                        productsForEachSupplier.put(contract , productsAndPrice);
                    }
                }
                else{
                    //sz_Result("There is no supplier with this product ");//TODO Result
                }
            }

            List<GeneralProduct> reportProducts = report.getProducts();

            //for each supplier(contract) get its catalog product and check the quantity needed throw the General Product
            for (Contract contract: productsForEachSupplier.keySet()) {
                int orderid = ordersController.createOrder(contract.getSupplierID() , OrderType.UpdateStockOrder);//(contract.getSupplierID() , productsForEachSupplier.get(contract));

                LinkedList<Pair<CatalogProduct , Integer>> cpPrice = productsForEachSupplier.get(contract);

                for (Pair<CatalogProduct , Integer> pair : cpPrice){
                    for (GeneralProduct gp:reportProducts ) {
                        if(gp.getCatalogID(contract.getSupplierID())== pair.getKey().getCatalogID() ){
                            ordersController.addProductToOrder(orderid , pair.getKey() , gp.quantityToOrder() , pair.getValue());
                        }
                    }
                }
            }
        }

        //return the order id
        public int createOrder(Integer supplierID) {
            return ordersController.createOrder(supplierID , OrderType.UpdateStockOrder);
        }

        //a periodic order is for one supplier
        public int createPeriodicOrder(Integer supplierID) {
            return ordersController.createOrder(supplierID , OrderType.PeriodicOrder);
        }

        public void addProductToPeriodicOrder(Integer orderId , CatalogProduct product , Integer quantity){

            Order order = ordersController.getOrder(orderId);
            float price = contractController.findContract(order.getSupplierID()).getProductPrice(product.getGpID());
            ordersController.addProductToOrder(orderId , product , quantity , (int)price);
        }

        public void removeProductFromPeriodicOrder(Integer orderId , CatalogProduct product){

            Order order = ordersController.getOrder(orderId);
            ordersController.removeFromOrder(orderId , product);
        }

        public void updateProductQuantityInPeriodicOrder(Integer orderId , CatalogProduct product , Integer newQuantity){

            Order order = ordersController.getOrder(orderId);
            ordersController.updateProductQuantityInOrder(orderId , product , newQuantity);
        }

        public Double getTotalAmountLastOrder() {
            return  ordersController.getTotalAmountLastOrder();
        }

        public void removeFromOrder(int orderid , CatalogProduct product) {
            ordersController.removeFromOrder(orderid , product);
        }

        public void removeOrder() {
            ordersController.removeOrder();
        }

        //TODO - it will be only for periodic orders
    //    public void updateProductQuantity(int productID, int quantity) {
    //        ordersController.updateProductQuantity(productID,quantity);
    //    }

        public LinkedList<String> displayAllOrders(){
            return ordersController.displayAllOrders();
        }

        public LinkedList<String> displayAllSupplierOrders(int supId) {
            return ordersController.displayAllSupplierOrders(supId);
        }

     /*   public GeneralProduct createNewProduct(int productID, String name, int price, String producer, String category, int catalogid) {
            return ordersController.createNewProduct(productID, name, price, producer, category, catalogid);
        }*/





    //#endregion



}



