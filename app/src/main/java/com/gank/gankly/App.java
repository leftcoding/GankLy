package com.gank.gankly;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import com.gank.gankly.config.Preferences;
import com.gank.gankly.data.DaoMaster;
import com.gank.gankly.data.DaoSession;
import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.ui.base.InitializeService;
import com.gank.gankly.ui.more.SettingFragment;
import com.gank.gankly.utils.GanklyPreferences;
import com.gank.gankly.utils.NetworkUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Create by LingYan on 2016-04-01
 * Email:137387869@qq.com
 */
public class App extends Application {
    private static final int PREFERENCES_VERSION = 1;
    private static Context mContext;
    private static DaoSession daoSession;
    private static boolean isNight;

    private RefWatcher mRefWatcher;

    public App() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        // leakCanary -- start
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        mRefWatcher = LeakCanary.install(this);
        // leakCanary -- end

        initPreferences();

        InitializeService.start(getApplicationContext());

        RxBus_.getInstance().toObservable(SQLiteDatabase.class)
                .subscribe(sqLiteDatabase -> {
                    if (sqLiteDatabase != null) {
                        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
                        daoSession = daoMaster.newSession();
                    }
                });
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

    public static Context getGankContext() {
        return mContext.getApplicationContext();
    }

    public static Resources getAppResources() {
        return getGankContext().getResources();
    }

    public static int getAppColor(int id) {
        return getAppResources().getColor(id);
    }

    public static String getAppString(int res) {
        return getAppResources().getString(res);
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static boolean isNight() {
        return isNight;
    }

    public static void setIsNight(boolean isNight) {
        App.isNight = isNight;
    }

    public static boolean isNetConnect() {
        return NetworkUtils.isNetworkAvailable();
    }

    public RefWatcher getRefWatcher() {
        return mRefWatcher;
    }
}
