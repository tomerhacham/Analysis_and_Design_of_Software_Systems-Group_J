package BusinessLayer;

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
    private HashMap<Integer ,SupplierCard> suppliers;

    //constructor
    private SupplierController(){
        suppliers = new HashMap<>();
    }

    // static method to create instance of Singleton class
    public static SupplierController getInstance()  {
        if (instance == null)
            instance = new SupplierController();

        return instance;
    }

    public HashMap<Integer, SupplierCard> getSuppliers() {
        return suppliers;
    }

    //SUPPLIER CARD
    //create a new supplierCard
    public void createSupplierCard(String SupplierName , String Address , String Email , String PhoneNumber ,
                                   int id ,String BankAccountNum , String Payment , LinkedList<String> ContactsName, Contract contract){

        SupplierCard supplierCard = new SupplierCard(SupplierName, Address, Email, PhoneNumber,
                                        id ,BankAccountNum ,Payment ,ContactsName, contract);

        addSupplierCardToList(supplierCard);
        contract.addSpplierToProductController(supplierCard);
    }

    //add a new supplierCard to the linked list
    private void addSupplierCardToList(SupplierCard supplierCard){
        if (suppliers.containsKey(supplierCard.getId())){
            Result.setMsg("The SupplierCard Already Exists. You Can Choose To Change Your Supplier Card");
        }
        else{
            suppliers.put(supplierCard.getId() , supplierCard);
        }
    }

    //delete a supplierCard
    public void deleteSupplierCard(int id){
        if ( ! isExist(id)){
            return;
        }
        suppliers.get(id).getContract().removeSupplier();
        suppliers.remove(id);

    }

    //update supplier name
    public void ChangeSupplierName(int id , String newName){
        if ( ! isExist(id)){
            return;
        }
        SupplierCard sc = suppliers.get(id);
        sc.setSupplierName(newName);
    }

    //update address
    public void ChangeAddress(int id ,String newAddress){
        if ( ! isExist(id)){
            return;
        }
        SupplierCard sc = suppliers.get(id);
        sc.setAddress(newAddress);
    }

    //update email
    public void ChangeEmail(int id , String newEmail){
        if ( ! isExist(id)){
            return;
        }
        SupplierCard sc = suppliers.get(id);
        sc.setEmail(newEmail);
    }

    //update phone number
    public void ChangePhoneNumber(int id, String newPhoneNum){
        if ( ! isExist(id)){
            return;
        }
        SupplierCard sc = suppliers.get(id);
        sc.setPhoneNumber(newPhoneNum);
    }

    //update bank account number
    public void ChangeBankAccount(int id, String newBankAccount){
        if ( ! isExist(id)){
            return;
        }
        SupplierCard sc = suppliers.get(id);
        sc.setBankAccountNum(newBankAccount);
    }

    //update payment
    public void ChangePayment(int id ,String newPayment){
        if ( ! isExist(id)){
            return;
        }
        SupplierCard sc = suppliers.get(id);
        sc.setPayment(newPayment);
    }

    //delete a contact name
    public void DeleteContactName(int id , String name2Delete){
        if ( ! isExist(id)){
            return;
        }
        SupplierCard sc = suppliers.get(id);
        sc.deleteContactName(name2Delete);
    }

    //add a conatct name
    public void AddContactName(int id ,String newContactName){
        if ( ! isExist(id)){
            return;
        }
        SupplierCard sc = suppliers.get(id);
        sc.addContactName(newContactName);
    }


    public void AddContactName(int id ,LinkedList<String> contactsName){
        if ( ! isExist(id)){
            return;
        }
        SupplierCard sc = suppliers.get(id);
        sc.setContactsName(contactsName);
    }


    //UPDATE CONTRACT
    //create new contract
    public Contract CreateContract(LinkedList<String> category , int supId ,String kind ){
        Contract c = new Contract(category , supId , kind );
        //suppliers.get(supId).setContract(c);
        return c;
    }

    //change contract category
    public void addCategory(int id , LinkedList<String> category){
        if ( ! isExist(id)){
            return;
        }
        suppliers.get(id).getContract().setCategory(category);
    }

    public void addCategory(int id , String category){
        if ( ! isExist(id)){
            return;
        }
        suppliers.get(id).getContract().addCategory(category);
    }

    public void deleteCategory(int id , String category){
        if ( ! isExist(id)){
            return;
        }
        suppliers.get(id).getContract().removeCategory(category);
    }

    //add new product to the contract
    public void addProductToContract(int id ,Product product){

        //check if the product category is in the suppliers category list
        if ( ! isExist(id)){
            return;
        }
        boolean contatinCategory = suppliers.get(id).checkCategory(product.getCategory());

        if (contatinCategory){
            suppliers.get(id).addProduct(product);
        }
        else{
            Result.setMsg("The Products Category Is Not In The Suppliers Category List\n");
        }
    }

    public void addProductToContract(int id ,Product[] product){
        HashMap<Integer, Product> products = new HashMap<>();
        for (Product p:product) {
            products.put(p.getCatalogID(), p);
        }
        if ( ! isExist(id)){
            return;
        }
        suppliers.get(id).getContract().setProducts(products);
    }

    // delete a product the supplier can supply from the contract
    public void deleteProduct (int id, int catalogid){
        if ( ! isExist(id)){
            return;
        }
        suppliers.get(id).removeProduct(catalogid);
    }

    //change the supplier kind
    public void ChangeSupplierKind(int id ,String kind){
        if ( ! isExist(id)){
            return;
        }
        suppliers.get(id).getContract().setKindOfSupplier(kind);
    }


    //UPDATE COST ENGINEERING
    //create cost enfineering
    public boolean createCostEngineering(int id){
        if ( ! isExist(id)){
            return false;
        }
        suppliers.get(id).createCostEngineering();
        return true;
    }

    //update min quantity
    public void changeMinQuantity(int id , int catalogid , int minQuantity){
        if ( ! isExist(id)){
            return;
        }
        suppliers.get(id).getCostEngineering().changeMinQuantity(catalogid , minQuantity);
    }

    public void changeMinQuantity(int id ,HashMap<Integer, Integer> minQuntity){
        if ( ! isExist(id)){
            return;
        }
        suppliers.get(id).getCostEngineering().setMinQuntity(minQuntity);
    }

    //update new price with sale
    public void changenewPriceAfterSale(int id , int catalogid , int price){
        if ( ! isExist(id)){
            return;
        }
        suppliers.get(id).getCostEngineering().changeNewPrice(catalogid , price);
    }

    public void changenewPriceAfterSale(int id , HashMap<Integer, Integer> newPrice){
        if ( ! isExist(id)){
            return;
        }
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
                Result.setMsg("The Product Is Not In The Product list");
            }
        }

        else{
            Result.setMsg("The Supplier Is Not In The System");
        }

    }

    //delete product from cost engineering
    public void removeProductCostEng(int id , int catalogid){

        //check if the product is in the suppliers product list
        if ( ! isExist(id)){
            return;
        }
        boolean containProduct = suppliers.get(id).getContract().checkProduct(catalogid);

        if(containProduct){
            suppliers.get(id).getCostEngineering().removeProduct(catalogid);
        }
        else{
            Result.setMsg("The Product Is Not In The Cost Engineering");
        }
    }

    public LinkedList<Product> getAllSupProducts(int supId) {

        SupplierCard spCard =  suppliers.get(supId);
        if (spCard.getContract().getProducts().isEmpty()){
            Result.setMsg("The Supplier Has No Products");
        }
        return spCard.getContract().getProducts();
    }


    public LinkedList<String> printallsuppliers() {
        LinkedList<String> toreturn = new LinkedList<>();

        if (suppliers.isEmpty()){
            toreturn.add("No Suppliers In The System");
            return toreturn;
        }
        for (Integer i :suppliers.keySet()) {
            toreturn.add(suppliers.get(i).toString());
        }

        return toreturn;
    }

    public boolean isExist (int id){
        if ( ! suppliers.containsKey(id)){
            Result.setMsg("There's No Such Supplier in the System");
            return false;
        }
        return true;
    }
}
