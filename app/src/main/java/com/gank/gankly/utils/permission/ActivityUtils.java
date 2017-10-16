package com.gank.gankly.utils.permission;

import android.os.Build;

/**
 * Created by Lingyan on 2017/3/22 0022.
 */

public class ActivityUtils {
    private ActivityUtils() {
    }

    public static boolean isRequestPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
