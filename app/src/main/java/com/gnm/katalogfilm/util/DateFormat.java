package com.gnm.katalogfilm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormat {

    private static String format(String date, String format) {
        String result = "";
        java.text.DateFormat old = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        try {
            Date oldDate = old.parse(date);
            java.text.DateFormat newFormat = new SimpleDateFormat(format);
            result = newFormat.format(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDate(String date) {
        return format(date, "dd-MM-yyyy");
    }

    public static String getDateDay(String date) {
        return format(date, "EEEE, dd MMM yyyy");
    }
}
