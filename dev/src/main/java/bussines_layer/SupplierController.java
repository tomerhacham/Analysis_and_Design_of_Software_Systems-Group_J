package bussines_layer;

import bussines_layer.enums.supplierType;
import data_access_layer.Mapper;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Singleton SupplierController.
 * Responsible of all the Suppliers in the system.
 * Holds documentation of all the SupplierCard.
 *
 * Functionality that related to Suppliers.
 *
 */

//Singleton
public class SupplierController {

    //fields
    // static variable single_instance of type Singleton
    private static SupplierController instance = null;
    private HashMap<Integer , SupplierCard> suppliers;   //supplierID --> supplierCard
    private Mapper mapper;

    //constructor
    private SupplierController(){
        this.mapper=Mapper.getInstance();
        suppliers = mapper.loadSuppliers();
    }

    // static method to create instance of Singleton class
    public static SupplierController getInstance()  {
        if (instance == null)
            instance = new SupplierController();

        return instance;
    }

    //region Create supplier

    /**
     * create new supplier card for the givven supplier
     * @param SupplierName - supplier name
     * @param Address - address of the supplier
     * @param Email -
     * @param PhoneNumber
     * @param id - unique (generated by the supplierController class)
     * @param BankAccountNum
     * @param Payment
     * @param ContactsName
     * @param typeString - enum;
     * @return
     */
    public Result createSupplierCard(String SupplierName , String Address , String Email , String PhoneNumber ,
                                     Integer id ,String BankAccountNum , String Payment , LinkedList<String> ContactsName, String typeString){
        supplierType type = convertStringToType(typeString);
        if (type == null){
            return new Result<>(false,id, String.format("Type: %s of supplier invalid",typeString));
        }
        SupplierCard supplierCard = new SupplierCard(SupplierName, Address, Email, PhoneNumber,
                id ,BankAccountNum ,Payment ,ContactsName, type);
        return addSupplierCardToList(supplierCard);
    }
    public Result createSupplierCard(String SupplierName , String Address , String Email , String PhoneNumber ,
                                     Integer id ,String BankAccountNum , String Payment , LinkedList<String> ContactsName, String typeString,Integer fix_day){
        supplierType type = convertStringToType(typeString);
        if (type == null){
            return new Result<>(false,id, String.format("Type: %s of supplier invalid",typeString));
        }
        SupplierCard supplierCard = new SupplierCard(SupplierName, Address, Email, PhoneNumber,
                id ,BankAccountNum ,Payment ,ContactsName, type,fix_day);
        return addSupplierCardToList(supplierCard);
    }

    /**
     * add new supplier to the list
     * @param supplierCard
     */
    private Result addSupplierCardToList(SupplierCard supplierCard){
        Result result;
        if (suppliers.containsKey(supplierCard.getId())){
            result=new Result<>(false,null, String.format("There is Already a supplier with the same ID %d",supplierCard.getId() ));
        }
        else{
            suppliers.put(supplierCard.getId() , supplierCard);
            result=new Result<>(true, supplierCard, String.format("Supplier %s has been Added", supplierCard));
            this.mapper.create(supplierCard);
        }
        return result;
    }
    //endregion

    //#region Edit supplier

    /**
     * set new name to a supplier
     * @param id - Integer, allocated by the supplierController
     * @param newName
     * @return
     */
    public Result ChangeSupplierName(Integer id , String newName){
        Result result;
        if (!isExist(id).isOK()){
            result=new Result<>(false,null, String.format("Could not find supplier with ID %d",id ));
        }
        else {
            SupplierCard sc = suppliers.get(id);
            sc.setSupplierName(newName);
            result=new Result<>(true,sc, String.format("Supplier %d has been edited", id) );
            mapper.update(sc);
        }
        return result;
    }

