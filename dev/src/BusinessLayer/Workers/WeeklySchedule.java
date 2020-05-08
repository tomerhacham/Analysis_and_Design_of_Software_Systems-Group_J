package BusinessLayer.Workers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeeklySchedule {
    private static final boolean morning=true;
    private static final boolean night=false;
    private DailySchedule[] shifts;
    Date dayStart;

    public WeeklySchedule(Date dayStart,boolean empty)
    {
        this.dayStart=dayStart;
    }

    public WeeklySchedule(Date dayStart) {
        this.dayStart = dayStart;
        shifts=new DailySchedule[7];
        for(int i=0;i<7;i++)
        {
            shifts[i]=new DailySchedule(DateManipulator.addDays(dayStart,i));
        }
    }

    public List<Shift> getShifts() {
        List<Shift>output=new ArrayList<>();
        for(DailySchedule d:shifts)
        {
            Shift s=d.getMorningShift();
            if(s==null)
                s=new EmptyShift(d.getDate(),morning);
            output.add(s);
            s=d.getNightShift();
            if(s==null)
                s=new EmptyShift(d.getDate(),night);
            output.add(s);
        }
        return output;
    }

    public Shift getShift(Date date,boolean timeOfDay)
    {
        DailySchedule day=getDay(date);
        if(day==null)
            return null;
        if(timeOfDay==morning)
            return day.getMorningShift();
        else
            return day.getNightShift();
    }

    public DailySchedule getDay(Date date)
    {
        if(date.before(shifts[0].getDate())||
                date.after(shifts[6].getDate()))
            return null;
        long dif=date.getTime()-shifts[0].getDate().getTime();
        int daysDifs=(int)dif/(24*60*60*1000);//getTime function returns the time in milliseconds
        return shifts[daysDifs];
    }

}
