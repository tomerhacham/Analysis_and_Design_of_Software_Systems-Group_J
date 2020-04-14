package com.company.InterfaceLayer;

import com.company.BussinesLayer.Scheduler;
import com.company.BussinesLayer.Shift;
import com.company.BussinesLayer.WeeklySchedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleController {
    private Scheduler scheduler;
    public String removeShift(Date date,boolean timeOfDay){
        return scheduler.removeShift(date,timeOfDay);
    }
    public ScheduleController() {
        scheduler=new Scheduler();
    }
    public String createShift(Date date, boolean timeOfDay)
    {
        return scheduler.createShift(date,timeOfDay);
    }
    public String editShift(Date date, boolean timeOfDay)
    {
        return scheduler.editShift(date,timeOfDay);
    }
    public ModelShift getCurrentEditedShift(){
        return new ModelShift(scheduler.getCurrentEditedShift());
    }
    public void cancelShift(){scheduler.cancelShift();}
    public String submitShift(){
        return scheduler.submitShift();
    }
    public String removePositionFromShift(String pos){
        return scheduler.removePositionToShift(pos);
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
        return scheduler.removeWorkerToPositionInShift(pos,id);
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
            output.add(new ModelShift(s));
        }
        return output;
    }





}
