package bussines_layer.employees_module;

import bussines_layer.employees_module.models.ModelShift;
import bussines_layer.employees_module.models.ModelWorker;
import bussines_layer.transport_module.TransportModule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//TODO: merge both of the controllers (Roaster and Scheduler)

public class EmployeesModule {

    private TransportModule transportModule;
    private Scheduler scheduler = Scheduler.getInstance();
    private Roster roster = Roster.getInstance();
    private Integer branch_id;

    public EmployeesModule(Integer branch_id) {
        this.branch_id=branch_id;
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
        List<Worker> workers=roster.getWorkers();
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
    public String addDriver(String name, double salary, Date startDate,String license)
    {
        return roster.addDriver(name,salary,startDate,license,branch_id);
    }
    public String removeWorker(String id)
    {
        return roster.removeWorker(id);
    }


//endregion
}