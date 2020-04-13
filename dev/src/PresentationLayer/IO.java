package PresentationLayer;

import InterfaceLayer.*;

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
        System.out.println("Transports system\n");
        initializeData();
        while (!terminated) {
            System.out.println( "Please choose an operation:\n" +
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
                    System.out.println("Trucks:\n" + facadeController.getAllTrucksDetails());
                    break;
                case 4:
                    System.out.println("Drivers:\n" + facadeController.getAllDriversDetails());
                    break;
                case 5:
                    System.out.println("Sites:\n" + facadeController.getAllSitesDetails());
                    break;
                case 6:
                    System.out.println("Transports:\n" + facadeController.getAllTransportsDetails());
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

    public void initializeData()
    {
        facadeController.createDriver("Eran", "C1");
        facadeController.createDriver("Omer","C");
        facadeController.createDriver("Noam","C1");
        facadeController.createSite("Beer-Sheva","054-1234567", "Shira",1);
        facadeController.createSite("Ofakim","052-1234567","Einav",1);
        facadeController.createSite("Omer","050-1234567", "Amit",1);
        facadeController.createSite("herzelia","052-8912345","Shachaf",3);
        facadeController.createSite("Tel-Aviv","050-8912345","Mai",3);
        facadeController.createSite("Jerusalem","050-8912345","Eden",4);
        facadeController.createTruck("12-L8","XXX",1000,1500,"C1");
        facadeController.createTruck("17-LD","X23",1050,1260,"C1");
        facadeController.createTruck("J0-38","1X6",700,1000,"C");
    }

    private void deleteSite() {
        String details = facadeController.getAllSitesDetails();
        if (details.equals("")){
            System.out.println("There are no sites to delete.\n");
        }
        else {
            System.out.println(details);
            System.out.println("Please choose the site ID you wish to remove:");
            int siteToDelete = Integer.parseInt(scanner.nextLine());
            boolean deleted = facadeController.deleteSite(siteToDelete);
            if(deleted) {
                System.out.println("The site deleted successfully.\n");
            }
            else {
                System.out.println("The site ID:"+siteToDelete+" is not in the system, operation canceled.\n");
            }
        }
    }

    private void deleteDriver() {
        String details = facadeController.getAllDriversDetails();
        if (details.equals("")){
            System.out.println("There are no drivers to delete.\n");
        }
        else {
            System.out.println(details);
            System.out.println("Please choose the driver ID you wish to remove:");
            int driverToDelete = Integer.parseInt(scanner.nextLine());
            boolean deleted =facadeController.deleteDriver(driverToDelete);
            if(deleted) {
                System.out.println("The driver deleted successfully.\n");
            }
            else {
                System.out.println("The driver ID:"+driverToDelete+" is not in the system, operation canceled.\n");
            }
        }
    }

    private void deleteTruck() {
        String details = facadeController.getAllTrucksDetails();
        if (details.equals("")){
            System.out.println("There are no trucks to delete.\n");
        }
        else {
            System.out.println(details);
            System.out.println("Please choose the truck ID you wish to remove:");
            int truckToDelete = Integer.parseInt(scanner.nextLine());
            boolean deleted =facadeController.deleteTruck(truckToDelete);
            if(deleted) {
                System.out.println("The truck deleted successfully.\n");
            }
            else {
                System.out.println("The truck ID:"+truckToDelete+" is not in the system, operation canceled.\n");
            }
        }
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
        System.out.println("\nThe site added successfully.\n");
    }

    private void addDriver() {
        System.out.println("Please enter the following details:\n");
        System.out.println("Driver name:");
        String name = scanner.nextLine();
        System.out.println("Driver's License:");
        String license = scanner.nextLine();
        facadeController.createDriver(name, license);
        System.out.println("\nThe driver added successfully.\n");
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
        if (maxWeight <= netWeight){
            System.out.println("Max weight should be bigger than net weight. Add truck failed.");
            return;
        }
        System.out.println("Drivers license:");
        String drivers_license = scanner.nextLine();
        facadeController.createTruck(license_plate, model, netWeight, maxWeight, drivers_license);
        System.out.println("\nThe truck added successfully.\n");
    }

    private void deleteTransport() {
        String details = facadeController.getAllTransportsDetails();
        if (details.equals("")){
            System.out.println("There are no transports to delete.\n");
        }
        else {
            System.out.println(details);
            System.out.println("Please choose the transport ID you wish to remove:");
            int transportToDelete = Integer.parseInt(scanner.nextLine());
            facadeController.removeInlayDate(facadeController.getTransportDate(transportToDelete), transportToDelete);
            boolean deleted = facadeController.deleteTransport(transportToDelete);
            if(deleted) {
                System.out.println("The transport deleted successfully.\n");
            }
            else {
                System.out.println("The transport ID:"+transportToDelete+" is not in the system, operation canceled.\n");
            }
        }
    }

    private void newTransport() {
        int transportID = facadeController.createTransport();
        System.out.println("Please enter a date in the format dd/mm/yyyy");
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
            facadeController.deleteTransport(transportID);
            return;
        }
        facadeController.setTransportSource(transportID, sourceID);
        //System.out.println("Destinations:\n" + facadeController.getAvailableSites(sourceID));
        //System.out.println("How many destinations would you like? ");
        //int numDest = Integer.parseInt(scanner.nextLine());
        HashMap<Integer, Integer> DestFiles = chooseProductsPerSite(sourceID);
        facadeController.setTransportDestFiles(transportID, DestFiles);
        int truckID = chooseTruck(transportID);
        if (truckID == -1) {
            facadeController.deleteTransport(transportID);
            return;
        }
        facadeController.setTransportTruck(transportID, truckID);
        float totalWeight = facadeController.getTotalWeight(DestFiles);
        facadeController.setTransportWeight(transportID, totalWeight);
        int driverID = chooseDriver(transportID);
        if (driverID == -1) {
            facadeController.deleteTransport(transportID);
            return;
        }
        facadeController.setTransportDriver(transportID, driverID);
        facadeController.addInlayDate(facadeController.getTransportDate(transportID), transportID);
        System.out.println("\nThe transport added successfully.\n");
    }

    private int chooseDriver(int transportID) {
        while (true) {
            String drivers = facadeController.getAvailableDrivers(facadeController.getTransportDate(transportID),
                    facadeController.getTransportTruck(transportID));
            if (drivers.equals("")) {
                System.out.println("There is no driver with compatible license to the selected truck in the system.\n" +
                        "\tchoose 1 to change truck.\n" +
                        "\tchoose 2 to abort transport.");
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
        float totalWeight = facadeController.getTotalWeight(facadeController.getTransportDestFiles(transportID));
        while (true) {
            String trucks = facadeController.getAvailableTrucks(facadeController.getTransportDate(transportID), totalWeight);
            if (trucks.equals("")) {
                System.out.println("There is no truck that can carry such weight in the system.\n" +
                        "\tchoose 1 to edit destination.\n" +
                        "\tchoose 2 to abort transport.");
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

    private HashMap<Integer, Integer> chooseProductsPerSite(int sourceID) {
        HashMap<Integer, Integer> destFiles = new HashMap<>();
        boolean flag=true;
        //for (int i = 0; i < numDest; i++){
        String Destinations =  facadeController.getAvailableSites(sourceID,destFiles);
        while(flag)
        {
            System.out.println("Destinations:\n" + Destinations);
            System.out.println("Please choose dest ID:\n");
            int destID = Integer.parseInt(scanner.nextLine());
            boolean exist =  facadeController.checkIfSiteExistAndAvailable(destID,sourceID);
            if(exist) {
                int fileID = facadeController.createProductsFile();
                System.out.println("How many products would you like for this destination? ");
                int numProducts = Integer.parseInt(scanner.nextLine());
                for (int j = 0; j < numProducts; j++) {
                    System.out.println("Please enter name of a product: ");
                    String productName = scanner.nextLine();
                    System.out.println("Please enter weight of a product: ");
                    float productWeight = Float.parseFloat(scanner.nextLine());
                    System.out.println("How many items form this product? ");
                    int quantity = Integer.parseInt(scanner.nextLine());
                    facadeController.createProduct(productName, productWeight, fileID, quantity);
                }
                destFiles.put(destID, fileID);
                Destinations = facadeController.getAvailableSites(sourceID, destFiles);
                if (Destinations.equals("")) {
                    flag = false;
                } else {
                    System.out.println("Choose 1 if you want to choose another destination\n" +
                            "Choose 2 if you want to continue");
                    while (true) {
                        int opt = Integer.parseInt(scanner.nextLine());
                        if (opt == 2) {
                            flag = false;
                            break;
                        } else if (opt == 1) {
                            break;

                        } else
                            System.out.println("The input is invalid please try again\n");
                    }

                }
            }
            else {
                System.out.println("The site ID:"+destID+" is not in the system, Please try again.\n");
            }
        }
        return destFiles;
    }

    private int chooseSource() {
        int sourceID;
        while (true) {
            System.out.println("\nSources:\n" + facadeController.getAllSitesDetails());
            System.out.println("Please choose source site ID from the above\n");
            sourceID = Integer.parseInt(scanner.nextLine());
            String destinations = facadeController.getAvailableSites(sourceID, new HashMap<>());
            if (destinations.equals("")) {
                System.out.println("No destination sites in this shipping area.\n" +
                        "\tchoose 1 to insert new source site\n" +
                        "\tchoose 2 to abort transport");
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
