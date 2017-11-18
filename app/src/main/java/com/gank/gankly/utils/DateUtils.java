package com.gank.gankly.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * 日期转换
 * Create by LingYan on 2016-04-28
 */
public class DateUtils {
    private static final String GMT_8 = "GMT+8";
    public static final String YY_MM_DD_SLASH = "yyyy/MM/dd";
    public static final String YY_MM_DD_HORIZONTAL = "yyyy-MM-dd";
    public static final String MM_DD = "MM-dd";
    public static final String YMD_S = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'";

    public static String formatString(final Date date, final String format) {
        if (null == date || TextUtils.isEmpty(format)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = getDateFormat(format);
        return getSimpleFormat(simpleDateFormat, date);
    }

    private static String getSimpleFormat(SimpleDateFormat simpleDateFormat, final Date date) {
        return simpleDateFormat == null ? "" : simpleDateFormat.format(date);
    }

    public static Date formatToDate(final String dateStr) {
        Date date = new Date();
        if (!TextUtils.isEmpty(dateStr)) {
            SimpleDateFormat dateFormat = getDateFormat(YMD_S);
            if (dateFormat != null) {
                try {
                    return dateFormat.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return date;
    }

    private static SimpleDateFormat getDateFormat(String format) {
        SimpleDateFormat dateFormat;
        try {
            TimeZone timeZone = TimeZone.getTimeZone(GMT_8);
            dateFormat = new SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE);
            dateFormat.setTimeZone(timeZone);
        } catch (Exception e) {
            dateFormat = null;
            Log.e("", e.toString());
        }
        return dateFormat;
    }
}
