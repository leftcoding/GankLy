package com.gank.gankly.utils.permission;

import android.os.Build;

/**
 * Created by Lingyan on 2017/3/22 0022.
 * Email:137387869@qq.com
 */

public class ActivityUtils {
    private ActivityUtils() {
    }

    public static boolean isRequest() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
