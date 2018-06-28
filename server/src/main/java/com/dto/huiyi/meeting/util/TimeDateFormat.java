package com.dto.huiyi.meeting.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeDateFormat {

    private static SimpleDateFormat activitiTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static String formatTime(Date d){
        if(d != null)
            return activitiTimeFormat.format(d);
        else
            return null;
    }

    public static String formatCalendar(Date d){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
        return format.format(d);
    }

    public static String formatToDay(Date d){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(d);
    }
}
