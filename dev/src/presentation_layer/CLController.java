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
}
