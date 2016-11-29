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
 * Create by LingYan on 2016-11-25
 * Email:137387869@qq.com
 */

public final class ThemeColor {
    @NonNull
    private Activity mActivity;
    private List<ViewTextView> mViewTextView = new ArrayList<>();
    private List<ViewBackground> mBackGroundView = new ArrayList<>();
    private List<ViewBackground> mBackGroundDrawable = new ArrayList<>();
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
                mViewTextView.add(new ViewTextView(color, textView));
            }
        }
        return this;
    }

    public ThemeColor backgroundResource(int arrId, @NonNull View... args) {
        if (args.length != 0) {
            int resource = getResourceId(arrId);
            for (View view : args) {
                mBackGroundView.add(new ViewBackground(resource, view));
            }
        }
        return this;
    }

    public void backgroundDrawable(int resId, @NonNull View... args) {
        if (args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                //empty
            }
        }
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

    public void start() {
        changeTextColor();
        changeBackGround();
        changeSwipeRefreshLayout(mSwipeRefreshLayout);
//        clearRecyclerViewItem(mRecyclerView);
        mRecyclerViewColor.start();
        clean();
    }

    private void changeTextColor() {
        ViewTextView viewTextView;
        TextView textView;
        for (int i = 0; i < mViewTextView.size(); i++) {
            viewTextView = mViewTextView.get(i);
            textView = viewTextView.getTextView();
            textView.setTextColor(viewTextView.getResId());
        }
    }

    private void changeBackGround() {
        ViewBackground back;
        View view;
        for (int i = 0; i < mBackGroundView.size(); i++) {
            back = mBackGroundView.get(i);
            view = back.getBackGroundView();
            view.setBackgroundResource(back.getResId());
        }
    }

    private void clean() {
        mViewTextView.clear();
        mBackGroundDrawable.clear();
        mSwipeRefreshLayout = null;
//        mRecyclerView = null;
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
