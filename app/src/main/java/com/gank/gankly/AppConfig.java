package com.gank.gankly;

import android.app.Application;
import android.ly.business.api.GankServerHelper;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.gank.gankly.config.Constants;
import com.gank.gankly.config.HttpUrlConfig;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.smtt.sdk.QbSdk;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Create by LingYan on 2016-04-01
 */
public class AppConfig extends Application {

    public AppConfig() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupStetho();
        setupX5WebView();

        GankServerHelper.init(AppConfig.this)
                .baseUrl(HttpUrlConfig.GANK_URL)
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

    }

    /**
     * 数据库Chrome上调试
     */
    private void setupStetho() {
        Stetho.initializeWithDefaults(AppConfig.this);
    }

    /**
     * Bugly 测试
     */
    private void setupBugly() {
        Beta.autoDownloadOnWifi = true;
//        Beta.autoCheckUpgrade = GanklyPreferences.getBoolean(Preferences.SETTING_AUTO_CHECK, true);
        Bugly.init(AppConfig.this, Constants.CRASH_LOG_ID, false);
    }

    /**
     * x5 搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
     */
    private void setupX5WebView() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(AppConfig.this, cb);
    }

    /**
     * 内存监视
     */
    private void setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
