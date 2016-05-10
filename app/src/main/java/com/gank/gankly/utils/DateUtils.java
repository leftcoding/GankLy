package com.gank.gankly.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Create by LingYan on 2016-04-28
 */
public class DateUtils {
    public static final String TYPE_ONE = "yyyy/MM/dd";
    public static final String TYPE_TWO = "yyyy-MM-dd";


    public static String getFormatDateStr(final Date date) {
        if (null == date) {
            return null;
        }
        return DateFormat.getDateInstance(DateFormat.DEFAULT).format(date);
    }

    public static String getFormatDate(final Date date, String type) {
        if (null == date || TextUtils.isEmpty(type)) {
            return null;
        }
        SimpleDateFormat sdf;
        try {
            sdf = new SimpleDateFormat(type, Locale.getDefault());
        } catch (Exception e) {
//            throw new RuntimeException(e);
            sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
        return sdf.format(date);
    }

    public static Date formatDateFromStr(final String dateStr) {
        Date date = new Date();
        if (!TextUtils.isEmpty(dateStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'", Locale.getDefault());
            try {
                date = sdf.parse(dateStr);
            } catch (Exception e) {
                System.out.print("Error,format Date error");
            }
        }
        return date;
    }

    public static String getMonth(final Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return String.valueOf(calendar.get(Calendar.MONTH) + 1);
        }
        return "0";
    }

    public static String getDay(final Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return String.valueOf(calendar.get(Calendar.DATE));
        }
        return "0";
    }

}
