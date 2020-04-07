package presentation_layer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class CLController {
    public static void main(String[] args)
    {
        printLogo();
        Scanner sc= new Scanner(System.in);    //System.in is a standard input stream
        System.out.print("Enter first number- ");


    }
    static private void printLogo(){
        String logo =   " _____                       _     _ \n" +
                        "/  ___|                     | |   (_)\n" +
                        "\\ `--. _   _ _ __   ___ _ __| |    _ \n" +
                        " `--. \\ | | | '_ \\ / _ \\ '__| |   | |\n" +
                        "/\\__/ / |_| | |_) |  __/ |  | |___| |\n" +
                        "\\____/ \\__,_| .__/ \\___|_|  \\_____/_|\n" +
                        "            | |                      \n" +
                        "            |_|     ";
        System.out.println(logo);
    }
    static private void printMainMenu(){
        String menu="";
        menu.concat("Choose one of the options:\n");
        menu.concat("1) Products management\n");
        menu.concat("2) Category management\n");
        menu.concat("3) Reports management\n");
        menu.concat("4) Sales management\n\n");
        System.out.println(menu);
    }

    static private void printProductMenu(){
        String menu="Products management\n";
        menu.concat("1) Add new general product\n");
        menu.concat("2) Edit general product\n");
        menu.concat("3) Remove general product\n");
        menu.concat("4) Add specific product\n");
        menu.concat("5) Remove specific product\n");
        menu.concat("6) Mark flaw specific product\n");
        menu.concat("7) Change location of specific product\n\n");
        System.out.println(menu);
    }
    static private void printAddGeneralProductMenu(){
        String menu="Please enter the following details\n";
        menu.concat("[CategoryID],[Manufacture],[CatalogID],[Name],[Supplier price],[Retail price],[initial quantity],[Minimum quantity]\n");
        System.out.println(menu);
    }

    static private void printRemoveGeneralProductMenu(){
        String menu="Please enter the following details\n";
        menu.concat("[CategoryID],[CatalogID]\n");
        System.out.println(menu);
    }
    static private void printEditGeneralProductMenu(){
        String menu="editing general product\n";
        menu.concat("1) Edit name\n");
        menu.concat("2) Edit supplier price\n");
        menu.concat("3) Edit retail price\n");
        menu.concat("4) Edit quantity\n");
        menu.concat("5) Edit minimum quantity\n");
        System.out.println(menu);
    }

    static private void printAddSpecificProductMenu(){
        String menu="Please enter the following details\n";
        menu.concat("[CatalogID],[Expiration date],[Quantity]\n");
        System.out.println(menu);
    }

    static private void printRemoveSpecificProductMenu(){
        String menu="Please enter the following details\n";
        menu.concat("[Specific productID]\n");
        System.out.println(menu);
    }
    static private void printMarkAsFlawtMenu(){
        String menu="Please enter the following details\n";
        menu.concat("[Specific productID]\n");
        System.out.println(menu);
    }
    static private void printMoveLocationtMenu(){
        String menu="Please enter the following details\n";
        menu.concat("[Specific productID]\n");
        System.out.println(menu);
    }
}
