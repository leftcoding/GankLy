package com.gank.gankly.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Create by LingYan on 2016-6-3
 */
public class NetworkUtils {

    /**
     * 判断网络连接是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        return (context.getSystemService(Context.CONNECTIVITY_SERVICE) !=
                null) && ((
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null);
    }

    /**
     * 判断WiFi是否打开
     *
     * @param context context
     * @return boolean
     */
    public static boolean isWiFiEnabled(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);
        return ((cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo()
                .getState() ==
                NetworkInfo.State.CONNECTED) || tm != null && tm.getNetworkType() ==
                TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断是否是移动网络
     */
    public static boolean isMobileNetwork(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * 判断是否是WiFi
     */
    public static boolean isWiFi(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }
}