package com.gank.gankly.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期转换
 * Create by LingYan on 2016-04-28
 */
public class DateUtils {
    public static final String TYPE_ONE = "yyyy/MM/dd";
    public static final String TYPE_TWO = "yyyy-MM-dd";
    public static final String TYPE_DD = "MM-dd";


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
            sdf = new SimpleDateFormat(type, Locale.SIMPLIFIED_CHINESE);
        } catch (Exception e) {
            sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        }
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    public static Date formatDateFromStr(final String dateStr) {
        Date date = new Date();
        if (!TextUtils.isEmpty(dateStr)) {
            String format = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE);
            TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
            sdf.setTimeZone(timeZone);
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
            TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
            calendar.setTimeZone(timeZone);
            calendar.setTime(date);
            return String.valueOf(calendar.get(Calendar.MONTH) + 1);
        }
        return "0";
    }

    public static String getDay(final Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
            calendar.setTimeZone(timeZone);
            calendar.setTime(date);
            return String.valueOf(calendar.get(Calendar.DATE));
        }
        return "0";
    }

}
