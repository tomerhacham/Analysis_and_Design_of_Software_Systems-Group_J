package bussines_layer;

import bussines_layer.enums.supplierType;

import java.util.LinkedList;
/**
 * Class SuppliersCard.
 * Holds all the Information about a Supplier.
 * Contains instance of the classes Contract and CostEngineering.
 *
 * Functionality that related to Supplier information.
 *
 */
public class SupplierCard {

    private Integer id;
    private String SupplierName;
    private String Address;
    private String Email;
    private String PhoneNumber;
    private String BankAccountNum;
    private String Payment;
    private supplierType type;
    private Integer numOfContract;
    private LinkedList<String> ContactsName;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setNumOfContract(int numOfContract) {
        this.numOfContract = numOfContract;
    }

    public supplierType getType () { return type; }

    public void setType (supplierType type) {this.type =type; }

    public Integer getNumOfContract () {return numOfContract;}

    public void incNumOfContract () {numOfContract++; }

    public void decNumOfContract () {numOfContract--; }


//#endregion


    public Result deleteContactName(String contactName){
    Result result;
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
            result=new Result(true, this, String.format("Contact name has been removed from supplier %d", this.getId()));
        }
        else{
            result = new Result(false, this, String.format("name: %s does not exist in the contact list of supplier %d", contactName,this.getId()) );
        }
        return result;
    }

    public Result addContactName (String contactName){
        ContactsName.add(contactName);
        return new Result(true, this, String.format("%s has been added to contact list of supplier %d", contactName,this.getId()));
    }

    @Override
    public String toString() {
        return "" +
                "SupplierName:'" + SupplierName + '\'' +
                ", Address:'" + Address + '\'' +
                ", Email:'" + Email + '\'' +
                ", PhoneNumber:'" + PhoneNumber + '\'' +
                ", id:" + id +
                ", BankAccountNum:'" + BankAccountNum + '\'' +
                ", Payment:'" + Payment + '\'' +
                ", ContactsName:" + ContactsName +
                ", numOfContract:" + numOfContract +
                ", type=" + type;
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

