package com.company.BussinesLayer;

import java.util.Calendar;
import java.util.Date;

public class DateManipulator {

    // Add days to a date in Java
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
    public static Date getFirstDayOfWeek(Date date)
    {
        Calendar cal =Calendar.getInstance();
        cal.setTime(date);
        return addDays(date,-cal.get(Calendar.DAY_OF_WEEK)+1);
    }

    // Add months to a date in Java
    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    // Add years to a date in Java
    public static Date addYears(Date date, int years) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }
}