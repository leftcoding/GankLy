package com.gank.gankly.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Lingyan on 2017/3/22 0022.
 * Email:137387869@qq.com
 */

public class PermissionUtils {
    private static PermissionUtils sPermissionUtils;

    public static PermissionUtils getInstance() {
        if (sPermissionUtils == null) {
            synchronized (PermissionUtils.class) {
                if (sPermissionUtils == null) {
                    sPermissionUtils = new PermissionUtils();
                }
            }
        }
        return sPermissionUtils;
    }


    public static void requestAllPermisson(Context context) {

    }

    private static synchronized String[] getManifestPermissions(@NonNull final Activity activity) {
        PackageInfo packageInfo = null;
        List<String> list = new ArrayList<>(1);
        try {
            KLog.d(TAG, activity.getPackageName());
            packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            KLog.e(TAG, "A problem occurred when retrieving permissions", e);
        }
        if (packageInfo != null) {
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions != null) {
                for (String perm : permissions) {
                    KLog.d(TAG, "Manifest contained permission: " + perm);
                    list.add(perm);
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }

}
