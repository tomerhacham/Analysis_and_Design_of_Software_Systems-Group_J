package PresentationLayer;
import BusinessLayer.Contract;
import BusinessLayer.FacadeController;
import BusinessLayer.Product;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import static java.lang.System.exit;

/**
 * Singleton IO.
 * Represent the presentation layer of the system.
 * All of the communication with the users of the system
 * its from this class.
 * including menus, error messages, and input from the user.
 *
 *  Minimum functionality.
 * Passing user input to facadeController.
 *
 */

public class IO {

    private static IO instance = null;
    public static FacadeController facadeController = FacadeController.getInstance();
    public static Scanner scanner = new Scanner(System.in);

    //constructor
    private IO(){
        System.out.println("Welcome !"+ '\n');
        System.out.println("Please Choose One Of The Following Options : ");
    }

    // static method to create instance of Singleton class
    public static IO getInstance(){
        if (instance == null)
            instance = new IO();

        return instance;
    }

   public static void initialize (){
        System.out.println("1. Run Initialize scenario");
        System.out.println("2. Display Menu");

        int option = Integer.parseInt(scanner.nextLine());

        switch (option) {
            case 1:
                runInitializeScenario();
                displayMenu();
                break;

            case 2:
                displayMenu();
                break;
        }
    }

   public static void runInitializeScenario(){
        LinkedList<String> contact = new LinkedList();
        contact.add("Moshe");
        contact.add("Rachel");
       LinkedList<String> category = new LinkedList<>();
       category.add("Dairy");
       category.add("Vegetables");
       Contract contract = facadeController.CreateContract(category , 14458727 , "by Order");

       facadeController.createSupplierCard("halavi-lee" , "ringelbloom 97, beer-sheva" , "halavi@gmail.com" , "08- 1234567" ,
                                            14458727  , "0975635" , "CreditCard" , contact, contract);

       Product p = facadeController.createNewProduct(12 , "milk" , 7 , "Tnuva" , "Dairy" , 27784);
       facadeController.addProductToContract(14458727 ,p);

       Product p1 = facadeController.createNewProduct(15 , "yogurt" , 12 , "Tnuva" , "Dairy" , 27845);
       facadeController.addProductToContract(14458727 ,p1);

       Product p2 = facadeController.createNewProduct(53 , "tomato" , 4 , "haklai" , "Vegetables" , 1135);
       facadeController.addProductToContract(14458727 ,p2);

       Product p3 = facadeController.createNewProduct(54 , "cucumber" , 3 , "haklai" , "Vegetables" , 1136);
       facadeController.addProductToContract(14458727 ,p3);

       facadeController.createCostEng(14458727);
       facadeController.addProductToCostEng(14458727, 27784 , 50, 5);

       LinkedList<String> contact2 = new LinkedList();
       contact.add("Yosi");
       LinkedList<String> category2 = new LinkedList<>();
       category2.add("Meat");
       Contract contract2 = facadeController.CreateContract(category2 , 18846738  , "by Order");

       facadeController.createSupplierCard("niceToMeat" , "mesada  37, beer-sheva" , "niceToMeat@gmail.com" , "08- 7594456" ,
               18846738   , "09754432" , "CreditCard" , contact2, contract2);

       Product p4 = facadeController.createNewProduct(40 , "red meat" , 37 , "theButcher" , "Meat" , 31668);
       facadeController.addProductToContract(18846738 ,p4);

       Product p5 = facadeController.createNewProduct(45 , "chicken" , 25 , "theButcher" , "Meat" , 31695);
       facadeController.addProductToContract(18846738 ,p5);

       facadeController.createOrder();
       facadeController.addProductToOrder(14458727 , 12 , 100);
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
                exit(0);
                break;
            }
            else{
                System.out.println("Invalid Option");
            }
        }
    }

