package com.gank.gankly.utils;

import com.socks.library.KLog;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Create by LingYan on 2016-07-12
 */
public class CrashUtils {
    public static void crashReport(Throwable e) {
        KLog.e(e);
        CrashReport.postCatchedException(e);
    }

    public static void crashReport(Throwable e, String str) {
        KLog.e(e);
        CrashReport.postCatchedException(new Throwable(str, e));
    }
}
