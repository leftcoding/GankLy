package com.gank.gankly.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.socks.library.KLog;

/**
 * Create by LingYan on 2016-09-23
 * Email:137387869@qq.com
 */

public class LYLinearLayoutRipple extends LinearLayoutCompat {
    private Context mContext;

    public LYLinearLayoutRipple(Context context) {
        this(context, null);
    }

    public LYLinearLayoutRipple(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LYLinearLayoutRipple(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

    }

    public void setBackground(int attr) {
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
