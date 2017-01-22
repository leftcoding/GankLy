package com.gank.gankly.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.gank.gankly.App;

/**
 * Create by LingYan on 2016-04-01
 */
public class ToastUtils {
    private static Toast mToast;

    private ToastUtils() {
    }

    public static void showToast(String msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resText) {
        show(resText, Toast.LENGTH_SHORT);
    }

    public static void longBottom(int resText) {
        show(resText, Toast.LENGTH_LONG, Gravity.BOTTOM);
    }

    public static void shortBottom(int resText) {
        show(resText, Toast.LENGTH_SHORT, Gravity.BOTTOM);
    }

    public static void longToast(int resText) {
        show(resText, Toast.LENGTH_LONG);
    }

    private static void show(String msg, int duration) {
        cancel();
        mToast = Toast.makeText(App.getGankContext(), msg, duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    private static void show(int resText, int duration) {
        cancel();
        mToast = Toast.makeText(App.getGankContext(), resText, duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    private static void show(int resText, int duration, int gravity) {
        cancel();
        mToast = Toast.makeText(App.getGankContext(), resText, duration);
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
