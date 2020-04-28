package PresentationLayer.Transport;

import InterfaceLayer.Transport.FacadeController;

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

    //the main menu of the system - activates when the system starts and running in loops until it closed.
    public void SystemActivation(){
        System.out.println("Transports system\n");
        while (!terminated) {
            System.out.println( "Please choose an operation:\n" +
                    "1. Initialize data.\n" +
                    "2. Book new transport.\n" +
                    "3. Delete transport.\n" +
                    "4. Display all trucks.\n" +
                    "5. Display all drivers.\n" +
                    "6. Display all sites.\n" +
                    "7. Display all transports.\n" +
                    "8. Add truck.\n" +
                    "9. Add driver.\n" +
                    "10. Add site.\n" +
                    "11. Remove truck.\n" +
                    "12. Remove driver.\n" +
                    "13. Remove site.\n" +
                    "14. Exit system.\n");
            int operation;
            try {
                operation = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                operation = -1;
            }
            switch (operation) {
                case 1 :
                    initializeData();
                    break;
                case 2 :
                    newTransport();
                    break;
                case 3:
                    deleteTransport();
                    break;
                case 4:
                    System.out.println("Trucks:\n" + facadeController.getAllTrucksDetails());
                    break;
                case 5:
                    System.out.println("Drivers:\n" + facadeController.getAllDriversDetails());
                    break;
                case 6:
                    System.out.println("Sites:\n" + facadeController.getAllSitesDetails());
                    break;
                case 7:
                    System.out.println("Transports:\n" + facadeController.getAllTransportsDetails());
                    break;
                case 8:
                    addTruck();
                    break;
                case 9:
                    addDriver();
                    break;
                case 10:
                    addSite();
                    break;
                case 11:
                    deleteTruck();
                    break;
                case 12:
                    deleteDriver();
                    break;
                case 13:
                    deleteSite();
                    break;
                case 14:
                    System.out.println("Thank you, good bye!");
                    terminated = true;
                    break;
                default:
                    System.out.println("Invalid operation.");
            }
        }
    }

    //this data is initialized in every new run of the system.
    public void initializeData()
    {
        facadeController.createDriver("Eran", "C1");
        facadeController.createDriver("Omer","C");
        facadeController.createDriver("Noam","C1");
        facadeController.createDriver("David", "C");
        facadeController.createDriver("Shon", "C1");
        facadeController.createDriver("Yosi", "C4");

        facadeController.createSite("Beer-Sheva","054-1234567", "Shira",1);
        facadeController.createSite("Ofakim","052-1234567","Einav",1);
        facadeController.createSite("Omer","050-1234567", "Amit",1);
        facadeController.createSite("Herzelia","052-8912345","Shachaf",3);
        facadeController.createSite("Tel-Aviv","050-8912345","Mai",3);
        facadeController.createSite("Jerusalem","050-8912345","Eden",4);
        facadeController.createSite("Oren Ashkelon", "0521234567", "Eli", 2);
        facadeController.createSite("Avraham avinu Sderot", "0501234567", "Maya", 2);

        facadeController.createTruck("12-L8","XXX",1000,1800,"C4");
        facadeController.createTruck("17-LD","X23",1050,2260,"C1");
        facadeController.createTruck("J0-38","1X6",700,1500,"C");
        facadeController.createTruck("12-23FF","XXL8",1000,2600,"C1");
        facadeController.createTruck("17-45LD","X24",1050,3260,"C1");
        facadeController.createTruck("J0-38AV","1X6ZA",700,1000,"C");
        facadeController.createTruck("12345678", "XX32", 1000, 2550, "C4");
    }

    //delete site function
    private void deleteSite() {
        String details = facadeController.getAllSitesDetails();
        if (details.equals("")){        //there are no sites in the system
            System.out.println("There are no sites to delete.\n");
        }
        else {
            System.out.println(details);
            System.out.println("Please choose the site ID you wish to remove:");
            int siteToDelete = integerParse(scanner.nextLine());
            //try to delete the given site, if returns false the id is not in the system
            boolean deleted = facadeController.deleteSite(siteToDelete);
            if(deleted) {
                System.out.println("The site deleted successfully.\n");
            }
            else {
                System.out.println("The site ID:"+siteToDelete+" is not in the system, operation canceled.\n");
            }
        }
    }

    //delete driver function
    private void deleteDriver() {
        String details = facadeController.getAllDriversDetails();
        if (details.equals("")){        //there are no drivers in the system
            System.out.println("There are no drivers to delete.\n");
        }
        else {
            System.out.println(details);
            System.out.println("Please choose the driver ID you wish to remove:");
            int driverToDelete = integerParse(scanner.nextLine());
            //try to delete the given driver, if returns false the id is not in the system
            boolean deleted =facadeController.deleteDriver(driverToDelete);
            if(deleted) {
                System.out.println("The driver deleted successfully.\n");
            }
            else {
                System.out.println("The driver ID:"+driverToDelete+" is not in the system, operation canceled.\n");
            }
        }
    }

    //delete truck function
    private void deleteTruck() {
        String details = facadeController.getAllTrucksDetails();
        if (details.equals("")){         //there are no trucks in the system
            System.out.println("There are no trucks to delete.\n");
        }
        else {
            System.out.println(details);
            System.out.println("Please choose the truck ID you wish to remove:");
            int truckToDelete = integerParse(scanner.nextLine());
            //try to delete the given truck, if returns false the id is not in the system
            boolean deleted =facadeController.deleteTruck(truckToDelete);
            if(deleted) {
                System.out.println("The truck deleted successfully.\n");
            }
            else {
                System.out.println("The truck ID:"+truckToDelete+" is not in the system, operation canceled.\n");
            }
        }
    }

    //adding a new site to the system
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

    //adding a new driver to the system
    private void addDriver() {
        System.out.println("Please enter the following details:\n");
        System.out.println("Driver name:");
        String name = scanner.nextLine();
        System.out.println("Driver's License:");
        String license = scanner.nextLine();
        facadeController.createDriver(name, license);
        System.out.println("\nThe driver added successfully.\n");
    }

    //adding a new truck to the system
    private void addTruck() {
        System.out.println("Please enter the following details:\n");
        System.out.println("License plate:");
        String license_plate = scanner.nextLine();
        System.out.println("Model:");
        String model = scanner.nextLine();
        System.out.println("Drivers license:");
        String drivers_license = scanner.nextLine();
        System.out.println("Net weight of the truck:");
        int netWeight = integerParse(scanner.nextLine());
        System.out.println("Max weight the truck can curry:");
        int maxWeight = integerParse(scanner.nextLine());
        boolean created = facadeController.createTruck(license_plate, model, netWeight, maxWeight, drivers_license);
        while (!created){ //check that the maxWeight is bigger than the net weight
            System.out.println("Max weight should be bigger than net weight. Please enter max weight again.");
            maxWeight = integerParse(scanner.nextLine());
            created = facadeController.createTruck(license_plate, model, netWeight, maxWeight, drivers_license);
        }
        System.out.println("\nThe truck added successfully.\n");
    }

    //delete transport function
    private void deleteTransport() {
        String details = facadeController.getAllTransportsDetails();
        if (details.equals("")){          //there are no transports in the system
            System.out.println("There are no transports to delete.\n");
        }
        else {
            System.out.println(details);
            System.out.println("Please choose the transport ID you wish to remove:");
            int transportToDelete = integerParse(scanner.nextLine());
            //try to delete the given transport, if returns false the id is not in the system
            boolean deleted = facadeController.deleteTransport(transportToDelete);
            if (deleted) {
                facadeController.removeDatesToDriverAndTruck(transportToDelete);
                System.out.println("The transport deleted successfully.\n");
            } else {
                System.out.println("The transport ID:" + transportToDelete + " is not in the system, operation canceled.\n");
            }
        }
    }

    //adding a new transport to the system
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
        if (sourceID == -1){ //the transport canceled
            facadeController.deleteTransport(transportID);
            return;
        }
        facadeController.setTransportSource(transportID, sourceID);

        // selecting destinations and products for each dest and setting the totalWeight of the transport
        HashMap<Integer, Integer> DestFiles = chooseProductsPerSite(sourceID);
        facadeController.setTransportDestFiles(transportID, DestFiles);
        facadeController.setTransportWeight(transportID);

        // selecting truck for transport
        int truckID = chooseTruck(transportID);
        if (truckID == -1) {//the transport canceled
            facadeController.deleteTransport(transportID);
            return;
        }
        facadeController.setTransportTruck(transportID, truckID);


        // selecting driver for transport
        int driverID = chooseDriver(transportID);
        if (driverID == -1) { //the transport canceled
            facadeController.deleteTransport(transportID);
            return;
        }
        facadeController.setTransportDriver(transportID, driverID);
        // add occupied date to truck and driver
        facadeController.addDatesToDriverAndTruck(transportID);

        System.out.println("\nThe transport added successfully.\n");
    }

    //choose the driver of a transport
    private int chooseDriver(int transportID) {
        while (true) {
            String drivers = facadeController.getAvailableDrivers(transportID);
            if (drivers.equals("")) { //there are no available drivers give option to edit
                System.out.println("There is no driver with compatible license to the selected truck in the system.\n" +
                        "\tchoose 1 to change truck.\n" +
                        "\tchoose 2 to abort transport.");
                int opt = integerParse(scanner.nextLine());
                if (opt == 2) { //abort transport
                    return -1;
                }
                else if (opt == 1){ //change truck
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
            else { //there are drivers
                System.out.println("Drivers:\n" + drivers);
                System.out.println("Please choose a driver ID from the above");
                int driverID = integerParse(scanner.nextLine());
                boolean exist = facadeController.checkIfDriverExistAndValid(driverID, transportID);
                if (exist)
                    return driverID;
                else {
                    System.out.println("The driver ID: " + driverID + " is not in valid for this operation, Please try again.\n");
                }
            }
        }
    }

    //choose the truck of a transport
    private int chooseTruck(int transportID) {
        while (true) {
            float totalWeight = facadeController.getTotalWeight(transportID); //get the total weight in the transport
            if(totalWeight==0)
            {
                System.out.println("the total weight of the products is 0. the Transport is canceled.\n");
                return -1;
            }
            String trucks = facadeController.getAvailableTrucks(facadeController.getTransportDate(transportID), totalWeight);
            // if there is no truck that can carry total weight give option to edit
            if (trucks.equals("")) {
                System.out.println("There is no truck that can carry such weight in the system.\n" +
                        "\tchoose 1 to edit destination or products.\n" +
                        "\tchoose 2 to abort transport.");
                int opt = integerParse(scanner.nextLine());
                if (opt == 2) { //abort transport
                    return -1;
                }
                else if (opt == 1){ //edit products and destination
                    editDestinationsOrProducts(transportID);
                }
                else {
                    System.out.println("The input is invalid, Please try again.");
                }
            }
            else { //there are trucks
                System.out.println("Trucks:\n" + trucks);
                System.out.println("Please choose a truck ID from the above");
                int truckID = integerParse(scanner.nextLine());
                boolean exist = facadeController.checkIfTruckExistAndValid(truckID, transportID);
                if (exist)
                    return truckID;
                else {
                    System.out.println("The truck ID: " + truckID + " is not in valid for this operation, Please try again.\n");
                }
            }
        }
    }

    //edit the destination or the products of a transport if the weight was to high and there were no trucks available
    private void editDestinationsOrProducts(int transportID) {
        System.out.println("Please choose the option you would like to edit:\n" +
                "\t1. Remove destination from transport.\n" +
                "\t2. Remove products from destination.");
        int opt = integerParse(scanner.nextLine());
        System.out.println("Destination and products in this transport:\n"+facadeController.getProductsByDest(transportID));
        if (opt == 1) { //delete destination
            System.out.println("Please choose destination site ID to remove");
            int destToRemove = integerParse(scanner.nextLine());
            //try to remove this destination
            boolean removed =  facadeController.removeDestFromTransport(transportID, destToRemove);
            if (removed) {
                facadeController.addTransportLog("The destination: " + facadeController.getSiteDetails(destToRemove) + "\n" +
                        "\t\twas removed from transport.", transportID);
                System.out.println("The destination: "+ facadeController.getSiteDetails(destToRemove)+"\n"
                                    +"was removed from transport.");
            }
            else {
                System.out.println("The input is invalid, transport did not changed.");
            }
        }
        else if (opt == 2){ //remove product from a destination
            System.out.println("Please choose destination site ID to edit");
            int destToEdit = integerParse(scanner.nextLine());
            boolean exist = facadeController.checkIfDestInFile(transportID, destToEdit);
            if (exist) {
                int fileToEdit = facadeController.getDestFileID(transportID, destToEdit);
                System.out.println("Please insert products ID to remove with spaces between");
                String[] productsToRemove = splitScan();
                boolean removed =  facadeController.removeProducts(productsToRemove, fileToEdit);
                if (removed) {
                    facadeController.addTransportLog("The products:\n" + facadeController.getProductsDetails(productsToRemove) +
                            "\t\twas removed from destination: " + facadeController.getSiteDetails(destToEdit), transportID);
                    System.out.println("the products removed from the destination");
                    float fileWeight= facadeController.getFileWeight(fileToEdit);
                    if(fileWeight==0)
                    {
                        facadeController.removeDestFromTransport(transportID, destToEdit);
                        facadeController.addTransportLog("The destination: " + facadeController.getSiteDetails(destToEdit) + "\n" +
                                "\t\twas removed from transport.", transportID);
                        System.out.println("total weight of this destination is 0, destination removed from transport");

                    }
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

    //choose the Destination and Products of a transport
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
            //check if the given ID is correct
            boolean exist =  facadeController.checkIfSiteExistAndValid(destID ,sourceID, destFiles);
            if(exist) {
                int fileID = facadeController.createProductsFile();
                System.out.println("How many products would you like for this destination? ");
                int numProducts = integerParse(scanner.nextLine());
                for (int j = 0; j < numProducts; j++) {
                    System.out.println("\nPlease enter name of a product: ");
                    String productName = scanner.nextLine();
                    System.out.println("Please enter weight of a product: ");
                    float productWeight = floatParse(scanner.nextLine());
                    while (productWeight<=0)
                    {
                        System.out.println("the weight must be grater than 0, please try again");
                        productWeight = floatParse(scanner.nextLine());
                    }
                    System.out.println("How many items from this product? ");
                    int quantity = integerParse(scanner.nextLine());
                    while (quantity<=0)
                    {
                        System.out.println("the quantity must be grater than 0, please try again");
                        quantity = integerParse(scanner.nextLine());
                    }
                    facadeController.createProduct(productName, productWeight, fileID, quantity);
                }
                // add to destinations file of transport: dest id and products file id (for the dest)
                // only if the number of product is bigger than 0
                if(numProducts!=0) {
                    destFiles.put(destID, fileID);
                }
                Destinations = facadeController.getAvailableSites(sourceID, destFiles);
                // if there is no more available destinations exit loop and continue with procedure
                if (Destinations.equals("")) {
                    flag = false;
                }
                else {
                    System.out.println("Choose 1 if you want to choose another destination\n" +
                                       "Choose 2 if you want to continue");
                    int opt = integerParse(scanner.nextLine());
                    while (opt!=1 && opt!=2){
                        System.out.println("The operation is invalid please try again\n");
                        opt = integerParse(scanner.nextLine());
                    }
                    if (opt == 2) {
                        flag = false;
                        break;
                    } else if (opt == 1) {
                        continue;
                    }
                }
            }
            else {
                System.out.println("The site ID: "+destID+" is not valid for this operation, Please try again.\n");
            }
        }
        return destFiles;
    }

    //choose the source of a transport
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
            else {
                System.out.println("The input site id is invalid,please try again.\n");
            }
        }
        return sourceID;
    }

    //all the functions below was written to get input from the user and handle exceptions
    private int integerParse(String s){
        int ret;
        while (true) {
            try {
                ret = Integer.parseInt(s);
                return ret;
            } catch (Exception e) {
                System.out.println("Invalid operation. Try Again.");
                s = scanner.nextLine();
            }
        }
    }

    private float floatParse(String s){
        float ret;
        while (true) {
            try {
                ret = Float.parseFloat(s);
                return ret;
            } catch (Exception e) {
                System.out.println("Invalid operation. Try Again.");
                s = scanner.nextLine();
            }
        }
    }

    private String[] splitScan()
    {
        while (true) {
            try {
                String[] in = (scanner.nextLine()).split(" ");
                return in;
            } catch (Exception e) {
                System.out.println("Invalid operation. Try Again.");
            }
        }
    }

}
