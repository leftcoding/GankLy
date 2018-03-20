package com.gank.gankly;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import com.gank.gankly.config.Preferences;
import com.gank.gankly.data.DaoMaster;
import com.gank.gankly.data.DaoSession;
import com.gank.gankly.ui.base.InitializeService;
import com.gank.gankly.ui.more.SettingFragment;
import com.gank.gankly.utils.GanklyPreferences;
import com.gank.gankly.utils.NetworkUtils;
import com.leftcoding.http.base.GankServerHelper;
import com.leftcoding.http.base.MyGsonConverterFactory;
import com.leftcoding.http.intercept.HttpLogging;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import okhttp3.logging.HttpLoggingInterceptor;

import static de.greenrobot.dao.test.DbTest.DB_NAME;

/**
 * Create by LingYan on 2016-04-01
 * Email:137387869@qq.com
 */
public class AppConfig extends Application {
    private static final int PREFERENCES_VERSION = 1;
    private static Context mContext;
    private static DaoSession daoSession;
    private static boolean isNight;

    private RefWatcher mRefWatcher;

    public AppConfig() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        // leakCanary -- start
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        mRefWatcher = LeakCanary.install(this);
        // leakCanary -- end

        GankServerHelper.get()
                .addNetworkInterceptor(HttpLogging.get().setLevel(HttpLoggingInterceptor.Level.HEADERS).build())
                .addConverterFactory(MyGsonConverterFactory.create());

        initPreferences();

        InitializeService.start(getApplicationContext());

        // GreenDao -- start
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        // GreenDao -- end
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
        AppConfig.isNight = isNight;
    }

    public static boolean isNetConnect() {
        return NetworkUtils.isNetworkAvailable();
    }

    public RefWatcher getRefWatcher() {
        return mRefWatcher;
    }
}
