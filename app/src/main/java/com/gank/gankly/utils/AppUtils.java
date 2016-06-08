package com.gank.gankly.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

import com.gank.gankly.App;
import com.socks.library.KLog;

import java.util.List;

/**
 * Create by LingYan on 2016-04-21
 */
public class AppUtils {

    public static void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.gc();
        System.runFinalization();
    }

    public static boolean isInstalledApk(Context context, String name) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void copyText(Context context, String text) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(
                Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("GankLy_copy", text);
        manager.setPrimaryClip(clipData);
    }

    public static int getVersionCode(Context context) {
        int currentVersionCode = 0;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
//            String appVersionName = info.versionName;
            currentVersionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            KLog.e(e);
        }
        return currentVersionCode;
    }

    public static String getVersionName(Context context) {
        String appVersionName = "";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            KLog.e(e);
        }
        return appVersionName;
    }

    public static int getDisplayWidth() {
        DisplayMetrics dm = App.getContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
}
