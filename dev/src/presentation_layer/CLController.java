package presentation_layer;

import bussines_layer.Inventory;
import bussines_layer.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CLController {

    public static void main(String[] args) {
        Inventory inventory=new Inventory();
        Boolean exit=false;
        Scanner sc = new Scanner(System.in);    //System.in is a standard input stream
        printLogo();
        printMainMenu();
        while(!exit) {
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printProductMenu(sc,exit,inventory);
                    break;
                case (2):
                    printCategoryMenu(sc,exit,inventory);
                    break;
                case (3):
                    printReportMenu(sc,exit,inventory);
                    break;
                case (4):
                    printSaleMenu(sc,exit,inventory);
                    break;
                case (5):
                    exit=true;
                    break;
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
                "            |_|     ";
        System.out.println(logo);
    }

    static private void printMainMenu() {
        String menu = "";
        menu=menu.concat("Choose one of the options:\n");
        menu=menu.concat("1) Products management\n");
        menu=menu.concat("2) Category management\n");
        menu=menu.concat("3) Reports management\n");
        menu=menu.concat("4) Sales management\n");
        menu=menu.concat("5) Exit\n");
        System.out.println(menu);
    }

    //region Products Management
    static private void printProductMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Products management\n";
        menu=menu.concat("1) Add new general product\n");
        menu=menu.concat("2) Edit general product\n");
        menu=menu.concat("3) Remove general product\n");
        menu=menu.concat("4) Add specific product\n");
        menu=menu.concat("5) Remove specific product\n");
        menu=menu.concat("6) Mark flaw specific product\n");
        menu=menu.concat("7) Change location of specific product\n");
        menu=menu.concat("8) Return\n");
        menu=menu.concat("9) exit\n\n");
        System.out.println(menu);
        while(!exit) {
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printAddGeneralProductMenu(sc,exit,inv);
                    break;
                case (2):
                    printEditGeneralProductMenu(sc,exit,inv);
                    break;
                case (3):
                    printRemoveGeneralProductMenu(sc,exit,inv);
                    break;
                case (4):
                    printAddSpecificProductMenu(sc,exit,inv);
                    break;
                case (5):
                    printRemoveSpecificProductMenu(sc,exit,inv);
                    break;
                case (6):
                    printMarkAsFlawtMenu(sc,exit,inv);
                    break;
                case (7):
                    printMoveLocationtMenu(sc,exit,inv);
                    break;
                case (8):
                    return;
                case (9):
                    exit=true;
                    break;
                default:
                    System.out.println("Option not valid, please retype");
            }
        }

    }

    //region General Product
    static private void printAddGeneralProductMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID],[Manufacture],[CatalogID],[Name],[Supplier price],[Retail price],[initial quantity],[Minimum quantity]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==8){
            result=inv.addGeneralProduct(Integer.getInteger(param[0]) ,param[1],param[2],param[3],
                                        Float.parseFloat(param[4]),Float.parseFloat(param[5]),
                                        Integer.getInteger(param[6]),Integer.getInteger(param[7]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printRemoveGeneralProductMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID],[CatalogID]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==2){
            result=inv.removeGeneralProduct(Integer.getInteger(param[0]),param[1]);
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printEditGeneralProductMenu(Scanner sc,Boolean exit,Inventory inv) {
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
        System.out.println(menu);

        String details = "Please enter the following details\n";
        while(!exit) {
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    details=details.concat("[CatalogID],[New name]\n");
                    System.out.println(details);
                    param = getInputParserbyComma(sc);
                    if(param.length==2) {
                        result = inv.editGeneralProduct_name(param[0], param[1]);
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid number of parameters");
                    }
                    break;
                case (2):
                    details=details.concat("[CatalogID],[New supplier price]\n");
                    System.out.println(details);
                    param = getInputParserbyComma(sc);
                    if(param.length==2) {
                        result = inv.editGeneralProduct_supplier_price(param[0], Float.parseFloat(param[1]));
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid number of parameters");
                    }
                    break;
                case (3):
                    details=details.concat("[CatalogID],[New retail price]\n");
                    System.out.println(details);
                    param=getInputParserbyComma(sc);
                    if(param.length==2) {
                        result = inv.editGeneralProduct_retail_price(param[0], Float.parseFloat(param[1]));
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid number of parameters");
                    }
                    break;
                case (4):
                    details=details.concat("[CatalogID],[New quantity]\n");
                    System.out.println(details);
                    param = getInputParserbyComma(sc);
                    if(param.length==2) {
                        result=inv.editGeneralProduct_quantity(param[0], Integer.parseInt(param[1]));
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid number of parameters");
                    }
                    break;
                case (5):
                    details=details.concat("[CatalogID],[New minimum quantity]\n");
                    System.out.println(details);
                    param = getInputParserbyComma(sc);
                    if(param.length==2) {
                        result=inv.editGeneralProduct_min_quantity(param[0], Integer.parseInt(param[1]));
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid number of parameters");
                    }
                    break;
                case (6):
                    return;
                case (7):
                    exit=true;
                    break;
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    //endregion
    //region Specific Product
    static private void printAddSpecificProductMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID],[Expiration date (dd/mm/YYYY)],[Quantity]\n");
        String[] param = getInputParserbyComma(sc);
        if(param.length==3) {
            Date date =convertStringToDate(param[1]);
            if (date!=null){
                result = inv.addSpecificProduct(param[0],date,Integer.getInteger(param[2]));
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

    static private void printRemoveSpecificProductMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Specific productID]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==1) {
            result = inv.removeSpecificProduct(Integer.parseInt(param[0]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printMarkAsFlawtMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Specific productID]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            result = inv.markAsFlaw(Integer.parseInt(param[0]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printMoveLocationtMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Specific productID]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            result = inv.moveLocation(Integer.parseInt(param[0]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion

    //endregion

    //region Category Management
    static private void printCategoryMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Category management\n";
        menu=menu.concat("1) Add new main category\n");
        menu=menu.concat("2) Add new sub category\n");
        menu=menu.concat("3) remove category\n");
        menu=menu.concat("4) Edit category name\n");
        menu=menu.concat("5) Return");
        menu=menu.concat("6) Exit\n\n");
        System.out.println(menu);

        while(!exit) {
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printAddMainCategoryMenu(sc,exit,inv);
                    break;
                case (2):
                    printAddSubCategory(sc,exit,inv);
                    break;
                case (3):
                    printRemoveCategory(sc,exit,inv);
                    break;
                case (4):
                    printEditCategoryNameMenu(sc,exit,inv);
                    break;
                case (5):
                    return;
                case (6):
                    exit=true;
                    break;
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    static private void printAddMainCategoryMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Name]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            result = inv.addMainCategory(param[0]);
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printAddSubCategory(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Super categoryID],[Name]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==2) {
            result = inv.addSubCategory(Integer.getInteger(param[0]),param[1]);
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printRemoveCategory(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            result = inv.removeCategory(Integer.getInteger(param[0]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printEditCategoryNameMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID],[Name]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==2) {
            result = inv.editCategoryname(Integer.getInteger(param[0]),param[1]);
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion

    //region Report Management
    static private void printReportMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Report management\n";
        menu=menu.concat("1) Issue out of stock report\n");
        menu=menu.concat("2) Issue in-stock report\n");
        menu=menu.concat("3) Issue damaged&expired report\n");
        menu=menu.concat("4) Return\n");
        menu=menu.concat("5) Exit\n\n");
        System.out.println(menu);
        while(!exit) {
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printOutOfStockReportMenu(sc,exit,inv);
                    break;
                case (2):
                    printInStockReportMenu(sc,exit,inv);
                    break;
                case (3):
                    printDNEReportMenu(sc,exit,inv);
                    break;
                case (4):
                    return;
                case (5):
                    exit=true;
                    break;
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }
    //region Out of Stock Report
    static private void printOutOfStockReportMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Choose one of the options\n";
        menu=menu.concat("1) By category\n");
        menu=menu.concat("2) By general product\n");
        menu=menu.concat("3) Return\n");
        menu=menu.concat("4) Exit\n\n");
        System.out.println(menu);
        while(!exit) {
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printOutOfStockReportByCategoryMenu(sc,exit,inv);
                    break;
                case (2):
                    printOutOfStockReportByGeneralProductMenu(sc,exit,inv);
                    break;
                case (3):
                    return;
                case (4):
                    exit=true;
                    break;
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    static private void printOutOfStockReportByCategoryMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        menu=menu.concat("for all categories type 'all'\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            result = inv.makeReport(Integer.getInteger(param[0]), "outofstock");
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printOutOfStockReportByGeneralProductMenu(Scanner sc,Boolean exit,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID]\n");
        System.out.println(menu);
        String[] param=getInputParserbyComma(sc);
        if(param.length==1) {
            result = inv.makeReport(param[0], "outofstock");
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion

    //region In-Stock Report
    static private void printInStockReportMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Choose one of the options\n";
        menu=menu.concat("1) By category\n");
        menu=menu.concat("2) By general product\n");
        menu=menu.concat("3) Return\n");
        menu=menu.concat("4) Exit\n\n");
        System.out.println(menu);
        while(!exit) {
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printInStocReportByCategoryMenu(sc,exit,inv);
                    break;
                case (2):
                    printInStocReportByGeneralProductMenu(sc,exit,inv);
                    break;
                case (3):
                    return;
                case (4):
                    exit=true;
                    break;
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    static private void printInStocReportByCategoryMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        menu=menu.concat("for all categories type 'all'\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==1) {
            result = inv.makeReport(Integer.getInteger(param[0]), "instock");
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printInStocReportByGeneralProductMenu(Scanner sc,Boolean exit,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==1) {
            result = inv.makeReport(param[0], "instock");
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion

    //region Damaged and Expired Report
    static private void printDNEReportMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Choose one of the options\n";
        menu=menu.concat("1) By category\n");
        menu=menu.concat("2) By general product\n");
        menu=menu.concat("3) Return\n");
        menu=menu.concat("4) Exit\n\n");
        System.out.println(menu);
        while(!exit) {
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printDNEReportByCategoryMenu(sc,exit,inv);
                    break;
                case (2):
                    printDNEReportByGeneralProductMenu(sc,exit,inv);
                    break;
                case (3):
                    return;
                case (4):
                    exit=true;
                    break;
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    static private void printDNEReportByCategoryMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        menu=menu.concat("for all categories type 'all'\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            result = inv.makeReport(Integer.getInteger(param[0]), "dne");
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printDNEReportByGeneralProductMenu(Scanner sc,Boolean exit,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==1) {
            result = inv.makeReport(param[0], "dne");
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion

    //endregion

    //region Sale Management
    static private void printSaleMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Sales management\n";
        menu=menu.concat("1) Add new sale\n");
        menu=menu.concat("2) Cancel sale\n");
        menu=menu.concat("3) Return\n");
        menu=menu.concat("4) Exit\n\n");
        System.out.println(menu);
        while(!exit) {
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printAddSaleMenu(sc,exit,inv);
                    break;
                case (2):
                    printRemoveSaleMenu(sc,exit,inv);
                    break;
                case (3):
                    return;
                case (4):
                    exit=true;
                    break;
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    //region Add Sale
    static private void printAddSaleMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Choose one of the options\n";
        menu=menu.concat("1) By category\n");
        menu=menu.concat("2) By general product\n");
        menu=menu.concat("3) Return\n");
        menu=menu.concat("4) Exit\n\n");
        System.out.println(menu);
        while(!exit) {
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printAddNewSaleByCategoryMenu(sc,exit,inv);
                    break;
                case (2):
                    printAddNewSaleByGeneralProductMenu(sc,exit,inv);
                    break;
                case (3):
                    return;
                case (4):
                    exit=true;
                    break;
                default:
                    System.out.println("Option not valid, please retype");
            }
        }     }

    static private void printAddNewSaleByCategoryMenu(Scanner sc,Boolean exit,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID],[Discount percentage\\Fixed price],[Optional: %],[Optional: Start date (dd/mm/YYYY)],[Must if apply 'Start date': End date (dd/mm/YYYY)]\n");
        menu=menu.concat("for discount by percentage please add '%'\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==2) {
            result = inv.addSale(Integer.getInteger(param[0]),"fix",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length==3){
            result = inv.addSale(Integer.getInteger(param[0]),"percentage",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length==4){
                Date start_date=convertStringToDate(param[2]);
                Date end_date =convertStringToDate(param[3]);
                if(start_date!=null && end_date!=null){
                    result = inv.addSale(Integer.getInteger(param[0]),"fix",Float.parseFloat(param[1]),start_date,end_date);
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
                result = inv.addSale(Integer.getInteger(param[0]),"percentage",Float.parseFloat(param[1]),start_date,end_date);
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
    static private void printAddNewSaleByGeneralProductMenu(Scanner sc,Boolean exit,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID],[Discount percentage\\Fixed price],[Optional: %],[Optional: Start date (dd/mm/YYYY)],[Must if apply 'Start date': End date (dd/mm/YYYY)]\n");
        menu=menu.concat("for discount by percentage please add '%'\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==2) {
            result = inv.addSale(param[0],"fix",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length==3){
            result = inv.addSale(param[0],"percentage",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length==4){
            Date start_date=convertStringToDate(param[2]);
            Date end_date =convertStringToDate(param[3]);
            if(start_date!=null && end_date!=null){
                result = inv.addSale(param[0],"fix",Float.parseFloat(param[1]),start_date,end_date);
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
                result = inv.addSale(param[0],"percentage",Float.parseFloat(param[1]),start_date,end_date);
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
    static private void printRemoveSaleMenu(Scanner sc,Boolean exit,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[SaleID]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==1){
            result=inv.removeSale(Integer.getInteger(param[0]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion

    //endregion

    //region Utilities
    static private String[] getInputParserbyComma(Scanner sc){
        String user_input = getNextLine(sc);
        System.out.println(user_input);
        return user_input.split(",");
    }
    static private Date convertStringToDate(String sdate){
        try {
            Date date=new SimpleDateFormat("dd/MM/yyyy").parse(sdate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static Integer getNextInt(Scanner sc){
        while(!sc.hasNext()){
            return sc.nextInt();
        }
        return null;
    }
    private static String getNextLine(Scanner sc){
        while(!sc.hasNext()){
            return sc.nextLine();
        }
        return null;
    }


    //endregion
}
