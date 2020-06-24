package bussines_layer.employees_module;

import bussines_layer.employees_module.models.ModelShift;
import bussines_layer.employees_module.models.ModelWorker;
import bussines_layer.transport_module.TransportModule;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
//TODO: merge both of the controllers (Roaster and Scheduler)

public class EmployeesModule {

    private TransportModule transportModule;
    private Scheduler scheduler;
    private Roster roster;
    private Integer branch_id;

    public EmployeesModule(Integer branch_id) {
        this.branch_id=branch_id;
        roster=new Roster();
        scheduler=new Scheduler();
        scheduler.setRoster(roster);
        roster.setScheduler(scheduler);
    }

    public void init(Integer branch_id)
    {
        scheduler.init(branch_id);
        roster.init(branch_id);
    }

//region Scheduler:

    //constants
    private static final boolean morning=true;
    private static final boolean night=false;

    public void setTransportModule(TransportModule transportModule) {
        //TODO: check if null (transportModule)
        this.transportModule = transportModule;
    }

    public String chooseDriverForTransport(Date date, boolean shift, String drivers_license) {
        return scheduler.chooseDriverForTransport(date, shift, drivers_license,branch_id);
    }

    public String getDriverName(String driverId) {
        return scheduler.getDriverName(driverId);
    }

    public boolean StorageManInShift(Date d, boolean b) {
        return scheduler.StorageManInShift(d, b,branch_id);
    }

    public boolean DriversAvailability(Date date, boolean shift) {
        return scheduler.DriversAvailability(date, shift,branch_id);
    }

    public void removeDriverFromTransport(Date d, boolean shift, String driverId) {
        scheduler.removeDriverFromTransport(d, shift, driverId,branch_id);
    }



    public String removeShift(Date date,boolean timeOfDay){
        if(transportModule.isTransportExist(date,timeOfDay))
            return "Can not remove shift schedueled for transportaion";
        return scheduler.removeShift(date,timeOfDay,branch_id);
    }
    public String createShift(Date date, boolean timeOfDay)
    {
        return scheduler.createShift(date,timeOfDay,branch_id);
    }
    public String editShift(Date date, boolean timeOfDay)
    {
        return scheduler.editShift(date,timeOfDay,branch_id);
    }
    public ModelShift getCurrentEditedModelShift()
    {
        return new ModelShift(scheduler.getCurrentEditedShift(),scheduler.cloneAvailableWorkersForShift(branch_id));
    }
    public void cancelShift(){scheduler.cancelShift();}
    public String submitShift(){
        return scheduler.submitShift(branch_id);
    }
    public String removePositionFromShift(String pos){
        String position=pos.toLowerCase();
        if(position.equals("storage man")&&transportModule.isTransportExist(scheduler.getCurrentEditedShift().getDate(),scheduler.getCurrentEditedShift().getTimeOfDay()))
            return "Can not remove storage man because there is transportation scheduled for this shift";
        return scheduler.removePositionToShift(position);
    }
    public String addPositionToShift(String pos,int quantity)
    {
        return scheduler.addPositionToShift(pos,quantity);
    }
    public String addWorkerToPositionInShift(String pos,String id){
        return scheduler.addWorkerToPositionInShift(pos,id,branch_id);
    }
    public String removeWorkerToPositionInShift(String pos,String id)
    {
        String position=pos.toLowerCase();
        if(position.equals("storage man")&&transportModule.isTransportExist(scheduler.getCurrentEditedShift().getDate(),scheduler.getCurrentEditedShift().getTimeOfDay()))
            return "Can not remove storage man from shift with transportation";
        return scheduler.removeWorkerToPositionInShift(position,id,branch_id,transportModule);
    }
    public String addAvailableWorker(Date date, boolean partOfDay, String id){
        return scheduler.addAvailableWorker(date,partOfDay,id,branch_id);
    }
    public String removeAvailableWorker(Date date,boolean partOfDay,String id) {

        return scheduler.removeAvailableWorker(date,partOfDay,id,branch_id);
    }
    public List<ModelShift> getWeeklyShifts(Date date)
    {
        List<ModelShift> output=new ArrayList<>();
        List<Shift>week=scheduler.getWeeklySchedulebyDate(date,branch_id);
        if(week==null)
            return null;
        for(Shift s:week)
        {
            if(s instanceof EmptyShift)
                output.add(new ModelShift((EmptyShift)s));
            else
                output.add(new ModelShift(s,new ArrayList<>()));

        }
        return output;
    }
    public String removeWorkerFromRoster(String id){
        return scheduler.removeWorkerFromRoster(id,branch_id);
    }



//endregion


//region Roster:

