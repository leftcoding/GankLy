package com.gank.gankly.utils.theme;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.gankly.utils.CrashUtils;
import com.socks.library.KLog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by LingYan on 2016-11-28
 * Email:137387869@qq.com
 */

public class RecyclerViewColor {
    private RecyclerView mRecyclerView;
    private List<TextViewId> mTextViewList;
    private List<TextViewId> mViews;
    private Resources.Theme mTheme;

    public RecyclerViewColor(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mTextViewList = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public List<TextViewId> getTextViewList() {
        return mTextViewList;
    }

    public List<TextViewId> getViews() {
        return mViews;
    }

    public RecyclerViewColor(@NonNull RecyclerViewColor recyclerViewColor, @NonNull Resources.Theme theme) {
        mRecyclerView = recyclerViewColor.getRecyclerView();
        mTextViewList = recyclerViewColor.getTextViewList();
        mViews = recyclerViewColor.getViews();
        mTheme = theme;
    }

    public void textViewColor(int id, int resId) {
        if (!mTextViewList.contains(id)) {
            mTextViewList.add(new TextViewId(id, resId));
        }
    }

    public void backGroundColor(int id, int resId) {
        if (!mViews.contains(id)) {
            mViews.add(new TextViewId(id, resId));
        }
    }

    private class TextViewId {
        int id;
        int resId;

        public TextViewId(int id, int resId) {
            this.id = id;
            this.resId = resId;
        }

        public int getId() {
            return id;
        }

        public int getResId() {
            return resId;
        }

    }

    public void start() {
        change();
        clearRecyclerViewItem(mRecyclerView);

        mTextViewList.clear();
        mViews.clear();
        mRecyclerView = null;
    }

    public int getResourceId(int arrId) {
        TypedValue typedValue = new TypedValue();
        mTheme.resolveAttribute(arrId, typedValue, true);
        return typedValue.resourceId;
    }

    public int getResourceData(int arrId) {
        TypedValue typedValue = new TypedValue();
        mTheme.resolveAttribute(arrId, typedValue, true);
        return typedValue.data;
    }

    private void change() {
        int size = mTextViewList.size();
        int views = mViews.size();
        int childCount = mRecyclerView.getChildCount();
        ViewGroup childView;

        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);

            if (size > 0) {
                try {
                    TextView textView;
                    for (int i = 0; i < size; i++) {
                        textView = (TextView) childView.findViewById(mTextViewList.get(i).getId());
                        textView.setTextColor(getResourceData(mTextViewList.get(i).getResId()));
                    }
                } catch (Exception e) {
                    KLog.e(e);
                }
            }

            if (views > 0) {
                try {
                    for (int x = 0; x < views; x++) {
                        View v = childView.findViewById(mViews.get(x).getId());
                        v.setBackgroundResource(getResourceId(mViews.get(x).getResId()));
                    }
                } catch (Exception e) {
                    KLog.e(e);
                }
            }
//            rl = (LYRelativeLayoutRipple) childView.findViewById(R.id.welfare_rl);
//            title = (TextView) childView.findViewById(R.id.goods_txt_title);
//            time = (TextView) childView.findViewById(R.id.goods_txt_time);

//            rl.setCustomBackgroundResource(R.attr.lyItemSelectBackground);
//            title.setTextColor(textColor);
//            time.setTextColor(textSecondaryColor);
//            time.setCompoundDrawables(drawable, null, null, null);
        }
    }

    public void clearRecyclerViewItem(@NonNull RecyclerView recyclerView) {
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear");
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(recyclerView));
            RecyclerView.RecycledViewPool recycledViewPool = recyclerView.getRecycledViewPool();
            recycledViewPool.clear();
        } catch (NoSuchFieldException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            KLog.e(e);
            CrashUtils.crashReport(e);
        }
    }
}
