package bussines_layer;

import java.util.LinkedList;

/**
 * Class SuppliersCard.
 * Holds all the Information about a Supplier.
 * Contains instance of the classes Contract and CostEngineering.
 *
 * Functionality that related to Supplier information.
 *
 */
enum supplierType {byOrder , periodic , selfDelivery;}

public class SupplierCard {

    private String SupplierName;
    private String Address;
    private String Email;
    private String PhoneNumber;
    private int id;
    private String BankAccountNum;
    private String Payment;
    private LinkedList<String> ContactsName;
    private int numOfContract;
    private supplierType type;

    //private CostEngineering costEngineering;

    public SupplierCard(String SupplierName , String Address , String Email , String PhoneNumber ,
                        int id , String BankAccountNum , String Payment , LinkedList<String> ContactsName, supplierType type){

        this.SupplierName = SupplierName;
        this.Address = Address;
        this.Email = Email;
        this.PhoneNumber = PhoneNumber;
        this.id = id;
        this.BankAccountNum = BankAccountNum;
        this.Payment = Payment;
        this.ContactsName = ContactsName;
        this.numOfContract =0;
        this.type = type;

        //this.contract = contract;
        //costEngineering = null;
    }

//#region getters_setters

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getBankAccountNum() {
        return BankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        BankAccountNum = bankAccountNum;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public LinkedList<String> getContactsName() {
        return ContactsName;
    }

    public void setContactsName(LinkedList<String> contactsName) {
        ContactsName = contactsName;
    }

    public supplierType getType () { return type; }

    public void setType (supplierType type) {this.type =type; }

    public Integer getNumOfContract () {return numOfContract;}

    public void incNumOfContract () {numOfContract++; }

    public void decNumOfContract () {numOfContract--; }


//#endregion

    public void deleteContactName(String contactName){

        int i = 0;
        boolean foundName = false;
        for (String cn:ContactsName) {
            if (cn.equals(contactName)){
                foundName = true;
                break;
            }
            else {
                i++;
            }
        }
        if(foundName){
            ContactsName.remove(i);
        }
        else{
            //sz_Result.setMsg("The Name Is Not In The Contact List");   //TODO - RESULT
        }
    }

    public void addContactName (String contactName){
        ContactsName.add(contactName);
    }





  /*  public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }*/


/*
    public CostEngineering getCostEngineering() {
        return costEngineering;
    }

    public void createCostEngineering() {
        this.costEngineering = new CostEngineering();
    }

    public void addProduct(Product product){
        contract.addProduct(product);
    }

    public void removeProduct(Integer prodCatalogid){
        contract.removeProduct(prodCatalogid);
    }

    public boolean checkCategory(String c){
        return contract.checkCategory(c);
    }

    public String toString(){

        String ans = "Supplier Name : "+getSupplierName()+'\n'+"Supplier Id : "+getId()+'\n';
        LinkedList<Product> plist = contract.getProducts();

        if(plist.isEmpty()){
            ans = ans +"No Products For This Supplier";
            return ans;
        }
        for (Product p:plist) {
            ans = ans +"Product Name : " +p.getName()+'\t'+'\t'+"Product Id : "+p.getGpID()+'\t'+'\t'+"Product Catalog Id : "+p.getCatalogID()+'\n';
        }
        return ans;
    }*/

}

