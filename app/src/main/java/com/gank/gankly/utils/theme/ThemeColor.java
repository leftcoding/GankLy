package com.gank.gankly.utils.theme;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.gank.gankly.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 主题控制
 * Create by LingYan on 2016-11-25
 * Email:137387869@qq.com
 */

public final class ThemeColor {
    @NonNull
    private Activity mActivity;
    private List<TextViewBean> mTextViewBean = new ArrayList<>();
    private List<ViewResorceBean> mBackGroundView = new ArrayList<>();
    private RecyclerViewColor mRecyclerViewColor;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public ThemeColor(@NonNull Fragment fragment) {
        mActivity = fragment.getActivity();
    }

    public ThemeColor(@NonNull Activity activity) {
        mActivity = activity;
    }

    public void setTheme(int resId) {
        mActivity.setTheme(resId);
    }

    public ThemeColor textViewColor(int resId, @NonNull TextView... args) {
        if (args.length != 0) {
            int color = getResourceData(resId);
            for (TextView textView : args) {
                mTextViewBean.add(new TextViewBean(color, textView));
            }
        }
        return this;
    }

    /**
     * 改变背景
     *
     * @param arrId 要的改变arr id
     * @param args  控件
     * @return ThemeColor
     */
    public ThemeColor backgroundResource(int arrId, @NonNull View... args) {
        if (args.length != 0) {
            int resource = getResourceId(arrId);
            for (View view : args) {
                mBackGroundView.add(new ViewResorceBean(resource, view));
            }
        }
        return this;
    }

    public ThemeColor swipeRefresh(@NonNull SwipeRefreshLayout swipeRefreshLayout) {
        mSwipeRefreshLayout = swipeRefreshLayout;
        return this;
    }

    public ThemeColor recyclerViewColor(@NonNull RecyclerViewColor recyclerViewColor) {
        mRecyclerViewColor = new RecyclerViewColor(recyclerViewColor, getTheme());
        return this;
    }

    public int getResourceId(int arrId) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(arrId, typedValue, true);
        return typedValue.resourceId;
    }

    public int getResourceData(int arrId) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(arrId, typedValue, true);
        return typedValue.data;
    }

    /**
     * 启动变化颜色
     */
    public void start() {
        changeTextColor();
        changeBackGround();
        if (mSwipeRefreshLayout != null) {
            changeSwipeRefreshLayout(mSwipeRefreshLayout);
        }
        if (mRecyclerViewColor != null) {
            mRecyclerViewColor.start();
        }
        clean();
    }

    private void changeTextColor() {
        TextViewBean textViewBean;
        TextView textView;
        for (int i = 0; i < mTextViewBean.size(); i++) {
            textViewBean = mTextViewBean.get(i);
            textView = textViewBean.getTextView();
            textView.setTextColor(textViewBean.getResId());
        }
    }

    private void changeBackGround() {
        ViewResorceBean back;
        View view;
        for (int i = 0; i < mBackGroundView.size(); i++) {
            back = mBackGroundView.get(i);
            view = back.getBackGroundView();
            view.setBackgroundResource(back.getResId());
        }
    }

    private void clean() {
        mTextViewBean.clear();
        mSwipeRefreshLayout = null;
    }

    private void changeSwipeRefreshLayout(@NonNull SwipeRefreshLayout swipeRefreshLayout) {
        int progressColor = getResourceData(R.attr.swipeRefreshLayoutProgressSchemeColor);
        int schemeColor = getResourceData(R.attr.swipeRefreshLayoutSchemeColors);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(progressColor);
        swipeRefreshLayout.setColorSchemeColors(schemeColor);
    }

    private Resources.Theme getTheme() {
        return mActivity.getTheme();
    }
}
