package com.gank.gankly.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.gank.gankly.App;


/**
 * Create by LingYan on 2016-06-01
 */
public class GanklyPreferences {
    private static final String PREFERENCES_NAME_DEFAULT = "gankly_config";
    private static final int PERFERENCES_VERSION = 1;

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
}
