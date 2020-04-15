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
            int operation;
            try {
                operation = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                operation = -1;
            }
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
            int siteToDelete = integerParse(scanner.nextLine());
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
            int driverToDelete = integerParse(scanner.nextLine());
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
            int truckToDelete = integerParse(scanner.nextLine());
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
        int shipping_area = integerParse(scanner.nextLine());
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
        int netWeight = integerParse(scanner.nextLine());
        System.out.println("Max weight the truck can curry:");
        int maxWeight = integerParse(scanner.nextLine());
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
            int transportToDelete = integerParse(scanner.nextLine());
            boolean exist = facadeController.checkIfTransportExist(transportToDelete);
            if (exist) {
                facadeController.removeInlayDate(facadeController.getTransportDate(transportToDelete), transportToDelete);
                boolean deleted = facadeController.deleteTransport(transportToDelete);
                if (deleted) {
                    System.out.println("The transport deleted successfully.\n");
                } else {
                    System.out.println("The transport ID:" + transportToDelete + " is not in the system, operation canceled.\n");
                }
            }
            else {
                System.out.println("The transport ID:" + transportToDelete + " is not in the system, operation canceled.\n");
            }
        }
    }

    private void newTransport() {
        // creates transport object and returns its ID
        int transportID = facadeController.createTransport();

        // selecting the date of transport
        System.out.println("Please enter a date in the format dd-mm-yyyy");
        String date;
        while (true){
            try {
                date = scanner.nextLine();
                // check availability of the date with trucks and drivers
                boolean dateAvailable = facadeController.setTransportDate(transportID, date);
                if (dateAvailable)
                    break;
                else
                    System.out.println("There are no available trucks or drivers in the specified date. Enter different date.\n");
            } catch (Exception e) {
                // wrong format or passed date
                System.out.println(e.getMessage());
            }
        }

        // selecting the source of transport
        int sourceID = chooseSource();
        if (sourceID == -1){
            facadeController.deleteTransport(transportID);
            return;
        }
        facadeController.setTransportSource(transportID, sourceID);

        // selecting destinations and products for each dest
        HashMap<Integer, Integer> DestFiles = chooseProductsPerSite(sourceID);
        facadeController.setTransportDestFiles(transportID, DestFiles);

        // selecting truck for transport
        int truckID = chooseTruck(transportID);
        if (truckID == -1) {
            facadeController.deleteTransport(transportID);
            return;
        }
        facadeController.setTransportTruck(transportID, truckID);

        float totalWeight = facadeController.getTotalWeight(DestFiles);
        facadeController.setTransportWeight(transportID, totalWeight);

        // selecting driver for transport
        int driverID = chooseDriver(transportID);
        if (driverID == -1) {
            facadeController.deleteTransport(transportID);
            return;
        }
        facadeController.setTransportDriver(transportID, driverID);
        // add occupied date to truck and driver
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
                int opt = integerParse(scanner.nextLine());
                if (opt == 2) {
                    return -1;
                }
                else if (opt == 1){
                    int truckID = facadeController.getTransportTruck(transportID);
                    facadeController.addTransportLog("The truck: " + facadeController.getTruckDetails(truckID) + "\n" +
                            "\t\twas changed.", transportID);
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
                int driverID = integerParse(scanner.nextLine());
                boolean exist = facadeController.checkIfDriverExist(driverID);
                if (exist)
                    return driverID;
                else {
                    System.out.println("The truck ID: " + driverID + " is not in the system, Please try again.\n");
                }
            }
        }
    }

    private int chooseTruck(int transportID) {
        float totalWeight = facadeController.getTotalWeight(facadeController.getTransportDestFiles(transportID));
        while (true) {
            String trucks = facadeController.getAvailableTrucks(facadeController.getTransportDate(transportID), totalWeight);
            // if there is no truck that can carry total weight give option to edit
            if (trucks.equals("")) {
                System.out.println("There is no truck that can carry such weight in the system.\n" +
                        "\tchoose 1 to edit destination.\n" +
                        "\tchoose 2 to abort transport.");
                int opt = integerParse(scanner.nextLine());
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
                int truckID = integerParse(scanner.nextLine());
                boolean exist = facadeController.checkIfTruckExist(truckID);
                if (exist)
                    return truckID;
                else {
                    System.out.println("The truck ID: " + truckID + " is not in the system, Please try again.\n");
                }
            }
        }
    }

    private void editDestinations(int transportID) {
        System.out.println("Please choose the option you would like to edit:\n" +
                "\t1. Remove destination from transport.\n" +
                "\t2. Remove products from destination.");
        int opt = integerParse(scanner.nextLine());
        System.out.println(facadeController.getProductsByDest(transportID));
        if (opt == 1) {
            System.out.println("Please choose destination site ID to remove");
            int destToRemove = integerParse(scanner.nextLine());
            boolean exist = facadeController.checkIfSiteExist(destToRemove);
            if (exist) {
                facadeController.addTransportLog("The destination: " + facadeController.getSiteDetails(destToRemove) + "\n" +
                        "\t\twas removed from transport.", transportID);
                facadeController.removeDestFromTransport(transportID, destToRemove);
            }
            else {
                System.out.println("The input is invalid, transport did not changed.");
            }
        }
        else if (opt == 2){
            System.out.println("Please choose destination site ID to edit");
            int destToEdit = integerParse(scanner.nextLine());
            boolean exist = facadeController.checkIfSiteExist(destToEdit) && facadeController.checkIfDestInFile(transportID, destToEdit);
            if (exist) {
                int fileToEdit = facadeController.getDestFileID(transportID, destToEdit);
                System.out.println("Please insert products ID to remove with spaces between");
                String[] productsToRemove = (scanner.nextLine()).split(" ");
                boolean valid = facadeController.validateProducts(productsToRemove, fileToEdit);
                if (valid) {
                    facadeController.addTransportLog("The products:\n" + facadeController.getProductsDetails(productsToRemove) +
                            "\t\twas removed from destination: " + facadeController.getSiteDetails(destToEdit), transportID);
                    facadeController.removeProducts(productsToRemove, fileToEdit);
                }
                else {
                    System.out.println("The input is invalid, products does not exist. Transport did not changed.");
                }
            }
            else{
                System.out.println("The input is invalid, transport did not changed.");
            }
        }
        else {
            System.out.println("The input is invalid, transport did not changed.");
        }
    }

    private HashMap<Integer, Integer> chooseProductsPerSite(int sourceID) {
        HashMap<Integer, Integer> destFiles = new HashMap<>();
        boolean flag = true;
        // returns all sites which are not source & not chosen already & in the same shipping area as source
        String Destinations =  facadeController.getAvailableSites(sourceID,destFiles);
        while(flag)
        {

            System.out.println("Destinations:\n" + Destinations);
            System.out.println("Please choose dest ID:");
            int destID = integerParse(scanner.nextLine());
            boolean exist =  facadeController.checkIfSiteExist(destID);
            if(exist) {
                int fileID = facadeController.createProductsFile();
                System.out.println("How many products would you like for this destination? ");
                int numProducts = integerParse(scanner.nextLine());
                for (int j = 0; j < numProducts; j++) {
                    System.out.println("\nPlease enter name of a product: ");
                    String productName = scanner.nextLine();
                    System.out.println("Please enter weight of a product: ");
                    float productWeight = Float.parseFloat(scanner.nextLine());
                    System.out.println("How many items from this product? ");
                    int quantity = integerParse(scanner.nextLine());
                    facadeController.createProduct(productName, productWeight, fileID, quantity);
                }
                // add to destinations file of transport: dest id and products file id (for the dest)
                destFiles.put(destID, fileID);

                Destinations = facadeController.getAvailableSites(sourceID, destFiles);
                // if there is no more available destinations exit loop and continue with procedure
                if (Destinations.equals("")) {
                    flag = false;
                }
                else {
                    System.out.println("Choose 1 if you want to choose another destination\n" +
                            "Choose 2 if you want to continue");
                    while (true) {
                        int opt = integerParse(scanner.nextLine());
                        if (opt == 2) {
                            flag = false;
                            break;
                        } else if (opt == 1) {
                            continue;
                        } else
                            System.out.println("The operation is invalid please try again\n");
                    }
                }
            }
            else {
                System.out.println("The site ID: "+destID+" is not in the system, Please try again.\n");
            }
        }
        return destFiles;
    }

    private int chooseSource() {
        int sourceID;
        while (true) {
            System.out.println("\nSources:\n" + facadeController.getAllSitesDetails());
            System.out.println("Please choose source site ID from the above");
            sourceID = integerParse(scanner.nextLine());
            boolean exist = facadeController.checkIfSiteExist(sourceID);
            if (exist) {
                String destinations = facadeController.getAvailableSites(sourceID, new HashMap<>());
                // if there is no destinations in the same shipping area
                if (destinations.equals("")) {
                    System.out.println("No destination sites in this shipping area.\n" +
                            "\tchoose 1 to insert new source site\n" +
                            "\tchoose 2 to abort transport");
                    int opt = integerParse(scanner.nextLine());
                    if (opt == 2) {
                        sourceID = -1;
                        break;
                    } else if (opt == 1)
                        continue;
                    else {
                        System.out.println("The input is invalid, transport aborted.\n");
                        return -1;
                    }
                } else
                    break;
            }
        }
        return sourceID;
    }

    private int integerParse(String s){
        int ret = -1;
        while (ret == -1) {
            try {
                ret = Integer.parseInt(s);
            } catch (Exception e) {
                System.out.println("Invalid operation. Try Again.");
                s = scanner.nextLine();
            }
        }
        return ret;
    }
}
