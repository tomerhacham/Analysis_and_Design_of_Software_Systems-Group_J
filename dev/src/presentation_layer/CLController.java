package presentation_layer;

import bussines_layer.Category;
import bussines_layer.Inventory;
import bussines_layer.Result;
import Initializer.Initializer;
import bussines_layer.Sale;
import com.sun.org.apache.xml.internal.security.Init;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class CLController {

    public static void main(String[] args) {
        Inventory inventory=new Inventory();
        Scanner sc = new Scanner(System.in);    //System.in is a standard input stream
        printLogo();
        printInitializeMenu(sc, inventory);
        while(true) {
            printMainMenu();
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printProductMenu(sc, inventory);
                    break;
                case (2):
                    printCategoryMenu(sc, inventory);
                    break;
                case (3):
                    printReportMenu(sc, inventory);
                    break;
                case (4):
                    printSaleMenu(sc, inventory);
                    break;
                case (5):
                    printDataMapperMenu(sc, inventory);
                    break;
                case (6):
                    Exit();
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
                "            |_|     \n";
        System.out.println(logo);
    }

    static private void printInitializeMenu(Scanner sc, Inventory inventory) {
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

    static private void printMainMenu() {
        String menu = "";
        menu=menu.concat("Choose one of the options:\n");
        menu=menu.concat("1) Products management\n");
        menu=menu.concat("2) Category management\n");
        menu=menu.concat("3) Reports management\n");
        menu=menu.concat("4) Sales management\n");
        menu=menu.concat("5) System status\n");
        menu=menu.concat("6) Exit\n");
        System.out.println(menu);
    }

    //region Products Management
    static private void printProductMenu(Scanner sc, Inventory inv) {
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
        while(true) {
            System.out.println(menu);
            Integer option = getNextInt(sc);
            switch (option) {
                case (1):
                    printAddGeneralProductMenu(sc, inv);
                    break;
                case (2):
                    printEditGeneralProductMenu(sc, inv);
                    break;
                case (3):
                    printRemoveGeneralProductMenu(sc,inv);
                    break;
                case (4):
                    printAddSpecificProductMenu(sc,inv);
                    break;
                case (5):
                    printRemoveSpecificProductMenu(sc,inv);
                    break;
                case (6):
                    printMarkAsFlawtMenu(sc,inv);
                    break;
                case (7):
                    printMoveLocationtMenu(sc,inv);
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
    static private void printAddGeneralProductMenu(Scanner sc,Inventory inv) {
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

    static private void printRemoveGeneralProductMenu(Scanner sc,Inventory inv) {
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

    static private void printEditGeneralProductMenu(Scanner sc,Inventory inv) {
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
                    if(param.length==2) {
                        result = inv.editGeneralProduct_name(param[0], param[1]);
                        System.out.println(result.getMessage());
                    }
                    else{
                        System.out.println("Invalid number of parameters");
                    }
                    break;
                case (2):
                    details=details.concat("[CatalogID],[New supplier price]");
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
                    details=details.concat("[CatalogID],[New retail price]");
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
                    details=details.concat("[CatalogID],[New quantity]");
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
                    details=details.concat("[CatalogID],[New minimum quantity]");
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
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }
    }

    //endregion
    //region Specific Product
    static private void printAddSpecificProductMenu(Scanner sc, Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID],[Expiration date (dd/mm/YYYY)],[Quantity]");
        String[] param = getInputParserbyComma(sc);
        if(param.length==3) {
            Date date =convertStringToDate(param[1]);
            if (date!=null){
                result = inv.addSpecificProduct(param[0],date,Integer.parseInt(param[2]));
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

    static private void printRemoveSpecificProductMenu(Scanner sc,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Specific productID]");
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

    static private void printMarkAsFlawtMenu(Scanner sc,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Specific productID]");
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

    static private void printMoveLocationtMenu(Scanner sc,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Specific productID]");
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
    static private void printCategoryMenu(Scanner sc, Inventory inv) {
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
                    printAddMainCategoryMenu(sc, inv);
                    break;
                case (2):
                    printAddSubCategory(sc,inv);
                    break;
                case (3):
                    printRemoveCategory(sc,inv);
                    break;
                case (4):
                    printEditCategoryNameMenu(sc,inv);
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

    static private void printAddMainCategoryMenu(Scanner sc, Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Name]");
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

    static private void printAddSubCategory(Scanner sc, Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[Super categoryID],[Name]\n");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==2) {
            Integer pred_id = Integer.parseInt(param[0]);
            result = inv.addSubCategory(pred_id,param[1]);
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printRemoveCategory(Scanner sc, Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            result = inv.removeCategory(Integer.parseInt(param[0]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printEditCategoryNameMenu(Scanner sc, Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID],[Name]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==2) {
            result = inv.editCategoryname(Integer.parseInt(param[0]),param[1]);
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion

    //region Report Management
    static private void printReportMenu(Scanner sc, Inventory inv) {
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
                    printOutOfStockReportMenu(sc,inv);
                    break;
                case (2):
                    printInStockReportMenu(sc,inv);
                    break;
                case (3):
                    printDNEReportMenu(sc,inv);
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
    static private void printOutOfStockReportMenu(Scanner sc, Inventory inv) {
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
                    printOutOfStockReportByCategoryMenu(sc,inv);
                    break;
                case (2):
                    printOutOfStockReportByGeneralProductMenu(sc,inv);
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

    static private void printOutOfStockReportByCategoryMenu(Scanner sc, Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        menu=menu.concat("for all categories type 'all'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            if(param[0].equals("all")){
                result = inv.makeReport(0, "outofstock");
            }
            else{
                result = inv.makeReport(Integer.parseInt(param[0]), "outofstock");
            }
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }

    static private void printOutOfStockReportByGeneralProductMenu(Scanner sc, Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID]");
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
    static private void printInStockReportMenu(Scanner sc,Inventory inv) {
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
                    printInStocReportByCategoryMenu(sc,inv);
                    break;
                case (2):
                    printInStocReportByGeneralProductMenu(sc,inv);
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

    static private void printInStocReportByCategoryMenu(Scanner sc,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        menu=menu.concat("for all categories type 'all'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            if(param[0].equals("all")){
                result = inv.makeReport(0, "instock");
            }
            else{
                result = inv.makeReport(Integer.parseInt(param[0]), "instock");
            }
            System.out.println(result.getMessage());
        }
    }

    static private void printInStocReportByGeneralProductMenu(Scanner sc,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID]");
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
    static private void printDNEReportMenu(Scanner sc,Inventory inv) {
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
                    printDNEReportByCategoryMenu(sc,inv);
                    break;
                case (2):
                    printDNEReportByGeneralProductMenu(sc,inv);
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

    static private void printDNEReportByCategoryMenu(Scanner sc, Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID]\n");
        menu=menu.concat("for all categories type 'all'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if (param.length==1) {
            if(param[0].equals("all")){
                result = inv.makeReport(0, "dne");
            }
            else{
                result = inv.makeReport(Integer.parseInt(param[0]), "dne");
            }
            System.out.println(result.getMessage());
        }
    }

    static private void printDNEReportByGeneralProductMenu(Scanner sc,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID]");
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
    static private void printSaleMenu(Scanner sc,Inventory inv) {
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
                    printAddSaleMenu(sc, inv);
                    break;
                case (2):
                    printRemoveSaleMenu(sc, inv);
                    break;
                case (3):
                    checkSaleSatus(inv);
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
    static private void printAddSaleMenu(Scanner sc,Inventory inv) {
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
                    printAddNewSaleByCategoryMenu(sc,inv);
                    break;
                case (2):
                    printAddNewSaleByGeneralProductMenu(sc,inv);
                    break;
                case (3):
                    return;
                case (4):
                    Exit();
                default:
                    System.out.println("Option not valid, please retype");
            }
        }     }

    static private void printAddNewSaleByCategoryMenu(Scanner sc,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CategoryID],[Discount percentage\\Fixed price],[Optional: %],[Optional: Start date (dd/mm/YYYY)],[Must if apply 'Start date': End date (dd/mm/YYYY)]\n");
        menu=menu.concat("for discount by percentage please add '%'");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==2) {
            result = inv.addSale(Integer.parseInt(param[0]),"fix",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length==3){
            result = inv.addSale(Integer.parseInt(param[0]),"percentage",Float.parseFloat(param[1]));
            System.out.println(result.getMessage());
        }
        else if(param.length==4){
                Date start_date=convertStringToDate(param[2]);
                Date end_date =convertStringToDate(param[3]);
                if(start_date!=null && end_date!=null){
                    result = inv.addSale(Integer.parseInt(param[0]),"fix",Float.parseFloat(param[1]),start_date,end_date);
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
                result = inv.addSale(Integer.parseInt(param[0]),"percentage",Float.parseFloat(param[1]),start_date,end_date);
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
    static private void printAddNewSaleByGeneralProductMenu(Scanner sc,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[CatalogID],[Discount percentage\\Fixed price],[Optional: %],[Optional: Start date (dd/mm/YYYY)],[Must if apply 'Start date': End date (dd/mm/YYYY)]\n");
        menu=menu.concat("for discount by percentage please add '%'");
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
    static private void printRemoveSaleMenu(Scanner sc,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu=menu.concat("[SaleID]");
        System.out.println(menu);
        String[] param = getInputParserbyComma(sc);
        if(param.length==1){
            result=inv.removeSale(Integer.parseInt(param[0]));
            System.out.println(result.getMessage());
        }
        else{
            System.out.println("Invalid number of parameters");
        }
    }
    //endregion
    //region Check Sales Status
    static private void checkSaleSatus(Inventory inv){
        Result result = inv.CheckSalesStatus();
        System.out.println(result.getMessage());
        System.out.println("Active sales:");
        List<Sale> active_sales = (LinkedList)result.getData();
        for(Sale sale:active_sales){
            System.out.println(sale.toString());
        }
    }

    //endregion

    //endregion

    //region Data mapping
    private static void printDataMapperMenu(Scanner sc,Inventory inv){
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
                    printAllCategories(inv);
                    break;
                case (2):
                    printAllGeneralProducts(inv);
                    break;
                case (3):
                    printAllSales(inv);
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
    private static void printAllCategories(Inventory inv){
        System.out.println(inv.mapAllCategories());
    }
    private static void printAllGeneralProducts(Inventory inv){
        System.out.println(inv.mapAllGeneralProducts());
    }
    private static void printAllSales(Inventory inv){
        System.out.println(inv.mapAllSales());
    }

    //endregion

    //region Utilities
    static private String[] getInputParserbyComma(Scanner sc){
        String user_input = getNextLine(sc);
        //System.out.println(user_input);
        String[] toreturn =user_input.split(",");
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
    private static void PlayBeep(){
        //String filePath = new File(System.getProperty("user.dir")).getParent()+"//resources//beep.wav";
        String filePath = new File(System.getProperty("user.dir"))+"//src//resources//beep.wav";
        File beep = new File(filePath);
        try (Clip clip = AudioSystem.getClip()) {
            clip.open(AudioSystem.getAudioInputStream(beep));
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //endregion
}
