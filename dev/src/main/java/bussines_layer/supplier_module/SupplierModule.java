package bussines_layer.supplier_module;
import bussines_layer.Result;
import bussines_layer.SupplierCard;
import bussines_layer.inventory_module.CatalogProduct;
import bussines_layer.inventory_module.GeneralProduct;
import bussines_layer.inventory_module.Report;

import java.util.HashMap;
import java.util.LinkedList;
import javafx.util.Pair;

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
    public static SupplierModule getInstance(Integer branchId){
       if (instance == null)
            instance = new SupplierModule(branchId);

        return instance;
    }

    //region Contract Controller

    //region Contract

        public Result addContract(SupplierCard supplier , LinkedList<String> catagories){
            return contractController.addContract(supplier, catagories);
        }

        public Result removeContract(SupplierCard supplier){

            //before removing this suppliers contract from the branch , check first if there are any periodic orders with this suppliers id
            for (Order order : ordersController.getAllOrders()) {
                if((order.getType()==OrderType.PeriodicOrder) && (order.getSupplierID() == supplier.getId())){
                    ordersController.removePeriodicOrder(order.getOrderID());
                }
            }
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

        public Result addProductToCostEng(Integer supid, Integer catalogid, Integer minQuantity, Float price) {
            return contractController.addProductToCostEng(supid , catalogid , minQuantity , price);
        }

        public Result removeProductCostEng(Integer supid, Integer catalogid2delete) {
            return contractController.removeProductFromCostEng(supid , catalogid2delete);
        }

        public Result updateMinQuantity(Integer supid, Integer catalogid, Integer minQuantity) {
            return contractController.updateMinQuantity(supid , catalogid , minQuantity);
        }

        public Result updatePriceAfterSale(Integer supid, Integer catalogid, Float price) {
            return contractController.updatePriceAfterSale(supid , catalogid , price);
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

    public Result issueOrder (Order order){
          return ordersController.issueOrder(order);
    }

    public Result<HashMap<CatalogProduct, Integer>> getProductsToAcceptOrder(Integer orderID){
        return OrdersController.getInstance().getProductsToAcceptOrder(orderID);
    }

    public Result<LinkedList<String>> displayAllOrders(){
        return ordersController.displayAllOrders();
    }

    public Result<LinkedList<String>> displayAllSupplierOrders(Integer supId) {
        return ordersController.displayAllSupplierOrders(supId);
    }

    //region OutOfStockOrder

    public Result createOutOfStockOrder(Report report){

        //HashMap<SupplierCard , LinkedList<CatalogProduct , Price>>
        HashMap<SupplierCard , LinkedList<Pair<CatalogProduct , Float>>> productsForEachSupplier = new HashMap<>();

        for (GeneralProduct product: report.getProducts()) {
            //Pair<SupplierCard , Price>
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
            Integer orderid = ordersController.createOrder(supplierCard , OrderType.OutOfStockOrder).getData();//(contract.getSupplierID() , productsForEachSupplier.get(contract));

            LinkedList<Pair<CatalogProduct , Float>> cpPrice = productsForEachSupplier.get(supplierCard);

            for (Pair<CatalogProduct , Float> pair : cpPrice){
                for (GeneralProduct gp:report.getProducts() ) {
                    if(gp.getCatalogID(supplierCard.getId()).equals(pair.getKey().getCatalogID())){
                        ordersController.addProductToOrder(orderid , pair.getKey() , gp.quantityToOrder() , pair.getValue());
                    }
                }
            }

            Result<Order> resultOrder =ordersController.getOrder(orderid);
            if (!resultOrder.isOK()) { return new Result<>(false, null, String.format("Order %d does not exist", orderid));}
            issueOrder(resultOrder.getData());
        }
        return new Result<>(true, report, String.format("All orders had been generated from the report successfully: %s", report));
    }

    //endregion

    //region PeriodicOrder

    public Result createPeriodicOrder(Integer supplierID , LinkedList<Pair<GeneralProduct , Integer>> productsAndQuantity , Integer date){
        Result<Contract> contractResult = contractController.findContract(supplierID);
        if (!contractResult.isOK()){return new Result<>(false, null, String.format("Supplier %d does not exist", supplierID));}
        int orderID = ordersController.createPeriodicOrder(contractResult.getData().getSupplier()).getData();
        Result<Order> resultOrder = ordersController.getOrder(orderID);
        if (!resultOrder.isOK()){return new Result<>(false, null, String.format("Order %d does not exist", orderID));}
        for (Pair<GeneralProduct , Integer> pair : productsAndQuantity) {
            resultOrder.getData().addProduct(pair.getKey().getSupplierCatalogProduct( supplierID) , pair.getValue() , contractController.findContract(supplierID).getData().getProductPrice(pair.getKey().getGpID()).getData() );
        }
        return new Result<>(true, ordersController.getOrder(orderID), String.format("The periodic order has been generated from the product list successfully: %s", productsAndQuantity));
    }

    public Result removePeriodicOrder(Integer orderId) {
        return ordersController.removePeriodicOrder(orderId);
    }

    public Result addProductToPeriodicOrder(Integer orderId , GeneralProduct gp , Integer quantity){
        Order order = ordersController.getOrder(orderId).getData();
        if (order == null){return new Result<>(false, null, String.format("Order %d does not exist", orderId));}

        if(order.getType() != OrderType.PeriodicOrder){
            return new Result<>(false,null, String.format("The order %d is not a periodic order therefore can not be modified " , orderId));
        }
        CatalogProduct product = gp.getSupplierCatalogProduct(order.getSupplierID());
        Result<Float> price = contractController.findContract(order.getSupplierID()).getData().getProductPriceConsideringQuantity(product.getGpID() , quantity);
        if(price.getData() == null ){
            return price; // return the false result
        }

        if(order.getProductsAndQuantity().keySet().contains(product)){
            return new Result(false,product, String.format("The order %d already has this product in the order , it is only possible to update the quantity " , orderId));
        }
        ordersController.addProductToOrder(orderId , product , quantity , price.getData());
        return new Result<>(true,product, String.format("The product %s has been added to the order:%d", product.getName() , orderId));
    }

    public Result updateProductQuantityInPeriodicOrder(Integer orderId , GeneralProduct gp , Integer newQuantity){
        Order order = ordersController.getOrder(orderId).getData();
        if (order == null){return new Result<>(false, null, String.format("Order %d does not exist", orderId));}

        if(order.getType() != OrderType.PeriodicOrder){
            return new Result<>(false,order, String.format("The order %d is not a periodic order therefore can not be modified " , orderId));
        }
        CatalogProduct product = gp.getSupplierCatalogProduct(order.getSupplierID());
        Result<Float> price = contractController.findContract(order.getSupplierID()).getData().getProductPriceConsideringQuantity(product.getGpID() , newQuantity);

        if(price.getData() == null ){
            return price; // return the false result - if the product is not in the contract product list
        }
        return ordersController.updateProductQuantityInPeriodicOrder(orderId , product , newQuantity , price.getData());
    }

    public Result removeProductFromPeriodicOrder(Integer orderId , GeneralProduct gp){
        CatalogProduct product = gp.getSupplierCatalogProduct(orderId);
        return ordersController.removeProductFromPeriodicOrder(orderId , product);
    }

    public Result updateSupplierToPeriodicOrder (Integer orderID, Integer supplierID){
        //check if supplier exist
        if (!contractController.isExistSupplier(supplierID)) { return new Result<>(false, null, String.format("Supplier %d does not exist", supplierID)); }
        //check if order exist
        Result <Order> result = ordersController.getOrder(orderID);
        if (!result.isOK()) { return new Result<>(false, null, String.format("Order %d does not exist", orderID));}
        //check if supplier has all products
        Order periodicOrder = result.getData();
        Contract contract = contractController.findContract(supplierID).getData();
        Result<CatalogProduct> resultProduct;
        //getProductPriceConsideringQuantity
        for (CatalogProduct product : periodicOrder.getProductsAndQuantity().keySet()){
            resultProduct = contract.isProductExist(product);
            if (!resultProduct.isOK()){return new Result<>(false, null, String.format("Product %d does not exist in supplier products list", resultProduct.getData().getGpID()));}
        }
        //All product exist in product list, need to change the prices
        Result<Float> resultPrice;
        for (CatalogProduct product : periodicOrder.getProductsAndQuantity().keySet()) {
            resultPrice = contract.getProductPriceConsideringQuantity(product.getGpID(), periodicOrder.getProductsAndQuantity().get(product));
            periodicOrder.getProductsAndPrice().replace(product, resultPrice.getData());
        }

        periodicOrder.setSupplier(contract.getSupplier());
        return new Result<>(true , periodicOrder , String.format("Order %d updated successfully", orderID));
    }

    public Result<LinkedList<String>> issuePeriodicOrder(){
        return ordersController.issuePeriodicOrder();
    }

    //endregion


    //endregion



}



