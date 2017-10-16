package com.gank.gankly.utils.theme;

import android.widget.TextView;

/**
 * Create by LingYan on 2016-11-25
 */

public class TextViewResorce {
    private TextView mTextView;
    private int mResId;

    public TextViewResorce(int resId, TextView textView) {
        mTextView = textView;
        mResId = resId;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public int getResId() {
        return mResId;
    }
}
