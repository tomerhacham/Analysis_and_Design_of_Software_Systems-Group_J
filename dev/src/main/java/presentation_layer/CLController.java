package presentation_layer;

import bussines_layer.Branch;
import bussines_layer.BranchController;
import bussines_layer.inventory_module.Inventory;
import bussines_layer.Result;
import Initializer.Initializer;
import bussines_layer.inventory_module.Sale;
import javafx.util.Pair;
import sun.awt.image.ImageWatched;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.in;

public class CLController {

    private static Scanner sc;
    private static BranchController branchController;

    public CLController(){
        branchController = new BranchController();
        sc = new Scanner(System.in);    //System.in is a standard input stream
    }

    public static void displayMenu() {
        printLogo();
        //printInitializeMenu(sc, branchController);      //TODO fix/remove initialize
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
                    Exit();
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
        menu = menu.concat("1) Run system with initial data\n");
        menu = menu.concat("2) Run system blank\n");
        menu = menu.concat("3) Exit\n\n");
        System.out.println(menu);
        Integer option = getNextInt(sc);
        while (true) {
            switch (option) {
                case 1:
                    Initializer.initialize(inventory);
                    return;
                case 2:
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
        menu = menu.concat("3) Exit\n");
        System.out.println(menu);
    }

    private static void printBranchesMenu() {
        String menu = "";
        menu = menu.concat("Choose one of the options:\n");
        menu = menu.concat("1) Choose existing branch\n");
        menu = menu.concat("2) Open new branch\n");
        menu = menu.concat("3) Remove branch\n");
        menu = menu.concat("4) Return\n");
        menu = menu.concat("5) Exit\n\n");
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
                    return;
                case 5:
                    Exit();
            }
        }
    }

    private static void printExistingBranchesMenu() {
        String menu = "";
        menu = menu.concat("Choose branch (By ID):\n");
        Integer option = 1;
        for (Branch b : branchController.getBranches().values()){
            menu = menu.concat(String.format("%d) %s (type: %d)\n", option, b.getName(), b.getBranchId()));
            option++;
        }
        menu = menu.concat(String.format("%d) Return\n", option));
        menu = menu.concat(String.format("%d) Exit\n\n", option+1));
        while(true){
            System.out.println(menu);
            Integer input = getNextInt(sc);
            if (input.equals(option)){
                return;
            }
            if (input.equals(option+1)){
                Exit();
            }
            branchController.switchBranch(input);
            printMainBranchMenu();
        }
    }

