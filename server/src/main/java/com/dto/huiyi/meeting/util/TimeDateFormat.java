package com.dto.huiyi.meeting.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDateFormat {

    private static SimpleDateFormat activitiTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static String formatTime(Date d){
        if(d != null)
            return activitiTimeFormat.format(d);
        else
            return null;
    }
}
