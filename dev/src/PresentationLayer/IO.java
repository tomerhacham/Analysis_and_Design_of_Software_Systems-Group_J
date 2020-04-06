package PresentationLayer;

import BusinessLayer.FacadeController;
import BusinessLayer.Product;
import BusinessLayer.SupplierController;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class IO {

    private static IO instance = null;
    public static FacadeController facadeController = FacadeController.getInstance();
    public static Scanner scanner = new Scanner(System.in);

    //constructor
    private IO(){
        System.out.println("Welcome !\n");
        System.out.println("Please Choose One Of The Following Options : \n");
    }

    // static method to create instance of Singleton class
    public static IO getInstance(){
        if (instance == null)
            instance = new IO();

        return instance;
    }


    public static void displayMenu(){

        while (true){
            System.out.println("1. Suppliers");
            System.out.println("2. Orders");
            System.out.println("3. Exit System");

            int option = Integer.parseInt(scanner.nextLine());

            if(option==1){
                supplierMenu();
            }
            else if (option==2){
                orderMenu();
            }
            else if( option ==3){
                System.out.println("Thank You, See You Next Time.\n");
                break;
            }
            else{
                System.out.println("Invalid Option");
            }
        }
    }

    public static void supplierMenu(){
        int option = 0;
        int supid =0;

        System.out.println("Enter Supplier ID : ");
        supid = Integer.parseInt(scanner.nextLine());
        System.out.println('\n');

        System.out.println("Please Choose One Of The Following Options : \n");
        System.out.println("1. Create a New Supplier Card");
        System.out.println("2. Delete a Supplier Card");
        System.out.println("3. Change Supplier Name");
        System.out.println("4. Change Supplier Address");
        System.out.println("5. Change Supplier Email");
        System.out.println("6. Change Supplier Phone Number");
        System.out.println("7. Change Supplier Bank Account");
        System.out.println("8. Change Supplier Payment");
        System.out.println("9. Add Supplier Contact ");
        System.out.println("10. Delete Supplier Contact");
        System.out.println("11. Create Cost Engineering");
        System.out.println("12. Add Supplier Category");
        System.out.println("13. Delete Supplier Category");
        System.out.println("14. Add Product To Contract");
        System.out.println("15. Delete Product From Contract");
        System.out.println("16. Change Supplier Order Agreement");
        System.out.println("17. Change Minimum Quantity For Product");
        System.out.println("18. Change The Product Price After Sale");
        System.out.println("19. Add Product To Cost Engineering");
        System.out.println("20. Remove Product From Sale");
        System.out.println("21. Print Supplier Products");

        option = Integer.parseInt(scanner.nextLine());

        switch (option){

            case 1:
                createSupplierCard(supid);
                createContract(supid);
                break;

            case 2:
                facadeController.deleteSupplierCard(supid);
                System.out.println("The Supplier With Id "+Integer.toString(supid)+" Is Deleted");
                break;

            case 3:
                System.out.println("Enter A New Name : ");
                String SupplierName = scanner.nextLine();
                facadeController.ChangeSupplierName(supid , SupplierName);
                System.out.println('\n');
                break;

            case 4:
                System.out.println("Enter A New Address : ");
                String Address = scanner.nextLine();
                facadeController.ChangeAddress(supid , Address);
                System.out.println('\n');
                break;

            case 5:
                System.out.println("Enter Email : ");
                String Email = scanner.nextLine();
                facadeController.ChangeEmail(supid , Email);
                System.out.println('\n');
                break;

            case 6:
                System.out.println("Enter PhoneNumber : ");
                String PhoneNumber = scanner.nextLine();
                facadeController.ChangePhoneNumber(supid , PhoneNumber);
                System.out.println('\n');
                break;

            case 7:
                System.out.println("Enter Bank Account Number : ");
                int BankAccountNum = Integer.parseInt(scanner.nextLine());
                facadeController.ChangeBankAccount(supid , BankAccountNum);
                System.out.println('\n');
                break;

            case 8:
                System.out.println("Enter Payment : ");
                String Payment = scanner.nextLine();
                facadeController.ChangePayment(supid , Payment);
                System.out.println('\n');
                break;

            case 9:
                System.out.println("Enter Contacts Name (Separated by a comma) : ");
                String contacts = scanner.nextLine();
                String contactsWithoutSpace = contacts.replaceAll(" ","");
                String[] ContactsToList = contactsWithoutSpace.split(",");
                LinkedList<String> ContactsName = new LinkedList<>();
                for (String s : ContactsToList) {
                    ContactsName.add(s);
                }
                facadeController.AddContactName(supid , ContactsName);
                System.out.println('\n');

            case 10:
                System.out.println("Enter Contact Name To Delete : ");
                String contactName = scanner.nextLine();
                facadeController.DeleteContactName(supid , contactName);
                System.out.println('\n');
                break;

            case 11:
                createCostEng(supid);
                break;

            case 12:
                System.out.println("Enter A New Category : ");
                String category = scanner.nextLine();
                facadeController.addCategory(supid , category);
                System.out.println('\n');
                break;

            case 13:
                System.out.println("Enter A Category To Delete : ");
                String categoryToDelete = scanner.nextLine();
                facadeController.deleteCategory(supid , categoryToDelete);
                System.out.println('\n');
                break;

            case 14:
                createProduct(supid);
                break;

            case 15:
                System.out.println("Enter Product Catalog Id You Wish To Remove : ");
                int catalogid = Integer.parseInt(scanner.nextLine());
                facadeController.deleteProduct(supid , catalogid);
                System.out.println('\n');
                break;

            case 16:
                System.out.println("Enter The Suppliers New Order Agreement : ");
                String kind = scanner.nextLine();
                facadeController.ChangeSupplierKind(supid , kind);
                System.out.println('\n');
                break;

            case 17:
                updateQuantity(supid);
                break;

            case 18:
                updatePrice(supid);
                break;

            case 19:
                addProductToCostEng(supid);
                break;

            case 20:
                System.out.println("Enter The Product Catalog Id : ");
                int catalogid2delete = Integer.parseInt(scanner.nextLine());
                facadeController.removeProductCostEng(supid , catalogid2delete);
                System.out.println('\n');
                break;

            case 21:
                printSupplierProducts(supid);
        }
    }

    public static void orderMenu(){
        int option = 0;

        System.out.println("Please Choose One Of The Following Options : \n");
        System.out.println("1. Create a New Order");
        System.out.println("2. Display all Orders");
        System.out.println("3. Display Orders by Supplier");

        option = Integer.parseInt(scanner.nextLine());

        switch (option){

            case 1:
                addOrder();
                break;

            case  2:
                displayAllOrders();
                break;

            case 3:
                displayOrderBySupplier();
                break;
        }

    }

//#region Suppliers

    public static void createSupplierCard(int supid){
        System.out.println("Enter Supplier Name : ");
        String SupplierName = scanner.nextLine();

        System.out.println("Enter Address : ");
        String Address = scanner.nextLine();

        System.out.println("Enter Email : ");
        String Email = scanner.nextLine();

        System.out.println("Enter PhoneNumber : ");
        String PhoneNumber = scanner.nextLine();

        System.out.println("Enter Bank Account Number : ");
        int BankAccountNum = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Payment : ");
        String Payment = scanner.nextLine();

        System.out.println("Enter Contacts Name (Separated by a comma) : ");
        String contacts = scanner.nextLine();
        String contactsWithoutSpace = contacts.replaceAll(" ","");
        String[] ContactsToList = contactsWithoutSpace.split(",");
        LinkedList<String> ContactsName = new LinkedList<>();
        for (String s : ContactsToList) {
            ContactsName.add(s);
        }

        facadeController.createSupplierCard(SupplierName , Address , Email , PhoneNumber , supid , BankAccountNum , Payment , ContactsName);
    }

    public static void createContract(int supid){

        System.out.println("Enter The Products Categories (Separated by a comma) : ");
        String categories = scanner.nextLine();
        String categoriesWithoutSpace = categories.replaceAll(" ","");
        String[] categoriesToList = categoriesWithoutSpace.split(",");
        LinkedList<String> category = new LinkedList<>();
        for (String s : categoriesToList) {
            category.add(s);
        }
        System.out.println('\n');

        System.out.println("Enter Supplier Order Agreement : ");
        String kind = scanner.nextLine();
        System.out.println('\n');

        facadeController.CreateContract(category , supid , kind);
    }

    public static void createProduct(int supid){

        System.out.println("Enter Product Name : ");
        String name = scanner.nextLine();

        System.out.println("Enter Product Id : ");
        int productID = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Product Price : ");
        int price = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Product Producer : ");
        String producer = scanner.nextLine();

        System.out.println("Enter Product Category : ");
        String category = scanner.nextLine();

        System.out.println("Enter Product Catalog Id : ");
        int catalogid = Integer.parseInt(scanner.nextLine());

        Product product = new Product(productID , name , price , producer , category , catalogid);

        facadeController.addProductToContract(supid ,product);
    }

    public static void updateQuantity(int supid){

        System.out.println("Enter The Product Catalog Id : ");
        int catalogid = Integer.parseInt(scanner.nextLine());
        System.out.println('\n');

        System.out.println("Enter A New Minimum Quantity For The Product : ");
        int minQuantity = Integer.parseInt(scanner.nextLine());
        System.out.println('\n');

        facadeController.changeMinQuantity(supid , catalogid , minQuantity);
    }

    public static void updatePrice(int supid){

        System.out.println("Enter The Product Catalog Id : ");
        int catalogid = Integer.parseInt(scanner.nextLine());
        System.out.println('\n');

        System.out.println("Enter A New Price After Sale For The Product : ");
        int price = Integer.parseInt(scanner.nextLine());
        System.out.println('\n');

        facadeController.changenewPriceAfterSale(supid , catalogid , price);
    }

    public static void addProductToCostEng(int supid){

        System.out.println("Enter The Product Catalog Id : ");
        int catalogid = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Minimum Quantity For The Product : ");
        int minQuantity = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter The Price After Sale For The Product : ");
        int price = Integer.parseInt(scanner.nextLine());

        facadeController.addProductToCostEng(supid , catalogid , minQuantity , price);
    }

    public static void printSupplierProducts (int supId){
        LinkedList <Product> listP = facadeController.getAllSupProducts(supId);

        for (Product p : listP){
            System.out.println(p.getName() + '\t' +'\t'+ p.getCatalogID() + '\n');
        }
    }

    private static void createCostEng(int supid) {

        boolean exit = false;
        facadeController.createCostEng(supid);

        while(!exit){
            System.out.println("1. Add Product to Cost Engineering");
            System.out.println("2. Finish Updating");

            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    addProductToCostEng(supid);
                    break;
                case 2:
                    exit = true;
                    break;
            }
        }
    }


