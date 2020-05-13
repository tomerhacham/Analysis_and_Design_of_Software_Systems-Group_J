package bussines_layer.supplier_module;
import bussines_layer.Result;
import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.Report;

import java.util.Date;
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

        public Result createOutOfStockOrder(Report report){
            return prepareOutOfStockBeforeIssue(report.getProducts() , OrderType.OutOfStockOrder);
        }

        public Result createPeriodicOrder(LinkedList<Pair<GeneralProduct , Integer>> productsAndQuantity , Date date , int option){
            ordersController.createOrder()

        return preparePeriodicOrderBeforeIssue(periodicOrder.getProductsAndQuantity() , OrderType.OutOfStockOrder);
        }

        public Result preparePeriodicOrderBeforeIssue (HashMap<CatalogProduct, Integer> productsAndQuantity , OrderType orderType){

            //HashMap<SupplierCard , LinkedList<CatalogProduct , Price>>
            HashMap<SupplierCard , LinkedList<Pair<CatalogProduct , Float>>> productsForEachSupplier = new HashMap<>();

            for (CatalogProduct product: productsAndQuantity.keySet()) {
                //Pair<SupplierCard , Price> //TODO - change the function getBestSupplier in ContracController (return supplierCArd and not contract)
                Pair<SupplierCard, Float> supplierAndPrice = contractController.getBestSupplierForProduct(product.getGpID() , productsAndQuantity.get(product));
                SupplierCard supplierCard = supplierAndPrice.getKey();

                if(supplierCard != null){
                    if(productsForEachSupplier.containsKey(supplierCard)){
                        productsForEachSupplier.get(supplierCard).add( new Pair<>(product , supplierAndPrice.getValue()));
                    }
                    else{
                        LinkedList<Pair<CatalogProduct , Float>> productsAndPrice = new LinkedList<>();
                        productsAndPrice.add(new Pair<>(product, supplierAndPrice.getValue()));
                        productsForEachSupplier.put(supplierCard , productsAndPrice);
                    }
                }
                else{
                    return new Result<>(false, null, String.format("There is no supplier with product: %s", product));
                }
            }

            //for each supplier(contract) get its catalog product and check the quantity needed throw the General Product
            for (SupplierCard supplierCard: productsForEachSupplier.keySet()) {
                int orderid = ordersController.createOrder(supplierCard.getId() , orderType);//(contract.getSupplierID() , productsForEachSupplier.get(contract));

                LinkedList<Pair<CatalogProduct , Float>> cpPrice = productsForEachSupplier.get(supplierCard);

                for (Pair<CatalogProduct , Float> pair : cpPrice){
                    for (CatalogProduct catalogProduct :productsAndQuantity.keySet() ) {
                        ordersController.addProductToOrder(orderid, catalogProduct , productsAndQuantity.get(catalogProduct) , pair.getValue());
                    }
                }
            }
            return new Result<>(true, productsAndQuantity, String.format("Orders been generated from the product list successfully: %s", productsAndQuantity));
        }

        public Result prepareOutOfStockBeforeIssue(List<GeneralProduct> generalProductList , OrderType orderType){

            //HashMap<SupplierCard , LinkedList<CatalogProduct , Price>>
            HashMap<SupplierCard , LinkedList<Pair<CatalogProduct , Float>>> productsForEachSupplier = new HashMap<>();

            for (GeneralProduct product: generalProductList) {
                //Pair<SupplierCard , Price> //TODO - change the function getBestSupplier in ContracController (return supplierCArd and not contract)
                Pair<SupplierCard, Float> supplierAndPrice = contractController.getBestSupplierForProduct(product.getGpID() , product.quantityToOrder());
                SupplierCard supplierCard = supplierAndPrice.getKey();

                if(supplierCard != null){
                    if(productsForEachSupplier.containsKey(supplierCard)){
                        productsForEachSupplier.get(supplierCard).add( new Pair<>(product.getSupplierCatalogProduct(supplierCard.getId()) , supplierAndPrice.getValue()));
                    }
                    else{
                        LinkedList<Pair<CatalogProduct , Float>> productsAndPrice = new LinkedList<>();
                        productsAndPrice.add(new Pair<>(product.getSupplierCatalogProduct(supplierCard.getId()) , supplierAndPrice.getValue()));
                        productsForEachSupplier.put(supplierCard , productsAndPrice);
                    }
                }
                else{
                    return new Result<>(false, null, String.format("There is no supplier with product: %s", product));
                }
            }

            //for each supplier(contract) get its catalog product and check the quantity needed throw the General Product
            for (SupplierCard supplierCard: productsForEachSupplier.keySet()) {
                int orderid = ordersController.createOrder(supplierCard.getId() , orderType);//(contract.getSupplierID() , productsForEachSupplier.get(contract));

                LinkedList<Pair<CatalogProduct , Float>> cpPrice = productsForEachSupplier.get(supplierCard);

                for (Pair<CatalogProduct , Float> pair : cpPrice){
                    for (GeneralProduct gp:generalProductList ) {
                        if(gp.getCatalogID(supplierCard.getId()).equals(pair.getKey().getCatalogID())){
                            ordersController.addProductToOrder(orderid , pair.getKey() , gp.quantityToOrder() , pair.getValue());
                        }
                    }
                }
            }
            return new Result<>(true, generalProductList, String.format("Orders been generated from the product list successfully: %s", generalProductList));
        }


        public void addProductToPeriodicOrder(Integer orderId , CatalogProduct product , Integer quantity){

            Order order = ordersController.getOrder(orderId);
            Float price = contractController.findContract(order.getSupplierID()).getData().getProductPrice(product.getGpID()).getData();
            ordersController.addProductToOrder(orderId , product , quantity , price);
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



