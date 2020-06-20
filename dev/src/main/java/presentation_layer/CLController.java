package presentation_layer;

import bussines_layer.Branch;
import bussines_layer.BranchController;
import bussines_layer.Result;
import bussines_layer.employees_module.models.ModelShift;
import bussines_layer.employees_module.models.ModelWorker;
import bussines_layer.employees_module.models.MyScanner;
import bussines_layer.inventory_module.Category;
import bussines_layer.inventory_module.Report;
import bussines_layer.inventory_module.Sale;
import javafx.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.System.exit;

public class CLController {

    private static Scanner sc;
    private static BranchController branchController;


    public CLController(){
        sc = new Scanner(System.in);    //System.in is a standard input stream
    }

    //TODO check if chooseBranch working
    public static void displayMenu() {
        printLogo();
        printInitializeMenu();
        while(true) {
            printSuperLiMenu();
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    printSuperLiManagementMenu();
                    break;
                case 2:
                    printChooseBranchMenu();
                    printBranchManagementMenu();
                    break;
                case 3:
                    printChooseBranchMenu();
                    printInventoryManagementMenu();
                    break;
                case 4:
                    printChooseBranchMenu();
                    printHumanResourcesManagementMenu();
                    break;
                case 5:
                    printChooseBranchMenu();
                    printLogisticManagementMenu();
                    break;
                case 6:
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
                case 7:
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
        menu = menu.concat("\nChoose one of the options:\n");
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
        menu = menu.concat("\nChoose your position in our team:\n");
        menu = menu.concat("1) SuperLi manager\n");
        menu = menu.concat("2) Branch manager\n");
        menu = menu.concat("3) Inventory manager\n");
        menu = menu.concat("4) Human Resources manager\n");
        menu = menu.concat("5) Logistic manager\n");
        menu = menu.concat("6) Simulate to next day\n");
        menu = menu.concat("7) Exit");
        System.out.println(menu);
    }

    private static void printChooseBranchMenu() {
        String menu = "";
        menu = menu.concat("\nChoose branch:\n");
        Integer option = 1;
        for(Integer branch_id : branchController.getBranches().keySet()){
            menu = menu.concat(String.format("%d) %s (ID: %d)\n", option, branchController.getBranches().get(branch_id), branch_id));
            option++;
        }
        menu = menu.concat(String.format("%d) Exit\n", branchController.getBranches().keySet().size()+1));
        while(true){
            System.out.println(menu);
            Integer input = getNextInt(sc);
            if( (input>branchController.getBranches().keySet().size()+2) || input <=0 ){
                System.out.println("This is not a valid option");
                continue;
            }
            if (input.equals(branchController.getBranches().keySet().size()+1)){
                Exit();
            }
            Result res = branchController.switchBranch((Integer)branchController.getBranches().keySet().toArray()[input-1]);
            System.out.println(res.getMessage());
            if (res.isOK()) {
                //printMainBranchMenu();
                return;
            }
        }
    }

    //region SuperLi Management
    private static void printSuperLiManagementMenu() {
        String menu = "";
        menu = menu.concat("\nChoose one of the options:\n");
        menu = menu.concat("1) Open new branch\n");
        menu = menu.concat("2) Remove branch\n");
        menu = menu.concat("3) Edit branch name\n");
        menu = menu.concat("4) Print all branches\n");
        menu = menu.concat("5) Suppliers Management\n");
        menu = menu.concat("6) Return\n");
        menu = menu.concat("7) Exit");
        while (true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    printOpenNewBranchMenu();
                    break;
                case 2:
                    printRemoveBranchMenu();
                    break;
                case 3:
                    printEditBranchNameMenu();
                    break;
                case 4:
                    printAllBranches();
                    break;
                case 5:
                    printSuppliersMenu();
                    break;
                case 6:
                    return;
                case 7:
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
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
            System.out.println("Invalid parameters");
        }
    }

    private static void printRemoveBranchMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[branchID]");
        System.out.println(menu);
        Integer branchID = getNextInt(sc);
        result = branchController.removeBranch(branchID);
        System.out.println(result.getMessage());
    }

