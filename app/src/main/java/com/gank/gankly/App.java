package com.gank.gankly;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gank.gankly.RxBus.RxBus;
import com.gank.gankly.config.Preferences;
import com.gank.gankly.data.DaoMaster;
import com.gank.gankly.data.DaoSession;
import com.gank.gankly.ui.base.InitializeService;
import com.gank.gankly.ui.more.SettingFragment;
import com.gank.gankly.utils.GanklyPreferences;
import com.gank.gankly.utils.NetworkUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

/**
 * Create by LingYan on 2016-04-01
 * Email:137387869@qq.com
 */
public class App extends Application {
    private static final int PREFERENCES_VERSION = 1;

    private static boolean isNewVersion;
    private static Context mContext;
    private static DaoSession daoSession;

    private static boolean isNight;
    private static RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        // leakCanary -- start
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);
        // leakCanary -- end

        //x5 -- start
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        //TbsDownloader.needDownload(getApplicationContext(), false);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                Log.e("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d("app", "onDownloadFinish");
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d("app", "onInstallFinish");
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d("app", "onDownloadProgress:" + i);
            }
        });

        QbSdk.initX5Environment(getApplicationContext(), cb);
        //x5 -- end

        InitializeService.start(mContext);
        initPreferences();
        RxBus.getInstance().toObservable(SQLiteDatabase.class)
                .subscribe(sqLiteDatabase -> {
                    if (sqLiteDatabase != null) {
                        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
                        daoSession = daoMaster.newSession();
                    }
                });


    }

    public static RefWatcher getRefWatcher() {
        return refWatcher;
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
        return App.mContext;
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

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static boolean isNewVersion() {
        return isNewVersion;
    }

    public static boolean isNight() {
        return isNight;
    }

    public static void setIsNight(boolean isNight) {
        App.isNight = isNight;
    }

    public static boolean isNetConnect() {
        return NetworkUtils.isNetworkAvailable(getGankContext());
    }
}