    /**
     * set new address to a supplier
     * @param id - Integer, allocated by the supplierController
     * @param newAddress
     * @return
     */
    public Result ChangeAddress(Integer id ,String newAddress){
        Result result;
        if ( !isExist(id).isOK()){
            result=new Result(false,null, String.format("Could not find supplier with ID %d",id ));
        }
        else {
            SupplierCard sc = suppliers.get(id);
            sc.setAddress(newAddress);
            result=new Result(true,sc, String.format("Supplier %d has been edited", id) );
            mapper.update(sc);
        }
        return result;
    }

    /**
     * set new email to a supplier
     * @param id - Integer, allocated by the supplierController
     * @param newEmail
     * @return
     */
    public Result ChangeEmail(Integer id , String newEmail){
        Result result;
        if ( !isExist(id).isOK()){
            result=new Result(false,null, String.format("Could not find supplier with ID %d",id ));
        }
        else {
            SupplierCard sc = suppliers.get(id);
            sc.setEmail(newEmail);
            result=new Result(true,sc, String.format("Supplier %d has been edited", id) );
            mapper.update(sc);
        }
        return result;
    }

    /**
     * set new phone number to a supplier
     * @param id - Integer, allocated by the supplierController
     * @param newPhoneNum
     * @return
     */
    public Result ChangePhoneNumber(Integer id, String newPhoneNum){
        Result result;
        if ( !isExist(id).isOK()){
            result=new Result(false,null, String.format("Could not find supplier with ID %d",id ));
        }
        else {
            SupplierCard sc = suppliers.get(id);
            sc.setPhoneNumber(newPhoneNum);
            result=new Result(true,sc, String.format("Supplier %d has been edited", id) );
            mapper.update(sc);
        }
        return result;
    }

    /**
     * set new bank account number to a supplier
     * @param id - Integer, allocated by the supplierController
     * @param newBankAccount
     * @return
     */
    public Result ChangeBankAccount(Integer id, String newBankAccount){
        Result result;
        if ( !isExist(id).isOK()){
            result=new Result(false,null, String.format("Could not find supplier with ID %d",id ));
        }
        else {
            SupplierCard sc = suppliers.get(id);
            sc.setBankAccountNum(newBankAccount);
            result=new Result(true,sc, String.format("Supplier %d has been edited", id) );
            mapper.update(sc);
        }
        return result;
    }

    /**
     * set new payment type to a supplier
     * @param id - Integer, allocated by the supplierController
     * @param newPayment
     * @return
     */
    public Result ChangePayment(Integer id ,String newPayment){
        Result result;
        if ( !isExist(id).isOK()){
            result=new Result(false,null, String.format("Could not find supplier with ID %d",id ));
        }
        else {
            SupplierCard sc = suppliers.get(id);
            sc.setPayment(newPayment);
            result=new Result(true,sc, String.format("Supplier %d has been edited", id) );
            mapper.update(sc);
        }
        return result;
    }

    /**
     * delete contact name
     * @param id - Integer, allocated by the supplierController
     * @param name2Delete
     * @return
     */
    public Result DeleteContactName(Integer id , String name2Delete){
        Result result=null;
        if ( !isExist(id).isOK()){
            result=new Result(false,null, String.format("Could not find supplier with ID %d",id ));
        }
        else {
            SupplierCard sc = suppliers.get(id);
            Result result1= sc.deleteContactName(name2Delete);
            if (result1.isOK()){mapper.delete_contact(name2Delete,sc); result=result1;}
        }
        return result;
    }

    /**
     * add contactName of the supplier
     * @param id - id of the supplier
     * @param contactsName
     */
    public Result AddContactName(Integer id ,LinkedList<String> contactsName){
        Result result;
        if ( ! isExist(id).isOK()){
            result=new Result(false,id, String.format("Could not find supplier ID:%d", id));
        }
        else{
            SupplierCard sc = suppliers.get(id);
            sc.setContactsName(contactsName);
            for (String contact:contactsName){mapper.create_contact(contact,sc);}
            result=new Result(true, sc, String.format("Contacts name's has added to supplier ID:%d", id));
        }
        return result;
    }

