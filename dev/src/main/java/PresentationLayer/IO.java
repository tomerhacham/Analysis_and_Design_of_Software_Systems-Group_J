package PresentationLayer;

//import PresentationLayer.Transport.IOTransport;
import DataAccessLayer.Mapper;
import PresentationLayer.Transport.IOTransport;
import PresentationLayer.Workers.IOWorkers;

import java.util.Scanner;

public class IO {

    private static IO instance = null;
    private static IOTransport TransportInstance = IOTransport.getInstance();
    private static IOWorkers WorkerInstance = IOWorkers.getInstance();
    private static Scanner scanner = new Scanner(System.in);
    private boolean terminated;

    private IO() {
        terminated = false;
    }

    public static IO getInstance(){
        if (instance == null)
            instance = new IO();
        return instance;
    }

    public void SystemActivation(){
        System.out.println("Transports and Workers system\n");
        while (!terminated) {
            System.out.println("Please choose an operation:\n" +
                    "1. Activate workers system.\n" +
                    "2. Activate Transport system.\n" +
                    "3. Exit system.\n");
            int operation;
            try {
                operation = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                operation = -1;
            }
            switch (operation) {
                case 1:
                    WorkerInstance.mainLoop();
                    break;
                case 2:
                    TransportInstance.SystemActivation();
                    break;
                case 3:
                    System.out.println("Thank you, good bye!");
                    terminated = true;
                    try {
                        Mapper.CloseConnection();
                    }catch (Exception e){
                        System.out.println("Database Connection did not closed.");
                    }
                    break;
                default:
                    System.out.println("Invalid operation.");
            }
        }
    }
}