    private static void printEditBranchNameMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[branchID],[newName]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2 && param[0].matches("[0-9]+")) {
            result = branchController.editBranchName(Integer.parseInt(param[0]), param[1]);
            System.out.println(result.getMessage());
        }
    }

    //region Supplier Controller
    private static void printSuppliersMenu() {
        String menu = "";
        menu = menu.concat("\nChoose one of the options:\n");
        menu = menu.concat("1) Create new supplier card\n");
        menu = menu.concat("2) Update supplier card\n");
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
        menu=menu.concat("\nChoose detail to edit:\n");
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
        Integer supplierID = getNextInt(sc);
        String details= "Please enter list of contacts names to add: [Name1],[Name2],...";
        System.out.println(details);
        String[] contactsInput = getInputParserbyComma(sc);
        LinkedList<String> contactsName = new LinkedList<>(Arrays.asList(contactsInput));
        result = branchController.AddContactName(supplierID,contactsName);
        System.out.println(result.getMessage());
    }

    private static void printDeleteContactName() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[Contact name to delete]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2 && param[0].matches("[0-9]+")) {
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
        if (param.length == 2 && param[0].matches("[0-9]+")) {
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
        if (param.length == 2 && param[0].matches("[0-9]+")) {
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
        if (param.length == 2 && param[0].matches("[0-9]+")) {
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
        if (param.length == 2 && param[0].matches("[0-9]+")) {
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
        if (param.length == 2 && param[0].matches("[0-9]+")) {
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
        if (param.length == 2 && param[0].matches("[0-9]+")) {
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
        if (param.length == 2 && param[0].matches("[0-9]+")) {
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
        menu=menu.concat("[supplierName],[address],[Email],[PhoneNumber],[supplier_id],[BankAccountNumber],[Payment],[by order/fix days/self delivery],[optional: fix_day]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 8 && param[4].matches("[0-9]+")) {
            String details= "Please enter list of contacts names: [Name1],[Name2],...\n";
            System.out.println(details);
            String[] contactsInput = getInputParserbyComma(sc);
            LinkedList<String> contactsName = new LinkedList<>(Arrays.asList(contactsInput));
            result = branchController.createSupplierCard(param[0],param[1],param[2],param[3],Integer.parseInt(param[4]),param[5],param[6],contactsName,param[7]);
            System.out.println(result.getMessage());
        }
        else if (param.length == 9 && param[7].equals("fix days") && param[4].matches("[0-9]+") && param[8].matches("[1-7]")) {
            String details= "Please enter list of contacts names: [Name1],[Name2],...\n";
            System.out.println(details);
            String[] contactsInput = getInputParserbyComma(sc);
            LinkedList<String> contactsName = new LinkedList<>(Arrays.asList(contactsInput));
            result = branchController.createSupplierCard(param[0],param[1],param[2],param[3],Integer.parseInt(param[4]),param[5],param[6],contactsName,param[7],Integer.parseInt(param[8])-1);
            System.out.println(result.getMessage());
        }
        else {
            System.out.println("Invalid parameters");
        }
    }
    //endregion

    //endregion

    //region Branch Management

    private static void printBranchManagementMenu() {
        String menu = "";
        menu = menu.concat("\nChoose one of the options:\n");
        menu = menu.concat("1) Reports Management\n");
        menu = menu.concat("2) Sales Management\n");
        menu = menu.concat("3) Suppliers Contracts Management\n");
        menu = menu.concat("4) Return\n");
        menu = menu.concat("5) Exit");
        while (true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    printReportMenu();
                    break;
                case 2:
                    printSaleMenu();
                    break;
                case 3:
                    printSupplierContractsMenu();
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

    //region Report Management
    static private void printReportMenu() {
        String menu = "Report management\n";
        menu=menu.concat("1) Issue out of stock report by category\n");
        menu=menu.concat("2) Issue out of stock report by General Product\n");
        menu=menu.concat("3) Issue in-stock report by category\n");
        menu=menu.concat("4) Issue in-stock report by General Product\n");
        menu=menu.concat("5) Issue damaged&expired report by category\n");
        menu=menu.concat("6) Issue damaged&expired report by General Product\n");
        menu=menu.concat("7) Return\n");
        menu=menu.concat("8) Exit");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    printOutOfStockReportByCategoryMenu();
                    break;
                case 2:
                    printOutOfStockReportByGeneralProductMenu();
                    break;
                case 3:
                    printInStocReportByCategoryMenu();
                    break;
                case 4:
                    printInStocReportByGeneralProductMenu();
                    break;
                case 5:
                    printDNEReportByCategoryMenu();
                    break;
                case 6:
                    printDNEReportByGeneralProductMenu();
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
    //region Out of Stock Report
//    static private void printOutOfStockReportMenu() {
//        String menu = "\nChoose one of the options\n";
//        menu=menu.concat("1) By category\n");
//        menu=menu.concat("2) By general product\n");
//        menu=menu.concat("3) Return\n");
//        menu=menu.concat("4) Exit");
//        while(true) {
//            System.out.println(menu);
//            Integer option = getNextInt(sc);
//            switch (option) {
//                case (1):
//                    printOutOfStockReportByCategoryMenu();
//                    break;
//                case (2):
//                    printOutOfStockReportByGeneralProductMenu();
//                    break;
//                case (3):
//                    return;
//                case (4):
//                    Exit();
//                default:
//                    System.out.println("Option not valid, please retype");
//            }
//        }
//    }

    static private void printOutOfStockReportByCategoryMenu() {
        Result<Report> result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        menu=menu.concat("for all categories type 'all'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1 && (param[0].matches("[0-9]+") || param[0].equals("all"))) {
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
            System.out.println("Invalid parameters");
        }
    }

    static private void printOutOfStockReportByGeneralProductMenu(){
        Result<Report> result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[gpID]");
        System.out.println(menu);
        Integer gpID = getNextInt(sc);
        result = branchController.makeReportByGeneralProduct(gpID, "outofstock");
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
    //endregion

    //region In-Stock Report
//    static private void printInStockReportMenu() {
//        String menu = "\nChoose one of the options\n";
//        menu=menu.concat("1) By category\n");
//        menu=menu.concat("2) By general product\n");
//        menu=menu.concat("3) Return\n");
//        menu=menu.concat("4) Exit");
//        while(true) {
//            System.out.println(menu);
//            Integer option = getNextInt(sc);
//            switch (option) {
//                case (1):
//                    printInStocReportByCategoryMenu();
//                    break;
//                case (2):
//                    printInStocReportByGeneralProductMenu();
//                    break;
//                case (3):
//                    return;
//                case (4):
//                    Exit();
//                default:
//                    System.out.println("Option not valid, please retype");
//            }
//        }
//    }

    static private void printInStocReportByCategoryMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        menu=menu.concat("for all categories type 'all'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1 && (param[0].matches("[0-9]+") || param[0].equals("all"))) {
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
        Integer cat_id = getNextInt(sc);
        result = branchController.makeReportByGeneralProduct(cat_id, "instock");
        System.out.println(result.getMessage());
    }
    //endregion

    //region Damaged and Expired Report
//    static private void printDNEReportMenu() {
//        String menu = "\nChoose one of the options\n";
//        menu=menu.concat("1) By category\n");
//        menu=menu.concat("2) By general product\n");
//        menu=menu.concat("3) Return\n");
//        menu=menu.concat("4) Exit");
//        while(true) {
//            System.out.println(menu);
//            Integer option = getNextInt(sc);
//            switch (option) {
//                case (1):
//                    printDNEReportByCategoryMenu();
//                    break;
//                case (2):
//                    printDNEReportByGeneralProductMenu();
//                    break;
//                case (3):
//                    return;
//                case (4):
//                    Exit();
//                default:
//                    System.out.println("Option not valid, please retype");
//            }
//        }
//    }

    static private void printDNEReportByCategoryMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        menu=menu.concat("for all categories type 'all'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1 && (param[0].matches("[0-9]+") || param[0].equals("all"))) {
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
        Integer cat_id = getNextInt(sc);
        result = branchController.makeReportByGeneralProduct(cat_id, "dne");
        System.out.println(result.getMessage());
    }
    //endregion

    //endregion

    //region Sale Management
    static private void printSaleMenu() {
        String menu = "Sales management\n";
        menu=menu.concat("1) Add new sale by category\n");
        menu=menu.concat("2) Add new sale by General Product\n");
        menu=menu.concat("3) Cancel sale\n");
        menu=menu.concat("4) Check sales status\n");
        menu=menu.concat("5) Return\n");
        menu=menu.concat("6) Exit");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    printAddNewSaleByCategoryMenu();
                    break;
                case 2:
                    printAddNewSaleByGeneralProductMenu();
                    break;
                case 3:
                    printRemoveSaleMenu();
                    break;
                case 4:
                    checkSaleSatus();
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

    //region Add Sale
//    static private void printAddSaleMenu() {
//        String menu = "\nChoose one of the options\n";
//        menu=menu.concat("1) By category\n");
//        menu=menu.concat("2) By general product\n");
//        menu=menu.concat("3) Return\n");
//        menu=menu.concat("4) Exit");
//        while(true) {
//            System.out.println(menu);
//            Integer option = getNextInt(sc);
//            switch (option) {
//                case (1):
//                    printAddNewSaleByCategoryMenu();
//                    break;
//                case (2):
//                    printAddNewSaleByGeneralProductMenu();
//                    break;
//                case (3):
//                    return;
//                case (4):
//                    Exit();
//                default:
//                    System.out.println("Option not valid, please retype");
//            }
//        }
//    }

    static private void printAddNewSaleByCategoryMenu(){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID],[Discount percentage\\Fixed price],[Optional: %],[Optional: Start date (dd/mm/YYYY)],[Must if apply 'Start date': End date (dd/mm/YYYY)]\n");
        menu=menu.concat("for discount by percentage please add '%'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length == 2 && param[0].matches("[0-9]+")) {
            result = branchController.addSaleByCategory(Integer.parseInt(param[0]),"fix",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length == 3 && param[0].matches("[0-9]+")){
            result = branchController.addSaleByCategory(Integer.parseInt(param[0]),"percentage",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length == 4 && param[0].matches("[0-9]+")){
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
        else if(param.length == 5 && param[0].matches("[0-9]+")){
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
            System.out.println("Invalid parameters");
        }
    }
    static private void printAddNewSaleByGeneralProductMenu(){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID],[Discount percentage\\Fixed price],[Optional: %],[Optional: Start date (dd/mm/YYYY)],[Must if apply 'Start date': End date (dd/mm/YYYY)]\n");
        menu=menu.concat("for discount by percentage please add '%'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param[0].matches("[0-9]+")){
            if (param.length == 2) {
                result = branchController.addSaleByGeneralProduct(Integer.parseInt(param[0]), "fix", Float.parseFloat(param[1]));
                System.out.println(result.getMessage());
            } else if (param.length == 3) {
                result = branchController.addSaleByGeneralProduct(Integer.parseInt(param[0]), "percentage", Float.parseFloat(param[1]));
                System.out.println(result.getMessage());
            } else if (param.length == 4) {
                Date start_date = convertStringToDate(param[2]);
                Date end_date = convertStringToDate(param[3]);
                if (start_date != null && end_date != null) {
                    result = branchController.addSaleByGeneralProduct(Integer.parseInt(param[0]), "fix", Float.parseFloat(param[1]), start_date, end_date);
                    System.out.println(result.getMessage());
                } else {
                    System.out.println("One of the dates was not inserted as the format.");
                }
            } else if (param.length == 5) {
                Date start_date = convertStringToDate(param[3]);
                Date end_date = convertStringToDate(param[4]);
                if (start_date != null && end_date != null && param[2].equals("%")) {
                    result = branchController.addSaleByGeneralProduct(Integer.parseInt(param[0]), "percentage", Float.parseFloat(param[1]), start_date, end_date);
                    System.out.println(result.getMessage());
                } else {
                    System.out.println("One of the dates was not inserted as the format or the '%' did not inserted");
                }
            }
        }
        else{
            System.out.println("Invalid parameters");
        }
    }
    //endregion
    //region Remove Sale
    static private void printRemoveSaleMenu(){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[SaleID]");
        System.out.println(menu);
        Integer sale_id = getNextInt(sc);
        result=branchController.removeSale(sale_id);
        System.out.println(result.getMessage());
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
        menu=menu.concat("\nChoose one of the options:\n");
        menu=menu.concat("1) Add contract\n");
        menu=menu.concat("2) Remove contract\n");
        menu=menu.concat("3) Add product to contract\n");
        menu=menu.concat("4) Remove product from contract\n");
        menu=menu.concat("5) Add category to contract\n");
        menu=menu.concat("6) Remove category from contract\n");
        menu=menu.concat("7) Cost Engineering\n");
        menu=menu.concat("8) Return\n");
        menu=menu.concat("9) Exit");
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
        menu=menu.concat("\nChoose one of the options:\n");
        menu=menu.concat("1) Add cost engineering\n");
        menu=menu.concat("2) Remove cost engineering\n");
        menu=menu.concat("3) Add product\n");
        menu=menu.concat("4) Remove product\n");
        menu=menu.concat("5) Update minimum quantity\n");
        menu=menu.concat("6) Update sale price\n");
        menu=menu.concat("7) Return\n");
        menu=menu.concat("8) Exit");
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
        if (param.length == 3 && param[0].matches("[0-9]+") && param[1].matches("[0-9]+") && param[2].matches("[0-9]+")) {
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
        if (param.length == 3 && param[0].matches("[0-9]+") && param[1].matches("[0-9]+") && param[2].matches("[0-9]+")) {
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
        if (param.length == 2 && param[0].matches("[0-9]+") && param[1].matches("[0-9]+")) {
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
        if (param.length == 4 && param[0].matches("[0-9]+") && param[1].matches("[0-9]+") && param[2].matches("[0-9]+") && param[3].matches("[0-9]+")) {
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
        Integer supplierID = getNextInt(sc);
        result = branchController.removeCostEng(supplierID);
        System.out.println(result.getMessage());
    }

    private static void printAddCostEng() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID]");
        System.out.println(menu);
        Integer supplierID = getNextInt(sc);
        result = branchController.addCostEng(supplierID);
        System.out.println(result.getMessage());
    }

    private static void printRemoveCategoryFromContract() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID],[categoryName]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2 && param[0].matches("[0-9]+")) {
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
        if (param.length == 2 && param[0].matches("[0-9]+")) {
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
        if (param.length == 2 && param[0].matches("[0-9]+") && param[1].matches("[0-9]+")) {
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
        if (param.length == 5 && param[0].matches("[0-9]+") && param[1].matches("[0-9]+") && param[2].matches("[0-9]+") && param[3].matches("[0-9]+")) {
            Integer supID = Integer.parseInt(param[0]);
            Integer catalogID = Integer.parseInt(param[1]);
            Integer gpID = Integer.parseInt(param[2]);
            Float sup_price = Float.parseFloat(param[3]);
            String sup_cat = param[4];
            result = branchController.addProductToContract(supID,catalogID,gpID,sup_price,sup_cat);
            System.out.println(result.getMessage());
            if (!result.isOK() && result.getMessage().substring(0, 23).matches("General Product with ID")){
                menu="Enter the following details to create new General Product before adding to contract:\n";
                menu=menu.concat("[category_id],[manufacture],[retail_price],[min_quantity],[name][weight]");
                System.out.println(menu);
                String[] addDetails = getInputParserbyComma(sc);
                if (addDetails.length == 6 && addDetails[0].matches("[0-9]+") && addDetails[3].matches("[0-9]+") && floatParse(addDetails[5])!=(-1)){
                    Integer category_id = Integer.parseInt(addDetails[0]);
                    String manufacture = addDetails[1];
                    Float ret_price = Float.parseFloat(addDetails[2]);
                    Integer min_quan = Integer.parseInt(addDetails[3]);
                    String name = addDetails[4];
                    Float weight = floatParse(addDetails[5]);
                    result = branchController.addGeneralProduct(category_id,manufacture,name,sup_price,ret_price,min_quan,catalogID,gpID,supID,sup_cat, weight);
                    System.out.println(result.getMessage());
                    result = branchController.addProductToContract(supID,catalogID,gpID,sup_price,sup_cat);
                    System.out.println(result.getMessage());
                } else {
                    System.out.println("Invalid parameters");
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
        Integer supplierID = getNextInt(sc);
        result = branchController.removeContract(supplierID);
        System.out.println(result.getMessage());
    }

    private static void printAddContract() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplierID]");
        System.out.println(menu);
        Integer supplierID = getNextInt(sc);
        String details= "Please enter list of categories names: [Category1],[Category2],...\n";
        System.out.println(details);
        String[] categoriesInput = getInputParserbyComma(sc);
        LinkedList<String> categories = new LinkedList<>(Arrays.asList(categoriesInput));
        result = branchController.addContract(supplierID , categories);
        System.out.println(result.getMessage());
    }

    //endregion

    //endregion

    //region Inventory Management
    static private void printInventoryManagementMenu() {
        String menu = "";
        menu=menu.concat("\nChoose one of the options:\n");
        menu=menu.concat("1) Products management\n");
        menu=menu.concat("2) Category management\n");
        menu=menu.concat("3) Orders management\n");
        menu=menu.concat("4) Supplier Contracts management\n");
 //       menu=menu.concat("5) System status\n");
        menu=menu.concat("5) Return\n");
        menu=menu.concat("6) Exit");
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
                    printOrdersMenu();
                    break;
                case (4):
                    printSupplierContractsMenu();
                    break;
//                case (5):
//                    printDataMapperMenu();
//                    break;
                case (5):
                    return;
                case (6):
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    //region Products Management
    static private void printProductMenu() {
        String menu = "Products management\n";
        menu=menu.concat("1) Update General Product\n");
        menu=menu.concat("2) Remove Specific Product\n");
        menu=menu.concat("3) Mark as flaw Specific Product\n");
        menu=menu.concat("4) Change location of Specific Product\n");
        menu=menu.concat("5) Return\n");
        menu=menu.concat("6) Exit");
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

    static private void printEditGeneralProductMenu() {
        Result result;
        String[] param;
        String menu = "editing general product\n";
        menu=menu.concat("1) Edit name\n");
        menu=menu.concat("2) Edit retail price\n");
        menu=menu.concat("3) Edit minimum quantity\n");
        menu=menu.concat("4) Return\n");
        menu=menu.concat("5) Exit");

        while(true) {
            System.out.println(menu);
            String details = "Please enter the following details\n";
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    details=details.concat("[gpID],[New name]");
                    System.out.println(details);
                    param = getInputParserbyComma(sc);
                    if(param.length == 2 && param[0].matches("[0-9]+")) {
                        result = branchController.editGeneralProductName(Integer.parseInt(param[0]), param[1]);
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid parameters");
                    }
                    break;
                case (2):
                    details=details.concat("[gpID],[New retail price]");
                    System.out.println(details);
                    param=getInputParserbyComma(sc);
                    if(param.length == 2 && param[0].matches("[0-9]+") && param[1].matches("[0-9]+")) {
                        result = branchController.editGeneralProductRetailPrice(Integer.parseInt(param[0]), Float.parseFloat(param[1]));
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid parameters");
                    }
                    break;
                case (3):
                    details=details.concat("[gpID],[New minimum quantity]");
                    System.out.println(details);
                    param = getInputParserbyComma(sc);
                    if(param.length == 2 && param[0].matches("[0-9]+") && param[1].matches("[0-9]+")) {
                        result=branchController.editGeneralProductMinQuantity(Integer.parseInt(param[0]), Integer.parseInt(param[1]));
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid parameters");
                    }
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

    //endregion
    //region Specific Product

    static private void printRemoveSpecificProductMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Specific productID]");
        System.out.println(menu);
        Integer sp_id = getNextInt(sc);
        result = branchController.removeSpecificProduct(sp_id);
        System.out.println(result.getMessage());
    }

    static private void printMarkAsFlawtMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Specific productID]");
        System.out.println(menu);
        Integer sp_id = getNextInt(sc);
        result = branchController.markAsFlaw(sp_id);
        System.out.println(result.getMessage());
    }

    static private void printMoveLocationtMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Specific productID]");
        System.out.println(menu);
        Integer sp_id = getNextInt(sc);
        result = branchController.moveLocation(sp_id);
        System.out.println(result.getMessage());
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
        menu=menu.concat("6) Exit");

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
        if (param.length == 1) {
            result = branchController.addMainCategory(param[0]);
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid parameters");
        }
    }

    static private void printAddSubCategory() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Super categoryID],[Name]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2 && param[0].matches("[0-9]+")) {
            Integer pred_id = Integer.parseInt(param[0]);
            result = branchController.addSubCategory(pred_id,param[1]);
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid parameters");
        }
    }

    static private void printRemoveCategory() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]");
        System.out.println(menu);
        Integer cat_id = getNextInt(sc);
        result = branchController.removeCategory(cat_id);
        System.out.println(result.getMessage());
    }

    static private void printEditCategoryNameMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID],[Name]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2 && param[0].matches("[0-9]+")) {
            result = branchController.editCategoryName(Integer.parseInt(param[0]),param[1]);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid parameters");
        }
    }
    //endregion

    //region Orders Management
    private static void printOrdersMenu() {
        String menu = "";
        menu=menu.concat("\nChoose one of the options:\n");
        menu=menu.concat("1) Create periodic order\n");
        menu=menu.concat("2) Remove periodic order\n");
        menu=menu.concat("3) Update periodic order\n");
        menu=menu.concat("4) Accept Transport Order\n");
        menu=menu.concat("5) Display all orders\n");
        menu=menu.concat("6) Return\n");
        menu=menu.concat("7) Exit");
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
                    printUpdatePeriodicOrderMenu();
                    break;
                case 4:
                    printAcceptOrder();
                    break;
                case 5:
                    printDisplayOrders();
                    break;
                case 6:
                    return;
                case 7:
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }

    }

    private static void printUpdatePeriodicOrderMenu() {
        String menu = "";
        menu=menu.concat("\nChoose one of the options:\n");
        menu=menu.concat("1) Add product to periodic order\n");
        menu=menu.concat("2) Update product quantity in periodic order\n");
        menu=menu.concat("3) Remove product from periodic order\n");
        menu=menu.concat("4) Update periodic order supplier \n");
        menu=menu.concat("5) Return\n");
        menu=menu.concat("6) Exit");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    printAddProductToPOrder();
                    break;
                case 2:
                    printUpdateProductQuantity();
                    break;
                case 3:
                    printRemoveProductPOrder();
                    break;
                case 4:
                    printUpdateSupplierInPO();
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

    private static void printUpdateSupplierInPO() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID,supplierID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2 && param[0].matches("[0-9]+") && param[1].matches("[0-9]+")) {
            Integer orderID = Integer.parseInt(param[0]);
            Integer supID = Integer.parseInt(param[1]);;
            result = branchController.updateSupplierToPeriodicOrder(orderID, supID);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid parameters");
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

    private static void printUpdateProductQuantity() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID,gpID,newQuantity]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 3 && param[0].matches("[0-9]+")  && param[1].matches("[0-9]+")  && param[2].matches("[0-9]+") ) {
            Integer orderID = Integer.parseInt(param[0]);
            Integer gpID = Integer.parseInt(param[1]);
            Integer newQuantity = Integer.parseInt(param[2]);
            result = branchController.updateProductQuantityInPeriodicOrder(orderID, gpID, newQuantity);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid parameters");
        }
    }

    private static void printRemoveProductPOrder() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID,gpID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2 && param[0].matches("[0-9]+") && param[1].matches("[0-9]+")) {
            Integer orderID = Integer.parseInt(param[0]);
            Integer gpID = Integer.parseInt(param[1]);
            result = branchController.removeProductFromPeriodicOrder(orderID, gpID);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid parameters for supplier ID");
        }
    }

    private static void printAddProductToPOrder() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID]");
        System.out.println(menu);
        Integer orderID = getNextInt(sc);
        menu = "Please enter the following details\n";
        menu=menu.concat("[gpID,quantity]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2 && param[0].matches("[0-9]+") && param[1].matches("[0-9]+")) {
            Integer gpID = Integer.parseInt(param[0]);
            Integer quantity = Integer.parseInt(param[1]);
            result = branchController.addProductToPeriodicOrder(orderID,gpID,quantity);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid parameters");
        }
    }

    private static void printRemovePOrder() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID]");
        System.out.println(menu);
        Integer orderID = getNextInt(sc);
        result = branchController.removePeriodicOrder(orderID);
        System.out.println(result.getMessage());
    }

    private static void printCreatePOrder() {
        LinkedList<Pair<Integer, Integer>> products = new LinkedList<>();
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplier ID]");
        System.out.println(menu);
        Integer supplierID = getNextInt(sc);
        menu = "Order will be made once a week. Choose delivery day (1- Sunday, 7- Saturday):";
        System.out.println(menu);
        Integer day = getNextInt(sc);
        if (day >= 1 && day <= 7) {
            day--;
            if (printEnterProductsToPOrder(products)) {
                Result res = branchController.createPeriodicOrder(supplierID, products, day);
                System.out.println(res.getMessage());
            }
        } else {
            System.out.println("Invalid number");
        }
    }

    private static boolean printEnterProductsToPOrder(LinkedList<Pair<Integer, Integer>> products) {
        boolean return_val = false;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[gpID,quantity]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2 && param[0].matches("[0-9]+") && param[1].matches("[0-9]+")){
            Integer gpID = Integer.parseInt(param[0]);
            Integer quantity = Integer.parseInt(param[1]);
            products.add(new Pair<>(gpID, quantity));
            menu="Add more products to order?\n";
            menu=menu.concat("1) Yes\n");
            menu=menu.concat("2) No");
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch(option){
                case 1:
                    return_val = printEnterProductsToPOrder(products);
                    break;
                case 2:
                    return_val =  true;
                    break;
                default:
                    System.out.println("Option not valid, please retype");
            }
        } else {
            System.out.println("Invalid numbers of parameters");
            return_val = false;
        }
        return return_val;
    }

    //TODO check function (AcceptTransportOrder)
    private static void printAcceptOrder() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID]");
        System.out.println(menu);
        Integer orderID = getNextInt(sc);
        result = branchController.acceptOrder(orderID);
        System.out.println(result.getMessage());

    }
    //endregion

    //endregion

    //region Human Resources Management

    public static void printHumanResourcesManagementMenu() {
        String menu = "";
        menu = menu.concat("\nChoose one of the options:\n");
        menu = menu.concat("1) Manage Workers\n");
        menu = menu.concat("2) Manage Workers Availability\n");
        menu = menu.concat("3) Manage Schedule\n");
        menu = menu.concat("4) Return\n");
        menu = menu.concat("5) Exit");
        while (true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    printManageWorkersMenu();
                    break;
                case 2:
                    printManageWorkersAvailability();
                    break;
                case 3:
                    printManageScheduleMenu();
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

    //region Schedule
    private static void printManageScheduleMenu() {
        String menu = "";
        menu = menu.concat("\nChoose one of the options:\n");
        menu = menu.concat("1) Display Weekly Schedule\n");
        menu = menu.concat("2) Construct Shift\n");
        menu = menu.concat("3) Remove shift\n");
        menu = menu.concat("4) Return\n");
        menu = menu.concat("5) Exit");
        while (true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    displayWeeklySchedule();
                    break;
                case 2:
                    printConstructShiftMenu();
                    break;
                case 3:
                    printRemoveShift();
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

    //region Schedule Methods
    private static void printRemoveShift() {
        Date date=getDateFromUser();
        boolean timeOfDay=getTimeOfDayFromUser();
        String output=branchController.removeShift(date,timeOfDay);
        if(output!=null)
            System.out.println(output);
    }

    private static void displayWeeklySchedule() {
        Date date=getDateFromUser();
        List<ModelShift>mw=branchController.getWeeklyShifts(date);
        if(mw==null)
            System.out.println("Invalid Date or no scheduled shifts for given date");
        else {
            SimpleDateFormat myFormat = new SimpleDateFormat("EE MMMM dd, yyyy",Locale.US);
            String DateString = myFormat.format(mw.get(0).date);
            System.out.println("Displaying shifts for week of " + DateString+ ":");
            for (ModelShift ms : mw) {
                System.out.println(ms.minimizedView());
            }
        }
    }
    private static void printConstructShiftMenu() {
        boolean creation=ChooseCreationOrEdit();
        Date date=getDateFromUser();
        String output=null;
        boolean isMorningShift=getTimeOfDayFromUser();
        if(creation) {
            output=branchController.createShift(date,isMorningShift);
        }
        else{
            output=branchController.editShift(date,isMorningShift);
        }
        if(output!=null)
            System.out.println(output);
        else {
            String menu = "";
            menu = menu.concat("\nChoose one of the options:\n");
            menu = menu.concat("1) Add Position\n");
            menu = menu.concat("2) Add Worker\n");
            menu = menu.concat("3) Remove Position\n");
            menu = menu.concat("4) Remove Worker\n");
            menu = menu.concat("5) Submit Shift\n");
            menu = menu.concat("6) Return\n");
            menu = menu.concat("7) Exit");
            while (true) {
                System.out.println(menu);
                Integer option = getNextInt(sc);
                switch (option) {
                    case 1:
                        printAddPositionToShift();
                        break;
                    case 2:
                        printAddWorkerToShift();
                        break;
                    case 3:
                        printRemovePositionFromShift();
                        break;
                    case 4:
                        printRemoveWorker();
                        break;
                    case 5:
                        output = branchController.submitShift();
                        if (output != null)
                            System.out.println(output);
                        break;
                    case 6:
                        return;
                    case 7:
                        Exit();
                    default:
                        System.out.println("Option not valid, please retype");
                }
            }
        }
    }


    //region Construct Shift Methods

    private static boolean ChooseCreationOrEdit() {
        String menu = "";
        menu = menu.concat("\nChoose one of the options:\n");
        menu = menu.concat("1) Create Shift\n");
        menu = menu.concat("2) Edit Shift\n");
        while (true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    return true;
                case 2:
                    return false;
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }
    private static void printRemovePositionFromShift() {
        String output=null;
        System.out.println("Enter a position:");
        String pos=WnextLine();
        output=branchController.removePositionFromShift(pos);
        if(output!=null)
            System.out.println(output);
    }

    private static void printAddWorkerToShift() {
        System.out.println("Enter position to be occupied:");
        String pos=WnextLine();
        int i=0;
        Stream<ModelWorker> smw= branchController.getCurrentEditedModelShift().availableWorkers.stream().filter((mw)->mw.positions.contains(pos));
        ModelWorker[] relevantWorkers=smw.toArray(size-> new ModelWorker[size]);
        if(relevantWorkers.length>0) {
            System.out.println("Available workers for this position:");
            for (ModelWorker mw : relevantWorkers) {
                System.out.println(i + "." + mw.name);
                i++;
            }
            int num = -1;
            while(num<0|num>=relevantWorkers.length)
            {
                System.out.println("Choose number of worker:");
                num=WnextInt();
            }
            String output=branchController.addWorkerToPositionInShift(pos,relevantWorkers[num].id);
            if(output!=null)
                System.out.println(output);
        }
        else
            System.out.println("No Available workers for this position");
    }
    private static void printAddPositionToShift() {
        String output=null;
        System.out.println("Enter a position:");
        String pos=WnextLine();
        System.out.println("Enter quantity of workers required fot this position in this shift:");
        int quantity=WnextInt();
        output=branchController.addPositionToShift(pos,quantity);
        if(output!=null)
            System.out.println(output);
    }

    //endregion
    //endregion
    //endregion
    //region Manage Availability


    private static void printManageWorkersAvailability() {
        String id=selectWorker();
        if(id!=null)
        {
            boolean goBack=false;
            while(!goBack)
            {
                System.out.println("Choose an option:");
                System.out.println("1.Mark worker available for shift");
                System.out.println("2.Unmark worker available for shift");
                System.out.println("3.Finish");
                int opt=WnextInt();
                switch (opt){
                    case(1):
                        MarkWorkerAvailbleForShift(id);
                        break;
                    case(2):
                        UnmarkWorkerAvailbleForShift(id);
                        break;
                    case(3):
                        goBack=true;
                        break;

                }
            }
        }
    }

    //region Manage Availabilty mehtods

    private static void UnmarkWorkerAvailbleForShift(String id) {
        System.out.println("Enter shift date");
        Date date=getDateFromUser();
        boolean timeOfday = getTimeOfDayFromUser();
        String output=branchController.removeAvailableWorker(date,timeOfday,id);
        if(output!=null)
            System.out.println(output);
    }
    private static void MarkWorkerAvailbleForShift(String id) {
        System.out.println("Enter shift date");
        Date date=getDateFromUser();
        boolean timeOfday = getTimeOfDayFromUser();
        branchController.addAvailableWorker(date,timeOfday,id);
    }
    //endregion

    //endregion
    //region manageWorkers

    private static void printManageWorkersMenu() {
        String menu = "";
        menu = menu.concat("\nChoose one of the options:\n");
        menu = menu.concat("1) Display Workers\n");
        menu = menu.concat("2) Add Worker\n");
        menu = menu.concat("3) Add Driver\n");
        menu = menu.concat("4) Remove Worker\n");
        menu = menu.concat("5) Edit Worker\n");
        menu = menu.concat("6) Return\n");
        menu = menu.concat("7) Exit");
        while (true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    displayWorkers();
                    break;
                case 2:
                    printAddWorker();
                    break;
                case 3:
                    printAddDriver();
                    break;
                case 4:
                    printRemoveWorker();
                    break;
                case 5:
                    printEditWorker();
                    break;
                case 6:
                    return;
                case 7:
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    //region Manage Worker Methods
    private static void printEditWorker() {
        String id=selectWorker();
        if(id!=null)
        {
            boolean goBack=false;
            while(!goBack)
            {
                System.out.println(branchController.displaySingleWorker(id));
                System.out.println("Choose an option:");
                System.out.println("1.Edit worker's name");
                System.out.println("2.Add position to worker");
                System.out.println("3.Remove position from worker");
                System.out.println("4.Change worker's salary");
                System.out.println("5.Finish editing");
                int opt=WnextInt();
                switch(opt){
                    case(1):
                        editWorkerName(id);
                        break;
                    case(2):
                        addPositionToWorker(id);
                        break;
                    case(3):
                        removePosition(id);
                        break;
                    case(4):
                        changeWorkerSalary(id);
                        break;
                    case(5):
                        goBack=true;
                        break;
                }

            }
        }

    }
    private static void printRemoveWorker() {
        String id=selectWorker();
        String output=null;
        if(id!=null)
            output=branchController.removeWorkerFromRoster(id);
        if(output!=null)
            System.out.println(output);
    }
    private static void displayWorkers() {
        int i=0;
        for(ModelWorker mw:branchController.displayWorkers())
        {
            System.out.println(i+":"+mw.toString());
            i++;
        }
    }
    private static void printAddWorker() {
        System.out.println("Please enter name:");
        String name=WnextLine();
        System.out.println("Please enter salary:");
        double salary=WnextDouble();
        System.out.println("Please enter positions separated by comma:");
        String pos=WnextLine();
        List<String> positions= Arrays.asList(pos.split(","));
        Date date = getDateFromUser();
        String output=branchController.addWorker(name,salary,date,positions);
        if(output!=null)
            System.out.println(output);
    }
    private static void printAddDriver() {
        System.out.println("Please enter name:");
        String name=WnextLine();
        System.out.println("Please enter salary:");
        double salary=WnextDouble();
        System.out.println("Please enter a license:");
        String lic=WnextLine();
        Date date = getDateFromUser();
        String output=branchController.addDriver(name,salary,date,lic);
        if(output!=null)
            System.out.println(output);
    }



    //region Edit Worker Methods

    private static void changeWorkerSalary(String id) {
        System.out.println("Enter new Salary:");
        double newSalary=WnextDouble();
        String output=branchController.editSalary(newSalary,id);
        if(output!=null)
            System.out.println(output);
    }
    private static void removePosition(String id) {
        System.out.println("Enter position to remove:");
        String newPosition=WnextLine();
        String output=branchController.removePosition(newPosition,id);
        if(output!=null)
            System.out.println(output);
    }

    private static void addPositionToWorker(String id) {
        System.out.println("Enter new position");
        String newPosition=WnextLine();
        String output=branchController.addPosition(newPosition,id);
        if(output!=null)
            System.out.println(output);
    }
    private static void editWorkerName(String id) {
        System.out.println("Enter new name");
        String newName=WnextLine();
        String output=branchController.editName(newName,id);
        if(output!=null)
            System.out.println(output);
    }

    //endregion



    //endregion
    //endregion


    //region additional
    private static boolean getTimeOfDayFromUser() {
        int opt = 0;
        while (opt < 1 | opt > 2) {
            System.out.println("Choose shift:");
            System.out.println("1.Morning Shift");
            System.out.println("2.Night shift");
            opt = WnextInt();
        }
        return opt == 1;
    }
    public static String WnextLine(){
        while (!sc.hasNext()) {}
        return sc.nextLine();
    }
    public static int WnextInt(){
        try
        {
            int output= sc.nextInt();
            sc.nextLine();
            return  output;
        }
        catch (InputMismatchException e)
        {
            sc.next();
            return -1;
        }
    }
    public static double WnextDouble(){
        try
        {
            double output= sc.nextDouble();
            sc.nextLine();
            return  output;
        }
        catch (InputMismatchException e)
        {
            sc.next();
            return -1;
        }
    }
    public static String Wnext()
    {
        return sc.next();
    }
    private static Date getDateFromUser() {
        Date date=null;
        while(date==null) {
            System.out.println("Please enter Date in format of:dd/MM/yyyy");
            String dateStr = WnextLine();
            date=parseDate(dateStr);
        }
        return date;
    }
    public static Date parseDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date2=null;
        try {
            //Parsing the String
            date2 = dateFormat.parse(date);
        } catch (ParseException e) {

        }
        return date2;
    }
    private static String selectWorker() {
        displayWorkers();
        List<ModelWorker> mw=branchController.displayWorkers();
        System.out.println("Please enter the number of the selected worker:");
        int selected=WnextInt();
        if(selected<0|selected>=mw.size())
            System.out.println("Invalid worker's number inserted. please try again!");
        else
        {
            return mw.get(selected).id;
        }
        return null;
    }

    //endregion


    //endregion


    //region Logistic Management
    private static void printLogisticManagementMenu() {
        String menu = "";
        menu = menu.concat("\nChoose one of the options:\n");
        menu = menu.concat("1) Create Truck\n");
        menu = menu.concat("2) Remove Truck\n");
        menu = menu.concat("3) Display Truck Details\n");
        menu = menu.concat("4) Display Transport Details\n");
        menu = menu.concat("5) Book Transport for Pending Orders\n");
        menu = menu.concat("6) Return\n");
        menu = menu.concat("7) Exit");
        while (true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case 1:
                    createTruck();
                    break;
                case 2:
                    removeTruck();
                    break;
                case 3:
                    DisplayTruckDetails();
                    break;
                case 4:
                    DisplayTransportsDetails();
                    break;
                case 5:
                    BookTransportForPendingOrder();
                    break;
                case 6:
                    return;
                case 7:
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    private static void BookTransportForPendingOrder() {
        String details = branchController.getPendingOrdersDetails();
        if (details.equals("")){         //there are no orders in waiting list
            System.out.println("There are no pending orders.");
        }
        else {
            System.out.println(details);
            System.out.println("Please choose order ID to re-book transport:");
            int orderID = integerParse(sc.nextLine());
            boolean exist = branchController.isOrderIdInPendingOrders(orderID);
            if (exist) {
                System.out.println(branchController.BookTransportForPendingOrders(orderID));
            }
            else {
                System.out.println("The order with id:" + orderID + "is not in the pending orders list.");
            }
        }
    }

    private static void DisplayTransportsDetails() {
        System.out.println("Transports:\n" + branchController.getAllTransportsDetails());
    }

    private static void DisplayTruckDetails() {
        System.out.println("Trucks:\n" + branchController.getAllTrucksDetails());
    }

    private static void removeTruck() {
        String details = branchController.getAllTrucksDetails();
        if (details.equals("")){         //there are no trucks in the system
            System.out.println("There are no trucks to delete.\n");
        }
        else {
            System.out.println(details);
            System.out.println("Please choose the truck ID you wish to remove:");
            int truckToDelete = integerParse(sc.nextLine());
            //try to delete the given truck, if returns false the id is not in the system
            boolean deleted = branchController.deleteTruck(truckToDelete);
            if(deleted) {
                System.out.println("The truck deleted successfully.\n");
            }
            else {
                System.out.println("The truck ID:"+ truckToDelete+" is not in the system, operation canceled.\n");
            }
        }
    }

    private static void createTruck() {
        String menu = "Please enter the following details:\n";
        menu = menu.concat("[License plate],[Model],[Driver license],[net weight],[max weight]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 5) {
            String license_plate = param[0];
            String model = param[1];
            String drivers_license = param[2];
            float netWeight = floatParse(param[3]);
            float maxWeight = floatParse(param[4]);
            if (netWeight != -1 && maxWeight != -1) {
                boolean created = branchController.createTruck(license_plate, model, netWeight, maxWeight, drivers_license);
                while (!created) {   //check that the maxWeight is bigger than the net weight
                    System.out.println("Max weight should be bigger than net weight. Please enter max weight again.");
                    maxWeight = floatParse(sc.nextLine());
                    created = branchController.createTruck(license_plate, model, netWeight, maxWeight, drivers_license);
                }
                System.out.println("\nThe truck added successfully.\n");
            }
            else {
                System.out.println("Invalid input. Operation canceled.");
            }
        }
        else {
            System.out.println("Invalid numbers of parameters");
        }

    }

    //endregion

    //region Data mapping
    private static void printDataMapperMenu(){
        String menu = "Data:\n";
        menu=menu.concat("1) Print all categories\n");
        menu=menu.concat("2) Print all general products\n");
        menu=menu.concat("3) Print all sales\n");
        menu=menu.concat("4) Return\n");
        menu=menu.concat("5) Exit");
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
                    return;
                case 5:
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
        while (!sc.hasNextInt()) {
            sc.nextLine();
            System.out.println("Please type a number");
        }
        Integer value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    private static String getNextLine(Scanner sc){
        while(!sc.hasNext()){}
        return sc.nextLine();
    }

    static private void Exit(){
        System.out.println("Thank you! See you next time.");
        exit(0);
    }

    private static int integerParse(String s){
        int ret;
        while (true) {
            try {
                ret = Integer.parseInt(s);
                return ret;
            } catch (Exception e) {
                System.out.println("Invalid operation. Try Again.");
                s = sc.nextLine();
            }
        }
    }

    private static float floatParse(String s){
        float ret;
        try {
            ret = Float.parseFloat(s);
            if (ret < 0){
                return -1;
            }
            return ret;
        } catch (Exception e) {
            return -1;
        }
    }

    private static String getVal(){
        String val = sc.nextLine();
        while (val.equals("")){
            System.out.println("Value can't be empty. Try again.");
            val = sc.nextLine();
        }
        return val;
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
        branchController.addGeneralProduct(((Category)res_cat_30.getData()).getId(), "Niguvim",name, sup_price, 31.5f,  20,catalogID,gpID,supplierID,"Hygiene" , (float)0.7);
        branchController.addSpecificProduct(gpID, convertStringToDate("11/04/2025"),21);
        //Add Product to contract
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Hygiene");

        name = "Crema shampoo for men 500ml";
        sup_price = 15.5f;
        catalogID = 9;
        gpID = 101;
        //Create GP
        branchController.addGeneralProduct(((Category)res_cat_500ml.getData()).getId(), "Crema", name, sup_price, 25.5f, 5,catalogID,gpID,supplierID,"Hygiene" , (float) 0.5);
        branchController.addSpecificProduct(gpID, convertStringToDate("11/04/2025"),6);
        //Add Product to contract
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Hygiene");

        name = "Dove shampoo for women 500ml";
        sup_price = 15.5f;
        catalogID = 11;
        gpID = 102;
        //Create GP
        branchController.addGeneralProduct(((Category)res_cat_500ml.getData()).getId(), "Dove", name, sup_price, 25.5f, 5,  5,gpID,supplierID,"Hygiene" , (float) 0.5);
        branchController.addSpecificProduct(gpID, convertStringToDate("11/04/2025"),6);
        //Add Product to contract
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Hygiene");

        name = "Crema shampoo for men 750ml";
        sup_price = 22.0f;
        catalogID = 12;
        gpID = 103;
        //Create GP
        branchController.addGeneralProduct(((Category)res_cat_500ml.getData()).getId(), "Crema", name, sup_price, 32.5f, 5,catalogID,gpID,supplierID,"Hygiene" , (float) 0.75);
        branchController.addSpecificProduct(gpID, convertStringToDate("11/04/2025"),6);
        //Add Product to contract
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Hygiene");

        String manufacture = "Moosh";
        gpID = 104;
        name = "Moosh packed ground meat 1/2kg";
        sup_price = 30.0f;
        Float ret_price = 40.0f;
        catalogID = 13;
        branchController.addGeneralProduct(((Category)meat_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,5,catalogID,gpID,supplierID,"Meat" , (float) 0.5);
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

        branchController.addGeneralProduct(((Category)meat_one_kg.getData()).getId(), manufacture,name,sup_price,ret_price,3,catalogID,gpID,supplierID,"Meat" , (float) 1);

        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Meat");


        manufacture = "Lakerda";
        gpID = 202;
        name = "Lakerda 1/2kg semi-fresh";
        sup_price = 10.0f;
        ret_price = 13.0f;
        catalogID = 21;
        branchController.addGeneralProduct(((Category)fish_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,5,catalogID,gpID,supplierID,"Meat" , (float) 0.5);
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Fish");


        manufacture = "Merluza";
        gpID = 203;
        name = "Merluza 1/2kg semi-fresh";
        sup_price = 9.5f;
        ret_price = 12.0f;
        catalogID = 22;
        branchController.addGeneralProduct(((Category)fish_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,7,catalogID,gpID,supplierID,"Fish" , (float) 0.5);
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Fish");

        manufacture = "Merluza";
        gpID = 204;
        name = "Merluza 1kg semi-fresh";
        sup_price = 15.5f;
        ret_price = 17.0f;
        catalogID = 23;
        branchController.addGeneralProduct(((Category)fish_one_kg.getData()).getId(), manufacture,name,sup_price,ret_price,10,catalogID,gpID,supplierID,"Fish" , (float) 1);
        branchController.addProductToContract(supplierID,catalogID,gpID,sup_price,"Fish");


//-------------------------------------------

        // trucks
        branchController.createTruck("12-L8","XXX",1000,1800,"C4");
        branchController.createTruck("17-LD","X23",1050,2260,"C1");
        branchController.createTruck("J0-38","1X6",700,1500,"C");
        branchController.createTruck("12-23FF","XXL8",1000,2600,"C1");
        branchController.createTruck("17-45LD","X24",1050,3260,"C1");
        branchController.createTruck("J0-38AV","1X6ZA",700,1000,"C");
        branchController.createTruck("12345678", "XX32", 1000, 2550, "C4");


//---------------------------------------------


        //workers

            //constants
             final boolean morning=true;
             final boolean night=false;

        List<String>positions1=new ArrayList<>();
        positions1.add("manager");
        positions1.add("storage man");
        List<String>positions2=new ArrayList<>();
        positions2.add("manager");

        Date startDate1=parseDate("11/04/2020");
        Date startDate2=parseDate("12/04/2020");

        String WID1 = "0000-0000-0000-0001";
        String WID2 = "0000-0000-0000-0002";
        String WID3 = "0000-0000-0000-0003";
        String WID4 = "0000-0000-0000-0004";
        String WID5 = "0000-0000-0000-0005";
        String WID6 = "0000-0000-0000-0006";
        String WID7 = "0000-0000-0000-0007";


        branchController.initAddWorker(WID1,"Gil",16,startDate1,positions1);
        branchController.initAddWorker(WID2,"Sharon",15.9,startDate2,positions2);
        branchController.initAddDriver(WID3,"Moshe",10,startDate1,"C4");
        branchController.initAddDriver(WID4,"Dani",100,startDate2,"C");
        branchController.initAddDriver(WID5,"Gadi",100,startDate2,"C1");

        positions2.add("security guard");

        branchController.initAddWorker(WID6,"Avi",100,startDate1,positions2);


        List<String>positions3=new ArrayList<>();
        positions3.add("storage man");
        positions3.add("cashier");
        branchController.initAddWorker(WID7,"bob",100,startDate1,positions3);


        Date shiftDate1=parseDate("20/05/2020");
        Date shiftDate2=parseDate("21/05/2020");

        branchController.addAvailableWorker(shiftDate1,morning,WID1);
        branchController.addAvailableWorker(shiftDate1,morning,WID7);
        branchController.addAvailableWorker(shiftDate1,morning,WID3);
        branchController.addAvailableWorker(shiftDate1,morning,WID4);

        branchController.addAvailableWorker(shiftDate2,morning,WID1);
        branchController.addAvailableWorker(shiftDate2,morning,WID2);
        branchController.addAvailableWorker(shiftDate2,morning,WID3);
        branchController.addAvailableWorker(shiftDate2,morning,WID4);
        branchController.addAvailableWorker(shiftDate2,morning,WID5);
        branchController.addAvailableWorker(shiftDate2,morning,WID6);
        branchController.addAvailableWorker(shiftDate2,morning,WID7);

        branchController.createShift(shiftDate1,morning);


        branchController.addPositionToShift("cashier",1);
        branchController.addWorkerToPositionInShift("manager",WID1);
        branchController.addWorkerToPositionInShift("cashier",WID7);
        branchController.submitShift();

        branchController.createShift(shiftDate2,morning);

        branchController.addPositionToShift("driver",1);
        branchController.addPositionToShift("storage man",1);
        branchController.addPositionToShift("security guard",1);
        branchController.addPositionToShift("cashier",1);
        branchController.addWorkerToPositionInShift("manager",WID2);
        branchController.addWorkerToPositionInShift("storage man",WID1);
        branchController.addWorkerToPositionInShift("security guard",WID6);
        branchController.addWorkerToPositionInShift("cashier",WID7);
        branchController.submitShift();

    }
    //endregion
}
