package com.company.BussinesLayer;

import javafx.util.Pair;

import java.util.*;

public class Scheduler {
    TreeSet<WeeklySchedule> schedule;
    private static final boolean morning=true;
    private static final boolean night=false;
    private HashMap<Date, Pair<List<Worker>,List<Worker>>> availableWorkers;
    private Shift currentEditedShift;
    private Date currentEditedShiftDate;
    private boolean  currentEditedShiftTOD;
    public Scheduler() {
        schedule=new TreeSet<>((a,b)-> {
            return a.dayStart.compareTo(b.dayStart);
        } );
        availableWorkers=new HashMap<>();
    }

    public Shift getCurrentEditedShift() {
        return currentEditedShift;
    }
    public String removeShift(Date date,boolean timeOfDay)
    {
        DailySchedule day=findDay(date);
        if(day==null)
            return "Invalid date";
        if(timeOfDay==morning)
            day.setMorningShift(null);
        else
            day.setNightShift(null);
        return null;
    }
    public List<Shift> getWeeklySchedulebyDate(Date date)
    {
        if(date==null)
            return null;
        WeeklySchedule week=schedule.floor(new WeeklySchedule(date,false));
        if(week==null)
            return null;
        return week.getShifts();
    }
    public String removeWorkerToPositionInShift(String pos,String id)
    {
        return currentEditedShift.removeWorkerFromPosition(pos,id);
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
        currentEditedShift=findShift(date,timeOfDay);
        currentEditedShift.setAvailableWorkers(cloned);
        return null;
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
        Pair<List<Worker>,List<Worker>> p=availableWorkers.putIfAbsent(date,new Pair<>(new ArrayList<>(),new ArrayList<>()));
        List<Worker> check=null;
        if(partOfDay==morning)
            check=availableWorkers.get(date).getKey();
        else
            check=availableWorkers.get(date).getValue();
        Worker worker=getWorkerById(id);
        if(check.contains(worker))
            return "The worker is already marked as available for this shift";
        check.add(worker);
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
}
