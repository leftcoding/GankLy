package com.gank.gankly.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Create by LingYan on 2016-04-01
 */
public class ToastUtils {
    private static Toast mToast;

    private ToastUtils() {
    }

    public static void showToast(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int resText) {
        show(context, resText, Toast.LENGTH_SHORT);
    }

    public static void longBottom(Context context, int resText) {
        show(context, resText, Toast.LENGTH_LONG, Gravity.BOTTOM);
    }

    public static void shortBottom(Context context, int resText) {
        show(context, resText, Toast.LENGTH_SHORT, Gravity.BOTTOM);
    }

    public static void longToast(Context context, int resText) {
        show(context, resText, Toast.LENGTH_LONG);
    }

    private static void show(Context context, String msg, int duration) {
        cancel();
        mToast = Toast.makeText(context.getApplicationContext(), msg, duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    private static void show(Context context, int resText, int duration) {
        cancel();
        mToast = Toast.makeText(context.getApplicationContext(), resText, duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    private static void show(Context context, int resText, int duration, int gravity) {
        cancel();
        mToast = Toast.makeText(context.getApplicationContext(), resText, duration);
        mToast.setGravity(gravity, 0, 100);
        mToast.show();
    }


    private static void cancel() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
