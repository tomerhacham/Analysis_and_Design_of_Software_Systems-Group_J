package BusinessLayer.Workers;

import DataAccessLayer.Mapper;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

public class Scheduler {
    private TreeSet<WeeklySchedule> schedule;
    private static final boolean morning=true;
    private static final boolean night=false;
    private HashMap<Date, Pair<LazyList<Worker>,LazyList<Worker>>> availableWorkers;
    private Shift currentEditedShift;
    private static Scheduler scheduler;
    private Mapper mapper=Mapper.getInstance();


    private Scheduler() {
        schedule=new TreeSet<>((a,b)-> {
            return a.dayStart.compareTo(b.dayStart);
        } );
        availableWorkers=new HashMap<>();
    }

    public static Scheduler getInstance()
    {
        if(scheduler==null)
            scheduler=new Scheduler();
        return scheduler;
    }


    public Shift getCurrentEditedShift() {
        return currentEditedShift;
    }

    public String removeShift(Date date,boolean timeOfDay)
    {
        if(date==null||findShift(date, timeOfDay)==null)
            return "Invalid date";
        DailySchedule day=findDay(date);
        if(day==null)
            return "Invalid date";
        if(timeOfDay==morning){
            mapper.deleteShift(day.getMorningShift().getId());
            day.setMorningShift(null);
        }
        else {
            mapper.deleteShift(day.getNightShift().getId());
            day.setNightShift(null);
        }
        return null;
    }

    public List<Shift> getWeeklySchedulebyDate(Date date)
    {/*
        if(date==null||schedule.size()==0||date.after(DateManipulator.addDays(schedule.last().dayStart,6)))
            return null;
        WeeklySchedule week=schedule.floor(new WeeklySchedule(date,false));
        if(week==null)
            return null;
        //return week.getShifts();
        */
        if(date==null)
            return null;
        Date dayStart=DateManipulator.getFirstDayOfWeek(date);
        List<Shift> output = new ArrayList<>();
        Shift s=null;
        for(int i=0; i<7;i++)
        {
            Date day=DateManipulator.addDays(dayStart,i);
            s=findShift(day,morning);
            if(s==null)
                s=new EmptyShift(day,morning);
            output.add(s);
            s=findShift(day,night);
            if(s==null)
                s=new EmptyShift(day,night);
            output.add(s);
        }
        return output;
    }

    public String removeWorkerToPositionInShift(String pos,String id)
    {
        if(pos==null)
            return "Invalid position";
        else
            return currentEditedShift.removeWorkerFromPosition(pos,id,cloneAvailableWorkersForShift());
    }

   // public HashMap<Date, Pair<List<Worker>, List<Worker>>> getAvailableWorkers() {
        //return availableWorkers;
   // }



    public String addWorkerToPositionInShift(String pos,String id)
    {
        return currentEditedShift.addWorkerToPosition(pos.toLowerCase(),id,cloneAvailableWorkersForShift());
    }

    public String addPositionToShift(String pos,int quantity)
    {
        return currentEditedShift.addPosition(pos.toLowerCase(),quantity);
    }

    public String removePositionToShift(String pos)
    {
        if(pos==null)
            return "Invalid positions";
        return currentEditedShift.removePosition(pos);
    }
    public void cancelShift()
    {
        currentEditedShift=null;
    }

    public String submitShift()
    {
        if(currentEditedShift!=null) {
            if (!currentEditedShift.isValid())
                return "The shift is invalid";
            addWeeksIfAbsent(currentEditedShift.getDate());
            DailySchedule day=findDay(currentEditedShift.getDate());
            if(day==null) {
                return "Invalid date";
            }
            Shift oldShift=findShift(currentEditedShift.getDate(),currentEditedShift.getTimeOfDay());
            if(currentEditedShift.getTimeOfDay()==morning){
                day.setMorningShift(currentEditedShift);}
            else{
                day.setNightShift(currentEditedShift);
            }
            if(oldShift==null) {
                mapper.addShift(currentEditedShift);
            }
            else {
                mapper.updateShift(currentEditedShift);
                updateShift(oldShift);
            }cancelShift();
            return null;
        }
            return "Error-No shift is being edited";
    }

    /*adds and deletes changes in shift to database*/
    private void updateShift(Shift oldShift) {
        for (String newPosition:currentEditedShift.getOccupation().keySet())
        {
            List<Worker> oldWorkers=new ArrayList<>();
            if(oldShift.getOccupation().containsKey(newPosition))
            {
                oldWorkers=oldShift.getOccupation().get(newPosition);
            }
            for(Worker newWorker:currentEditedShift.getOccupation().get(newPosition))
            {

                if(!oldWorkers.remove(newWorker))
                    mapper.addOccupation(oldShift.getId(),newPosition,newWorker.getId());
            }

        }
        for (String oldPosition:oldShift.getOccupation().keySet())
        {
            for(Worker oldWorker:oldShift.getOccupation().get(oldPosition))
            {
                mapper.deleteOccupation(oldShift.getId(),oldPosition,oldWorker.getId());
            }
        }
    }

