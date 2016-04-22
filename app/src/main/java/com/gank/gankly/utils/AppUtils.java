package com.gank.gankly.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

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
}
