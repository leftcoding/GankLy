package com.gank.gankly.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.gank.gankly.App;

/**
 * Create by LingYan on 2016-04-01
 */
public class ToastUtils {
    private static Toast mToast;

    public ToastUtils() {

    }

    public static void showToast(String msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resText) {
        show(resText, Toast.LENGTH_SHORT);
    }

    private static void show(String msg, int duration) {
        cancel();
        mToast = Toast.makeText(App.getContext(), msg, duration);
        mToast.setGravity(Gravity.BOTTOM | Gravity.START, 0, 0);
        mToast.show();
    }

    private static void show(int resText, int duration) {
        cancel();
        mToast = Toast.makeText(App.getContext(), resText, duration);
        mToast.setGravity(Gravity.BOTTOM | Gravity.START, 0, 0);
        mToast.show();
    }


    private static void cancel() {
//        check();
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }


}
