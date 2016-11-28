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

public final class ThemeUtils {
    @NonNull
    private Activity mActivity;
    private List<TextViewColor> mTextViewColor = new ArrayList<>();
    private List<BackgroundColor> mBackGroundColor = new ArrayList<>();
    private List<BackgroundColor> mBackGroundDrawable = new ArrayList<>();
    private RecyclerViewColor mRecyclerViewColor;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public ThemeUtils(@NonNull Fragment fragment) {
        mActivity = fragment.getActivity();
    }

    public ThemeUtils(@NonNull Activity activity) {
        mActivity = activity;
    }

    public void setTheme(int resId) {
        mActivity.setTheme(resId);
    }

    public ThemeUtils textViewColor(int resId, @NonNull TextView... args) {
        if (args.length != 0) {
            int color = getResourceData(resId);
            for (TextView textView : args) {
                mTextViewColor.add(new TextViewColor(color, textView));
            }
        }
        return this;
    }

    public ThemeUtils backgroundResource(int arrId, @NonNull View... args) {
        if (args.length != 0) {
            int resource = getResourceId(arrId);
            for (View view : args) {
                mBackGroundColor.add(new BackgroundColor(resource, view));
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

    public ThemeUtils swipeRefresh(@NonNull SwipeRefreshLayout swipeRefreshLayout) {
        mSwipeRefreshLayout = swipeRefreshLayout;
        return this;
    }

    public ThemeUtils recyclerViewColor(@NonNull RecyclerViewColor recyclerViewColor) {
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
        TextViewColor textViewColor;
        TextView textView;
        for (int i = 0; i < mTextViewColor.size(); i++) {
            textViewColor = mTextViewColor.get(i);
            textView = textViewColor.getTextView();
            textView.setTextColor(textViewColor.getResId());
        }
    }

    private void changeBackGround() {
        BackgroundColor back;
        View view;
        for (int i = 0; i < mBackGroundColor.size(); i++) {
            back = mBackGroundColor.get(i);
            view = back.getBackGroundView();
            view.setBackgroundResource(back.getResId());
        }
    }

    private void clean() {
        mTextViewColor.clear();
        mBackGroundDrawable.clear();
        mSwipeRefreshLayout = null;
//        mRecyclerView = null;
    }

    public void changeSwipeRefreshLayout(@NonNull SwipeRefreshLayout swipeRefreshLayout) {
        int progressColor = getResourceData(R.attr.swipeRefreshLayoutProgressSchemeColor);
        int schemeColor = getResourceData(R.attr.swipeRefreshLayoutSchemeColors);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(progressColor);
        swipeRefreshLayout.setColorSchemeColors(schemeColor);
    }

    private Resources.Theme getTheme() {
        return mActivity.getTheme();
    }
}