    public String createShift(Date date,boolean timeOfDay)
    {
        if(date==null)
            return "Invalid date";
        /*if(!availableWorkers.containsKey(date))
            return "No Available workers were marked for this shift";*/
        List<Worker> workerList = getAvailableWorkersForShift(date, timeOfDay);
        if(workerList.size()==0)
            return "No Available workers were marked for this shift";
        /*List<Worker>cloned =new ArrayList<>();
        cloned.addAll(workerList);*/
        Shift s=findShift(date,timeOfDay);
        String newId=UUID.randomUUID().toString();
        if(s!=null)
            currentEditedShift=new Shift(date,timeOfDay,s.getId());
        else
            currentEditedShift=new Shift(date,timeOfDay,newId);
        /*
        currentEditedShift.setDate(date);
        currentEditedShift.setTimeOfDay(timeOfDay);
        */
        return null;
    }

    public String editShift(Date date,boolean timeOfDay)
    {
        if(date==null)
            return "Invalid date";
        /*
        if(!availableWorkers.containsKey(date))
            return "No Available workers were marked for this shift";

        if (timeOfDay == morning) {
            check = availableWorkers.get(date).getKey();
        } else
            check = availableWorkers.get(date).getValue();
        if(check.size()==0)
            return "No Available workers were marked for this shift";
        List<Worker> workersList = getAvailableWorkersForShift(date, timeOfDay);
        List<Worker>clonedWorkers =new ArrayList<>();
        clonedWorkers.addAll(workersList);*/
        Shift s=findShift(date,timeOfDay);
        if (s==null)
            return createShift(date, timeOfDay);
        currentEditedShift=new Shift(s);
        /*
        currentEditedShift.setDate(date);
        currentEditedShift.setTimeOfDay(timeOfDay);
        */
        //filterAvailableWorkers(clonedWorkers,currentEditedShift.getOccupation());
       // currentEditedShift.setAvailableWorkers(clonedWorkers);
        return null;
    }

    private DailySchedule findDay(Date date)
    {
        if(date==null||schedule.size()==0||date.before(schedule.first().dayStart)||
                date.after(DateManipulator.addDays(schedule.last().dayStart,6)))
            return null;
        DailySchedule output=null;
        for(WeeklySchedule ws:schedule)
        {
            output=ws.getDay(date);
            if(output!=null)
                return output;
        }
        return null;
    }


    public List<Worker> cloneAvailableWorkersForShift()
    {
        if(currentEditedShift==null)
            return new ArrayList<>();
        List<Worker> workersList = getAvailableWorkersForShift(currentEditedShift.getDate(),currentEditedShift.getTimeOfDay());
        List<Worker>clonedWorkers =new ArrayList<>();
        clonedWorkers.addAll(workersList);
        filterAvailableWorkers(clonedWorkers,currentEditedShift.getOccupation());
        return clonedWorkers;
    }
    private List<Worker> filterAvailableWorkers(List<Worker> availables, HashMap<String, FixedSizeList<Worker>> occupation) {
        for(String pos:occupation.keySet()) {
            availables.removeAll(occupation.get(pos));
        }
        return availables;
    }

    public String addWeeksIfAbsent(Date date)
    {
        if(date==null)
            return "Invalid date";
        if(schedule.size()==0)
        {
            schedule.add(new WeeklySchedule(DateManipulator.getFirstDayOfWeek(date)));
            return null;
        }
        Date endOfTheLastWeek=DateManipulator.addDays(schedule.last().dayStart,6);
        while(date.after(endOfTheLastWeek))
        {
            schedule.add(new WeeklySchedule(DateManipulator.addDays(endOfTheLastWeek,1)));
            endOfTheLastWeek=DateManipulator.addDays(schedule.last().dayStart,6);
        }
        Date firstDay=schedule.first().dayStart;
        while(date.before(firstDay))
        {
            schedule.add(new WeeklySchedule(DateManipulator.addDays(firstDay,-7)));
            firstDay=schedule.first().dayStart;
        }
        return null;
    }

    public Shift findShift(Date date,boolean timeOfDay)
    {
        Shift shift=null;
        DailySchedule day=findDay(date);
        if(day!=null) {
            if (timeOfDay == morning)
                shift = day.getMorningShift();
            else
                shift = day.getNightShift();
        }
        if(shift==null)
        {
            shift=mapper.getShift(date,timeOfDay);
            if(shift!=null)
            {
                if (setShiftToSchedule(date, timeOfDay, shift))
                    return null;
            }
        }
        return shift;
    }

