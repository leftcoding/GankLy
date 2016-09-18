/**
 * DisplayUtils.java
 * <p/>
 * Created by xuanzhui on 2015/9/10.
 * Copyright (c) 2015 BeeCloud. All rights reserved.
 */
package com.gank.gankly.utils;

import android.content.Context;

import com.gank.gankly.App;

public class DisplayUtils {


    public static float px2dp(final float px) {
        return px / App.getAppResources().getDisplayMetrics().density;
    }

    public static float dp2px(final float dp) {
        return dp * App.getAppResources().getDisplayMetrics().density;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
