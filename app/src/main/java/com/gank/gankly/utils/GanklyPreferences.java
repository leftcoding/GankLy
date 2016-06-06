package com.gank.gankly.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gank.gankly.App;

import javax.annotation.Nullable;


/**
 * Create by LingYan on 2016-06-01
 */
public class GanklyPreferences {
    private static final String PREFERENCES_NAME_DEFAULT = "gankly_config";

    public static SharedPreferences getDefaultPreference() {
        return initPreference(PREFERENCES_NAME_DEFAULT);
    }

    public static SharedPreferences.Editor getDefaultEditor() {
        return initPreference(PREFERENCES_NAME_DEFAULT).edit();
    }

    public static SharedPreferences getPreference(String name) {
        return initPreference(name);
    }

    public static SharedPreferences initPreference(String name) {
        return App.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static void clear() {
        getDefaultPreference().edit().clear().commit();
    }

    public static int getInt(String key, int defValue) {
        return getDefaultPreference().getInt(key, defValue);
    }

    public static long getLong(String key, int defValue) {
        return getDefaultPreference().getLong(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getDefaultPreference().getBoolean(key, defValue);
    }

    @Nullable
    public static String getString(String key, @Nullable String defValue) {
        return getDefaultPreference().getString(key, defValue);
    }

    public static void putInt(String key, int value) {
        getDefaultEditor().putInt(key, value).apply();
    }

    public static void putLong(String key, long value) {
        getDefaultEditor().putLong(key, value).apply();
    }

    public static void putBoolean(String key, boolean value) {
        getDefaultEditor().putBoolean(key, value).apply();
    }

    public static void putString(String key, @Nullable String value) {
        getDefaultEditor().putString(key, value).apply();
    }


}