    public List<ModelWorker> displayWorkers()
    {
        List<Worker> workers=roster.getWorkers(branch_id);
        List<ModelWorker> output=new ArrayList<>();
        for(Worker w:workers)
        {
            output.add(w.getModel());
        }
        return output;
    }

    public ModelWorker displaySingleWorker(String id)
    {
        return (roster.findWorker(id).getModel());
    }

    public String editName(String newName,String id)
    {
        return roster.editName(newName,id);
    }
    public String editSalary(double newSalary,String id)
    {
        return roster.editSalary(newSalary,id);
    }
    public String addPosition(String pos,String id)
    {
        return roster.addPosition(pos,id);
    }
    public String removePosition(String pos,String id)
    {
        return roster.removePosition(pos,id,branch_id);
    }
    public String addWorker(String name, double salary, Date startDate,List<String>positions)
    {
        return roster.addWorker(name,salary,startDate,branch_id,positions);
    }
    public String initAddWorker(String id, String name, double salary, Date startDate,List<String>positions)
    {
        return roster.initAddWorker(id,name,salary,startDate,branch_id,positions);
    }
    public String addDriver(String name, double salary, Date startDate,String license)
    {
        return roster.addDriver(name,salary,startDate,license,branch_id);
    }
    public String initAddDriver(String id,String name, double salary, Date startDate,String license)
    {
        return roster.initAddDriver(id,name,salary,startDate,license,branch_id);
    }
    public String removeWorker(String id)
    {
        return roster.removeWorker(id);
    }


    public static Date parseDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1=null;
        try {
            //Parsing the String
            date1 = dateFormat.parse(date);
        } catch (ParseException e) {

        }
        return date1;
    }


//endregion


    public void initData()
    {
        List<String>positions1=new ArrayList<>();
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


        roster.initAddWorker(WID1,"Gil",16,startDate1,branch_id,positions1);
        roster.initAddWorker(WID2,"Sharon",15.9,startDate2,branch_id,positions2);
        roster.initAddDriver(WID3,"Moshe",10,startDate1,"C4",branch_id);
        roster.initAddDriver(WID4,"Dani",100,startDate2,"C",branch_id);
        roster.initAddDriver(WID5,"Gadi",100,startDate2,"C1",branch_id);

        positions2.add("security guard");

        roster.initAddWorker(WID6,"Avi",100,startDate1,branch_id,positions2);


        List<String>positions3=new ArrayList<>();
        positions3.add("storage man");
        positions3.add("cashier");
        roster.initAddWorker(WID7,"bob",100,startDate1,branch_id,positions3);


        Date shiftDate1=parseDate("20/05/2020");
        Date shiftDate2=parseDate("21/05/2020");

        addAvailableWorker(shiftDate1,morning,WID1);
        addAvailableWorker(shiftDate1,morning,WID7);
        addAvailableWorker(shiftDate1,morning,WID3);
        addAvailableWorker(shiftDate1,morning,WID4);

        addAvailableWorker(shiftDate2,morning,WID1);
        addAvailableWorker(shiftDate2,morning,WID2);
        addAvailableWorker(shiftDate2,morning,WID3);
        addAvailableWorker(shiftDate2,morning,WID4);
        addAvailableWorker(shiftDate2,morning,WID5);
        addAvailableWorker(shiftDate2,morning,WID6);
        addAvailableWorker(shiftDate2,morning,WID7);

        createShift(shiftDate1,morning);


        addPositionToShift("cashier",1);
        addWorkerToPositionInShift("manager",WID1);
        addWorkerToPositionInShift("cashier",WID7);
        submitShift();

        createShift(shiftDate2,morning);

        addPositionToShift("driver",1);
        addPositionToShift("storage man",1);
        addPositionToShift("security guard",1);
        addPositionToShift("cashier",1);
        addWorkerToPositionInShift("manager",WID2);
        addWorkerToPositionInShift("storage man",WID1);
        addWorkerToPositionInShift("security guard",WID6);
        addWorkerToPositionInShift("cashier",WID7);
        submitShift();


//        editShift(shiftDate2,morning);
//        scheduler.getCurrentEditedShift().removeWorkerFromPosition("driver",workers.get(2).id,scheduler.cloneAvailableWorkersForShift());
//        this.submitShift();
//        scheduler.removeDriverFromTransport(shiftDate2,morning,workers.get(3).id);
    }


//    public void removeExistingWorkers() {
//        roster.removeExistingWorkers();
//    }
//
//    public Map<Date, Pair<LazyList<Worker>, LazyList<Worker>>> getAvailableWorkers() {
//        return scheduler.getAvailableWorkers();
//    }

    public Scheduler testScheduler() {
        return scheduler;
    }

    public Roster testRoster() {
        return roster;
    }
}