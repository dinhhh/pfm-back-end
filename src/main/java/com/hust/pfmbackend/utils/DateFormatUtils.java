package com.hust.pfmbackend.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtils {

    public static String convertToString(Date date, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        String result = df.format(date);
        return result;
    }

}
