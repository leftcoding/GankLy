package com.gank.gankly.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gank.gankly.App;
import com.gank.gankly.BuildConfig;
//import com.gank.gankly.BuildConfig;

/**
 * Create by LingYan on 2016-05-09
 */
public class StringUtils {
    public static final boolean DISABLE = Boolean.FALSE;
    public static final String POWERWORD = "powerword";

    public static final boolean hasChinese(String strName) {
        if (strName == null) {
            return DISABLE;
        }
        char[] ch = strName.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }
        return DISABLE;
    }

    public static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) ? true : DISABLE;
    }


    public static String getString(Context context, String columnName, String defValue) {
        try {
            return context.getSharedPreferences(POWERWORD, 0).getString(columnName, defValue);
        } catch (Exception e) {
            e.printStackTrace();
            return BuildConfig.pwd;
        }
    }


    public static void saveString(String columnName, String value) {
        saveString(App.getContext(), columnName, value);
    }

    public static void saveString(Context context, String columnName, String value) {
        try {
            SharedPreferences.Editor passfileEditor = context.getSharedPreferences(POWERWORD, 0).edit();
            passfileEditor.putString(columnName, value);
            passfileEditor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
