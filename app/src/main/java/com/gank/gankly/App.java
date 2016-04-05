package com.gank.gankly;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Create by LingYan on 2016-04-01
 */
public class App extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext.getApplicationContext();
    }

    public static Resources getAppResources() {
        return mContext.getApplicationContext().getResources();
    }

    public static int getAppColor(int id) {
        return mContext.getApplicationContext().getResources().getColor(id);
    }


}
