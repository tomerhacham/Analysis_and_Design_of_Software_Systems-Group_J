package PresentationLayer;

import InterfaceLayer.FacadeController;

import java.util.HashMap;
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
            int operation = Integer.parseInt(scanner.nextLine());
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
        int siteToDelete = Integer.parseInt(scanner.nextLine());
        facadeController.deleteSite(siteToDelete);
    }

    private void deleteDriver() {
        System.out.println(facadeController.getAllDriversDetails());
        System.out.println("Please choose the driver ID you wish to remove:");
        int driverToDelete = Integer.parseInt(scanner.nextLine());
        facadeController.deleteDriver(driverToDelete);
    }

    private void deleteTruck() {
        System.out.println(facadeController.getAllTrucksDetails());
        System.out.println("Please choose the truck ID you wish to remove:");
        int truckToDelete = Integer.parseInt(scanner.nextLine());
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
        int shipping_area = Integer.parseInt(scanner.nextLine());
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
        System.out.println("Net weight of the truck:");
        int netWeight = Integer.parseInt(scanner.nextLine());
        System.out.println("Max weight the truck can curry:");
        int maxWeight = Integer.parseInt(scanner.nextLine());
        System.out.println("Drivers license:");
        String drivers_license = scanner.nextLine();
        facadeController.createTruck(license_plate, model, netWeight, maxWeight, drivers_license);
    }

    private void deleteTransport() {
        System.out.println(facadeController.getAllTransportsDetails());
        System.out.println("Please choose the truck ID you wish to remove:");
        int transportToDelete = Integer.parseInt(scanner.nextLine());
        facadeController.deleteTransport(transportToDelete);
    }

    private void newTransport() {
        int transportID = facadeController.createTransport();
        System.out.println("Please enter a date in the format dd/mm/yyyy\n");
        String date;
        while (true){
            try {
                date = scanner.nextLine();
                boolean dateAvailable = facadeController.setTransportDate(transportID, date);
                if (dateAvailable)
                    break;
                else
                    System.out.println("There are no available trucks or drivers in the specified date. Choose different date.\n");
            } catch (Exception e) {
                System.out.println("Format is incorrect. Try again.\n");
            }
        }
        int sourceID = chooseSource();
        if (sourceID == -1){
            facadeController.removeInlayDate(facadeController.getTransportDate(transportID), transportID);
            facadeController.deleteTransport(transportID);
            return;
        }
        facadeController.setTransportSource(transportID, sourceID);
        System.out.println("Destinations:\n" + facadeController.getAvailableSites(sourceID));
        System.out.println("How many destinations would you like? ");
        int numDest = Integer.parseInt(scanner.nextLine());
        HashMap<Integer, Integer> DestFiles = chooseProductsPerSite(numDest);
        facadeController.setTransportDestFiles(transportID, DestFiles);
        int truckID = chooseTruck(transportID);
        if (truckID == -1) {
            facadeController.removeInlayDate(facadeController.getTransportDate(transportID), transportID);
            facadeController.deleteTransport(transportID);
            return;
        }
        facadeController.setTransportTruck(transportID, truckID);
        int totalWeight = facadeController.getTotalWeight(DestFiles);
        facadeController.setTransportWeight(transportID, totalWeight);
        int driverID = chooseDriver(transportID);
        if (driverID == -1) {
            facadeController.removeInlayDate(facadeController.getTransportDate(transportID), transportID);
            facadeController.deleteTransport(transportID);
            return;
        }
        facadeController.setTransportDriver(transportID, driverID);
        facadeController.addInlayDate(facadeController.getTransportDate(transportID), transportID);
    }

    private int chooseDriver(int transportID) {
        while (true) {
            String drivers = facadeController.getAvailableDrivers(facadeController.getTransportDate(transportID),
                    facadeController.getTransportTruck(transportID));
            if (drivers.equals("")) {
                System.out.println("There is no driver with compatible license to the selected truck in the system.\n" +
                        "choose 1 to change truck.\n" +
                        "choose 2 to abort transport.\n");
                int opt = Integer.parseInt(scanner.nextLine());
                if (opt == 2) {
                    return -1;
                }
                else if (opt == 1){
                    int truckID = facadeController.getTransportTruck(transportID);
                    facadeController.addTransportLog("The truck: " + facadeController.getTruckDetails(truckID) + "\n" +
                            "was changed.", transportID);
                    facadeController.setTransportTruck(transportID, chooseTruck(transportID));
                }
                else {
                    System.out.println("The input is invalid, transport aborted.");
                    return -1;
                }
            }
            else {
                System.out.println("Drivers:\n" + drivers);
                System.out.println("Please choose a driver ID from the above");
                return Integer.parseInt(scanner.nextLine());
            }
        }
    }

    private int chooseTruck(int transportID) {
        int totalWeight = facadeController.getTotalWeight(facadeController.getTransportDestFiles(transportID));
        while (true) {
            String trucks = facadeController.getAvailableTrucks(facadeController.getTransportDate(transportID), totalWeight); //TODO:: separate date and weight
            if (trucks.equals("")) {
                System.out.println("There is no truck that can carry such weight in the system.\n" +
                        "choose 1 to edit destination.\n" +
                        "choose 2 to abort transport.\n");
                int opt = Integer.parseInt(scanner.nextLine());
                if (opt == 2) {
                    return -1;
                }
                else if (opt == 1){
                    editDestinations(transportID);
                    totalWeight = facadeController.getTotalWeight(facadeController.getTransportDestFiles(transportID));
                }
                else {
                    System.out.println("The input is invalid, transport aborted.");
                    return -1;
                }
            }
            else {
                System.out.println("Trucks:\n" + trucks);
                System.out.println("Please choose a truck ID from the above");
                return Integer.parseInt(scanner.nextLine());
            }
        }
    }

    private void editDestinations(int transportID) {
        System.out.println("Please choose the option you would like to edit:\n" +
                "1. Remove destination from transport.\n" +
                "2. Remove products from destination.\n");
        int opt = Integer.parseInt(scanner.nextLine());
        System.out.println(facadeController.getProductsByDest(transportID));
        if (opt == 1) {
            System.out.println("Please choose destination site ID to remove\n");
            int destToRemove = Integer.parseInt(scanner.nextLine());
            facadeController.addTransportLog("The destination: " + facadeController.getSiteDetails(destToRemove) + "\n" +
                            "was removed from transport.", transportID);
            facadeController.removeDestFromTransport(transportID, destToRemove);
        }
        else if (opt == 2){
            System.out.println("Please choose destination site ID to edit\n");
            int destToEdit = Integer.parseInt(scanner.nextLine());
            int fileToEdit = facadeController.getDestFileID(transportID, destToEdit);
            System.out.println("Please insert products ID to remove with spaces between\n");
            String[] productsToRemove = (scanner.nextLine()).split(" ");
            facadeController.addTransportLog("The products:\n" + facadeController.getProductsDetails(productsToRemove) + "\n" +
                    "was removed from destination: " + facadeController.getSiteDetails(destToEdit), transportID);
            facadeController.removeProducts(productsToRemove, fileToEdit);
        }
        else {
            System.out.println("The input is invalid, transport did not changed.");
        }
    }

    private HashMap<Integer, Integer> chooseProductsPerSite(int numDest) {
        HashMap<Integer, Integer> destFiles = new HashMap<>();
        for (int i = 0; i < numDest; i++){
            System.out.println("Please choose dest ID\n");
            int destID = Integer.parseInt(scanner.nextLine());
            int fileID = facadeController.createProductsFile();
            System.out.println("How many products would you like for this destination? ");
            int numProducts = Integer.parseInt(scanner.nextLine());
            for (int j = 0; j < numProducts; j++){
                System.out.println("Please enter name of a product: ");
                String productName = scanner.nextLine();
                System.out.println("Please enter weight of a product: ");
                int productWeight = Integer.parseInt(scanner.nextLine());
                System.out.println("How many items form this product? ");
                int quantity = Integer.parseInt(scanner.nextLine());
                facadeController.createProduct(productName, productWeight, fileID, quantity);
            }
            destFiles.put(destID, fileID);
        }
        return destFiles;
    }

    private int chooseSource() {
        int sourceID;
        while (true) {
            System.out.println("Sources:\n" + facadeController.getAllSitesDetails());
            System.out.println("Please choose source site ID from the above\n");
            sourceID = Integer.parseInt(scanner.nextLine());
            String destinations = facadeController.getAvailableSites(sourceID);
            if (destinations.equals("")) {
                System.out.println("No destination sites in this shipping area.\n " +
                        "choose 1 to insert new source site\n" +
                        "choose 2 to abort transport\n");
                int opt = Integer.parseInt(scanner.nextLine());
                if (opt == 2){
                    sourceID = -1;
                    break;
                }
                else if(opt == 1)
                    continue;
                else {
                    System.out.println("The input is invalid, transport aborted.");
                    return -1;
                }
            }
            else
                break;
        }
        return sourceID;
    }
}
