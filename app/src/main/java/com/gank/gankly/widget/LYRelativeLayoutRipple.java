package com.gank.gankly.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.socks.library.KLog;

/**
 * RelativeLayout 水波纹点击背景
 * Create by LingYan on 2016-09-23
 */

public class LYRelativeLayoutRipple extends RelativeLayout {
    private Context mContext;

    public LYRelativeLayoutRipple(Context context) {
        this(context, null);
    }

    public LYRelativeLayoutRipple(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LYRelativeLayoutRipple(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

    }

    public void setCustomBackgroundResource(int attr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = mContext.getTheme();
        try {
            theme.resolveAttribute(attr, typedValue, true);
            int background = typedValue.resourceId;
            setBackgroundResource(background);
        } catch (Exception e) {
            KLog.e(e);
        }
    }
}