    /**
     * change supplier type
     * @param id
     * @param typeString - ENUM {byOrder , periodic , selfDelivery;}
     */
    public Result ChangeSupplierKind(Integer id ,String typeString){
        Result result;
        if ( ! isExist(id).isOK()){
            result=new Result<>(false,id, String.format("Could not find supplier ID:%d", id));
        }
        else {
            supplierType type = convertStringToType(typeString);
            if (type == null) {
                result = new Result<>(false, id, String.format("Type: %s of supplier invalid", typeString));
            } else {
                suppliers.get(id).setType(type);
                result = new Result<>(true, id, String.format("Supplier ID:%d type has been set to %s", id, type.name()));
                mapper.update(suppliers.get(id));
            }
        }
        return result;
    }

    public Result ChangeSupplierKind(Integer id ,String typeString, Integer fix_day){
        Result result;
        if ( ! isExist(id).isOK()){
            result=new Result<>(false,id, String.format("Could not find supplier ID:%d", id));
        }
        else {
            supplierType type = convertStringToType(typeString);
            if (type == null) {
                result = new Result<>(false, id, String.format("Type: %s of supplier invalid", typeString));
            } else {
                suppliers.get(id).setType(type, fix_day);
                result = new Result<>(true, id, String.format("Supplier ID:%d type has been set to %s", id, type.name()));
                mapper.update(suppliers.get(id));
            }
        }
        return result;
    }

//#endregion

    //region Utilities
    /**
     * return instance of supplier card by its ID
     * @param id
     * @return
     */
    public Result<SupplierCard> getSupplierCardByID (Integer id){
        Result<SupplierCard> result;
        if ( !isExist(id).isOK()){
            result= new Result<>(false,null, String.format("There is not supplier with ID %d",id ));
        }
        else{
            SupplierCard supplier =suppliers.get(id);
            result = new Result<>(true, supplier, String.format("Supplier %s has been found",supplier ));
        }
        return result;
    }

    /**
     * concat string which represent each entity of supplierCard in the system
     * @return
     */
    public Result printallsuppliers() {
        Result result;
        String msg="";
        LinkedList<String> toreturn = new LinkedList<String>();

        if (suppliers.isEmpty()){
            msg=msg.concat("There is no suppliers in the system\n");
            result=new Result<>(false,null,msg);
        }
        else {
            for (SupplierCard supplier : suppliers.values()) {
                msg = msg.concat(supplier + "\n");
            }
            result=new Result<>(true, msg, msg);
        }
        return result;
    }

    /**
     * checks if the supplier exist
     * @param id
     * @return
     */
    public Result isExist (Integer id){
        Result result;
        if ( ! suppliers.containsKey(id)){
            result=new Result<>(false,id, String.format("There's no supplier with ID: %d", id));
        }
        else{
            result=new Result<>(true, id,"Supplier found");
        }
        return result;
    }

    public HashMap<Integer, SupplierCard> getSuppliers() {
        return suppliers;
    }

    private supplierType convertStringToType(String t){
        supplierType type = null;
        if (t.equals("by Order") || t.equals("by order")){
            type = supplierType.byOrder;
        } else if (t.equals("fix days") || t.equals("fix days")){
            type = supplierType.fix_days;
        } else if (t.equals("self Delivery") || t.equals("self delivery")){
            type = supplierType.selfDelivery;
        } else {
            return null;
        }
        return type;
    }

    @Override
    public String toString(){
        String toReturn = "Suppliers:\n";
        for (SupplierCard supplier : suppliers.values()){
            toReturn=toReturn.concat(String.format("ID: %d\tName: %s\n", supplier.getId(),supplier.getSupplierName()));
        }
        return toReturn;
    }

    //endregion
}