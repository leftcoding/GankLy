package com.gank.gankly.utils;

/**
 * Create by LingYan on 2016-04-21
 */
public class AppUtils {
    public static void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.gc();
        System.runFinalization();
    }
}
