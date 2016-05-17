package com.gank.gankly.utils;

import android.text.TextUtils;

/**
 * Create by LingYan on 2016-05-17
 */
public class StringUtils {
    public static String getSuffixImageName(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        String[] strings = url.split("/");
        int size = strings.length;
        return strings[size - 1];
    }
}
