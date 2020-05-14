import DataAccessLayer.Mapper;
import PresentationLayer.IO;
import org.junit.jupiter.api.parallel.Execution;

public class Main {

    public static void main(String[] args) {
        IO io = IO.getInstance();
       io.SystemActivation();
        /*
        Mapper m = Mapper.getInstance();
        System.out.println(m==null);
        try {
            m.CloseConnetion();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }*/
    }
}