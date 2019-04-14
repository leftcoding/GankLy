package com.gank.gankly.utils.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lingyan on 2017/3/22 0022.
 */

public class PermissionUtils {
    private static final String TAG = "PermissionUtils";
    private static final int REQUEST_CODE = 1;

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

    public static void shouldShowRequestPermission(Activity activity, String permission) {
        if (ActivityUtils.isRequestPermission()) {
            String[] strings = new String[]{permission};
            KLog.e(strings[0]);
            boolean hasPermission = hasPermission(activity, permission);
            if (hasPermission) {
                boolean isShould = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                if (!isShould) {
                    new AlertDialog.Builder(activity)
                            .setPositiveButton("设置", (dialog, which) -> {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + activity.getPackageName())); // 根据包名打开对应的设置界面
                                activity.startActivity(intent);
                            })
                            .setNegativeButton("拒绝", (dialog, which) -> dialog.cancel())
                            .setCancelable(false)
                            .setMessage("是否设置存储权限，拒绝将导致某些功能无法使用")
                            .show();
                } else {
                    requestPermissionGranted(activity, new String[]{permission}, 0);
                }
            }
        }
    }

    public static synchronized void requestAllPermissions(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }

        if (ActivityUtils.isRequestPermission()) {
            String[] perms = getManifestPermissions(activity);
            if (perms.length >= 1) {
                requestPermissionGranted(activity, perms, REQUEST_CODE);
            }
        }
    }

    public static synchronized String[] getManifestPermissions(
            @NonNull final Activity activity) {
        PackageInfo packageInfo = null;
        List<String> list = new ArrayList<>();
        try {
            packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            KLog.e(TAG, "A problem occurred when retrieving permissions", e);
        }
        if (packageInfo != null) {
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions != null) {
                for (String perm : permissions) {
                    boolean isPermission = hasPermission(activity, perm);
                    if (isPermission) {
                        list.add(perm);
                    }
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }

    private static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }

    public static void requestPermissionGranted(Activity activity, @NonNull String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
}
