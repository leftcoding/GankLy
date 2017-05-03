package com.gank.gankly.ui.base;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.gank.gankly.config.Constants;
import com.gank.gankly.config.Preferences;
import com.gank.gankly.data.DaoMaster;
import com.gank.gankly.rxjava.RxBus_;
import com.gank.gankly.utils.GanklyPreferences;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.smtt.sdk.QbSdk;

/**
 * Create by LingYan on 2016-09-18
 * Email:137387869@qq.com
 */
public class InitializeService extends IntentService {
    public static final String SERVICE_ACTION = "com.gank.gankly.ui.base.InitializeService";
    private static final String DB_NAME = "gank.db";
    private Context mContext;

    public InitializeService() {
        this(SERVICE_ACTION);
    }

    public InitializeService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && SERVICE_ACTION.equals(intent.getAction())) {
            initStart();
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(SERVICE_ACTION);
        context.startService(intent);
    }

    private void initStart() {
        //数据库Chrome上调试 -- start
        Stetho.initializeWithDefaults(getApplicationContext());
        //stetho -- end

        // GreenDao -- start
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        RxBus_.getInstance().post(db);
        // GreenDao end --

        //Bugly 测试：true -- start
        Beta.autoDownloadOnWifi = true;
        Beta.autoCheckUpgrade = GanklyPreferences.getBoolean(Preferences.SETTING_AUTO_CHECK, true);
        Bugly.init(getApplicationContext(), Constants.CRASH_LOG_ID, false);
        //Bugly -- end

        //x5 -- start
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
        //x5 -- end
    }
}
