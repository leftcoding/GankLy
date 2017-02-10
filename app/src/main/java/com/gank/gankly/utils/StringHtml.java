package com.gank.gankly.utils;

import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * 整体字符串修改
 * Create by LingYan on 2016-5-10
 */
public class StringHtml {
    /**
     * 修改字符串中，部分字符颜色
     *
     * @param str            前半部分字符
     * @param str2           后半部分字符
     * @param changeStr      中间修改字符
     * @param changeStrColor 修改字符颜色 ps:#000000
     * @return 修改后字符串
     */
    public static Spanned getString(String str, String str2, String changeStr,
                                    String changeStrColor) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            return Html.fromHtml(str + "<font color=\"" + changeStrColor + "\">"
                    + changeStr + "</font>" + str2);
        } else {
            return Html.fromHtml(str + "<font color=\"" + changeStrColor + "\">"
                    + changeStr + "</font>" + str2, Html.FROM_HTML_MODE_LEGACY);
        }
    }

    /**
     * 修改字符串中，部分字符颜色、大小
     *
     * @param str            前半部分字符
     * @param str2           后半部分字符
     * @param changeStr      中间修改字符
     * @param changeStrColor 中间修改字符颜色
     * @param textSize       中间修改字符大小
     * @return 修改后字符串
     */
    public static Spanned getString(String str, String str2, String changeStr,
                                    String changeStrColor, int textSize) {
        SpannableString msp = new SpannableString(str + changeStr + str2);
        msp.setSpan(new ForegroundColorSpan(Color.parseColor(changeStrColor)),
                str.length(), str.length() + changeStr.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        msp.setSpan(new AbsoluteSizeSpan(textSize, true), str.length(),
                str.length() + changeStr.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    /**
     * 修改字符串中，部分字符大小，并改变颜色
     *
     * @param str            需要修改字符大小，前半部分字符
     * @param str2           后半部分字符
     * @param changeStr      中间部分字符
     * @param changeStrColor 修改字符串的颜色
     * @param textSize       修改字体的大小
     * @return 返回修改后的字符串
     */
    public static Spanned getStringSize(String str, String str2, String changeStr,
                                        String changeStrColor, int textSize) {
        SpannableString msp = new SpannableString(str + changeStr + str2);
        msp.setSpan(new ForegroundColorSpan(Color.parseColor(changeStrColor)),
                str.length(), str.length() + changeStr.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        msp.setSpan(new AbsoluteSizeSpan(textSize, true), 0,
                str.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }
}