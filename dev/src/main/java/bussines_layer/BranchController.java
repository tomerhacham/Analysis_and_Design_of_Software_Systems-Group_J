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

    public void createSupplierCard (String supplierName, String address, String email, String phoneNumber, int id, String bankAccountNum, String payment, LinkedList<String> contactsName, supplierType type) {
        supplierController.createSupplierCard(supplierName , address , email , phoneNumber , id , bankAccountNum , payment , contactsName, type);
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

    public void ChangeBankAccount(int supid, String bankAccountNum) {
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

    public void ChangeSupplierType(int supid, supplierType type) {
        supplierController.ChangeSupplierKind(supid , type);
    }

    public boolean isExistSupplier(int supid) {
        return supplierController.isExist(supid);
    }

    public LinkedList<String> printallsuppliers() {
        return supplierController.printallsuppliers();
    }


//#endregion

}
