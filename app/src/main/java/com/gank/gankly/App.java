package com.gank.gankly;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.Stetho;
import com.gank.gankly.config.Preferences;
import com.gank.gankly.data.DaoMaster;
import com.gank.gankly.data.DaoSession;
import com.gank.gankly.utils.GanklyPreferences;
import com.socks.library.KLog;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Create by LingYan on 2016-04-01
 */
public class App extends Application {
    private static final int PREFERENCES_VERSION = 1;
    private static final String DB_NAME = "gank.db";
    public static boolean isNewVersion;

    public static Context mContext;
    private static SQLiteDatabase db;

    private static boolean isDayNight;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        initPreferences();

        //数据库Chrome上调试
        Stetho.initializeWithDefaults(this);

        //GreenDao
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME, null);
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        //Bugly 测试：true
        CrashReport.initCrashReport(getApplicationContext(), "900039150", true);

    }

    private static DaoSession daoSession;

    private void initPreferences() {
        int version = GanklyPreferences.getInt(Preferences.APP_VERSION, 1);
        KLog.d("version:" + version);
        if (version < PREFERENCES_VERSION) {
            GanklyPreferences.clear();
            GanklyPreferences.putInt(Preferences.APP_VERSION, PREFERENCES_VERSION);
        }
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

    public static String getAppString(int res) {
        return getAppResources().getString(res);
    }

    public static SQLiteDatabase getDatabase() {
        return db;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static boolean isNewVersion() {
        return isNewVersion;
    }

    public static void setNewVersion(boolean newVersion) {
        isNewVersion = newVersion;
    }

    public static boolean isDayNight() {
        return isDayNight;
    }

    public static void setIsDayNight(boolean isDayNight) {
        App.isDayNight = isDayNight;
    }
}
