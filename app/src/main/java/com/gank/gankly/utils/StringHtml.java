package com.gank.gankly.utils;

import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * Create by LingYan on 2016-5-10
 */
public class StringHtml {
    public static Spanned getString(String str, String str2, String changeStr,
                                    String changeStrColor) {
        return Html.fromHtml(str + "<font color=\"" + changeStrColor + "\">"
                + changeStr + "</font>" + str2);
    }

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
}