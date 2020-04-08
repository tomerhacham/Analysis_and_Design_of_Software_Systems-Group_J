package presentation_layer;

import bussines_layer.Inventory;
import bussines_layer.Result;
import java.util.Scanner;

public class CLController {

    public static void main(String[] args) {
        Inventory inventory=new Inventory();
        Boolean exit=false;
        Scanner sc = new Scanner(System.in);    //System.in is a standard input stream
        printLogo();
        printMainMenu();
        while(!exit) {
            Integer option = sc.nextInt();
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
        menu.concat("Choose one of the options:\n");
        menu.concat("1) Products management\n");
        menu.concat("2) Category management\n");
        menu.concat("3) Reports management\n");
        menu.concat("4) Sales management\n");
        menu.concat("5) Exit\n\n");
        System.out.println(menu);
    }

    //region Products Management
    static private void printProductMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Products management\n";
        menu.concat("1) Add new general product\n");
        menu.concat("2) Edit general product\n");
        menu.concat("3) Remove general product\n");
        menu.concat("4) Add specific product\n");
        menu.concat("5) Remove specific product\n");
        menu.concat("6) Mark flaw specific product\n");
        menu.concat("7) Change location of specific product\n");
        menu.concat("8) Return\n");
        menu.concat("9) exit\n\n");
        System.out.println(menu);
        Integer option = sc.nextInt();
        while(!exit) {
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
        String menu = "Please enter the following details\n";
        menu.concat("[CategoryID],[Manufacture],[CatalogID],[Name],[Supplier price],[Retail price],[initial quantity],[Minimum quantity]\n");
        //TODO: parsing
        System.out.println(menu);
    }

    static private void printRemoveGeneralProductMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Please enter the following details\n";
        menu.concat("[CategoryID],[CatalogID]\n");
        //TODO:parsing
        System.out.println(menu);
    }

    static private void printEditGeneralProductMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "editing general product\n";
        menu.concat("1) Edit name\n");
        menu.concat("2) Edit supplier price\n");
        menu.concat("3) Edit retail price\n");
        menu.concat("4) Edit quantity\n");
        menu.concat("5) Edit minimum quantity\n");
        menu.concat("6) Return\n");
        menu.concat("7) Exit\n\n");
        System.out.println(menu);

        String details = "Please enter the following details\n";
        //menu.concat("[CategoryID],[CatalogID]\n");
        //TODO:parsing
        System.out.println(menu);

        //TODO: parsing the arguments and then pass to the inventory function
        Integer option = sc.nextInt();
        while(!exit) {
            switch (option) {
                case (1):
                    //TODO:get catalogID, name
                    details.concat("[CatalogID],[New name]\n");
                    System.out.println(details);
                    result=inv.editGeneralProduct_name();
                    System.out.println(result.getMessage());
                    break;
                case (2):
                    //TODO:get catalogId, new supplier
                    details.concat("[CatalogID],[New supplier price]\n");
                    System.out.println(details);
                    result=inv.editGeneralProduct_supplier_price();
                    System.out.println(result.getMessage());
                    break;
                case (3):
                    //TODO:get catalogId, new retail price
                    details.concat("[CatalogID],[New retail price]\n");
                    System.out.println(details);
                    result=inv.editGeneralProduct_retail_price();
                    System.out.println(result.getMessage());
                    break;
                case (4):
                    //TODO:get catalogId, new quantity
                    details.concat("[CatalogID],[New quantity]\n");
                    System.out.println(details);
                    result=inv.editGeneralProduct_quantity();
                    System.out.println(result.getMessage());
                    break;
                case (5):
                    //TODO:get catalogId, new minimum quantity
                    details.concat("[CatalogID],[New minimum quantity]\n");
                    System.out.println(details);
                    result=inv.editGeneralProduct_min_quantity();
                    System.out.println(result.getMessage());
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
        menu.concat("[CatalogID],[Expiration date],[Quantity]\n");
        //TODO:get catalogID, Expiration date, Quantity
        result=inv.addSpecificProduct();
        System.out.println(result.getMessage());
    }

    static private void printRemoveSpecificProductMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu.concat("[Specific productID]\n");
        System.out.println(menu);
        //TODO:get specific productID
        result=inv.removeSpecificProduct();
        System.out.println(result.getMessage());
    }