//#region Suppliers

    public static void supplierMenu(){
        int option = 0;
        int supid =0;

        System.out.println("Enter Supplier ID : ");
        supid = Integer.parseInt(scanner.nextLine());
        //System.out.println('\n');

        System.out.println("Please Choose One Of The Following Options : ");
        System.out.println("1. Create a New Supplier Card");
        System.out.println("2. Delete a Supplier Card");
        System.out.println("3. Update Supplier Name");
        System.out.println("4. Update Supplier Address");
        System.out.println("5. Update Supplier Email");
        System.out.println("6. Update Supplier Phone Number");
        System.out.println("7. Update Supplier Bank Account");
        System.out.println("8. Update Supplier Payment");
        System.out.println("9. Add Supplier Contacts ");
        System.out.println("10. Delete Supplier Contacts");
        System.out.println("11. Create Cost Engineering");
        System.out.println("12. Add Supplier Category");
        System.out.println("13. Delete Supplier Category");
        System.out.println("14. Add Product To Contract");
        System.out.println("15. Delete Product From Contract");
        System.out.println("16. Update  Supplier Order Agreement");
        System.out.println("17. Update Minimum Quantity For Product");
        System.out.println("18. Update The Product Price After Sale");
        System.out.println("19. Add Product To Cost Engineering");
        System.out.println("20. Remove Product From Sale(Cost Engineering)");
        System.out.println("21. Print Supplier Products");

        option = Integer.parseInt(scanner.nextLine());

        switch (option){

            case 1:
                createSupplierCard(supid);
                createContract(supid);
                break;

            case 2:
                facadeController.deleteSupplierCard(supid);
                //System.out.println("The Supplier With Id "+Integer.toString(supid)+" Is Deleted");
                break;

            case 3:
                System.out.println("Enter A New Name : ");
                String SupplierName = scanner.nextLine();
                facadeController.ChangeSupplierName(supid , SupplierName);
                break;

            case 4:
                System.out.println("Enter A New Address : ");
                String Address = scanner.nextLine();
                facadeController.ChangeAddress(supid , Address);
                break;

            case 5:
                System.out.println("Enter Email : ");
                String Email = scanner.nextLine();
                facadeController.ChangeEmail(supid , Email);
                break;

            case 6:
                System.out.println("Enter PhoneNumber : ");
                String PhoneNumber = scanner.nextLine();
                facadeController.ChangePhoneNumber(supid , PhoneNumber);
                break;

            case 7:
                System.out.println("Enter Bank Account Number : ");
                String BankAccountNum = scanner.nextLine();
                facadeController.ChangeBankAccount(supid , BankAccountNum);
                break;

            case 8:
                System.out.println("Enter Payment : ");
                String Payment = scanner.nextLine();
                facadeController.ChangePayment(supid , Payment);
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
                break;

            case 10:
                System.out.println("Enter Contact Name To Delete : ");
                String contactName = scanner.nextLine();
                facadeController.DeleteContactName(supid , contactName);
                break;

            case 11:
                createCostEng(supid);
                break;

            case 12:
                System.out.println("Enter A New Category : ");
                String category = scanner.nextLine();
                facadeController.addCategory(supid , category);
                break;

            case 13:
                System.out.println("Enter A Category To Delete : ");
                String categoryToDelete = scanner.nextLine();
                facadeController.deleteCategory(supid , categoryToDelete);
                break;

            case 14:
                createProduct(supid);
                break;

            case 15:
                System.out.println("Enter Product Catalog Id You Wish To Remove : ");
                int catalogid = Integer.parseInt(scanner.nextLine());
                facadeController.deleteProduct(supid , catalogid);
                break;

            case 16:
                System.out.println("Enter The Suppliers New Order Agreement : ");
                String kind = scanner.nextLine();
                facadeController.ChangeSupplierKind(supid , kind);
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
                break;

            case 21:
                printSupplierProducts(supid);
                break;
        }
    }

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
        String BankAccountNum = scanner.nextLine();

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
        Contract contract = createContract(supid);

        facadeController.createSupplierCard(SupplierName , Address , Email , PhoneNumber , supid , BankAccountNum , Payment , ContactsName, contract);
    }


    public static Contract createContract(int supid){

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

        return facadeController.CreateContract(category , supid , kind);
    }

    public static void createProduct(int supid){

        boolean exist = facadeController.isExistSupplier (supid);
        if (!exist){
            return;
        }

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

        Product product = facadeController.createNewProduct(productID , name , price , producer , category , catalogid);

        facadeController.addProductToContract(supid ,product);
    }

    public static void updateQuantity(int supid){

        boolean exist = facadeController.isExistSupplier (supid);
        if (!exist){
            return;
        }

        System.out.println("Enter The Product Catalog Id : ");
        int catalogid = Integer.parseInt(scanner.nextLine());
        System.out.println('\n');

        System.out.println("Enter A New Minimum Quantity For The Product : ");
        int minQuantity = Integer.parseInt(scanner.nextLine());
        System.out.println('\n');

        facadeController.changeMinQuantity(supid , catalogid , minQuantity);
    }

    public static void updatePrice(int supid){

        boolean exist = facadeController.isExistSupplier (supid);
        if (!exist){
            return;
        }

        System.out.println("Enter The Product Catalog Id : ");
        int catalogid = Integer.parseInt(scanner.nextLine());
        System.out.println('\n');

        System.out.println("Enter A New Price After Sale For The Product : ");
        int price = Integer.parseInt(scanner.nextLine());
        System.out.println('\n');

        facadeController.changenewPriceAfterSale(supid , catalogid , price);
    }

    public static void addProductToCostEng(int supid){

        boolean exist = facadeController.isExistSupplier (supid);
        if (!exist){
            return;
        }
        System.out.println("Enter The Product Catalog Id : ");
        int catalogid = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Minimum Quantity For The Product : ");
        int minQuantity = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter The Price After Sale For The Product : ");
        int price = Integer.parseInt(scanner.nextLine());

        facadeController.addProductToCostEng(supid , catalogid , minQuantity , price);
    }

    public static void printSupplierProducts (int supId){
        boolean exist = facadeController.isExistSupplier (supId);
        if (!exist){
            return;
        }
        LinkedList <Product> listP = facadeController.getAllSupProducts(supId);

        for (Product p : listP){
            System.out.println("Product Name : "+p.getName() + '\t' +'\t'+ "Catalog Id : "+p.getCatalogID());
        }
    }

    private static void createCostEng(int supid) {

        boolean exit = false;
        boolean exist =facadeController.createCostEng(supid);
        if (!exist){
            return;
        }
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

    public static void orderMenu(){
        int option = 0;

        System.out.println("Please Choose One Of The Following Options : ");
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

    public static void addOrder(){
        facadeController.createOrder();

        boolean exit= false;

        while (!exit) {

            System.out.println("1. Add Product to Order");
            System.out.println("2. Finish Order");
            System.out.println("3. Present All Suppliers And Their Products");

            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    addProductToExistOrder();
                    break;
                case 2:
                    exit = true;
                    endOrder();
                    break;
                case 3:
                    printallsuppliers();
                    break;
            }
        }
    }

    public static void printallsuppliers() {

        LinkedList<String> allsup = facadeController.printallsuppliers();

        for (String s :allsup) {
            System.out.println(s);
        }
    }

    public static void endOrder (){

        HashMap<Product , Integer> productList = facadeController.endOrder();

        System.out.println("Your Order : ");
        System.out.println("Product" + '\t' + '\t' + "Quantity");

        if (productList.isEmpty()){
            System.out.println( "----------------");
            System.out.println("No Products In This Order");
        }

        for (Product p : productList.keySet()) {
            String key = p.getName();
            String value = productList.get(p).toString();
            System.out.println(key + '\t' + '\t' + value);
        }

        Double total = facadeController.getTotalAmountLastOrder();
        System.out.println("Total Amount : " + total + '\n');

        System.out.println("1. Send Order");
        System.out.println("2. Update Order");

        int option = Integer.parseInt(scanner.nextLine());

        switch (option){
            case 1:
                if (productList.isEmpty()) {
                    System.out.println("Can't Send This Order because its Empty\n");
                    updateOrder();
                }
                else
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
        if (option != 4)
            endOrder();
        else
            displayMenu();
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

        System.out.println("Enter Product Id you want to remove and the Supplier Id (separated by comma): ");
        String s= scanner.nextLine();
        String answer = s.replaceAll(" ","");

        int productID = Integer.parseInt(answer.split(",")[0]);
        int supid= Integer.parseInt(answer.split(",")[1]);

        facadeController.removeFromOrder (productID , supid);
    }

    public static void changeQuantity () {
        System.out.println("Enter Product Id and New Quantity (Separated by a comma) :");
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

    public static void printResult (String msg){
        System.out.println(msg);
    }

}
