package bussines_layer.supplier_module;
import bussines_layer.Result;
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
    public static SupplierModule getInstance(Integer branchId)
    {
       if (instance == null)
            instance = new SupplierModule(branchId);

        return instance;
    }

    //TODO Change functions' return value to Result

    //region Contract Controller

    //region Contract

        public Result addContract(SupplierCard supplier){
            return contractController.addContract(supplier);
        }

        public Result removeContract(SupplierCard supplier){
            return contractController.removeContract(supplier);
        }

        public Result addProductToContract(Integer supplierID, CatalogProduct product){
            return contractController.addProductToContract(supplierID, product);
        }

        public Result removeProductFromContract(Integer supplierID, CatalogProduct product){
            return contractController.removeProductFromContract(supplierID, product);
        }

        public Result addCategory (Integer supplierID, String category){
            return contractController.addCategory(supplierID, category);
        }

        public Result removeCategory (Integer supplierID, String category){
            return contractController.removeCategory(supplierID, category);
        }


    //endregion

    //region CostEngineering

        public Result addProductToCostEng(Integer supid, Integer catalogid, Integer minQuantity, Integer price) {
            return contractController.addProductToCostEng(supid , catalogid , minQuantity , price);
        }

        public Result removeProductCostEng(Integer supid, Integer catalogid2delete) {
            return contractController.removeProductFromCostEng(supid , catalogid2delete);
        }

        public Result updateMinQuantity(Integer supid, Integer catalogid, Integer minQuantity) {
            return contractController.updateMinQuantity(supid , catalogid , minQuantity);
        }

        public Result updatePriceAfterSale(Integer supid, Integer catalogid, Integer price) {
            contractController.updatePriceAfterSale(supid , catalogid , price);
        }

        public Result<LinkedList<CatalogProduct>> getAllSupplierProducts(Integer supId) {
            return contractController.getAllSupplierProducts(supId);
        }

        public Result addCostEng(Integer supid) {
            return contractController.addCostEngineering(supid);
        }

        public Result removeCostEng(Integer supid) {
            return contractController.removeCostEngineering(supid);
        }
    //endregion

    //endregion


    //region Order Controller

        public Result generateOrdersFromReport(Report report){

            HashMap<Contract , LinkedList<Pair<CatalogProduct , Integer>>> productsForEachSupplier = new HashMap<>();

            for (GeneralProduct product: report.getProducts() ) {
                Pair<Contract, Integer> supplierAndPrice = contractController.getBestSupplierForProduct(product.getGpID() , product.quantityToOrder());
                Contract contract = supplierAndPrice.getKey();

                if(contract != null){
                    if(productsForEachSupplier.containsKey(contract)){
                        productsForEachSupplier.get(contract).add( new Pair<>(product.getSupplierCatalogProduct(contract.getSupplierID()) , supplierAndPrice.getValue()));
                    }
                    else{
                        LinkedList<Pair<CatalogProduct , Integer>> productsAndPrice = new LinkedList<>();
                        productsAndPrice.add(new Pair<>(product.getSupplierCatalogProduct(contract.getSupplierID()) , supplierAndPrice.getValue()));
                        productsForEachSupplier.put(contract , productsAndPrice);
                    }
                }
                else{
                    return new Result<>(false, null, String.format("There is no supplier with product: %s", product));
                }
            }

            List<GeneralProduct> reportProducts = report.getProducts();

            //for each supplier(contract) get its catalog product and check the quantity needed throw the General Product
            for (Contract contract: productsForEachSupplier.keySet()) {
                int orderid = ordersController.createOrder(contract.getSupplierID() , OrderType.UpdateStockOrder);//(contract.getSupplierID() , productsForEachSupplier.get(contract));

                LinkedList<Pair<CatalogProduct , Integer>> cpPrice = productsForEachSupplier.get(contract);

                for (Pair<CatalogProduct , Integer> pair : cpPrice){
                    for (GeneralProduct gp:reportProducts ) {
                        if(gp.getCatalogID(contract.getSupplierID()).equals(pair.getKey().getCatalogID())){
                            ordersController.addProductToOrder(orderid , pair.getKey() , gp.quantityToOrder() , pair.getValue());
                        }
                    }
                }
            }
            return new Result<>(true, report, String.format("Orders been generated from report successfully: %s", report));
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





    //endregion



}



