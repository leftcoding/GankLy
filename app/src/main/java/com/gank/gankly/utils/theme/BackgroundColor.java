package com.gank.gankly.utils.theme;

import android.view.View;

/**
 * Create by LingYan on 2016-11-25
 * Email:137387869@qq.com
 */

public class BackgroundColor {
    private View mView;
    private int mResId;

    public BackgroundColor(int resId, View view) {
        mView = view;
        mResId = resId;
    }

    public View getBackGroundView() {
        return mView;
    }

    public int getResId() {
        return mResId;
    }
}