    static private void printMarkAsFlawtMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu.concat("[Specific productID]\n");
        System.out.println(menu);
        //TODO:get specific productID
        result=inv.markAsFlaw();
        System.out.println(result.getMessage());
    }

    static private void printMoveLocationtMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu.concat("[Specific productID]\n");
        //TODO:get specific productID
        System.out.println(menu);
        result=inv.moveLocation();
        System.out.println(result.getMessage());
    }
    //endregion

    //endregion

    //region Category Management
    static private void printCategoryMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Category management\n";
        menu.concat("1) Add new main category\n");
        menu.concat("2) Add new sub category\n");
        menu.concat("3) remove category\n");
        menu.concat("4) Edit category name\n");
        menu.concat("5) Return");
        menu.concat("6) Exit\n\n");
        System.out.println(menu);

        //TODO: parsing the arguments and then pass to the inventory function
        Integer option = sc.nextInt();
        while(!exit) {
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
        menu.concat("[Name]\n");
        System.out.println(menu);
        //TODO:get category name
        result=inv.addMainCategory();
        System.out.println(result.getMessage());
    }

    static private void printAddSubCategory(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu.concat("[Super categoryID],[Name]\n");
        System.out.println(menu);
        //TODO:get superCategoryID and name
        result=inv.addSubCategory();
        System.out.println(result.getMessage());
    }

    static private void printRemoveCategory(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu.concat("[CategoryID]\n");
        System.out.println(menu);
        //TODO:get categoryID
        result=inv.removeCategory();
        System.out.println(result.getMessage());
    }

    static private void printEditCategoryNameMenu(Scanner sc,Boolean exit,Inventory inv) {
        Result result;
        String menu = "Please enter the following details\n";
        menu.concat("[CategoryID],[Name]\n");
        System.out.println(menu);
        //TODO:get categoryId and new name
        result=inv.editCategoryname();
        System.out.println(result.getMessage());
    }
    //endregion

    //region Report Management
    static private void printReportMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Report management\n";
        menu.concat("1) Issue out of stock report\n");
        menu.concat("2) Issue in-stock report\n");
        menu.concat("3) Issue damaged&expired report\n");
        menu.concat("4) Return\n");
        menu.concat("5) Exit\n\n");
        System.out.println(menu);
        //TODO: parsing the arguments and then pass to the inventory function
        Integer option = sc.nextInt();
        while(!exit) {
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
        menu.concat("1) By category\n");
        menu.concat("2) By general product\n");
        menu.concat("3) Return\n");
        menu.concat("4) Exit\n\n");
        System.out.println(menu);
        //TODO: parsing the arguments and then pass to the inventory function
        Integer option = sc.nextInt();
        while(!exit) {
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
        menu.concat("[CategoryID]\n");
        menu.concat("for all categories type 'all'\n");
        System.out.println(menu);
        //TODO:get categoryId
        result=inv.makeReport(, "outofstock");
        System.out.println(result.getMessage());
    }

    static private void printOutOfStockReportByGeneralProductMenu(Scanner sc,Boolean exit,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu.concat("[CatalogID]\n");
        System.out.println(menu);
        //TODO:get catalogID
        result=inv.makeReport(,"outofstock");
        System.out.println(result.getMessage());
    }
    //endregion

    //region In-Stock Report
    static private void printInStockReportMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Choose one of the options\n";
        menu.concat("1) By category\n");
        menu.concat("2) By general product\n");
        menu.concat("3) Return\n");
        menu.concat("4) Exit\n\n");
        System.out.println(menu);
        //TODO: parsing the arguments and then pass to the inventory function
        Integer option = sc.nextInt();
        while(!exit) {
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
        menu.concat("[CategoryID]\n");
        menu.concat("for all categories type 'all'\n");
        System.out.println(menu);
        //TODO:get categoryID
        result=inv.makeReport(,"instock");
        System.out.println(result.getMessage());
    }

    static private void printInStocReportByGeneralProductMenu(Scanner sc,Boolean exit,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu.concat("[CatalogID]\n");
        System.out.println(menu);
        //TODO:get catalogID
        result=inv.makeReport(,"instock");
        System.out.println(result.getMessage());
    }
    //endregion

    //region Damaged and Expired Report
    static private void printDNEReportMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Choose one of the options\n";
        menu.concat("1) By category\n");
        menu.concat("2) By general product\n");
        menu.concat("3) Return\n");
        menu.concat("4) Exit\n\n");
        System.out.println(menu);
        //TODO: parsing the arguments and then pass to the inventory function
        Integer option = sc.nextInt();
        while(!exit) {
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
        menu.concat("[CategoryID]\n");
        menu.concat("for all categories type 'all'\n");
        System.out.println(menu);
        //TODO:get categoryID
        result=inv.makeReport(,"dne");
        System.out.println(result.getMessage());
    }

    static private void printDNEReportByGeneralProductMenu(Scanner sc,Boolean exit,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu.concat("[CatalogID]\n");
        System.out.println(menu);
        //TODO:get catalogID
        result=inv.makeReport(,"dne");
        System.out.println(result.getMessage());
    }
    //endregion

    //endregion

    //region Sale Management
    static private void printSaleMenu(Scanner sc,Boolean exit,Inventory inv) {
        String menu = "Sales management\n";
        menu.concat("1) Add new sale\n");
        menu.concat("2) Cancel sale\n");
        menu.concat("3) Return\n");
        menu.concat("4) Exit\n\n");
        System.out.println(menu);
        //TODO: parsing the arguments and then pass to the inventory function
        Integer option = sc.nextInt();
        while(!exit) {
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
        menu.concat("1) By category\n");
        menu.concat("2) By general product\n");
        menu.concat("3) Return\n");
        menu.concat("4) Exit\n\n");
        System.out.println(menu);
        //TODO: parsing the arguments and then pass to the inventory function
        Integer option = sc.nextInt();
        while(!exit) {
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
        menu.concat("[CategoryID],[Discount percentage\\Fixed price] [Optional: %]\n");
        menu.concat("for discount by percentage please add '%'\n");
        System.out.println(menu);
        //TODO:get category id ,he amount and mode
        result=inv.addSale();
        System.out.println(result.getMessage());
    }
    static private void printAddNewSaleByGeneralProductMenu(Scanner sc,Boolean exit,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu.concat("[CatalogID],[Discount percentage\\Fixed price] [Optional: %]\n");
        menu.concat("for discount by percentage please add '%'\n");
        System.out.println(menu);
        //TODO:get catalogID, amount and mode
        result=inv.addSale();
        System.out.println(result.getMessage());
    }
    //endregion
    //region Remove Sale
    static private void printRemoveSaleMenu(Scanner sc,Boolean exit,Inventory inv){
        Result result;
        String menu = "Please enter the following details\n";
        menu.concat("[SaleID]\n");
        System.out.println(menu);
        //TODO:get saleID
        result=inv.removeSale();
        System.out.println(result.getMessage());
    }
    //endregion

    //endregion

    //region Utilities

    //endregion
}
