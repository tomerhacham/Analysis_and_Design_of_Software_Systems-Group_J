package presentation_layer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class CLController {
    public static void main(String[] args) {
        printLogo();
        Scanner sc = new Scanner(System.in);    //System.in is a standard input stream
        System.out.print("Enter first number- ");


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
        menu.concat("4) Sales management\n\n");
        System.out.println(menu);
    }

    //region Products Management
    static private void printProductMenu() {
        String menu = "Products management\n";
        menu.concat("1) Add new general product\n");
        menu.concat("2) Edit general product\n");
        menu.concat("3) Remove general product\n");
        menu.concat("4) Add specific product\n");
        menu.concat("5) Remove specific product\n");
        menu.concat("6) Mark flaw specific product\n");
        menu.concat("7) Change location of specific product\n\n");
        System.out.println(menu);
    }

    //region General Product
    static private void printAddGeneralProductMenu() {
        String menu = "Please enter the following details\n";
        menu.concat("[CategoryID],[Manufacture],[CatalogID],[Name],[Supplier price],[Retail price],[initial quantity],[Minimum quantity]\n");
        System.out.println(menu);
    }

    static private void printRemoveGeneralProductMenu() {
        String menu = "Please enter the following details\n";
        menu.concat("[CategoryID],[CatalogID]\n");
        System.out.println(menu);
    }

    static private void printEditGeneralProductMenu() {
        String menu = "editing general product\n";
        menu.concat("1) Edit name\n");
        menu.concat("2) Edit supplier price\n");
        menu.concat("3) Edit retail price\n");
        menu.concat("4) Edit quantity\n");
        menu.concat("5) Edit minimum quantity\n");
        System.out.println(menu);
    }

    //endregion
    //region Specific Product
    static private void printAddSpecificProductMenu() {
        String menu = "Please enter the following details\n";
        menu.concat("[CatalogID],[Expiration date],[Quantity]\n");
        System.out.println(menu);
    }

    static private void printRemoveSpecificProductMenu() {
        String menu = "Please enter the following details\n";
        menu.concat("[Specific productID]\n");
        System.out.println(menu);
    }

    static private void printMarkAsFlawtMenu() {
        String menu = "Please enter the following details\n";
        menu.concat("[Specific productID]\n");
        System.out.println(menu);
    }

    static private void printMoveLocationtMenu() {
        String menu = "Please enter the following details\n";
        menu.concat("[Specific productID]\n");
        System.out.println(menu);
    }
    //endregion

    //endregion

    //region Category Management
    static private void printCategoryMenu() {
        String menu = "Category management\n";
        menu.concat("1) Add new main category\n");
        menu.concat("2) Add new sub category\n");
        menu.concat("3) remove category\n");
        menu.concat("4) Edit category name\n\n");
        System.out.println(menu);
    }

    static private void printAddMainCategoryMenu() {
        String menu = "Please enter the following details\n";
        menu.concat("[Name]\n");
        System.out.println(menu);
    }

    static private void printAddSubCategory() {
        String menu = "Please enter the following details\n";
        menu.concat("[Super categoryID],[Name]\n");
        System.out.println(menu);
    }

    static private void printRemoveCategory() {
        String menu = "Please enter the following details\n";
        menu.concat("[CategoryID]\n");
        System.out.println(menu);
    }

    static private void printEditCategoryNameMenu() {
        String menu = "Please enter the following details\n";
        menu.concat("[CategoryID],[Name]\n");
        System.out.println(menu);
    }
    //endregion

    //region Report Management
    static private void printReportMenu() {
        String menu = "Report management\n";
        menu.concat("1) Issue out of stock report\n");
        menu.concat("2) Issue in-stock report\n");
        menu.concat("3) Issue damaged&expired report\n");
        System.out.println(menu);
    }
    //region Out of Stock Report
    static private void printOutOfStockReportMenu() {
        String menu = "Choose one of the options\n";
        menu.concat("1) By category\n");
        menu.concat("2) By general product\n");
        System.out.println(menu);
    }

    static private void printOutOfStockReportByCategoryMenu() {
        String menu = "Please enter the following details\n";
        menu.concat("[CategoryID]\n");
        menu.concat("for all categories type 'all'\n");
        System.out.println(menu);
    }

    static private void printOutOfStockReportByGeneralProductMenu(){
        String menu = "Please enter the following details\n";
        menu.concat("[CatalogID]\n");
        System.out.println(menu);
    }
    //endregion

    //region In-Stock Report
    static private void printInStockReportMenu() {
        String menu = "Choose one of the options\n";
        menu.concat("1) By category\n");
        menu.concat("2) By general product\n");
        System.out.println(menu);
    }

    static private void printInStocReportByCategoryMenu() {
        String menu = "Please enter the following details\n";
        menu.concat("[CategoryID]\n");
        menu.concat("for all categories type 'all'\n");
        System.out.println(menu);
    }

    static private void printInStocReportByGeneralProductMenu(){
        String menu = "Please enter the following details\n";
        menu.concat("[CatalogID]\n");
        System.out.println(menu);
    }
    //endregion

    //region Damaged and Expired Report
    static private void printDNEReportMenu() {
        String menu = "Choose one of the options\n";
        menu.concat("1) By category\n");
        menu.concat("2) By general product\n");
        System.out.println(menu);
    }

    static private void printDNEReportByCategoryMenu() {
        String menu = "Please enter the following details\n";
        menu.concat("[CategoryID]\n");
        menu.concat("for all categories type 'all'\n");
        System.out.println(menu);
    }

    static private void printDNEReportByGeneralProductMenu(){
        String menu = "Please enter the following details\n";
        menu.concat("[CatalogID]\n");
        System.out.println(menu);
    }
    //endregion

    //endregion

    //region Sale Management
    static private void printSaleMenu() {
        String menu = "Sales management\n";
        menu.concat("1) Add new sale\n");
        menu.concat("2) Cancel sale\n");
        System.out.println(menu);
    }

    //region Add Sale
    static private void PrintAddSaleMenu() {
        String menu = "Choose one of the options\n";
        menu.concat("1) By category\n");
        menu.concat("2) By general product\n");
        System.out.println(menu);
    }
    static private void printAddNewSaleByCategoryMenu(){
        String menu = "Please enter the following details\n";
        menu.concat("[CategoryID],[Discount percentage\\Fixed price] [Optional: %]\n");
        menu.concat("for discount by percentage please add '%'\n");
        System.out.println(menu);
    }
    static private void printAddNewSaleByGeneralProductMenu(){
        String menu = "Please enter the following details\n";
        menu.concat("[CatalogID],[Discount percentage\\Fixed price] [Optional: %]\n");
        menu.concat("for discount by percentage please add '%'\n");
        System.out.println(menu);
    }
    //endregion
    //region Remove Sale
    static private void printRemoveSaleMenu(){
        String menu = "Please enter the following details\n";
        menu.concat("[SaleID]\n");
        System.out.println(menu);
    }
    //endregion

    //endregion
}
