package com.gank.gankly;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import com.gank.gankly.RxBus.RxBus;
import com.gank.gankly.config.Preferences;
import com.gank.gankly.data.DaoMaster;
import com.gank.gankly.data.DaoSession;
import com.gank.gankly.ui.base.InitializeService;
import com.gank.gankly.ui.main.SettingFragment;
import com.gank.gankly.utils.GanklyPreferences;
import com.socks.library.KLog;

import rx.functions.Action1;

/**
 * Create by LingYan on 2016-04-01
 * Email:137387869@qq.com
 */
public class App extends Application {
    private static final int PREFERENCES_VERSION = 1;

    public static boolean isNewVersion;
    public static Context mContext;
    private static DaoSession daoSession;

    private static boolean isNight;

    @Override
    public void onCreate() {
        long start = System.currentTimeMillis();
        super.onCreate();
        mContext = this;

        InitializeService.start(this);

        initPreferences();

//        //数据库Chrome上调试
//        Stetho.initializeWithDefaults(this);
//
//        //GreenDao
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME, null);
//        db = helper.getWritableDatabase();
//        DaoMaster daoMaster = new DaoMaster(db);
//        daoSession = daoMaster.newSession();
//
//        //Bugly 测试：true
//        CrashReport.initCrashReport(getApplicationContext(), "900039150", true);

        RxBus.getInstance().toSubscription(SQLiteDatabase.class, new Action1<SQLiteDatabase>() {
            @Override
            public void call(SQLiteDatabase sqLiteDatabase) {
                if (sqLiteDatabase != null) {
                    DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
                    daoSession = daoMaster.newSession();
                }
            }
        });
        long e = System.currentTimeMillis() - start;
        KLog.d("s-e:" + e);
    }

    private void initPreferences() {
        int version = GanklyPreferences.getInt(Preferences.APP_VERSION, 1);

        if (version < PREFERENCES_VERSION) {
            GanklyPreferences.clear();
            GanklyPreferences.putInt(Preferences.APP_VERSION, PREFERENCES_VERSION);
        }

        isNight = GanklyPreferences.getBoolean(SettingFragment.IS_NIGHT, false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
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

//    public static SQLiteDatabase getDatabase() {
//        return db;
//    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static boolean isNewVersion() {
        return isNewVersion;
    }

    public static void setNewVersion(boolean newVersion) {
        isNewVersion = newVersion;
    }

    public static boolean isNight() {
        return isNight;
    }

    public static void setIsNight(boolean isNight) {
        App.isNight = isNight;
    }
}
