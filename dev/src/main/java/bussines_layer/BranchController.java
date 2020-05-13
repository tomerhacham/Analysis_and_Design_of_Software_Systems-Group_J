package bussines_layer;

import bussines_layer.supplier_module.Contract;

import java.util.LinkedList;
import java.util.List;

public class BranchController {

    private List<Branch> branches;
    private SupplierController supplierController;

    public BranchController (){
        branches= new LinkedList<>();
        supplierController = SupplierController.getInstance();
    }

//#region Supplier Controller

    public Result createSupplierCard (String supplierName, String address, String email, String phoneNumber, int id, String bankAccountNum, String payment, LinkedList<String> contactsName, supplierType type) {
        return supplierController.createSupplierCard(supplierName , address , email , phoneNumber , id , bankAccountNum , payment , contactsName, type);
    }

    public Result ChangeSupplierName(int supid, String supplierName) {
        return supplierController.ChangeSupplierName(supid , supplierName);
    }

    public Result ChangeAddress(int supid, String address) {
        return supplierController.ChangeAddress(supid , address);
    }

    public Result ChangeEmail(int supid, String email) {
        return supplierController.ChangeEmail(supid , email);
    }

    public Result ChangePhoneNumber(int supid, String phoneNumber) {
        return supplierController.ChangePhoneNumber(supid , phoneNumber);
    }

    public Result ChangeBankAccount(int supid, String bankAccountNum) {
        return supplierController.ChangeBankAccount(supid , bankAccountNum);
    }

    public Result ChangePayment(int supid, String payment) {
        return supplierController.ChangePayment(supid , payment);
    }

    public Result AddContactName(int supid, LinkedList<String> contactsName) {
       return supplierController.AddContactName(supid , contactsName);
    }

    public Result DeleteContactName(int supid, String contactName) {
        return supplierController.DeleteContactName(supid , contactName);
    }

    public Result ChangeSupplierType(int supid, supplierType type) {
       return supplierController.ChangeSupplierKind(supid , type);
    }

    public Result isExistSupplier(int supid) {
        return supplierController.isExist(supid);
    }

    public Result printallsuppliers() {
        return supplierController.printallsuppliers();
    }


//#endregion

}
