package com.gank.gankly.utils;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Create by LingYan on 2016-07-12
 */
public class CrashUtils {
    public static void crashReport(Throwable e) {
        CrashReport.postCatchedException(e);
    }

    public static void crashReport(Throwable e, String str) {
        CrashReport.postCatchedException(new Throwable(str, e));
    }
}
