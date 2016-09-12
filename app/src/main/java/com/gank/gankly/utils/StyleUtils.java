package com.gank.gankly.utils;

import android.support.v4.widget.SwipeRefreshLayout;

import com.gank.gankly.App;
import com.gank.gankly.R;

/**
 * Create by LingYan on 2016-09-12
 * Email:137387869@qq.com
 */
public class StyleUtils {
    public static void changeSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout == null) {
            throw new RuntimeException("swipeRefreshLayout not be null");
        }

        int progressColor = R.color.white;
        int schemeColor = R.color.colorAccent;

        if (App.isNight()) {
            progressColor = R.color.baseSwipeRefreshLayoutProgressSchemeColor;
            schemeColor = R.color.baseSwipeRefreshLayoutSchemeColors;
        }

        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(App.getAppColor(progressColor));
        swipeRefreshLayout.setColorSchemeColors(App.getAppColor(schemeColor));
    }
}
