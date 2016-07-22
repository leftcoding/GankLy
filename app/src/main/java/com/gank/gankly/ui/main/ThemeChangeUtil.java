package com.gank.gankly.ui.main;

import android.app.Activity;

import com.gank.gankly.R;

public class ThemeChangeUtil {
    public static boolean isChange = false;

    public static void changeTheme(Activity activity) {
        if (isChange) {
            activity.setTheme(R.style.ThemeNight);
        } else {
            activity.setTheme(R.style.ThemeLight);
        }
    }
}