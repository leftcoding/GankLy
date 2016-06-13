package com.gank.gankly.utils;

import java.util.regex.Pattern;

/**
 * Create by LingYan on 2016-05-17
 */
public class StringUtils {

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
