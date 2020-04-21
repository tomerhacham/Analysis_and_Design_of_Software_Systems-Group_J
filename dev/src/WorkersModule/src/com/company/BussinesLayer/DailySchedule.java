package com.company.BussinesLayer;

import java.util.Date;

public class DailySchedule {
    private Shift morningShift;
    private Shift nightShift;
    private Date date;

    public Date getDate() {
        return date;
    }

    public DailySchedule(Date time) {
        this.date = time;
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
