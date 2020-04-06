package BusinessLayer;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;

//Singleton
public class FacadeController {

    // static variable single_instance of type Singleton
    private static FacadeController instance = null;
    public static SupplierController supplierController = SupplierController.getInstance();
    public static OrdersController ordersController = OrdersController.getInstance();

    private FacadeController(){}

    // static method to create instance of Singleton class
    public static FacadeController getInstance()
    {
        if (instance == null)
            instance = new FacadeController();

        return instance;
    }

//#region Supplier

     public void createSupplierCard(String supplierName, String address, String email, String phoneNumber, int id, int bankAccountNum, String payment, LinkedList<String> contactsName) {
        supplierController.createSupplierCard(supplierName , address , email , phoneNumber , id , bankAccountNum , payment , contactsName);
    }

    public void CreateContract(LinkedList<String> category, int supid, String kind) {
        supplierController.CreateContract(category , supid , kind);
    }

    public void addProductToContract(int supid, Product product) {
        supplierController.addProductToContract(supid ,product);
    }

    public void deleteSupplierCard(int supid) {
        supplierController.deleteSupplierCard(supid);
    }

    public void ChangeSupplierName(int supid, String supplierName) {
        supplierController.ChangeSupplierName(supid , supplierName);
    }

    public void ChangeAddress(int supid, String address) {
        supplierController.ChangeAddress(supid , address);
    }

    public void ChangeEmail(int supid, String email) {
        supplierController.ChangeEmail(supid , email);
    }

    public void ChangePhoneNumber(int supid, String phoneNumber) {
        supplierController.ChangePhoneNumber(supid , phoneNumber);
    }

    public void ChangeBankAccount(int supid, int bankAccountNum) {
        supplierController.ChangeBankAccount(supid , bankAccountNum);
    }

    public void ChangePayment(int supid, String payment) {
        supplierController.ChangePayment(supid , payment);
    }

    public void AddContactName(int supid, LinkedList<String> contactsName) {
        supplierController.AddContactName(supid , contactsName);
    }

    public void DeleteContactName(int supid, String contactName) {
        supplierController.DeleteContactName(supid , contactName);
    }

    public void addCategory(int supid, String category) {
        supplierController.addCategory(supid , category);
    }

    public void deleteCategory(int supid, String categoryToDelete) {
        supplierController.deleteCategory(supid , categoryToDelete);
    }

    public void deleteProduct(int supid, int catalogid) {
        supplierController.deleteProduct(supid , catalogid);
    }

    public void ChangeSupplierKind(int supid, String kind) {
        supplierController.ChangeSupplierKind(supid , kind);
    }

    public void removeProductCostEng(int supid, int catalogid2delete) {
        supplierController.removeProductCostEng(supid , catalogid2delete);
    }

    public void changeMinQuantity(int supid, int catalogid, int minQuantity) {
        supplierController.changeMinQuantity(supid , catalogid , minQuantity);
    }

    public void changenewPriceAfterSale(int supid, int catalogid, int price) {
        supplierController.changenewPriceAfterSale(supid , catalogid , price);
    }

    public void addProductToCostEng(int supid, int catalogid, int minQuantity, int price) {
        supplierController.addProductToCostEng(supid , catalogid , minQuantity , price);
    }

    public LinkedList<Product> getAllSupProducts(int supId) {
        return supplierController.getAllSupProducts(supId);
    }


//#endregion

    public void createOrder() {
        ordersController.createOrder();
    }

    public void addProductToOrder(int supID, int productID , int quantity) {
        ordersController.addProductToOrder(supID, productID, quantity);
    }


    public HashMap<Product , Integer> endOrder() {
        return ordersController.endOrder();
    }

    public int getTotalAmountLastOrder() {
        return  ordersController.getTotalAmountLastOrder();
    }

    public void removeFromOrder(int productID) {
        ordersController.removeFromOrder(productID);
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


    public void createCostEng(int supid) {
        supplierController.createCostEngineering(supid);
    }
}



