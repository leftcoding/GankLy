package com.gank.gankly.utils.theme;

import android.view.View;

/**
 * Create by LingYan on 2016-11-25
 */

public class ViewResorce {
    private View mView;
    private int mResId;

    public ViewResorce(int resId, View view) {
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
