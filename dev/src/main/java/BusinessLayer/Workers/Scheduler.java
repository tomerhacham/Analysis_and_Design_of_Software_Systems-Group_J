package BusinessLayer.Workers;

import BusinessLayer.Transport.TransportController;
import javafx.util.Pair;

import java.text.SimpleDateFormat;
import java.util.*;

public class Scheduler {
    private TreeSet<WeeklySchedule> schedule;
//TODO:to-lower all the inserted positions(in roster too)
    private static final boolean morning=true;
    private static final boolean night=false;
    private HashMap<Date, Pair<List<Worker>,List<Worker>>> availableWorkers;
    private TransportController transportController;
    private Shift currentEditedShift;
    private Date currentEditedShiftDate;
    private boolean  currentEditedShiftTOD;

    public Scheduler() {
        schedule=new TreeSet<>((a,b)-> {
            return a.dayStart.compareTo(b.dayStart);
        } );
        availableWorkers=new HashMap<>();
    }

    public void setAvailableWorkers(HashMap<Date, Pair<List<Worker>, List<Worker>>> availableWorkers) {
        this.availableWorkers = availableWorkers;
        transportController=TransportController.getInstance();
    }

    public Shift getCurrentEditedShift() {
        return currentEditedShift;
    }

    public String removeShift(Date date,boolean timeOfDay)
    {
        DailySchedule day=findDay(date);
        if(day==null)
            return "Invalid date";
        if(transportController.isTransportExist(currentEditedShiftDate,currentEditedShiftTOD))
           return "Can not remove shift schedueled for transportaion";
        if(timeOfDay==morning)
            day.setMorningShift(null);
        else
            day.setNightShift(null);
        return null;
    }

    public List<Shift> getWeeklySchedulebyDate(Date date)
    {
        if(date==null||schedule.size()==0||date.after(DateManipulator.addDays(schedule.last().dayStart,6)))
            return null;
        WeeklySchedule week=schedule.floor(new WeeklySchedule(date,false));
        if(week==null)
            return null;
        return week.getShifts();
    }

    public String removeWorkerToPositionInShift(String pos,String id)
    {
        if(pos.equals("storage man")&&transportController.isTransportExist(currentEditedShiftDate,currentEditedShiftTOD))
            return "Can not remove storage man from shift with transportation";
        else
            return currentEditedShift.removeWorkerFromPosition(pos,id);
    }

    public HashMap<Date, Pair<List<Worker>, List<Worker>>> getAvailableWorkers() {
        return availableWorkers;
    }

    public TreeSet<WeeklySchedule> getSchedule() {
        return schedule;
    }

    public String addWorkerToPositionInShift(String pos,String id)
    {
        return currentEditedShift.addWorkerToPosition(pos,id);
    }

    public String addPositionToShift(String pos,int quantity)
    {
        return currentEditedShift.addPosition(pos,quantity);
    }

    public String removePositionToShift(String pos)
    {
        if(pos!=null)
            return "Invalid positions";
        if(pos.equals("storage man")&transportController.isTransportExist(currentEditedShiftDate,currentEditedShiftTOD))
            return "Can not remove storage man because there is transportation scheduled for this shift";
        return currentEditedShift.removePosition(pos);
    }
    public void cancelShift()
    {
        currentEditedShift=null;
        currentEditedShiftDate=null;
    }

