package com.gank.gankly.ui.base;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.Stetho;
import com.gank.gankly.RxBus.RxBus_;
import com.gank.gankly.data.DaoMaster;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

/**
 * Create by LingYan on 2016-09-18
 * Email:137387869@qq.com
 */
public class InitializeService extends IntentService {
    public static final String SERVICE_ACTION = "com.gank.gankly.ui.base.InitializeService";
    private static final String DB_NAME = "gank.db";
    private static final String CRASH_LOG_ID = "900039150";
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
        CrashReport.initCrashReport(getApplicationContext(), CRASH_LOG_ID, true);
        //Bugly -- end

        //x5 -- start
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        //TbsDownloader.needDownload(getApplicationContext(), false);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
            }

            @Override
            public void onInstallFinish(int i) {
            }

            @Override
            public void onDownloadProgress(int i) {
            }
        });

        QbSdk.initX5Environment(getApplicationContext(), cb);
        //x5 -- end
    }
}
