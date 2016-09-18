package com.gank.gankly.ui.base;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.Stetho;
import com.gank.gankly.RxBus.RxBus;
import com.gank.gankly.data.DaoMaster;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Create by LingYan on 2016-09-18
 * Email:137387869@qq.com
 */
public class InitializeService extends IntentService {
    public static final String SERVICE_ACTION = "com.gank.gankly.ui.base.InitializeService";
    private static final String DB_NAME = "gank.db";
    private static final String CRASH_LOG_ID = "900039150";

    public InitializeService() {
        this(SERVICE_ACTION);
    }

    public InitializeService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (SERVICE_ACTION.equals(intent.getAction())) {
                initStart();
            }
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(SERVICE_ACTION);
        context.startService(intent);
    }

    private void initStart() {
        //数据库Chrome上调试
        Stetho.initializeWithDefaults(getApplicationContext());

        //GreenDao
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        RxBus.getInstance().post(db);

        //Bugly 测试：true
        CrashReport.initCrashReport(getApplicationContext(), CRASH_LOG_ID, true);
    }
}
