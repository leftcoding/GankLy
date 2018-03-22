package com.gank.gankly.utils;

import android.content.Context;
import android.content.SharedPreferences;

import javax.annotation.Nullable;


/**
 * Create by LingYan on 2016-06-01
 */
public class GanklyPreferences {
    private static final String PREFERENCES_NAME_DEFAULT = "gankly_config";
    private volatile static SharedPreferences mSharedPreferences;
    private volatile static SharedPreferences.Editor sEditor;

    public static SharedPreferences getDefaultPreference(Context context) {
        return initPreference(context, PREFERENCES_NAME_DEFAULT);
    }

    public static SharedPreferences.Editor getDefaultEditor(Context context) {
        if (mSharedPreferences == null) {
            initPreference(context, PREFERENCES_NAME_DEFAULT);
        }

        if (sEditor == null) {
            synchronized (GanklyPreferences.class) {
                if (sEditor == null) {
                    sEditor = mSharedPreferences.edit();
                }
            }
        }
        return sEditor;
    }

    public static SharedPreferences getPreference(Context context, String name) {
        return initPreference(context, name);
    }

    public static SharedPreferences initPreference(Context context, String name) {
        if (mSharedPreferences == null) {
            synchronized (GanklyPreferences.class) {
                if (mSharedPreferences == null) {
                    mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
                }
            }
        }
        return mSharedPreferences;
    }

    public static void clear(Context context) {
        getDefaultPreference(context).edit().clear().commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        return getDefaultPreference(context).getInt(key, defValue);
    }

    public static long getLong(Context context, String key, int defValue) {
        return getDefaultPreference(context).getLong(key, defValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getDefaultPreference(context).getBoolean(key, defValue);
    }

    @Nullable
    public static String getString(Context context, String key, @Nullable String defValue) {
        return getDefaultPreference(context).getString(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        getDefaultEditor(context).putInt(key, value).apply();
    }

    public static void putLong(Context context, String key, long value) {
        getDefaultEditor(context).putLong(key, value).apply();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getDefaultEditor(context).putBoolean(key, value).apply();
    }

    public static void putString(Context context, String key, @Nullable String value) {
        getDefaultEditor(context).putString(key, value).apply();
    }
}