//#endregion

//#region Order

    public static void addOrder(){
        facadeController.createOrder();

        boolean exit= false;

        while (!exit) {

            addProductToExistOrder();

            System.out.println("1. Add Product to Order");
            System.out.println("2. Finish Order");

            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    break;
                case 2:
                    exit = true;
                    endOrder();
                    break;
            }
        }

    }

    public static void endOrder (){
        System.out.println("Your Order : ");
        System.out.println("Product"+'\t'+'\t'+"Quantity");

        HashMap<Product , Integer> productList = facadeController.endOrder();
        for (Product p: productList.keySet()){
            String key = p.getName();
            String value = productList.get(p).toString();
            System.out.println(key + '\t' +'\t'+ value);
        }
        int total= facadeController.getTotalAmountLastOrder();
        System.out.println("Total Amount : " + total + '\n');

        System.out.println("1. Send Order");
        System.out.println("2. Update Order");

        int option = Integer.parseInt(scanner.nextLine());

        switch (option){
            case 1:
                System.out.println("Your Order Has Been Sent.\n");
                break;
            case 2:
                updateOrder();
                break;
        }
    }

    public static void updateOrder (){
        System.out.println("1. Add product");
        System.out.println("2. Remove product");
        System.out.println("3. Change quantity of product");
        System.out.println("4. Delete Order");

        int option = Integer.parseInt(scanner.nextLine());

        switch (option){
            case 1:
                addProductToExistOrder();
                break;
            case 2:
                removeProductToExistOrder();
                break;
            case 3:
                changeQuantity();
                break;
            case 4:
                deleteOrder();
                break;
        }

        endOrder();
    }

    public static void addProductToExistOrder () {
        String s= "";
        int supID, productID, quantity;

        System.out.println("Enter Supplier Id, Product Id and quantity (separated by comma): ");
        s= scanner.nextLine();
        String answer = s.replaceAll(" ","");

        supID = Integer.parseInt(answer.split(",")[0]);
        productID = Integer.parseInt(answer.split(",")[1]);
        quantity = Integer.parseInt(answer.split(",")[2]);

        facadeController.addProductToOrder(supID, productID, quantity);
    }

    public static void removeProductToExistOrder () {
        System.out.println("Enter Product Id you want to remove");
        int productID = Integer.parseInt(scanner.nextLine());
        facadeController.removeFromOrder (productID);
    }

    public static void changeQuantity () {
        System.out.println("Enter Product Id and New Quantity");
        String s= scanner.nextLine();
        String answer = s.replaceAll(" ","");

        int productID = Integer.parseInt(answer.split(",")[0]);
        int quantity = Integer.parseInt(answer.split(",")[1]);

        facadeController.updateProductQuantity(productID , quantity);
    }

    public static void deleteOrder (){
        facadeController.removeOrder();
    }

    public static void displayAllOrders (){
        LinkedList<String> toDisplay = facadeController.displayAllOrders();

        for (String s: toDisplay){
            System.out.println(s);
        }
    }

    public static void displayOrderBySupplier (){
        System.out.println("Enter Supplier id");
        int supId = Integer.parseInt(scanner.nextLine());

        LinkedList<String> toDisplay = facadeController.displayOrderBySupplier(supId);

        for (String s: toDisplay){
            System.out.println(s);
        }
    }


//#endregion

}