    private static void printOpenNewBranchMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Name]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            result = branchController.addMainCategory(param[0]);
            System.out.println(result.getMessage());
        }
    }

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
        menu=menu.concat("9) Exit\n");
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

    private static void printOrdersMenu() {
        String menu = "";
        menu=menu.concat("Choose one of the options:\n");
        menu=menu.concat("1) Create periodic order\n");
        menu=menu.concat("2) Remove periodic order\n");
        menu=menu.concat("3) Add product to periodic order\n");
        menu=menu.concat("4) Remove product from periodic order\n");
        menu=menu.concat("5) Update product quantity\n");
        menu=menu.concat("6) Accept order\n");
        menu=menu.concat("7) Display all orders\n");
        menu=menu.concat("8) Return\n");
        menu=menu.concat("9) Exit\n");
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
                    printAcceptOrder();
                    break;
                case 7:
                    printDisplayOrders();
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

    private static void printDisplayOrders() {
        branchController.displayAllOrders();
    }

    private static void printUpdateProductQuantity() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID,gpID,newQuantity]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 3) {
            Integer orderID = Integer.getInteger(param[0]);
            Integer gpID = Integer.getInteger(param[1]);
            Integer newQuantity = Integer.getInteger(param[2]);
            result = branchController.updateProductQuantityInPeriodicOrder(orderID, gpID, newQuantity);
            System.out.println(result.getMessage());
        }
    }

    private static void printRemoveProductPOrder() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID,gpID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2) {
            Integer orderID = Integer.getInteger(param[0]);
            Integer gpID = Integer.getInteger(param[1]);
            result = branchController.removeProductFromPeriodicOrder(orderID, gpID);
            System.out.println(result.getMessage());
        }
    }

    private static void printAddProductToPOrder() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[orderID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            Integer orderID = Integer.getInteger(param[0]);
            menu = "Please enter the following details\n";
            menu=menu.concat("[gpID,quantity]");
            System.out.println(menu);
            param = getInputParserbyComma(sc);
            if (param.length == 2) {
                Integer gpID = Integer.getInteger(param[0]);
                Integer quantity = Integer.getInteger(param[1]);
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
            Integer orderID = Integer.getInteger(param[0]);
            result = branchController.removePeriodicOrder(orderID);
            System.out.println(result);
        }
    }

    private static void printCreatePOrder() {
        LinkedList<Pair<Integer, Integer>> products = new LinkedList<>();
        String menu = "Please enter the following details\n";
        menu=menu.concat("[supplier ID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 1) {
            Integer supplierID = Integer.getInteger(param[0]);
            menu = "Order will be made once a week. Choose delivery day (1- Sunday, 6- Friday):\n";
            param = getInputParserbyComma(sc);
            if (param.length == 1) {
                Integer day = Integer.getInteger(param[0]);
                Result res = branchController.createPeriodicOrder(supplierID, products, day);
                System.out.println(res.getMessage());
            } else {
                System.out.println("Invalid number of parameters");
            }
            printEnterProductsToPOrder(products);

        }else {
            System.out.println("Invalid number of parameters for supplier ID");
        }


    }

    private static void printEnterProductsToPOrder(LinkedList<Pair<Integer, Integer>> products) {
        String menu = "Please enter the following details\n";
        menu=menu.concat("[gpID,quantity]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length == 2){
            Integer gpID = Integer.getInteger(param[0]);
            Integer quantity = Integer.getInteger(param[1]);
            products.add(new Pair<>(gpID, quantity));
            menu="Add more products to order?";
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
            Integer orderID = Integer.getInteger(param[0]);
            result = branchController.acceptOrder(orderID);
            System.out.println(result.getMessage());
        } else{
            System.out.println("Invalid number of parameters");
        }

    }
    //endregion


    //region Products Management
    static private void printProductMenu() {
        String menu = "Products management\n";
        //menu=menu.concat("1) Add new general product\n");
        menu=menu.concat("2) Edit general product\n");
       // menu=menu.concat("3) Remove general product\n");
        //menu=menu.concat("4) Add specific product\n");
        menu=menu.concat("5) Remove specific product\n");
        menu=menu.concat("6) Mark flaw specific product\n");
        menu=menu.concat("7) Change location of specific product\n");
        menu=menu.concat("8) Return\n");
        menu=menu.concat("9) exit\n\n");
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printAddGeneralProductMenu();
                    break;
                case (2):
                    printEditGeneralProductMenu();
                    break;
                case (3):
                    printRemoveGeneralProductMenu();
                    break;
                case (4):
                    printAddSpecificProductMenu();
                    break;
                case (5):
                    printRemoveSpecificProductMenu();
                    break;
                case (6):
                    printMarkAsFlawtMenu();
                    break;
                case (7):
                    printMoveLocationtMenu();
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

    //region General Product
    static private void printAddGeneralProductMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID],[Manufacture],[CatalogID],[Name],[Supplier price],[Retail price],[Minimum quantity]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==7){
            result=inv.addGeneralProduct(Integer.parseInt(param[0]),param[1],param[2],param[3],
                    Float.parseFloat(param[4]),Float.parseFloat(param[5]),Integer.parseInt(param[6]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printRemoveGeneralProductMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID],[CatalogID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==2){
            result=inv.removeGeneralProduct(Integer.parseInt(param[0]),param[1]);
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

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
        menu=menu.concat("7) Exit\n\n");

        while(true) {
            System.out.println(menu);
            String details = "Please enter the following details\n";
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    details=details.concat("[CatalogID],[New name]");
                    System.out.println(details);
                    param = getInputParserbyComma(sc);
                    //TODO convert param[0] to Integer
                    if(param.length==2) {
                        result = branchController.editGeneralProductName(param[0], param[1]);
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
                    if(param.length==2) {
                        result = branchController.editGeneralProductSupplierPrice(param[0], Float.parseFloat(param[1]));
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
                        result = branchController.editGeneralProductRetailPrice(param[0], Float.parseFloat(param[1]));
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
                        result=branchController.editGeneralProductQuantity(param[0], Integer.parseInt(param[1]));
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
                        result=branchController.editGeneralProductMinQuantity(param[0], Integer.parseInt(param[1]));
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
    static private void printAddSpecificProductMenu() {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID],[Expiration date (dd/mm/YYYY)],[Quantity]");
        String[] param = getInputParserbyComma(sc);
        if(param.length==3) {
            Date date =convertStringToDate(param[1]);
            if (date!=null){
                result = branchController.addSpecificProduct(Integer.getInteger(param[0]),date,Integer.parseInt(param[2]));
                System.out.println(result.getMessage());
            }
            else{
                System.out.println("Date is not in the right format");
            }
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

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
        menu=menu.concat("5) Exit\n\n");
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
        menu=menu.concat("4) Exit\n\n");
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
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        menu=menu.concat("for all categories type 'all'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            if(param[0].equals("all")){
                result = branchController.makeReportByCategory(0, "outofstock");
            }
            else{
                result = branchController.makeReportByCategory(Integer.parseInt(param[0]), "outofstock");
            }
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printOutOfStockReportByGeneralProductMenu(){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID]");
        System.out.println(menu);
        String[] param=getInputParserbyComma(sc);
        if(param.length==1) {
            result = branchController.makeReportByGeneralProduct(Integer.getInteger(param[0]), "outofstock");
            System.out.println(result.getMessage());
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
        menu=menu.concat("4) Exit\n\n");
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
                result = branchController.makeReportByCategory(Integer.getInteger(param[0]), "instock");
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
            result = branchController.makeReportByGeneralProduct(Integer.getInteger(param[0]), "instock");
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
        menu=menu.concat("4) Exit\n\n");
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
                result = branchController.makeReportByCategory(Integer.getInteger(param[0]), "dne");
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
            result = branchController.makeReportByGeneralProduct(Integer.getInteger(param[0]), "dne");
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
        menu=menu.concat("5) Exit\n\n");
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
        menu=menu.concat("4) Exit\n\n");
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
                result = inv.addSaleByCategory(Integer.parseInt(param[0]),"fix",Float.parseFloat(param[1]),start_date,end_date);
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
            result = branchController.addSaleByGeneralProduct(Integer.getInteger(param[0]),"fix",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length==3){
            result = branchController.addSaleByGeneralProduct(Integer.getInteger(param[0]),"percentage",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length==4){
            Date start_date=convertStringToDate(param[2]);
            Date end_date =convertStringToDate(param[3]);
            if(start_date!=null && end_date!=null){
                result = branchController.addSaleByGeneralProduct(Integer.getInteger(param[0]),"fix",Float.parseFloat(param[1]),start_date,end_date);
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
                result = branchController.addSaleByGeneralProduct(Integer.getInteger(param[0]),"percentage",Float.parseFloat(param[1]),start_date,end_date);
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

    //region Data mapping
    private static void printDataMapperMenu(){
        String menu = "Data:\n";
        menu=menu.concat("1) Print all categories\n");
        menu=menu.concat("2) Print all general products\n");
        menu=menu.concat("3) Print all sales\n");
        menu=menu.concat("4) Return\n");
        menu=menu.concat("5) exit\n\n");
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
                case (4):
                    return;
                case (5):
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

    //endregion

    //region Utilities
    static private String[] getInputParserbyComma(Scanner sc){
        String user_input = getNextLine(sc);
        //System.out.println(user_input);
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
        //PlayBeep();
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
//    private static void PlayBeep(){
//        //String filePath = new File(System.getProperty("user.dir")).getParent()+"//resources//beep.wav";
//        String filePath = new File(System.getProperty("user.dir"))+"//src//resources//beep.wav";
//        File beep = new File(filePath);
//        try (Clip clip = AudioSystem.getClip()) {
//            clip.open(AudioSystem.getAudioInputStream(beep));
//            clip.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


    //endregion
}
