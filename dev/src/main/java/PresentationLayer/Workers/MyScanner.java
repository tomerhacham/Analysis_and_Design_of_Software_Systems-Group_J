package PresentationLayer.Workers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MyScanner  {
    private Scanner scanner;

    public MyScanner(InputStream source) {
        scanner=new Scanner(source);
    }
    public String nextLine(){
        scanner.skip("\\R");
        return scanner.nextLine();
    }
    public int nextInt(){
        try
        {
            return scanner.nextInt();
        }
        catch (InputMismatchException e)
        {
            scanner.next();
            return -1;
        }
    }
    public double nextDouble(){
        try
        {
            return scanner.nextDouble();
        }
        catch (InputMismatchException e)
        {
            scanner.next();
            return -1;
        }
    }
    public String next()
    {
        return scanner.next();
    }
}
