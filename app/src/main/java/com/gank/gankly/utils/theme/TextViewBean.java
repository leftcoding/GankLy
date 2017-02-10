package com.gank.gankly.utils.theme;

import android.widget.TextView;

/**
 * Create by LingYan on 2016-11-25
 * Email:137387869@qq.com
 */

public class TextViewBean {
    private TextView mTextView;
    private int mResId;

    public TextViewBean(int resId, TextView textView) {
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
