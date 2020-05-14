package InterfaceLayer.Workers;

import BusinessLayer.Workers.EmptyShift;
import BusinessLayer.Workers.Roster;
import BusinessLayer.Workers.Scheduler;
import BusinessLayer.Workers.Shift;
import InterfaceLayer.Transport.FacadeController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static PresentationLayer.Workers.IOWorkers.parseDate;

public class ScheduleController {
    private Scheduler scheduler;
    private static ScheduleController instance = null;
    private static final boolean morning=true;
    private static final boolean night=false;
    private FacadeController facade=FacadeController.getInstance();

    private ScheduleController() {
        scheduler=Scheduler.getInstance();
    }

    public static ScheduleController getInstance(){
        if (instance == null)
            instance = new ScheduleController();
        return instance;
    }

    public String removeShift(Date date,boolean timeOfDay){
        if(facade.isTransportExist(date,timeOfDay))
            return "Can not remove shift schedueled for transportaion";
        return scheduler.removeShift(date,timeOfDay);
    }
    public String createShift(Date date, boolean timeOfDay)
    {
        return scheduler.createShift(date,timeOfDay);
    }
    public String editShift(Date date, boolean timeOfDay)
    {
        return scheduler.editShift(date,timeOfDay);
    }
    public ModelShift getCurrentEditedModelShift(){
        return new ModelShift(scheduler.getCurrentEditedShift());
    }
    public void cancelShift(){scheduler.cancelShift();}
    public String submitShift(){
        return scheduler.submitShift();
    }
    public String removePositionFromShift(String pos){
        String position=pos.toLowerCase();
        if(position.equals("storage man")&&facade.isTransportExist(scheduler.getCurrentEditedShift().getDate(),scheduler.getCurrentEditedShift().getTimeOfDay()))
            return "Can not remove storage man because there is transportation scheduled for this shift";
        return scheduler.removePositionToShift(position);
    }
    public String addPositionToShift(String pos,int quantity)
    {
        return scheduler.addPositionToShift(pos,quantity);
    }
    public String addWorkerToPositionInShift(String pos,String id){
        return scheduler.addWorkerToPositionInShift(pos,id);
    }
    public String removeWorkerToPositionInShift(String pos,String id)
    {
        String position=pos.toLowerCase();
        if(position.equals("storage man")&&facade.isTransportExist(scheduler.getCurrentEditedShift().getDate(),scheduler.getCurrentEditedShift().getTimeOfDay()))
            return "Can not remove storage man from shift with transportation";
        return scheduler.removeWorkerToPositionInShift(position,id);
    }
    public String addAvailableWorker(Date date, boolean partOfDay, String id){
        return scheduler.addAvailableWorker(date,partOfDay,id);
    }
    public String removeAvailableWorker(Date date,boolean partOfDay,String id) {

        return scheduler.removeAvailableWorker(date,partOfDay,id);
    }
    public List<ModelShift> getWeeklyShifts(Date date)
    {
        List<ModelShift> output=new ArrayList<>();
        List<Shift>week=scheduler.getWeeklySchedulebyDate(date);
        if(week==null)
            return null;
        for(Shift s:week)
        {
            if(s instanceof EmptyShift)
                output.add(new ModelShift((EmptyShift)s));
            else
                output.add(new ModelShift(s));

        }
        return output;
    }
    public String removeWorkerFromRoster(String id){
        return scheduler.removeWorkerFromRoster(id);
    }
    public void test(RosterController rc)
    {
        List<String>positions1=new ArrayList<>();
        positions1.add("manager");
        positions1.add("storage man");
        List<String>positions2=new ArrayList<>();
        positions2.add("driver");
        Date startDate1=parseDate("11/04/2020");
        Date startDate2=parseDate("12/04/2020");
        rc.addWorker("Gil",16,startDate1,positions1);
        rc.addWorker("Sharon",15.9,startDate2,positions1);
        rc.addDriver("Moshe",10,startDate1,"C4");
        rc.addDriver("Dani",100,startDate2,"C");
        rc.addDriver("Gadi",100,startDate2,"C1");

        positions2.add("security guard");
        rc.addWorker("Avi",100,startDate1,positions2);
        List<ModelWorker> workers=rc.displayWorkers();
        Date shiftDate1=parseDate("20/05/2020");
        Date shiftDate2=parseDate("21/05/2020");
        this.addAvailableWorker(shiftDate1,morning,workers.get(0).id);
        this.addAvailableWorker(shiftDate1,night,workers.get(1).id);
        this.addAvailableWorker(shiftDate1,morning,workers.get(2).id);
        this.addAvailableWorker(shiftDate2,morning,workers.get(0).id);
        this.addAvailableWorker(shiftDate2,morning,workers.get(1).id);
        this.addAvailableWorker(shiftDate2,morning,workers.get(2).id);
        this.addAvailableWorker(shiftDate2,morning,workers.get(3).id);
        this.addAvailableWorker(shiftDate2,morning,workers.get(4).id);
        this.addAvailableWorker(shiftDate2,morning,workers.get(5).id);

        this.createShift(shiftDate2,morning);
        this.addPositionToShift("driver",1);
        this.addPositionToShift("storage man",1);
        this.addPositionToShift("security guard",1);
        this.addWorkerToPositionInShift("manager",workers.get(0).id);
        this.addWorkerToPositionInShift("storage man",workers.get(1).id);
        this.addWorkerToPositionInShift("security guard",workers.get(5).id);
        this.submitShift();
        scheduler.chooseDriverForTransport(shiftDate2,morning,"A");
        scheduler.chooseDriverForTransport(shiftDate2,morning,"B");
        editShift(shiftDate2,morning);
        scheduler.getCurrentEditedShift().removeWorkerFromPosition("driver",workers.get(2).id);
        this.submitShift();
        scheduler.removeDriverFromTransport(shiftDate2,morning,workers.get(3).id);
//        System.out.println(scheduler.StorageManInShift(shiftDate2,morning));
    }


    public String getDriverName(String driverID) {
        return scheduler.getDriverName(driverID);
    }

    public void removeDriverFromTransport(Date d, boolean shift, String driverId) {
        scheduler.removeDriverFromTransport(d, shift, driverId);
    }

    public boolean StorageManInShift(Date d, boolean shift) {
        return scheduler.StorageManInShift(d, shift);
    }

    public boolean DriversAvailability(Date date, boolean shift) {
        return scheduler.DriversAvailability(date, shift);
    }

    public String chooseDriverForTransport(Date date, boolean shift, String licence) {
        return scheduler.chooseDriverForTransport(date, shift, licence);
    }
}
