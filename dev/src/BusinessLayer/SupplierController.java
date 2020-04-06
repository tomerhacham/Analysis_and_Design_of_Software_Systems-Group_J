package BusinessLayer;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

//Singleton
public class SupplierController {

    //fields
    // static variable single_instance of type Singleton
    private static SupplierController instance = null;
    private HashMap<Integer ,SupplierCard> suppliers;


    //constructor
    private SupplierController(){
        suppliers = new HashMap<>();
    }

    // static method to create instance of Singleton class
    public static SupplierController getInstance()
    {
        if (instance == null)
            instance = new SupplierController();

        return instance;
    }

    //SUPPLIER CARD
    //create a new supplierCard
    public void createSupplierCard(String SupplierName , String Address , String Email , String PhoneNumber ,
                                   int id ,int BankAccountNum , String Payment , LinkedList<String> ContactsName){

        SupplierCard supplierCard = new SupplierCard(SupplierName, Address, Email, PhoneNumber,
                                        id ,BankAccountNum ,Payment ,ContactsName);

        addSupplierCardToList(supplierCard);
    }

    //add a new supplierCard to the linked list
    private void addSupplierCardToList(SupplierCard supplierCard){
        if (suppliers.containsKey(supplierCard.getId())){
            System.out.println("The SupplierCard Already Exists. You Can Choose To Change Your Supplier Card");
        }
        else{
            suppliers.putIfAbsent(supplierCard.getId() , supplierCard);
        }
    }

    //delete a supplierCard
    public void deleteSupplierCard(int id){
        suppliers.get(id).getContract().removeSupplier();
        suppliers.remove(id);

    }

    //update supplier name
    public void ChangeSupplierName(int id , String newName){
        SupplierCard sc = suppliers.get(id);
        sc.setSupplierName(newName);
    }

    //update address
    public void ChangeAddress(int id ,String newAddress){
        SupplierCard sc = suppliers.get(id);
        sc.setAddress(newAddress);
    }

    //update email
    public void ChangeEmail(int id , String newEmail){
        SupplierCard sc = suppliers.get(id);
        sc.setEmail(newEmail);
    }

    //update phone number
    public void ChangePhoneNumber(int id, String newPhoneNum){
        SupplierCard sc = suppliers.get(id);
        sc.setPhoneNumber(newPhoneNum);
    }

    //update bank account number
    public void ChangeBankAccount(int id, int newBankAccount){
        SupplierCard sc = suppliers.get(id);
        sc.setBankAccountNum(newBankAccount);
    }

    //update payment
    public void ChangePayment(int id ,String newPayment){
        SupplierCard sc = suppliers.get(id);
        sc.setPayment(newPayment);
    }

    //delete a contact name
    public void DeleteContactName(int id , String name2Delete){
        SupplierCard sc = suppliers.get(id);
        sc.deleteContactName(name2Delete);
    }

    //add a conatct name
    public void AddContactName(int id ,String newContactName){
        SupplierCard sc = suppliers.get(id);
        sc.addContactName(newContactName);
    }
    public void AddContactName(int id ,LinkedList<String> contactsName){
        SupplierCard sc = suppliers.get(id);
        sc.setContactsName(contactsName);
    }


    //UPDATE CONTRACT
    //create new contract
    public void CreateContract(LinkedList<String> category , int supId ,String kind ){
        Contract c = new Contract(category , supId , kind , suppliers.get(supId));
        suppliers.get(supId).setContract(c);
    }

    //change contract category
    public void addCategory(int id , LinkedList<String> category){
        suppliers.get(id).getContract().setCategory(category);
    }

    public void addCategory(int id , String category){
        suppliers.get(id).getContract().addCategory(category);
    }

    public void deleteCategory(int id , String category){
        suppliers.get(id).getContract().removeCategory(category);
    }



    //add new product to the contract
    public void addProductToContract(int id ,Product product){

        //check if the product category is in the suppliers category list
        boolean contatinCategory = suppliers.get(id).checkCategory(product.getCategory());

        if (contatinCategory){
            suppliers.get(id).addProduct(product);
        }
        else{
            System.out.println("The Products Category Is Not In The Suppliers Category List\n");
        }
        //TODO - we check if the category is in the list in the contract class , do we need it again here?
    }

    public void addProductToContract(int id ,Product[] product){
        HashMap<Integer, Product> products = new HashMap<>();
        for (Product p:product) {
            products.put(p.getCatalogID(), p);
        }
        suppliers.get(id).getContract().setProducts(products);
    }

    // delete a product the supplier can supply from the contract
    public void deleteProduct (int id, int p){
        suppliers.get(id).removeProduct(p);
    }

    //change the supplier kind
    public void ChangeSupplierKind(int id ,String kind){
        suppliers.get(id).getContract().setKindOfSupplier(kind);
    }


    //UPDATE COST ENGINEERING
    //create cost enfineering
    public void createCostEngineering(int id){
        suppliers.get(id).createCostEngineering();
    }

    //update min quantity
    public void changeMinQuantity(int id , int catalogid , int minQuantity){
        suppliers.get(id).getCostEngineering().changeMinQuantity(catalogid , minQuantity);
    }

    public void changeMinQuantity(int id ,HashMap<Integer, Integer> minQuntity){
        suppliers.get(id).getCostEngineering().setMinQuntity(minQuntity);
    }



    //update new price with sale
    public void changenewPriceAfterSale(int id , int catalogid , int price){
        suppliers.get(id).getCostEngineering().changeNewPrice(catalogid , price);
    }

    public void changenewPriceAfterSale(int id , HashMap<Integer, Integer> newPrice){
        suppliers.get(id).getCostEngineering().setNewPrice(newPrice);
    }

    //add product to cost engineering
    public void addProductToCostEng(int id , int catalogid , int minQuantity , int price){

        if (suppliers.containsKey(id)){
            //check if the product is in the suppliers product list
            boolean containProduct = suppliers.get(id).getContract().checkProduct(catalogid);

            if(containProduct){
                suppliers.get(id).getCostEngineering().addProduct(catalogid , minQuantity , price);
            }
            else{
                System.out.println("The Product Is Not In The Product list");
            }
        }

        else{
            System.out.println("The Supplier Is Not In The System");
        }

    }

    //delete product from cost engineering
    public void removeProductCostEng(int id , int catalogid){

        //check if the product is in the suppliers product list
        boolean containProduct = suppliers.get(id).getContract().checkProduct(catalogid);

        if(containProduct){
            suppliers.get(id).getCostEngineering().removeProduct(catalogid);
        }
        else {
            System.out.println("The Product Is Not In The Product List");
        }

        //TODO - we checked and printed this in the cost eng class
    }

    public LinkedList<Product> getAllSupProducts(int supId) {
        SupplierCard spCard =  suppliers.get(supId);
        return spCard.getContract().getProducts();
    }


}
