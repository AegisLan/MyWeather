package com.aegisLan.weather.util;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by AegisLan on 2016.1.26.
 */
public class CalendarTools {
    public static String getDayOfWeekByDateTime(String dateTime) {
        String week = "星期";
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(format.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                week += "日";break;
            case 2:
                week += "一";break;
            case 3:
                week += "二";break;
            case 4:
                week += "三";break;
            case 5:
                week += "四";break;
            case 6:
                week += "五";break;
            case 7:
                week += "六";break;
        }
        return week;
    }
    public static boolean isToday(String dateTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(format.parse(dateTime));
            int inputYear = c.get(Calendar.YEAR);
            int inputMonth = c.get(Calendar.MONTH);
            int inputDay = c.get(Calendar.DAY_OF_MONTH);
            if(inputDay == currentDay && currentMonth == inputMonth && currentYear == inputYear) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
