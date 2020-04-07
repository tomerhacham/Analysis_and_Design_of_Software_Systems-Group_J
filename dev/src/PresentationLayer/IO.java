package PresentationLayer;

import InterfaceLayer.FacadeController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class IO {

    private static IO instance = null;
    private static FacadeController facadeController = FacadeController.getInstance();
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
        System.out.println("Transports system\n Please choose an operation:\n");

        while (!terminated) {
            System.out.println(
                    "1. Book new transport.\n" +
                    "2. Delete transport.\n" +
                    "3. Display all trucks.\n" +
                    "4. Display all drivers.\n" +
                    "5. Display all sites.\n" +
                    "6. Display all transports.\n" +
                    "7. Add truck.\n" +
                    "8. Add driver.\n" +
                    "9. Add site.\n" +
                    "10. Remove truck.\n" +
                    "11. Remove driver.\n" +
                    "12. Remove site.\n" +
                    "13. Exit system.\n");
            int operation = scanner.nextInt();
            switch (operation) {
                case 1 :
                    newTransport();
                    break;
                case 2:
                    deleteTransport();
                    break;
                case 3:
                    System.out.println(facadeController.getAllTrucksDetails());
                    break;
                case 4:
                    System.out.println(facadeController.getAllDriversDetails());
                    break;
                case 5:
                    System.out.println(facadeController.getAllSitesDetails());
                    break;
                case 6:
                    System.out.println(facadeController.getAllTransportsDetails());
                    break;
                case 7:
                    addTruck();
                    break;
                case 8:
                    addDriver();
                    break;
                case 9:
                    addSite();
                    break;
                case 10:
                    deleteTruck();
                    break;
                case 11:
                    deleteDriver();
                    break;
                case 12:
                    deleteSite();
                    break;
                case 13:
                    System.out.println("Thank you, good bye!");
                    terminated = true;
                    break;
                default:
                    System.out.println("Invalid operation.");
            }
        }
    }

    private void deleteSite() {
        System.out.println(facadeController.getAllSitesDetails());
        System.out.println("Please choose the site ID you wish to remove:");
        int siteToDelete = scanner.nextInt();
        facadeController.deleteSite(siteToDelete);
    }

    private void deleteDriver() {
        System.out.println(facadeController.getAllDriversDetails());
        System.out.println("Please choose the driver ID you wish to remove:");
        int driverToDelete = scanner.nextInt();
        facadeController.deleteDriver(driverToDelete);
    }

    private void deleteTruck() {
        System.out.println(facadeController.getAllTrucksDetails());
        System.out.println("Please choose the truck ID you wish to remove:");
        int truckToDelete = scanner.nextInt();
        facadeController.deleteTruck(truckToDelete);
    }

    private void addSite() {
        System.out.println("Please enter the following details:\n");
        System.out.println("Address:");
        String address = scanner.nextLine();
        System.out.println("Phone number:");
        String phone_number = scanner.nextLine();
        System.out.println("Contact:");
        String contact = scanner.nextLine();
        System.out.println("Shipping area:");
        int shipping_area = scanner.nextInt();
        facadeController.createSite(address, phone_number, contact, shipping_area);
    }

    private void addDriver() {
        System.out.println("Please enter the following details:\n");
        System.out.println("Driver name:");
        String name = scanner.nextLine();
        System.out.println("Driver's License:");
        String license = scanner.nextLine();
        facadeController.createDriver(name, license);
    }

    private void addTruck() {
        System.out.println("Please enter the following details:\n");
        System.out.println("License plate:");
        String license_plate = scanner.nextLine();
        System.out.println("Model:");
        String model = scanner.nextLine();
        System.out.println("Weight:");
        int weight = Integer.parseInt(scanner.nextLine());
        System.out.println("Drivers license:");
        String drivers_license = scanner.nextLine();
        facadeController.createTruck(license_plate, model, weight, drivers_license);
    }

    private void deleteTransport() {
        System.out.println(facadeController.getAllTransportsDetails());
        System.out.println("Please choose the truck ID you wish to remove:");
        int transportToDelete = scanner.nextInt();
        facadeController.deleteTransport(transportToDelete);
    }

    private void newTransport() {
        System.out.println("Please enter a date in the format dd-mm-yyyy\n");
        DateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        Date date;
        while (true){
            try {
                date = formatter.parse(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Format is incorrect. Try again.");
            }
        }
        System.out.println(facadeController.getAvailableTrucks(date));
        System.out.println("Please choose a truck ID from the above");
        int truckID = scanner.nextInt();
        System.out.println(facadeController.getAvailableDrivers(date, truckID));
        System.out.println("Please choose a driver ID from the above");
        int driverID = scanner.nextInt();
        System.out.println(facadeController.getAllSitesDetails());
        System.out.println("Please choose source site ID from the above");
        int sourceID = scanner.nextInt();
        // TODO:: destinations? files with products?
        facadeController.createTransport(date, truckID, driverID, sourceID);
    }
}
