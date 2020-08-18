package com.phoebus.appdemowallet.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String formatDate (String date, String initDateFormat, String endDateFormat){
        String parsedDate = "";
        try {
            Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
            parsedDate = formatter.format(initDate);
            return parsedDate;
        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }
        return parsedDate;
    }
}
