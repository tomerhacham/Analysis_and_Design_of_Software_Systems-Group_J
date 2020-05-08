package BusinessLayer.Workers;
import java.util.Date;

public class DailySchedule {
    private Shift morningShift;
    private Shift nightShift;
    private Date date;

    public DailySchedule(Date time) {
        this.date = time;
    }

    public Date getDate() {
        return date;
    }

    public Shift getMorningShift() {
        return morningShift;
    }

    public Shift getNightShift() {
        return nightShift;
    }

    public void setMorningShift(Shift morningShift) {
        this.morningShift = morningShift;
    }

    public void setNightShift(Shift nightShift) {
        this.nightShift = nightShift;
    }
}
