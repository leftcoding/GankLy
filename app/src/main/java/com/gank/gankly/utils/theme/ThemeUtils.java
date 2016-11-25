package com.gank.gankly.utils.theme;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.gank.gankly.R;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-11-25
 * Email:137387869@qq.com
 */

public final class ThemeUtils {
    @NonNull
    private Activity mActivity;
    private List<TextViewColor> mTextViewColor = new ArrayList<>();
    private List<BackgroundColor> mBackGroundColor = new ArrayList<>();

    public ThemeUtils(@NonNull Fragment fragment) {
        mActivity = fragment.getActivity();
    }

    public ThemeUtils(@NonNull Activity activity) {
        mActivity = activity;
    }

    public ThemeUtils textViewColor(int resId, @NonNull TextView... args) {
        if (args.length != 0) {
            int color = getColor(resId);
            for (TextView textView : args) {
                KLog.d("textView:" + textView);
                mTextViewColor.add(new TextViewColor(color, textView));
            }
        }
        return this;
    }

    public ThemeUtils backgroundColor(int arrId, @NonNull View... args) {
        if (args.length != 0) {
            int resource = getResourceColor(arrId);
            for (View view : args) {
                mBackGroundColor.add(new BackgroundColor(resource, view));
            }
        }
        return this;
    }

    public void backgroundDrawable(int resid, @NonNull View... args) {
        if (args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                KLog.d("args:" + args[i]);
            }
        }
    }

    public void start() {
        for (int i = 0; i < mTextViewColor.size(); i++) {
            TextViewColor textViewColor = mTextViewColor.get(i);
            textViewColor.getTextView().setTextColor(textViewColor.getResId());
        }

        for (int i = 0; i < mBackGroundColor.size(); i++) {
            BackgroundColor back = mBackGroundColor.get(i);
            back.getBackGroundView().setBackgroundResource(back.getResId());
        }
    }

    private void setTextView() {
        Resources.Theme theme = mActivity.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, typedValue, true);
        int background = typedValue.data;
        theme.resolveAttribute(R.attr.baseAdapterItemTextColor, typedValue, true);
        int textColor = typedValue.data;
        theme.resolveAttribute(R.attr.textSecondaryColor, typedValue, true);
        int authorColor = typedValue.data;
        theme.resolveAttribute(R.attr.themeBackground, typedValue, true);
        int mainColor = typedValue.data;
    }

    private Resources.Theme getTheme() {
        return mActivity.getTheme();
    }

    private int getColor(int resId) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(resId, typedValue, true);
        return typedValue.data;
    }

    private int getResourceColor(int resId) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(resId, typedValue, true);
        return typedValue.resourceId;
    }
}