    private boolean setShiftToSchedule(Date date, boolean timeOfDay, Shift shift) {
        DailySchedule day;
        addWeeksIfAbsent(date);
        day=findDay(date);
        if(day==null)
            return true;
        else if(timeOfDay==morning){
            day.setMorningShift(shift);
        }
        else{
            day.setNightShift(shift);
        }
        return false;
    }

    public String removeWorkerFromRoster(String id)
    {
        Worker w=Roster.getInstance().findWorker(id);
        if(w==null)
            return "No such worker is in the system";
        boolean isScheduled = isWorkerScheduled(w.getId());
        if(isScheduled)
            return "Can not remove worker as they are already scheduled to work on shifts\n";
        Roster.getInstance().removeWorker(id);
        removeAvailableWorker(w);
        return null;
    }

    private String getShiftSimpleTime(Shift shift) {
        String output="";
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String DateString = myFormat.format(shift.getDate());
        String timeOfDay = null;
        if (shift.getTimeOfDay() == morning)
            timeOfDay = " morning shift";
        else
            timeOfDay = " night shift";
        output = DateString + timeOfDay;
        return output;
    }

    public String addAvailableWorker(Date date,boolean partOfDay,String id)
    {
        if(date==null)
            return "Invalid date";
        Worker worker=getWorkerById(id);
        if(worker==null)
            return "The worker hasn't been added to the system";
        if(worker.getStart_Date().after(date))
            return "Can not add worker before his start date of working";
        List<Worker> workers=getAvailableWorkersForShift(date, partOfDay);
        /*Pair<List<Worker>,List<Worker>> p=availableWorkers.putIfAbsent(date,new Pair<>(new ArrayList<>(),new ArrayList<>()));
        if(partOfDay==morning) {
            check=availableWorkers.get(date).getKey();
        }
        else {
            check=availableWorkers.get(date).getValue();
        }
        */
        if(workers.contains(worker))
            return "The worker is already marked as available for this shift";
        workers.add(worker);
        mapper.addShiftAvailableWorkers(worker.getId(),date,partOfDay);
        return null;
    }
    private List<Worker> getAvailableWorkersForShift(Date date,boolean partOfDay)
    {
        LazyList<Worker> workerList = null;
        if (availableWorkers.containsKey(date))
            {
            if (partOfDay == morning)
            {
                workerList = availableWorkers.get(date).getMorning();
            }
            else
                workerList = availableWorkers.get(date).getNight();
        }
        else
        {
            availableWorkers.put(date,new Pair<>(null,null));
        }
        if(workerList==null)
            workerList=new LazyList<>();
        if(!workerList.isRead())
        {
            List<Worker> currWorkers=mapper.getAvailableWorkers(date,partOfDay);
            if(currWorkers!=null)
                workerList.addAll(currWorkers);
            workerList.setRead(true);

            Pair pair = availableWorkers.get(date);
            if(partOfDay==morning)
                pair.setMorning(workerList);
            else
                pair.setNight(workerList);
            }

        return workerList;
    }
    private List<Worker> getAvailableWorkersForShiftNOPull(Date date,boolean partOfDay)
    {
        LazyList<Worker> workerList = null;
        if (availableWorkers.containsKey(date))
        {
            if (partOfDay == morning)
            {
                workerList = availableWorkers.get(date).getMorning();
            }
            else
                workerList = availableWorkers.get(date).getNight();
        }
        return workerList;
    }
    public String removeAvailableWorker(Date date,boolean partOfDay,String id)
    {
        Worker worker=getWorkerById(id);
        if (worker==null)
            return  "Invalid worker id";
        if(isWorkerScheduled(worker,date,partOfDay))
            return "Unable to remove availability because the worker is already scheduled for this shift";
        if (mapper.deleteShiftAvailableWorkers(worker.getId(),date,partOfDay)){
            List<Worker> workerList =getAvailableWorkersForShiftNOPull(date,partOfDay);
            if(workerList!=null)
            {
                int index=-1;
                for(Worker oldWorker:workerList)
                {
                   if(oldWorker.getId().equals(worker.getId())){
                        index=workerList.indexOf(oldWorker);
                        break;
                   }
                }
                workerList.remove(index);
            return null;
            }
        }
        return "The worker is not available for this shift";
    }

    private void removeAvailableWorker(Worker w)
    {
        for(Pair<LazyList<Worker>,LazyList<Worker>> p:availableWorkers.values())
        {
            List<Worker> workers = p.getMorning();
            if(workers!=null)
                workers.remove(w);
            workers = p.getNight();
            if(workers!=null)
                workers.remove(w);
        }
    }

