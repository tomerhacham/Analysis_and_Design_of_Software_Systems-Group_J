import bussines_layer.Branch;
import bussines_layer.BranchController;
import bussines_layer.Result;
import bussines_layer.employees_module.*;
import bussines_layer.inventory_module.Category;
import data_access_layer.Mapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presentation_layer.CLController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {
    private  Date startDate= parseDate("02/04/2020");
    private  Date dateOfShift= new Date();
    private  Date invalidShift= parseDate("01/04/2020");
    private static final boolean morning=true;
    private static final boolean night=false;
    private BranchController branchController;
    private Scheduler scheduler;
    private Roster roster;
    private Integer branch_id;


    @BeforeEach
    public void init()
    {

        branch_id=1;
        initialize();
        branchController = new BranchController(true);
        branchController.switchBranch(1);

        roster= branchController.testRoster();
        scheduler= branchController.testScheduler();

    }
    @AfterEach
    public void tearDown() {

        roster.removeExistingWorkers();
        /*
        for (Date date:scheduler.getAvailableWorkers().keySet()){
            Pair<LazyList<Worker>, LazyList<Worker>> pair= scheduler.getAvailableWorkers().get(date) ;{
                for (Worker w :pair.getMorning())
                {
                    scheduler.removeAvailableWorker(date,morning,w.getId(),branch_id);
                }
                for (Worker w :pair.getNight())
                {
                    scheduler.removeAvailableWorker(date,night,w.getId(),branch_id);
                }
            }
        }*/
        scheduler.clearAllSchedule(branch_id);
        Mapper.getInstance().clearDatabase();
    }
    @Test
    public void addAvailableWorker(){

        String output=branchController.addAvailableWorker(null,morning,"1");
        assertTrue("Invalid date".equals(output));
        output=scheduler.addAvailableWorker(invalidShift,morning,"0000-0000-0000-0001",branch_id);
        assertTrue( "Can not add worker before his start date of working".equals(output));
        branchController.addAvailableWorker(dateOfShift,morning,"0000-0000-0000-0001");
        Worker temp=scheduler.getAvailableWorkers().get(dateOfShift).getMorning().get(0);
        assertTrue(scheduler.getAvailableWorkers().get(dateOfShift).getMorning().contains(temp));

    }


    @Test
    public void removeAvailableWorker(){
        Date shiftdate2= parseDate("08/03/2020");
        String output=branchController.removeAvailableWorker(shiftdate2,morning,"0000-0000-0000-0002");
        assertTrue("The worker is not available for this shift".equals(output));
        scheduler.removeAvailableWorker(dateOfShift,morning,"1",branch_id);
        boolean found=false;//scheduler.getAvailableWorkers().get(dateOfShift).getMorning().contains(worker1);
        assertFalse(found);
    }

    @Test
    public void createShift()
    {
        String output=branchController.createShift(dateOfShift,night);
        assertTrue("No Available workers were marked for this shift".equals(output));
    }

    private static Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date=null;
        try {
            //Parsing the String
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {

        }
        return date;
    }



    public void initialize() {
        System.err.println("Warning: all of the data in the DB will be erase\n");
        BranchController.clearDB();
        branchController = new BranchController(true);
        branchController.loadID();
        // Open and switch to initial branch
        Branch initialBranch = branchController.createNewBranch("Initial branch").getData();
        branchController.switchBranch(initialBranch.getBranchId());

        //Add categories:
        //              Hygiene
        //                  Toilet Paper
        //                      30 units
        //                  Shampoo
        //                      500ml
        //                      750ml
        //              Meat%Fish
        //                  Meat
        //                      1/2kg
        //                      1kg
        //                  Fish
        //                      1/2kg
        //                      1kg
        Result res_cat_hygiene = branchController.addMainCategory("Hygiene");
        Result res_cat_tp = branchController.addSubCategory(((Category) res_cat_hygiene.getData()).getId(), "Toilet paper");
        Result res_cat_30 = branchController.addSubCategory(((Category) res_cat_tp.getData()).getId(), "30 units");
        Result res_cat_shampoo = branchController.addSubCategory(((Category) res_cat_hygiene.getData()).getId(), "Shampoo");
        Result res_cat_500ml = branchController.addSubCategory(((Category) res_cat_shampoo.getData()).getId(), "500ml");
        Result res_cat_750ml = branchController.addSubCategory(((Category) res_cat_shampoo.getData()).getId(), "750ml");
        Result res_cat_mnf = branchController.addMainCategory("Meat%Fish");
        Result res_cat_meat = branchController.addSubCategory(((Category) res_cat_mnf.getData()).getId(), "Meat");
        Result res_cat_fish = branchController.addSubCategory(((Category) res_cat_mnf.getData()).getId(), "Fish");
        Result meat_half_kg = branchController.addSubCategory(((Category) res_cat_meat.getData()).getId(), "1/2kg");
        Result meat_one_kg = branchController.addSubCategory(((Category) res_cat_meat.getData()).getId(), "1kg");
        Result fish_half_kg = branchController.addSubCategory(((Category) res_cat_fish.getData()).getId(), "1/2kg");
        Result fish_one_kg = branchController.addSubCategory(((Category) res_cat_fish.getData()).getId(), "1kg");


//-----------------------------------------------------


        Integer supplierID = 1;
        Integer catalogID = 10;
        Integer gpID = 100;
        Float sup_price = 18.5f;
        String name = "Toilet paper double layer 30u";

        LinkedList<String> contact = new LinkedList<>();
        contact.add("Moshe");
        contact.add("Rachel");

        //Create supplier halavi-lee
        branchController.createSupplierCard("halavi-lee", "ringelbloom 97 beer-sheva", "halavi@gmail.com", "081234567",
                supplierID, "0975635", "CreditCard", contact, "self delivery");

        LinkedList<String> categories = new LinkedList<>();
        categories.add("Hygiene");
        categories.add("Meat");

        //create contract
        branchController.addContract(supplierID, categories);

        //Create GP
        branchController.addGeneralProduct(((Category) res_cat_30.getData()).getId(), "Niguvim", name, sup_price, 31.5f, 20, catalogID, gpID, supplierID, "Hygiene", (float) 0.7);
        branchController.addSpecificProduct(gpID, CLController.convertStringToDate("11/04/2025"), 21);
        //Add Product to contract
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Hygiene");

        name = "Crema shampoo for men 500ml";
        sup_price = 15.5f;
        catalogID = 9;
        gpID = 101;
        //Create GP
        branchController.addGeneralProduct(((Category) res_cat_500ml.getData()).getId(), "Crema", name, sup_price, 25.5f, 5, catalogID, gpID, supplierID, "Hygiene", (float) 0.5);
        branchController.addSpecificProduct(gpID, CLController.convertStringToDate("11/04/2025"), 6);
        //Add Product to contract
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Hygiene");

        name = "Dove shampoo for women 500ml";
        sup_price = 15.5f;
        catalogID = 11;
        gpID = 102;
        //Create GP
        branchController.addGeneralProduct(((Category) res_cat_500ml.getData()).getId(), "Dove", name, sup_price, 25.5f, 5, 5, gpID, supplierID, "Hygiene", (float) 0.5);
        branchController.addSpecificProduct(gpID, CLController.convertStringToDate("11/04/2025"), 6);
        //Add Product to contract
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Hygiene");

        name = "Crema shampoo for men 750ml";
        sup_price = 22.0f;
        catalogID = 12;
        gpID = 103;
        //Create GP
        branchController.addGeneralProduct(((Category) res_cat_500ml.getData()).getId(), "Crema", name, sup_price, 32.5f, 5, catalogID, gpID, supplierID, "Hygiene", (float) 0.75);
        branchController.addSpecificProduct(gpID, CLController.convertStringToDate("11/04/2025"), 6);
        //Add Product to contract
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Hygiene");

        String manufacture = "Moosh";
        gpID = 104;
        name = "Moosh packed ground meat 1/2kg";
        sup_price = 30.0f;
        Float ret_price = 40.0f;
        catalogID = 13;
        branchController.addGeneralProduct(((Category) meat_half_kg.getData()).getId(), manufacture, name, sup_price, ret_price, 5, catalogID, gpID, supplierID, "Meat", (float) 0.5);
        branchController.addSpecificProduct(gpID, CLController.convertStringToDate("11/04/2025"), 4);
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Meat");

//------------------------------------- initialize periodic order -----------------------------
        LinkedList<javafx.util.Pair<Integer, Integer>> product_quantity = new LinkedList<>();
        product_quantity.add(new javafx.util.Pair<>(100, 10));
        branchController.createPeriodicOrder(1, product_quantity, 3);    //wednesday
//--------------------------------------------
        gpID = 104;
        name = "Moosh packed ground meat 1/2kg";
        sup_price = 32.5f;
        catalogID = 20;
        supplierID = 2;

        LinkedList<String> contact2 = new LinkedList<>();
        contact.add("Yossi");

        LinkedList<String> categories2 = new LinkedList<>();
        categories2.add("Meat");
        categories2.add("Fish");


        //Create supplier niceToMeat
        branchController.createSupplierCard("niceToMeat", "mesada 37 beer-sheva", "niceToMeat@gmail.com", "087594456",
                supplierID, "09754432", "CreditCard", contact2, "fix days", 5);

        //create contract
        branchController.addContract(supplierID, categories2);

        ////branchController.addGeneralProduct(((Category)meat_half_kg.getData()).getId(), manufacture,name,sup_price,ret_price,5,catalogID,gpID,supplierID,"Meat");
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Meat");

        manufacture = "Moosh";
        gpID = 201;
        name = "Moosh packed ground meat 1kg";
        sup_price = 40.0f;
        ret_price = 45.0f;
        catalogID = 24;

        branchController.addGeneralProduct(((Category) meat_one_kg.getData()).getId(), manufacture, name, sup_price, ret_price, 3, catalogID, gpID, supplierID, "Meat", (float) 1);

        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Meat");


        manufacture = "Lakerda";
        gpID = 202;
        name = "Lakerda 1/2kg semi-fresh";
        sup_price = 10.0f;
        ret_price = 13.0f;
        catalogID = 21;
        branchController.addGeneralProduct(((Category) fish_half_kg.getData()).getId(), manufacture, name, sup_price, ret_price, 5, catalogID, gpID, supplierID, "Meat", (float) 0.5);
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Fish");


        manufacture = "Merluza";
        gpID = 203;
        name = "Merluza 1/2kg semi-fresh";
        sup_price = 9.5f;
        ret_price = 12.0f;
        catalogID = 22;
        branchController.addGeneralProduct(((Category) fish_half_kg.getData()).getId(), manufacture, name, sup_price, ret_price, 7, catalogID, gpID, supplierID, "Fish", (float) 0.5);
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Fish");

        manufacture = "Merluza";
        gpID = 204;
        name = "Merluza 1kg semi-fresh";
        sup_price = 15.5f;
        ret_price = 17.0f;
        catalogID = 23;
        branchController.addGeneralProduct(((Category) fish_one_kg.getData()).getId(), manufacture, name, sup_price, ret_price, 10, catalogID, gpID, supplierID, "Fish", (float) 1);
        branchController.addProductToContract(supplierID, catalogID, gpID, sup_price, "Fish");



        //workers

        //constants
        final boolean morning=true;
        final boolean night=false;

        List<String> positions1=new ArrayList<>();
        positions1.add("manager");
        positions1.add("storage man");
        List<String>positions2=new ArrayList<>();
        positions2.add("manager");

        Date startDate1=parseDate("11/04/2020");
        Date startDate2=parseDate("12/04/2020");

        String WID1 = "0000-0000-0000-0001";
        String WID2 = "0000-0000-0000-0002";
        String WID3 = "0000-0000-0000-0003";
        String WID4 = "0000-0000-0000-0004";
        String WID5 = "0000-0000-0000-0005";
        String WID6 = "0000-0000-0000-0006";
        String WID7 = "0000-0000-0000-0007";


        branchController.initAddWorker(WID1,"Gil",16,startDate1,positions1);
        branchController.initAddWorker(WID2,"Sharon",15.9,startDate2,positions2);
        branchController.initAddDriver(WID3,"Moshe",10,startDate1,"C4");
        branchController.initAddDriver(WID4,"Dani",100,startDate2,"C");
        branchController.initAddDriver(WID5,"Gadi",100,startDate2,"C1");

        positions2.add("security guard");

        branchController.initAddWorker(WID6,"Avi",100,startDate1,positions2);


        List<String>positions3=new ArrayList<>();
        positions3.add("storage man");
        positions3.add("cashier");
        branchController.initAddWorker(WID7,"bob",100,startDate1,positions3);


        Date shiftDate=null;

        shiftDate=parseDate("24/06/2020");

        branchController.addAvailableWorker(shiftDate,morning,WID1);
        branchController.addAvailableWorker(shiftDate,morning,WID2);
        branchController.addAvailableWorker(shiftDate,morning,WID3);
        branchController.addAvailableWorker(shiftDate,morning,WID4);
        branchController.addAvailableWorker(shiftDate,morning,WID5);
        branchController.addAvailableWorker(shiftDate,morning,WID6);
        branchController.addAvailableWorker(shiftDate,morning,WID7);


        branchController.createShift(shiftDate,morning);
        branchController.addPositionToShift("driver",1);
        branchController.addPositionToShift("storage man",1);
        branchController.addPositionToShift("security guard",1);
        branchController.addPositionToShift("cashier",1);
        branchController.addWorkerToPositionInShift("manager",WID2);
        branchController.addWorkerToPositionInShift("storage man",WID1);
        branchController.addWorkerToPositionInShift("security guard",WID6);
        branchController.addWorkerToPositionInShift("cashier",WID7);
        branchController.submitShift();

        shiftDate=parseDate("01/07/2020");

        branchController.addAvailableWorker(shiftDate,morning,WID1);
        branchController.addAvailableWorker(shiftDate,morning,WID2);
        branchController.addAvailableWorker(shiftDate,morning,WID3);
        branchController.addAvailableWorker(shiftDate,morning,WID4);
        branchController.addAvailableWorker(shiftDate,morning,WID5);
        branchController.addAvailableWorker(shiftDate,morning,WID6);
        branchController.addAvailableWorker(shiftDate,morning,WID7);


        branchController.createShift(shiftDate,morning);
        branchController.addPositionToShift("driver",1);
        branchController.addPositionToShift("storage man",1);
        branchController.addPositionToShift("security guard",1);
        branchController.addPositionToShift("cashier",1);
        branchController.addWorkerToPositionInShift("manager",WID2);
        branchController.addWorkerToPositionInShift("storage man",WID1);
        branchController.addWorkerToPositionInShift("security guard",WID6);
        branchController.addWorkerToPositionInShift("cashier",WID7);
        branchController.submitShift();


        shiftDate=parseDate("08/07/2020");

        branchController.addAvailableWorker(shiftDate,morning,WID1);
        branchController.addAvailableWorker(shiftDate,morning,WID2);
        branchController.addAvailableWorker(shiftDate,morning,WID3);
        branchController.addAvailableWorker(shiftDate,morning,WID4);
        branchController.addAvailableWorker(shiftDate,morning,WID5);
        branchController.addAvailableWorker(shiftDate,morning,WID6);
        branchController.addAvailableWorker(shiftDate,morning,WID7);


        branchController.createShift(shiftDate,morning);
        branchController.addPositionToShift("driver",1);
        branchController.addPositionToShift("storage man",1);
        branchController.addPositionToShift("security guard",1);
        branchController.addPositionToShift("cashier",1);
        branchController.addWorkerToPositionInShift("manager",WID2);
        branchController.addWorkerToPositionInShift("storage man",WID1);
        branchController.addWorkerToPositionInShift("security guard",WID6);
        branchController.addWorkerToPositionInShift("cashier",WID7);
        branchController.submitShift();


        shiftDate=parseDate("15/07/2020");

        branchController.addAvailableWorker(shiftDate,morning,WID1);
        branchController.addAvailableWorker(shiftDate,morning,WID2);
        branchController.addAvailableWorker(shiftDate,morning,WID3);
        branchController.addAvailableWorker(shiftDate,morning,WID4);
        branchController.addAvailableWorker(shiftDate,morning,WID5);
        branchController.addAvailableWorker(shiftDate,morning,WID6);
        branchController.addAvailableWorker(shiftDate,morning,WID7);


        branchController.createShift(shiftDate,morning);
        branchController.addPositionToShift("driver",1);
        branchController.addPositionToShift("storage man",1);
        branchController.addPositionToShift("security guard",1);
        branchController.addPositionToShift("cashier",1);
        branchController.addWorkerToPositionInShift("manager",WID2);
        branchController.addWorkerToPositionInShift("storage man",WID1);
        branchController.addWorkerToPositionInShift("security guard",WID6);
        branchController.addWorkerToPositionInShift("cashier",WID7);
        branchController.submitShift();


        shiftDate=parseDate("22/07/2020");

        branchController.addAvailableWorker(shiftDate,morning,WID1);
        branchController.addAvailableWorker(shiftDate,morning,WID2);
        branchController.addAvailableWorker(shiftDate,morning,WID3);
        branchController.addAvailableWorker(shiftDate,morning,WID4);
        branchController.addAvailableWorker(shiftDate,morning,WID5);
        branchController.addAvailableWorker(shiftDate,morning,WID6);
        branchController.addAvailableWorker(shiftDate,morning,WID7);


        branchController.createShift(shiftDate,morning);
        branchController.addPositionToShift("driver",1);
        branchController.addPositionToShift("storage man",1);
        branchController.addPositionToShift("security guard",1);
        branchController.addPositionToShift("cashier",1);
        branchController.addWorkerToPositionInShift("manager",WID2);
        branchController.addWorkerToPositionInShift("storage man",WID1);
        branchController.addWorkerToPositionInShift("security guard",WID6);
        branchController.addWorkerToPositionInShift("cashier",WID7);
        branchController.submitShift();

        shiftDate=parseDate("29/07/2020");

        branchController.addAvailableWorker(shiftDate,morning,WID1);
        branchController.addAvailableWorker(shiftDate,morning,WID2);
        branchController.addAvailableWorker(shiftDate,morning,WID3);
        branchController.addAvailableWorker(shiftDate,morning,WID4);
        branchController.addAvailableWorker(shiftDate,morning,WID5);
        branchController.addAvailableWorker(shiftDate,morning,WID6);
        branchController.addAvailableWorker(shiftDate,morning,WID7);


        branchController.createShift(shiftDate,morning);
        branchController.addPositionToShift("driver",1);
        branchController.addPositionToShift("storage man",1);
        branchController.addPositionToShift("security guard",1);
        branchController.addPositionToShift("cashier",1);
        branchController.addWorkerToPositionInShift("manager",WID2);
        branchController.addWorkerToPositionInShift("storage man",WID1);
        branchController.addWorkerToPositionInShift("security guard",WID6);
        branchController.addWorkerToPositionInShift("cashier",WID7);
        branchController.submitShift();






    }

}