    public String submitShift()
    {
        if(currentEditedShift!=null) {
            if (!currentEditedShift.isValid())
                return "The shift is invalid";
            addWeeksIfAbsent(currentEditedShiftDate);
            DailySchedule day=findDay(currentEditedShiftDate);
            if(day==null)
                return "Invalid date";
            else if(currentEditedShiftTOD==morning)
                day.setMorningShift(currentEditedShift);
            else
                day.setNightShift(currentEditedShift);
            cancelShift();
            return null;
        }
            return "Error-No shift is being edited";
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

    public String createShift(Date date,boolean timeOfDay)
    {
        if(date==null)
            return "Invalid date";
        currentEditedShiftDate=date;
        currentEditedShiftTOD=timeOfDay;
        if(!availableWorkers.containsKey(date))
            return "No Available workers were marked for this shift";
        List<Worker> check = null;
        if (timeOfDay == morning) {
            check = availableWorkers.get(date).getKey();
        } else
            check = availableWorkers.get(date).getValue();
        if(check.size()==0)
            return "No Available workers were marked for this shift";
        List<Worker>cloned =new ArrayList<>();
        cloned.addAll(check);
        currentEditedShift=new Shift(cloned,date,timeOfDay);
        return null;
    }

    public String editShift(Date date,boolean timeOfDay)
    {
        if(date==null)
            return "Invalid date";
        currentEditedShiftDate=date;
        currentEditedShiftTOD=timeOfDay;
        if(!availableWorkers.containsKey(date))
            return "No Available workers were marked for this shift";
        List<Worker> check = null;
        if (timeOfDay == morning) {
            check = availableWorkers.get(date).getKey();
        } else
            check = availableWorkers.get(date).getValue();
        if(check.size()==0)
            return "No Available workers were marked for this shift";
        List<Worker>cloned =new ArrayList<>();
        cloned.addAll(check);
        currentEditedShift=new Shift(findShift(date,timeOfDay));
        filterAvailableWorkers(cloned,currentEditedShift.getOccupation());
        currentEditedShift.setAvailableWorkers(cloned);
        return null;
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
        DailySchedule day=findDay(date);
        if(day==null)
            return null;
        if(timeOfDay==morning)
            return day.getMorningShift();
        else
            return day.getNightShift();
    }

    public String addAvailableWorker(Date date,boolean partOfDay,String id)
    {
        if(date==null)
            return "Invalid date";
        Worker worker=getWorkerById(id);
        if(Roster.getInstance().findWorker(id)==null)
            return "The worker hasn't been added to the system";
        if(worker.getStart_Date().after(date))
            return "Can not add worker before his start date of working";
        Pair<List<Worker>,List<Worker>> p=availableWorkers.putIfAbsent(date,new Pair<>(new ArrayList<>(),new ArrayList<>()));
        List<Worker> check=null;
        if(partOfDay==morning) {
            check=availableWorkers.get(date).getKey();
        }
        else {
            check=availableWorkers.get(date).getValue();
        }
        if(check.contains(worker))
            return "The worker is already marked as available for this shift";
        check.add(worker);
        return null;
    }

    public String removeWorkerFromRoster(String id)
    {
        Worker w=Roster.getInstance().findWorker(id);
        if(w==null)
            return "No such worker is in the system";
        String output="";
        Date currentDate=new Date();
        for(WeeklySchedule ws:schedule)
        {
            for(Shift shift:ws.getShifts())
            {
                if(shift.getDate().after(currentDate))
                for(List<Worker> workers:shift.getOccupation().values())
                {
                    if(workers.contains(w)) {
                        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String DateString = myFormat.format(shift.getDate());
                        String timeOfDay=null;
                        if(shift.getTimeOfDay()==morning)
                            timeOfDay=" morning shift";
                        else
                            timeOfDay=" night shift";
                        output += DateString+timeOfDay+", ";
                    }

                }
            }
        }
        if(output.length()>0)
            return "Can not remove worker because they are already scheduled to work on the following shifts:\n"+
                    output;
        Roster.getInstance().removeWorker(id);
        for(Pair<List<Worker>,List<Worker>> p:availableWorkers.values())
        {
            p.getValue().remove(w);
            p.getKey().remove(w);
        }
        return null;
    }

    public String removeAvailableWorker(Date date,boolean partOfDay,String id) {
        if (availableWorkers.containsKey(date)) {
            List<Worker> check = null;
            if (partOfDay == morning) {
                check = availableWorkers.get(date).getKey();
            } else
                check = availableWorkers.get(date).getValue();
            Worker worker=getWorkerById(id);
            if(isWorkerScheduled(worker,date,partOfDay))
                return "Unable to remove availability because the worker is already scheduled for this shift";
            if (check.remove(worker))
                return null;
        }
            return "The worker is not available for this shift";
    }

    private boolean isWorkerScheduled(Worker worker,Date date,boolean partOfDay) {
        Shift shift=findShift(date,partOfDay);
        if(shift==null)
            return false;
        if(worker instanceof Driver&&shift.getScheduledDrivers().contains((Driver)worker))
            return true;
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

    public boolean DriversAvailability(Date date, boolean timeOfDay)//TODO:the next function covers this one
    {
        if(!availableWorkers.containsKey(date))
            return false;
        List<Worker> availables=availableWorkers.get(date).getValue();
        if(timeOfDay=morning)
            availables=availableWorkers.get(date).getKey();
        for(Worker w: availables)
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
        List<Worker> availables=availableWorkers.get(date).getValue();
        if(timeOfDay==morning)
            availables=availableWorkers.get(date).getKey();
        Worker temp=null;
        for(Worker w: availables)
        {
            if(w.positions.contains("driver")&&w.getLicense().equals(license)) {
                shift.addDriverToShift((Driver)w);
                temp=w;
                break;
            }
        }
        if(temp!=null) {
            availables.remove(temp);
            return temp.getId();
        }
        return null;
    }

    public void removeDriverFromTransport(Date date, boolean timeOfDay, String id)
    {
        Shift shift=findShift(date,timeOfDay);
        Driver d=null;
        if(shift!=null)
            d=shift.removeDriver(id);
        if(d!=null) {
            List<Worker> availables = availableWorkers.get(date).getValue();
            if(timeOfDay==morning)
                availables = availableWorkers.get(date).getKey();
            availables.add(d);
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
