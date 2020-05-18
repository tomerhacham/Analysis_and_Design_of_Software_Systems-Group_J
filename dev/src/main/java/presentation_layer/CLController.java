package presentation_layer;

import bussines_layer.Branch;
import bussines_layer.BranchController;
import bussines_layer.Result;
import bussines_layer.inventory_module.Category;
import bussines_layer.inventory_module.Report;
import bussines_layer.inventory_module.Sale;
import javafx.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.System.exit;

public class CLController {

    private static Scanner sc;
    private static BranchController branchController;

    public CLController(){
        //branchController = new BranchController();
        sc = new Scanner(System.in);    //System.in is a standard input stream
    }

    public static void displayMenu() {
        printLogo();
        printInitializeMenu();
        while(true) {
            printSuperLiMenu();
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    printBranchesMenu();
                    break;
                case 2:
                    printSuppliersMenu();
                    break;
                case 3:
                    String sim = "Simulating next day...\n";
                    System.out.println(sim);
                    String msg = String.format("Old date: %s\t", BranchController.system_curr_date);

                    Result<LinkedList<String>> periodicOrdersToPrint = branchController.simulateNextDay();
                    if (periodicOrdersToPrint.getData() == null){ // if there are no periodic orders to print at this day
                        System.out.println(periodicOrdersToPrint.getMessage());
                    }
                    else{
                        for (String str : periodicOrdersToPrint.getData()) {
                            System.out.println(str);
                        }
                    }
                    msg = msg.concat(String.format("New date: %s\n", BranchController.system_curr_date));
                    System.out.println(msg);
                    break;
                case 4:
                    Exit();
                    return;
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    static private void printLogo() {
        String logo = " _____                       _     _ \n" +
                "/  ___|                     | |   (_)\n" +
                "\\ `--. _   _ _ __   ___ _ __| |    _ \n" +
                " `--. \\ | | | '_ \\ / _ \\ '__| |   | |\n" +
                "/\\__/ / |_| | |_) |  __/ |  | |___| |\n" +
                "\\____/ \\__,_| .__/ \\___|_|  \\_____/_|\n" +
                "            | |                      \n" +
                "            |_|     \n";
        System.out.println(logo);
    }

    static private void printInitializeMenu() {
        String menu = "";
        menu = menu.concat("Choose one of the options:\n");
        menu = menu.concat("1) Restart system DB with initial data\n");
        menu = menu.concat("2) Run system with current DB\n");
        menu = menu.concat("3) Exit");
        System.out.println(menu);
        Integer option = getNextInt(sc);
        while (true) {
            switch (option) {
                case 1:
                    initialize();
                    return;
                case 2:
                    branchController=new BranchController(false);
                    return;
                case 3:
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    private static void printSuperLiMenu() {
        String menu = "";
        menu = menu.concat("Choose one of the options:\n");
        menu = menu.concat("1) Branch Management\n");
        menu = menu.concat("2) Suppliers Management\n");
        menu = menu.concat("3) Simulate to next day\n");
        menu = menu.concat("4) Exit");
        System.out.println(menu);
    }

    //region Branch Management

    private static void printBranchesMenu() {
        String menu = "";
        menu = menu.concat("Choose one of the options:\n");
        menu = menu.concat("1) Choose existing branch\n");
        menu = menu.concat("2) Open new branch\n");
        menu = menu.concat("3) Remove branch\n");
        menu = menu.concat("4) Edit branch name\n");
        menu = menu.concat("5) Return\n");
        menu = menu.concat("6) Exit");
        while (true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    printExistingBranchesMenu();
                    break;
                case 2:
                    printOpenNewBranchMenu();
                    break;
                case 3:
                    printRemoveBranchMenu();
                    break;
                case 4:
                    printEditBranchNameMenu();
                case 5:
                    return;
                case 6:
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    private static void printEditBranchNameMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[branchID],[newName]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            result = branchController.editBranchName(Integer.parseInt(param[0]), param[1]);
            System.out.println(result.getMessage());
        }
    }

    private static void printRemoveBranchMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[branchID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            result = branchController.removeBranch(Integer.parseInt(param[0]));
            System.out.println(result.getMessage());
        }
    }

    private static void printExistingBranchesMenu() {
        String menu = "";
        menu = menu.concat("Choose branch (By ID):\n");
        Integer option = 1;
        for(Integer branch_id : branchController.getBranches().keySet()){
            menu = menu.concat(String.format("%d) %s (type: %d)\n", option, branchController.getBranches().get(branch_id), branch_id));
            option++;
        }

        menu = menu.concat(String.format("%d) Return\n", option));
        menu = menu.concat(String.format("%d) Exit", option+1));
        while(true){
            System.out.println(menu);
            Integer input = getNextInt(sc);
            if (input.equals(option)){
                return;
            }
            if (input.equals(option+1)){
                Exit();
            }
            Result res = branchController.switchBranch(input);
            System.out.println(res.getMessage());
            if (res.isOK()) {
                printMainBranchMenu();
            }
        }
    }

    private static void printOpenNewBranchMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Name]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            result = branchController.createNewBranch(param[0]);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid number of parameters");
        }
    }

    //endregion

    //region Supplier Controller
    private static void printSuppliersMenu() {
        String menu = "";
        menu = menu.concat("Choose one of the options:\n");
        menu = menu.concat("1) Create new supplier card\n");
        menu = menu.concat("2) Edit supplier card details\n");
        menu = menu.concat("3) Print all suppliers\n");
        menu = menu.concat("4) Return\n");
        menu = menu.concat("5) Exit");
        while (true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    printCreateSupplierCardMenu();
                    break;
                case 2:
                    printEditSupplierCardDetails();
                    break;
                case 3:
                    printAllSuppliers();
                    break;
                case 4:
                    return;
                case 5:
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    private static void printEditSupplierCardDetails() {
        String menu = "";
        menu=menu.concat("Choose detail to edit:\n");
        menu=menu.concat("1) Supplier name\n");
        menu=menu.concat("2) Address\n");
        menu=menu.concat("3) Email\n");
        menu=menu.concat("4) Phone number \n");
        menu=menu.concat("5) Bank account\n");
        menu=menu.concat("6) Payment\n");
        menu=menu.concat("7) Supplier type\n");
        menu=menu.concat("8) Delete contact name\n");
        menu=menu.concat("9) Add contact name\n");
        menu=menu.concat("10) Return\n");
        menu=menu.concat("11) Exit");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    printEditSupplierName();
                    break;
                case 2:
                    printEditAddress();
                    break;
                case 3:
                    printEditEmail();
                    break;
                case 4:
                    printEditPhoneNumber();
                    break;
                case 5:
                    printEditBankAccount();
                    break;
                case 6:
                    printEditPayment();
                    break;
                case 7:
                    printEditSupplierType();
                    break;
                case 8:
                    printDeleteContactName();
                    break;
                case 9:
                    printAddContactName();
                    break;
                case 10:
                    return;
                case 11:
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    private static void printAddContactName() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            Integer supplierID = Integer.parseInt(param[0]);
            String details= "Please enter list of contacts names to add: [Name1],[Name2],...\n";
            System.out.println(details);
            String[] contactsInput = getInputParserbyComma(sc);
            LinkedList<String> contactsName = new LinkedList<>(Arrays.asList(contactsInput));
            result = branchController.AddContactName(supplierID,contactsName);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printDeleteContactName() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[Contact name to delete]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer supplierID = Integer.parseInt(param[0]);
            result = branchController.DeleteContactName(supplierID,param[1]);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printEditSupplierType() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[SupplierType]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer supplierID = Integer.parseInt(param[0]);
            result = branchController.ChangeSupplierType(supplierID,param[1]);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printEditPayment() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[New payment]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer supplierID = Integer.parseInt(param[0]);
            result = branchController.ChangePayment(supplierID,param[1]);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printEditBankAccount() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[New bank account number]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer supplierID = Integer.parseInt(param[0]);
            result = branchController.ChangeBankAccount(supplierID,param[1]);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printEditPhoneNumber() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[newPhoneNumber]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer supplierID = Integer.parseInt(param[0]);
            result = branchController.ChangePhoneNumber(supplierID,param[1]);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printEditEmail() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[newEmail]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer supplierID = Integer.parseInt(param[0]);
            result = branchController.ChangeEmail(supplierID,param[1]);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printEditAddress() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[newAddress]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer supplierID = Integer.parseInt(param[0]);
            result = branchController.changeAddress(supplierID,param[1]);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printEditSupplierName() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[newName]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer supplierID = Integer.parseInt(param[0]);
            result = branchController.changeSupplierName(supplierID,param[1]);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printCreateSupplierCardMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierName],[address],[Email],[PhoneNumber],[supplier_id],[BankAccountNumber],[Payment],[by order/periodic/self delivery]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 8) {
            String details= "Please enter list of contacts names: [Name1],[Name2],...\n";
            System.out.println(details);
            String[] contactsInput = getInputParserbyComma(sc);
            LinkedList<String> contactsName = new LinkedList<>(Arrays.asList(contactsInput));
            result = branchController.createSupplierCard(param[0],param[1],param[2],param[3],Integer.parseInt(param[4]),param[5],param[6],contactsName,param[7]);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion

    static private void printMainBranchMenu() {
        String menu = "";
        menu=menu.concat("Choose one of the options:\n");
        menu=menu.concat("1) Products management\n");
        menu=menu.concat("2) Category management\n");
        menu=menu.concat("3) Reports management\n");
        menu=menu.concat("4) Sales management\n");
        menu=menu.concat("5) Orders management\n");
        menu=menu.concat("6) Supplier Contracts management\n");
        menu=menu.concat("7) System status\n");
        menu=menu.concat("8) Return\n");
        menu=menu.concat("9) Exit");
        while (true){
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printProductMenu();
                    break;
                case (2):
                    printCategoryMenu();
                    break;
                case (3):
                    printReportMenu();
                    break;
                case (4):
                    printSaleMenu();
                    break;
                case (5):
                    printOrdersMenu();
                    break;
                case (6):
                    printSupplierContractsMenu();
                    break;
                case (7):
                    printDataMapperMenu();
                    break;
                case (8):
                    return;
                case (9):
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    //region Products Management
    static private void printProductMenu() {
        String menu = "Products management\n";
        menu=menu.concat("1) Edit general product\n");
        menu=menu.concat("2) Remove specific product\n");
        menu=menu.concat("3) Mark flaw specific product\n");
        menu=menu.concat("4) Change location of specific product\n");
        menu=menu.concat("5) Return\n");
        menu=menu.concat("6) exit\n");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printEditGeneralProductMenu();
                    break;
                case (2):
                    printRemoveSpecificProductMenu();
                    break;
                case (3):
                    printMarkAsFlawtMenu();
                    break;
                case (4):
                    printMoveLocationtMenu();
                    break;
                case (5):
                    return;
                case (6):
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    //region General Product
//    static private void printAddGeneralProductMenu() {
//        Result result;
//        String menu = "Please enter the following details\n";
//        menu=menu.concat("[CategoryID],[Manufacture],[CatalogID],[Name],[Supplier price],[Retail price],[Minimum quantity]");
//        System.out.println(menu);
//        String[] param = getInputParserbyComma(sc);
//        if (param.length==7){
//            result=branchController.addGeneralProduct(Integer.parseInt(param[0]),param[1],param[2],param[3],
//                    Float.parseFloat(param[4]),Float.parseFloat(param[5]),Integer.parseInt(param[6]));
//            System.out.println(result.getMessage());
//        }
//        else{
//            System.out.println("Invalid number of parameters");
//        }
//    }

//    static private void printRemoveGeneralProductMenu() {
//        Result result;
//        String menu = "Please enter the following details\n";
//        menu=menu.concat("[CategoryID],[CatalogID]");
//        System.out.println(menu);
//        String[] param = getInputParserbyComma(sc);
//        if(param.length==2){
//            result=branchController.removeGeneralProduct(Integer.parseInt(param[0]),param[1]);
//            System.out.println(result.getMessage());
//        }
//        else{
//            System.out.println("Invalid number of parameters");
//        }
//    }

    static private void printEditGeneralProductMenu() {
        Result result;
        String[] param;
        String menu = "editing general product\n";
        menu=menu.concat("1) Edit name\n");
        menu=menu.concat("2) Edit supplier price\n");
        menu=menu.concat("3) Edit retail price\n");
        menu=menu.concat("4) Edit quantity\n");
        menu=menu.concat("5) Edit minimum quantity\n");
        menu=menu.concat("6) Return\n");
        menu=menu.concat("7) Exit\n");

        while(true) {
            System.out.println(menu);
            String details = "Please enter the following details\n";
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    details=details.concat("[CatalogID],[New name]");
                    System.out.println(details);
                    param = getInputParserbyComma(sc);
                    if(param.length==2) {
                        result = branchController.editGeneralProductName(Integer.parseInt(param[0]), param[1]);
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid number of parameters");
                    }
                    break;
                case (2):
                    details=details.concat("[CatalogID],[New supplier price],[supplierID]");
                    System.out.println(details);
                    param = getInputParserbyComma(sc);
                    if(param.length==3) {
                        result = branchController.editGeneralProductSupplierPrice(Integer.parseInt(param[0]), Float.parseFloat(param[1]),Integer.parseInt(param[2]));
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid number of parameters");
                    }
                    break;
                case (3):
                    details=details.concat("[CatalogID],[New retail price]");
                    System.out.println(details);
                    param=getInputParserbyComma(sc);
                    if(param.length==2) {
                        result = branchController.editGeneralProductRetailPrice(Integer.parseInt(param[0]), Float.parseFloat(param[1]));
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid number of parameters");
                    }
                    break;
                case (4):
                    details=details.concat("[CatalogID],[New quantity]");
                    System.out.println(details);
                    param = getInputParserbyComma(sc);
                    if(param.length==2) {
                        result=branchController.editGeneralProductQuantity(Integer.parseInt(param[0]), Integer.parseInt(param[1]));
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid number of parameters");
                    }
                    break;
                case (5):
                    details=details.concat("[CatalogID],[New minimum quantity]");
                    System.out.println(details);
                    param = getInputParserbyComma(sc);
                    if(param.length==2) {
                        result=branchController.editGeneralProductMinQuantity(Integer.parseInt(param[0]), Integer.parseInt(param[1]));
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid number of parameters");
                    }
                    break;
                case (6):
                    return;
                case (7):
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    //endregion
    //region Specific Product
//    static private void printAddSpecificProductMenu() {
//        Result result;
//        String menu = "Please enter the following details\n";
//        menu=menu.concat("[CatalogID],[Expiration date (dd/mm/YYYY)],[Quantity]");
//        String[] param = getInputParserbyComma(sc);
//        if(param.length==3) {
//            Date date =convertStringToDate(param[1]);
//            if (date!=null){
//                result = branchController.addSpecificProduct(Integer.parseInt(param[0]),date,Integer.parseInt(param[2]));
//                System.out.println(result.getMessage());
//            }
//            else{
//                System.out.println("Date is not in the right format");
//            }
//        }
//        else{
//            System.out.println("Invalid number of parameters");
//        }
//    }

    static private void printRemoveSpecificProductMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Specific productID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==1) {
            result = branchController.removeSpecificProduct(Integer.parseInt(param[0]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printMarkAsFlawtMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Specific productID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            result = branchController.markAsFlaw(Integer.parseInt(param[0]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printMoveLocationtMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Specific productID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            result = branchController.moveLocation(Integer.parseInt(param[0]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion

    //endregion

    //region Category Management
    static private void printCategoryMenu() {
        String menu = "Category management\n";
        menu=menu.concat("1) Add new main category\n");
        menu=menu.concat("2) Add new sub category\n");
        menu=menu.concat("3) remove category\n");
        menu=menu.concat("4) Edit category name\n");
        menu=menu.concat("5) Return\n");
        menu=menu.concat("6) Exit\n");

        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printAddMainCategoryMenu();
                    break;
                case (2):
                    printAddSubCategory();
                    break;
                case (3):
                    printRemoveCategory();
                    break;
                case (4):
                    printEditCategoryNameMenu();
                    break;
                case (5):
                    return;
                case (6):
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    static private void printAddMainCategoryMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Name]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            result = branchController.addMainCategory(param[0]);
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printAddSubCategory() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Super categoryID],[Name]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==2) {
            Integer pred_id = Integer.parseInt(param[0]);
            result = branchController.addSubCategory(pred_id,param[1]);
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printRemoveCategory() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            result = branchController.removeCategory(Integer.parseInt(param[0]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printEditCategoryNameMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID],[Name]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==2) {
            result = branchController.editCategoryName(Integer.parseInt(param[0]),param[1]);
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion

    //region Report Management
    static private void printReportMenu() {
        String menu = "Report management\n";
        menu=menu.concat("1) Issue out of stock report\n");
        menu=menu.concat("2) Issue in-stock report\n");
        menu=menu.concat("3) Issue damaged&expired report\n");
        menu=menu.concat("4) Return\n");
        menu=menu.concat("5) Exit\n");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printOutOfStockReportMenu();
                    break;
                case (2):
                    printInStockReportMenu();
                    break;
                case (3):
                    printDNEReportMenu();
                    break;
                case (4):
                    return;
                case (5):
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }
    //region Out of Stock Report
    static private void printOutOfStockReportMenu() {
        String menu = "Choose one of the options\n";
        menu=menu.concat("1) By category\n");
        menu=menu.concat("2) By general product\n");
        menu=menu.concat("3) Return\n");
        menu=menu.concat("4) Exit\n");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printOutOfStockReportByCategoryMenu();
                    break;
                case (2):
                    printOutOfStockReportByGeneralProductMenu();
                    break;
                case (3):
                    return;
                case (4):
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    static private void printOutOfStockReportByCategoryMenu() {
        Result<Report> result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        menu=menu.concat("for all categories type 'all'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1 && (param[0].matches("[0-9]+") || param[0].equals("all"))) {
            if(param[0].equals("all")){
                result = branchController.makeReportByCategory(0, "outofstock");
            }
            else{
                result = branchController.makeReportByCategory(Integer.parseInt(param[0]), "outofstock");
            }
            System.out.println(result.getMessage());
            if (result.isOK()){
                System.out.println("Ordering out-of-stock delivery.");
                Result<String> orders_res = branchController.createOutOfStockOrder(result.getData());
                if(orders_res.isOK()){
                    System.out.println(orders_res.getData());
                } else {
                    System.out.println(orders_res.getMessage());
                }
            }

        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printOutOfStockReportByGeneralProductMenu(){
        Result<Report> result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[gpID]");
        System.out.println(menu);
        String[] param=getInputParserbyComma(sc);
        if(param.length==1) {
            result = branchController.makeReportByGeneralProduct(Integer.parseInt(param[0]), "outofstock");
            System.out.println(result.getMessage());
            if (result.isOK()){
                System.out.println("Ordering out-of-stock delivery.");
                Result<String> orders_res = branchController.createOutOfStockOrder(result.getData());
                if(orders_res.isOK()){
                    System.out.println(orders_res.getData());
                } else {
                    System.out.println(orders_res.getMessage());
                }
            }
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion

    //region In-Stock Report
    static private void printInStockReportMenu() {
        String menu = "Choose one of the options\n";
        menu=menu.concat("1) By category\n");
        menu=menu.concat("2) By general product\n");
        menu=menu.concat("3) Return\n");
        menu=menu.concat("4) Exit\n");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printInStocReportByCategoryMenu();
                    break;
                case (2):
                    printInStocReportByGeneralProductMenu();
                    break;
                case (3):
                    return;
                case (4):
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    static private void printInStocReportByCategoryMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        menu=menu.concat("for all categories type 'all'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            if(param[0].equals("all")){
                result = branchController.makeReportByCategory(0, "instock");
            }
            else{
                result = branchController.makeReportByCategory(Integer.parseInt(param[0]), "instock");
            }
            System.out.println(result.getMessage());
        }
    }

    static private void printInStocReportByGeneralProductMenu(){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==1) {
            result = branchController.makeReportByGeneralProduct(Integer.parseInt(param[0]), "instock");
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion

    //region Damaged and Expired Report
    static private void printDNEReportMenu() {
        String menu = "Choose one of the options\n";
        menu=menu.concat("1) By category\n");
        menu=menu.concat("2) By general product\n");
        menu=menu.concat("3) Return\n");
        menu=menu.concat("4) Exit\n");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printDNEReportByCategoryMenu();
                    break;
                case (2):
                    printDNEReportByGeneralProductMenu();
                    break;
                case (3):
                    return;
                case (4):
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    static private void printDNEReportByCategoryMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        menu=menu.concat("for all categories type 'all'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            if(param[0].equals("all")){
                result = branchController.makeReportByCategory(0, "dne");
            }
            else{
                result = branchController.makeReportByCategory(Integer.parseInt(param[0]), "dne");
            }
            System.out.println(result.getMessage());
        }
    }

    static private void printDNEReportByGeneralProductMenu(){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==1) {
            result = branchController.makeReportByGeneralProduct(Integer.parseInt(param[0]), "dne");
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion

    //endregion

    //region Sale Management
    static private void printSaleMenu() {
        String menu = "Sales management\n";
        menu=menu.concat("1) Add new sale\n");
        menu=menu.concat("2) Cancel sale\n");
        menu=menu.concat("3) Check sales status\n");
        menu=menu.concat("4) Return\n");
        menu=menu.concat("5) Exit\n");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printAddSaleMenu();
                    break;
                case (2):
                    printRemoveSaleMenu();
                    break;
                case (3):
                    checkSaleSatus();
                    break;
                case (4):
                    return;
                case (5):
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    //region Add Sale
    static private void printAddSaleMenu() {
        String menu = "Choose one of the options\n";
        menu=menu.concat("1) By category\n");
        menu=menu.concat("2) By general product\n");
        menu=menu.concat("3) Return\n");
        menu=menu.concat("4) Exit\n");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printAddNewSaleByCategoryMenu();
                    break;
                case (2):
                    printAddNewSaleByGeneralProductMenu();
                    break;
                case (3):
                    return;
                case (4):
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }     }

    static private void printAddNewSaleByCategoryMenu(){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID],[Discount percentage\\Fixed price],[Optional: %],[Optional: Start date (dd/mm/YYYY)],[Must if apply 'Start date': End date (dd/mm/YYYY)]\n");
        menu=menu.concat("for discount by percentage please add '%'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==2) {
            result = branchController.addSaleByCategory(Integer.parseInt(param[0]),"fix",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length==3){
            result = branchController.addSaleByCategory(Integer.parseInt(param[0]),"percentage",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length==4){
            Date start_date=convertStringToDate(param[2]);
            Date end_date =convertStringToDate(param[3]);
            if(start_date!=null && end_date!=null){
                result = branchController.addSaleByCategory(Integer.parseInt(param[0]),"fix",Float.parseFloat(param[1]),start_date,end_date);
                System.out.println(result.getMessage());
            }
            else{
                System.out.println("One of the dates was not inserted as the format.");
            }
        }
        else if(param.length==5){
            Date start_date=convertStringToDate(param[3]);
            Date end_date =convertStringToDate(param[4]);
            if(start_date!=null && end_date!=null && param[2].equals("%")){
                result = branchController.addSaleByCategory(Integer.parseInt(param[0]),"percentage",Float.parseFloat(param[1]),start_date,end_date);
                System.out.println(result.getMessage());
            }
            else{
                System.out.println("One of the dates was not inserted as the format or the '%' did not inserted");
            }
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    static private void printAddNewSaleByGeneralProductMenu(){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID],[Discount percentage\\Fixed price],[Optional: %],[Optional: Start date (dd/mm/YYYY)],[Must if apply 'Start date': End date (dd/mm/YYYY)]\n");
        menu=menu.concat("for discount by percentage please add '%'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==2) {
            result = branchController.addSaleByGeneralProduct(Integer.parseInt(param[0]),"fix",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length==3){
            result = branchController.addSaleByGeneralProduct(Integer.parseInt(param[0]),"percentage",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length==4){
            Date start_date=convertStringToDate(param[2]);
            Date end_date =convertStringToDate(param[3]);
            if(start_date!=null && end_date!=null){
                result = branchController.addSaleByGeneralProduct(Integer.parseInt(param[0]),"fix",Float.parseFloat(param[1]),start_date,end_date);
                System.out.println(result.getMessage());
            }
            else{
                System.out.println("One of the dates was not inserted as the format.");
            }
        }
        else if(param.length==5){
            Date start_date=convertStringToDate(param[3]);
            Date end_date =convertStringToDate(param[4]);
            if(start_date!=null && end_date!=null && param[2].equals("%")){
                result = branchController.addSaleByGeneralProduct(Integer.parseInt(param[0]),"percentage",Float.parseFloat(param[1]),start_date,end_date);
                System.out.println(result.getMessage());
            }
            else{
                System.out.println("One of the dates was not inserted as the format or the '%' did not inserted");
            }
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion
    //region Remove Sale
    static private void printRemoveSaleMenu(){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[SaleID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==1){
            result=branchController.removeSale(Integer.parseInt(param[0]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion
    //region Check Sales Status
    static private void checkSaleSatus(){
        Result<List<Sale>> result = branchController.CheckSalesStatus();
        System.out.println(result.getMessage());
        System.out.println("Active sales:");
        List<Sale> active_sales = result.getData();
        for(Sale sale:active_sales){
            System.out.println(sale.toString());
        }
    }

    //endregion

    //endregion

    //region Supplier Contracts management
    private static void printSupplierContractsMenu() {
        String menu = "";
        menu=menu.concat("Choose one of the options:\n");
        menu=menu.concat("1) Add contract\n");
        menu=menu.concat("2) Remove contract\n");
        menu=menu.concat("3) Add product to contract\n");
        menu=menu.concat("4) Remove product from contract\n");
        menu=menu.concat("5) Add category to contract\n");
        menu=menu.concat("6) Remove category from contract\n");
        menu=menu.concat("7) Cost Engineering\n");
        menu=menu.concat("8) Return\n");
        menu=menu.concat("9) Exit\n");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    printAddContract();
                    break;
                case 2:
                    printRemoveContract();
                    break;
                case 3:
                    printAddProductToContract();
                    break;
                case 4:
                    printRemoveProductFromContract();
                    break;
                case 5:
                    printAddCategoryToContract();
                    break;
                case 6:
                    printRemoveCategoryFromContract();
                    break;
                case 7:
                    printCostEngineeringMenu();
                    break;
                case 8:
                    return;
                case 9:
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }

    }

    private static void printCostEngineeringMenu() {
        String menu = "";
        menu=menu.concat("Choose one of the options:\n");
        menu=menu.concat("1) Add cost engineering\n");
        menu=menu.concat("2) Remove cost engineering\n");
        menu=menu.concat("3) Add product\n");
        menu=menu.concat("4) Remove product\n");
        menu=menu.concat("5) Update minimum quantity\n");
        menu=menu.concat("6) Update sale price\n");
        menu=menu.concat("7) Return\n");
        menu=menu.concat("8) Exit\n");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    printAddCostEng();
                    break;
                case 2:
                    printRemoveCostEng();
                    break;
                case 3:
                    printAddProductToCostEng();
                    break;
                case 4:
                    printRemoveProductFromCostEng();
                    break;
                case 5:
                    printUpdateMinQuantityCostEng();
                    break;
                case 6:
                    printUpdateSalePriceCostEng();
                    break;
                case 7:
                    return;
                case 8:
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    private static void printUpdateSalePriceCostEng() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[catalogID],[price]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 3) {
            Integer supplierID = Integer.parseInt(param[0]);
            Integer catalogID = Integer.parseInt(param[1]);
            Float price = Float.parseFloat(param[2]);
            result = branchController.updatePriceAfterSale(supplierID,catalogID,price);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printUpdateMinQuantityCostEng() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[catalogID],[minQuantity]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 3) {
            Integer supplierID = Integer.parseInt(param[0]);
            Integer catalogID = Integer.parseInt(param[1]);
            Integer minQuan = Integer.parseInt(param[2]);
            result = branchController.updateMinQuantity(supplierID,catalogID,minQuan);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printRemoveProductFromCostEng() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[catalogID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer supplierID = Integer.parseInt(param[0]);
            Integer catalogID = Integer.parseInt(param[1]);
            result = branchController.removeProductCostEng(supplierID,catalogID);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printAddProductToCostEng() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[catalogID],[minQuantity],[price]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 4) {
            Integer supplierID = Integer.parseInt(param[0]);
            Integer catalogID = Integer.parseInt(param[1]);
            Integer minQuan = Integer.parseInt(param[2]);
            Float price = Float.parseFloat(param[3]);
            result = branchController.addProductToCostEng(supplierID,catalogID,minQuan,price);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printRemoveCostEng() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            Integer supplierID = Integer.parseInt(param[0]);
            result = branchController.removeCostEng(supplierID);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printAddCostEng() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            Integer supplierID = Integer.parseInt(param[0]);
            result = branchController.addCostEng(supplierID);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printRemoveCategoryFromContract() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[categoryName]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer supplierID = Integer.parseInt(param[0]);
            result = branchController.removeCategory(supplierID, param[1]);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printAddCategoryToContract() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[categoryName]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer supplierID = Integer.parseInt(param[0]);
            result = branchController.addCategory(supplierID,param[1]);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printRemoveProductFromContract() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[gpID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer supplierID = Integer.parseInt(param[0]);
            Integer gpID = Integer.parseInt(param[1]);
            result = branchController.removeProductFromContract(supplierID,gpID);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printAddProductToContract() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[catalogID],[gpID],[supplier_price],[supplier_category]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 5) {
            Integer supID = Integer.parseInt(param[0]);
            Integer catalogID = Integer.parseInt(param[1]);
            Integer gpID = Integer.parseInt(param[2]);
            Float sup_price = Float.parseFloat(param[3]);
            String sup_cat = param[4];
            result = branchController.addProductToContract(supID,catalogID,gpID,sup_price,sup_cat);
            System.out.println(result.getMessage());
            if (!result.isOK() && result.getMessage().substring(0, 23).matches("General Product with ID")){
                menu="Enter the following details to create new General Product before adding to contract:\n";
                menu=menu.concat("[category_id],[manufacture],[retail_price],[min_quantity],[name]");
                System.out.println(menu);
                String[] addDetails = getInputParserbyComma(sc);
                if (addDetails.length == 5){
                    Integer category_id = Integer.parseInt(addDetails[0]);
                    String manufacture = addDetails[1];
                    Float ret_price = Float.parseFloat(addDetails[2]);
                    Integer min_quan = Integer.parseInt(addDetails[3]);
                    String name = addDetails[4];
                    result = branchController.addGeneralProduct(category_id,manufacture,name,sup_price,ret_price,min_quan,catalogID,gpID,supID,sup_cat);
                    System.out.println(result.getMessage());
                    result = branchController.addProductToContract(supID,catalogID,gpID,sup_price,sup_cat);
                    System.out.println(result.getMessage());
                } else {
                    System.out.println("Invalid number of parameters");
                }
            }
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printRemoveContract() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            Integer supplierID = Integer.parseInt(param[0]);
            result = branchController.removeContract(supplierID);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    private static void printAddContract() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            Integer supplierID = Integer.parseInt(param[0]);
            String details= "Please enter list of categories names: [Category1],[Category2],...\n";
            System.out.println(details);
            String[] categoriesInput = getInputParserbyComma(sc);
            LinkedList<String> categories = new LinkedList<>(Arrays.asList(categoriesInput));
            result = branchController.addContract(supplierID , categories);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }

    //endregion

    //region Orders Management
    private static void printOrdersMenu() {
        String menu = "";
        menu=menu.concat("Choose one of the options:\n");
        menu=menu.concat("1) Create periodic order\n");
        menu=menu.concat("2) Remove periodic order\n");
        menu=menu.concat("3) Add product to periodic order\n");
        menu=menu.concat("4) Remove product from periodic order\n");
        menu=menu.concat("5) Update product quantity in periodic order\n");
        menu=menu.concat("6) Update periodic order supplier \n");
        menu=menu.concat("7) Accept order\n");
        menu=menu.concat("8) Display all orders\n");
        menu=menu.concat("9) Return\n");
        menu=menu.concat("10) Exit\n");
        while(true){
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch(option){
                case 1:
                    printCreatePOrder();
                    break;
                case 2:
                    printRemovePOrder();
                    break;
                case 3:
                    printAddProductToPOrder();
                    break;
                case 4:
                    printRemoveProductPOrder();
                    break;
                case 5:
                    printUpdateProductQuantity();
                    break;
                case 6:
                    printUpdateSupplierInPO();
                    break;
                case 7:
                    printAcceptOrder();
                    break;
                case 8:
                    printDisplayOrders();
                    break;
                case 9:
                    return;
                case 10:
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }

    }

    private static void printUpdateSupplierInPO() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID,supplierID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer orderID = Integer.parseInt(param[0]);
            Integer supID = Integer.parseInt(param[1]);;
            result = branchController.updateSupplierToPeriodicOrder(orderID, supID);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid number of parameters");
        }
    }

    private static void printDisplayOrders() {
        Result<LinkedList<String>> result = branchController.displayAllOrders();
        if (result.isOK()){
            System.out.println(result.getData().toString());
        } else {
            System.out.println(result.getMessage());
        }
    }
    private static void printDisplaySuppliersAndProducts() {
    }

        private static void printUpdateProductQuantity() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID,gpID,newQuantity]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 3) {
            Integer orderID = Integer.parseInt(param[0]);
            Integer gpID = Integer.parseInt(param[1]);
            Integer newQuantity = Integer.parseInt(param[2]);
            result = branchController.updateProductQuantityInPeriodicOrder(orderID, gpID, newQuantity);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid number of parameters");
        }
    }

    private static void printRemoveProductPOrder() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID,gpID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer orderID = Integer.parseInt(param[0]);
            Integer gpID = Integer.parseInt(param[1]);
            result = branchController.removeProductFromPeriodicOrder(orderID, gpID);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid number of parameters for supplier ID");
        }
    }

    private static void printAddProductToPOrder() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            Integer orderID = Integer.parseInt(param[0]);
            menu = "Please enter the following details\n";
            menu=menu.concat("[gpID,quantity]");
            System.out.println(menu);
            param = getInputParserbyComma(sc);
            if (param.length == 2) {
                Integer gpID = Integer.parseInt(param[0]);
                Integer quantity = Integer.parseInt(param[1]);
                result = branchController.addProductToPeriodicOrder(orderID,gpID,quantity);
                System.out.println(result.getMessage());
            } else {
                System.out.println("Invalid number of parameters");
            }
        } else {
            System.out.println("Invalid number of parameters");
        }
    }

    private static void printRemovePOrder() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            Integer orderID = Integer.parseInt(param[0]);
            result = branchController.removePeriodicOrder(orderID);
            System.out.println(result);
        } else {
            System.out.println("Invalid number of parameters for supplier ID");
        }
    }

    private static void printCreatePOrder() {
        LinkedList<Pair<Integer, Integer>> products = new LinkedList<>();
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplier ID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            Integer supplierID = Integer.parseInt(param[0]);
            menu = "Order will be made once a week. Choose delivery day (1- Sunday, 6- Friday):\n";
            System.out.println(menu);
            param = getInputParserbyComma(sc);
            if (param.length == 1) {
                Integer day = Integer.parseInt(param[0]);
                day--;
                printEnterProductsToPOrder(products);
                Result res = branchController.createPeriodicOrder(supplierID, products, day);
                System.out.println(res.getMessage());
            } else {
                System.out.println("Invalid number of parameters");
            }

        }else {
            System.out.println("Invalid number of parameters");
        }


    }

    private static void printEnterProductsToPOrder(LinkedList<Pair<Integer, Integer>> products) {
        String menu = "Please enter the following details\n";
        menu=menu.concat("[gpID,quantity]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2){
            Integer gpID = Integer.parseInt(param[0]);
            Integer quantity = Integer.parseInt(param[1]);
            products.add(new Pair<>(gpID, quantity));
            menu="Add more products to order?\n";
            menu=menu.concat("1) Yes\n");
            menu=menu.concat("2) No\n");
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch(option){
                case 1:
                    printEnterProductsToPOrder(products);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Option not valid, please retype");
            }
        } else {
            System.out.println("Invalid numbers of parameters");
        }
    }
    private static void printAcceptOrder() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            Integer orderID = Integer.parseInt(param[0]);
            result = branchController.acceptOrder(orderID);
            System.out.println(result.getMessage());
        } else{
            System.out.println("Invalid number of parameters");
        }

    }
    //endregion

    //region Data mapping
    private static void printDataMapperMenu(){
        String menu = "Data:\n";
        menu=menu.concat("1) Print all categories\n");
        menu=menu.concat("2) Print all general products\n");
        menu=menu.concat("3) Print all sales\n");
        menu=menu.concat("4) Print all branches\n");
        menu=menu.concat("5) Return\n");
        menu=menu.concat("6) Exit\n");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printAllCategories();
                    break;
                case (2):
                    printAllGeneralProducts();
                    break;
                case (3):
                    printAllSales();
                    break;
                case 4:
                    printAllBranches();
                    break;
                case 5:
                    return;
                case 6:
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }

    }
    private static void printAllCategories(){
        System.out.println(branchController.mapAllCategories());
    }
    private static void printAllGeneralProducts(){
        System.out.println(branchController.mapAllGeneralProducts());
    }
    private static void printAllSales(){
        System.out.println(branchController.mapAllSales());
    }
    private static void printAllBranches(){
        System.out.println(branchController.toString());
    }
    private static void printAllSuppliers() {
        System.out.println(branchController.mapAllSuppliers());
    }

    //endregion

    //region Utilities
    static private String[] getInputParserbyComma(Scanner sc){
        String user_input = getNextLine(sc);
        //System.out.println(user_input);
        user_input = user_input.replaceAll("\\s+,", ",").replaceAll(",\\s+", ",");
        String[] toreturn = user_input.split(",");
        return toreturn;
    }
    static public Date convertStringToDate(String sdate){
        try {
            Date date=new SimpleDateFormat("dd/MM/yyyy").parse(sdate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static Integer getNextInt(Scanner sc){
        while(!sc.hasNext()){}
        return Integer.parseInt(sc.nextLine());
    }
    private static String getNextLine(Scanner sc){
        while(!sc.hasNext()){}
        return sc.nextLine();
    }
    static private void Exit(){
        System.out.println("Bye!");
        exit(0);
    }

    //endregion

    //region initialize
    private static void initialize() {
        System.err.println("Warning: all of the data in the DB will be erase\n");
        BranchController.clearDB();
        branchController=new BranchController(true);
        branchController.loadID();
        // Open and switch to initial branch
        Branch initialBranch = branchController.createNewBranch("Initial branch").getData();
        branchController.switchBranch(initialBranch.getBranchId());

        //Add categories:
        //              Hygiene
        //                  Toilet Paper
        //                      30 units
        //                  Shampoo
        //                      500ml
        //                      750ml
        //              Meat%Fish
        //                  Meat
        //                      1/2kg
        //                      1kg
        //                  Fish
        //                      1/2kg
        //                      1kg
        Result res_cat_hygiene = branchController.addMainCategory("Hygiene");
        Result res_cat_tp = branchController.addSubCategory(((Category)res_cat_hygiene.getData()).getId(), "Toilet paper");
        Result res_cat_30 = branchController.addSubCategory(((Category)res_cat_tp.getData()).getId(), "30 units");
        Result res_cat_shampoo = branchController.addSubCategory(((Category)res_cat_hygiene.getData()).getId(), "Shampoo");
        Result res_cat_500ml = branchController.addSubCategory(((Category)res_cat_shampoo.getData()).getId(), "500ml");
        Result res_cat_750ml = branchController.addSubCategory(((Category)res_cat_shampoo.getData()).getId(), "750ml");
        Result res_cat_mnf = branchController.addMainCategory("Meat%Fish");
        Result res_cat_meat = branchController.addSubCategory(((Category)res_cat_mnf.getData()).getId(), "Meat");
        Result res_cat_fish = branchController.addSubCategory(((Category)res_cat_mnf.getData()).getId(), "Fish");
        Result meat_half_kg = branchController.addSubCategory(((Category)res_cat_meat.getData()).getId(), "1/2kg");
        Result meat_one_kg = branchController.addSubCategory(((Category)res_cat_meat.getData()).getId(), "1kg");
        Result fish_half_kg = branchController.addSubCategory(((Category)res_cat_fish.getData()).getId(), "1/2kg");
        Result fish_one_kg = branchController.addSubCategory(((Category)res_cat_fish.getData()).getId(), "1kg");


//-----------------------------------------------------


        Integer supplierID = 1;
        Integer catalogID = 10;
        Integer gpID = 100;
        Float sup_price = 18.5f;
        String name = "Toilet paper double layer 30u";

        LinkedList<String> contact = new LinkedList<>();
        contact.add("Moshe");
        contact.add("Rachel");

        //Create supplier halavi-lee
        branchController.createSupplierCard("halavi-lee" , "ringelbloom 97 beer-sheva" , "halavi@gmail.com" , "081234567" ,
                supplierID, "0975635" , "CreditCard" , contact,"periodic");

        LinkedList <String> categories = new LinkedList<>();
        categories.add("Hygiene");
        categories.add("Meat");

        //create contract
        branchController.addContract(supplierID, categories);

        //Create GP
        branchController.addGeneralProduct(((Category)res_cat_30.getData()).getId(), "Niguvim",name, sup_price, 31.5f,  20,catalogID,gpID,supplierID,"Hygiene");
        branchController.addSpecificProduct(gpID, convertStringToDate("11/04/2025"),21);
        //Add Product to contract
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Hygiene");

        name = "Crema shampoo for men 500ml";
        sup_price = 15.99f;
        catalogID = 9;
        gpID = 101;
        //Create GP
        branchController.addGeneralProduct(((Category)res_cat_500ml.getData()).getId(), "Crema", name, sup_price, 25.99f, 5,catalogID,gpID,supplierID,"Hygiene");
        branchController.addSpecificProduct(gpID, convertStringToDate("11/04/2025"),6);
        //Add Product to contract
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Hygiene");

        name = "Dove shampoo for women 500ml";
        sup_price = 15.99f;
        catalogID = 11;
        gpID = 102;
        //Create GP
        branchController.addGeneralProduct(((Category)res_cat_500ml.getData()).getId(), "Dove", name, sup_price, 25.99f, 5,  5,gpID,supplierID,"Hygiene");
        branchController.addSpecificProduct(gpID, convertStringToDate("11/04/2025"),6);
        //Add Product to contract
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Hygiene");

        name = "Crema shampoo for men 750ml";
        sup_price = 22.0f;
        catalogID = 12;
        gpID = 103;
        //Create GP
        branchController.addGeneralProduct(((Category)res_cat_500ml.getData()).getId(), "Crema", name, sup_price, 32.99f, 5,catalogID,gpID,supplierID,"Hygiene");
        branchController.addSpecificProduct(gpID, convertStringToDate("11/04/2025"),6);
        //Add Product to contract
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Hygiene");

        String manufacture = "Moosh";
        gpID = 104;
        name = "Moosh packed ground meat 1/2kg";
        sup_price = 30.0f;
        Float ret_price = 40.0f;
        catalogID = 13;
        branchController.addGeneralProduct(((Category)meat_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,5,catalogID,gpID,supplierID,"Meat");
        branchController.addSpecificProduct(gpID, convertStringToDate("11/04/2025"),4);
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Meat");

//-------------------------------------------


        gpID = 104;
        name = "Moosh packed ground meat 1/2kg";
        sup_price = 32.5f;
        catalogID = 20;
        supplierID = 2;

        LinkedList<String> contact2 = new LinkedList<>();
        contact.add("Yossi");

        LinkedList <String> categories2 = new LinkedList<>();
        categories2.add("Meat");
        categories2.add("Fish");


        //Create supplier niceToMeat
        branchController.createSupplierCard("niceToMeat" , "mesada 37 beer-sheva" , "niceToMeat@gmail.com" , "087594456" ,
                supplierID, "09754432", "CreditCard" , contact2, "by order");

        //create contract
        branchController.addContract(supplierID, categories2);

        ////branchController.addGeneralProduct(((Category)meat_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,5,catalogID,gpID,supplierID,"Meat");
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Meat");

        manufacture = "Moosh";
        gpID = 201;
        name = "Moosh packed ground meat 1kg";
        sup_price = 40.0f;
        ret_price = 45.0f;
        catalogID = 24;

        branchController.addGeneralProduct(((Category)meat_one_kg.getData()).getId(), manufacture,name,sup_price,ret_price,3,catalogID,gpID,supplierID,"Meat");

        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Meat");


        manufacture = "Lakerda";
        gpID = 202;
        name = "Lakerda 1/2kg semi-fresh";
        sup_price = 10.0f;
        ret_price = 13.0f;
        catalogID = 21;
        branchController.addGeneralProduct(((Category)fish_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,5,catalogID,gpID,supplierID,"Meat");
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Fish");


        manufacture = "Merluza";
        gpID = 203;
        name = "Merluza 1/2kg semi-fresh";
        sup_price = 9.5f;
        ret_price = 12.0f;
        catalogID = 22;
        branchController.addGeneralProduct(((Category)fish_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,7,catalogID,gpID,supplierID,"Fish");
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Fish");

        manufacture = "Merluza";
        gpID = 204;
        name = "Merluza 1kg semi-fresh";
        sup_price = 15.5f;
        ret_price = 17.0f;
        catalogID = 23;
        branchController.addGeneralProduct(((Category)fish_one_kg.getData()).getId(), manufacture,name,sup_price,ret_price,10,catalogID,gpID,supplierID,"Fish");
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Fish");





    }
    //endregion
}