    /*returns a list of every shift to which the worker is scheduled */
    private List<Shift> isWorkerScheduled(Worker w)
    {
        List<Shift> output=new ArrayList<>();
        Date currentDate=new Date();
        for(WeeklySchedule ws:schedule)
        {
            for(Shift shift:ws.getShifts())
            {
                if(shift.getDate().after(currentDate))
                    for(List<Worker> workers:shift.getOccupation().values())
                    {
                        if(workers.contains(w)) {
                            output.add(shift);

                            //TODO::get from mapper function all shifts where worker is occupied (by ID)
                        }
                    }
                    if(shift.getScheduledDrivers().contains(w))
                        output.add(shift);
                        //TODO::get from mapper function all shifts where worker is a driver (by ID)
            }
        }
        return output;
    }
    public boolean isWorkerScheduled(String id)
    {
        Date currentDate=new Date();
        for(WeeklySchedule ws:schedule)
        {
            for(Shift shift:ws.getShifts())
            {
                if(shift.getDate().after(currentDate))
                    for(List<Worker> workers:shift.getOccupation().values())
                    {
                        for(Worker w: workers)
                            if(w.getId().equals(id)) {
                                return true;
                        }
                    }
                for(Worker w:shift.getScheduledDrivers())
                    if(w.getId().equals(id))
                        {return true;}
            }
        }
         return mapper.isScheduled(id);
    }
    /*returns a string of every shift to which the worker is scheduled for the specific position.
    * returns null if none.*/

    /*
    private String isWorkerScheduled(Worker w, String pos)
    {
        String output="";
        List<Shift> scheduledShifts = isWorkerScheduled(w); //TODO:change isscheduled usage
        for (Shift shift: scheduledShifts)
        {
            if(shift.getOccupation().containsKey(pos) &&
                shift.getOccupation().get(pos).contains(w))
                output+= getShiftSimpleTime(shift)+", ";
        }
        return output;
    }*/

    /*returns true if the worker is scheduled for the specific shift */
    private boolean isWorkerScheduled(Worker worker,Date date,boolean partOfDay){
        Shift shift=findShift(date,partOfDay);
        if(shift==null)
            return false;
        //if(worker instanceof Driver&&shift.getScheduledDrivers().contains((Driver)worker))
        //    return true;
        for(Worker oldWorker: shift.getScheduledDrivers())
        {
            if(oldWorker.getId().equals(worker.getId()))
                return true;
        }
        Map<String,FixedSizeList<Worker>> occupation=shift.getOccupation();
        for(String pos:worker.getPositions())
        {
            if(occupation.containsKey(pos)&&occupation.get(pos).contains(worker))
                return true;
        }
        return false;
    }

    private Worker getWorkerById(String id)
    {
        Roster roster=Roster.getInstance();
        return roster.findWorker(id);
    }

    //Integration functions
    public boolean StorageManInShift(Date date, boolean timeOfDay)
    {
        Shift shift=findShift(date,timeOfDay);
        if(shift==null)
            return false;
        return shift.getOccupation().containsKey("storage man");
    }

    public boolean DriversAvailability(Date date, boolean timeOfDay)
    {
        /*
        if(!availableWorkers.containsKey(date))
            return false;
        List<Worker> availables=availableWorkers.get(date).getValue();
        if(timeOfDay=morning)
            availables=availableWorkers.get(date).getKey();*/
        List<Worker> availableWorkers= getAvailableWorkersForShift(date, timeOfDay);
        for(Worker w: availableWorkers)
        {
            if(w.positions.contains("driver"))
                return true;
        }
        return false;
    }

    public String chooseDriverForTransport(Date date, boolean timeOfDay, String license)
    {
        if(date==null||license==null)
            return null;
        Shift shift=findShift(date,timeOfDay);
        if(shift==null)
            return null;
        /*List<Worker> availables=availableWorkers.get(date).getValue();
        if(timeOfDay==morning)
            availables=availableWorkers.get(date).getKey();*/
        List<Worker> availableWorkers=getAvailableWorkersForShift(date,timeOfDay);
        Driver d=null;
        for(Worker w: availableWorkers)
        {
            if(w.positions.contains("driver")&&w.getLicense().equals(license))
            {
                d= (Driver)w;
                mapper.addShiftDriver(d.getId(),shift.getId());
                removeAvailableWorker(date, timeOfDay, d.getId());
                shift.addDriverToShift(d);
                return d.getId();
            }
        }
        return null;
    }

    public void removeDriverFromTransport(Date date, boolean timeOfDay, String id)
    {
        Shift shift=findShift(date,timeOfDay);
        Driver d=null;
        if(shift!=null){
            d=shift.removeDriver(id);
        }
        if(d!=null) {
            mapper.deleteShiftDriver(d.getId(),shift.getId());
            addAvailableWorker(date, timeOfDay, id);
        }
    }

    public String getDriverName(String id)
    {
         Worker w=getWorkerById(id);
         if(w!=null)
             return w.getName();
         return null;
    }

}
