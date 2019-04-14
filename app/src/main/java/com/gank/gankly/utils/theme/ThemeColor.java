package com.gank.gankly.utils.theme;

import android.app.Activity;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.gank.gankly.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 主题控制
 * Create by LingYan on 2016-11-25
 */

public final class ThemeColor {
    @NonNull
    private Activity mActivity;
    private List<TextViewResorce> tvList = new ArrayList<>();
    private List<TextViewResorce> textViewSizeList = new ArrayList<>();
    private List<ViewResorce> mViewResorceList = new ArrayList<>();
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

    public ThemeColor setTextViewColor(int resId, @NonNull TextView... args) {
        if (args.length != 0) {
            int color = getResourceData(resId);
            for (TextView textView : args) {
                tvList.add(new TextViewResorce(color, textView));
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
    public ThemeColor setBackgroundResource(int arrId, @NonNull View... args) {
        if (args.length != 0) {
            int resource = getResourceId(arrId);
            for (View view : args) {
                mViewResorceList.add(new ViewResorce(resource, view));
            }
        }
        return this;
    }

    public ThemeColor setTextViewSize(int resId, @NonNull TextView... args) {
        if (args.length != 0) {
            int size = getResourceId(resId);
            for (TextView view : args) {
                textViewSizeList.add(new TextViewResorce(size, view));
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
        changeBackground();
        if (mSwipeRefreshLayout != null) {
            changeSwipeRefreshLayout(mSwipeRefreshLayout);
        }
        if (mRecyclerViewColor != null) {
            mRecyclerViewColor.start();
        }
        clean();
    }

    private void changeTextColor() {
        TextViewResorce textViewResorce;
        TextView textView;
        for (int i = 0; i < tvList.size(); i++) {
            textViewResorce = tvList.get(i);
            textView = textViewResorce.getTextView();
            textView.setTextColor(textViewResorce.getResId());
        }
    }

    private void changeBackground() {
        ViewResorce back;
        View view;
        for (int i = 0; i < mViewResorceList.size(); i++) {
            back = mViewResorceList.get(i);
            view = back.getBackGroundView();
            view.setBackgroundResource(back.getResId());
        }
    }

    private void clean() {
        tvList.clear();
        mViewResorceList.clear();
        textViewSizeList.clear();
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
