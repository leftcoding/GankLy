package com.gank.gankly.utils.theme;

import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
 */

public class RecyclerViewColor {
    private RecyclerView mRecyclerView;
    private List<RecyclerViewItem> tvColorList = new ArrayList<>();
    private List<RecyclerViewItem> mViews = new ArrayList<>();
    private Resources.Theme mTheme;

    public RecyclerViewColor(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public List<RecyclerViewItem> getTvColorList() {
        return tvColorList;
    }

    public List<RecyclerViewItem> getViews() {
        return mViews;
    }

    public RecyclerViewColor(@NonNull RecyclerViewColor recyclerViewColor, @NonNull Resources.Theme theme) {
        mRecyclerView = recyclerViewColor.getRecyclerView();
        tvColorList = recyclerViewColor.getTvColorList();
        mViews = recyclerViewColor.getViews();
        mTheme = theme;
    }

    /**
     * Modified TextView Color
     *
     * @param id    TextView id
     * @param resId TextView attr id
     */
    public void setItemColor(int id, int resId) {
        if (!isContains(tvColorList, id)) {
            tvColorList.add(new RecyclerViewItem(id, resId));
        }
    }

    /**
     * Modified Views background
     *
     * @param id    Views id
     * @param resId Views attr id
     */
    public void setItemBackgroundColor(int id, int resId) {
        if (!isContains(mViews, id)) {
            mViews.add(new RecyclerViewItem(id, resId));
        }
    }

    private boolean isContains(List<RecyclerViewItem> list, int id) {
        if (list != null) {
            for (RecyclerViewItem aMTextViewList : list) {
                int _id = aMTextViewList.getId();
                if (id == _id && id != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public void start() {
        changeView();
        clearRecyclerViewItem(mRecyclerView);

        tvColorList.clear();
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

    private void changeView() {
        int tvColorSize = tvColorList.size();
        int views = mViews.size();
        int childCount = mRecyclerView.getChildCount();
        ViewGroup childView;

        TextView textView;
        RecyclerViewItem recyclerViewItem;
        View view;
        RecyclerViewItem viewId;

        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);

            if (tvColorSize > 0) {
                try {
                    for (int i = 0; i < tvColorSize; i++) {
                        recyclerViewItem = tvColorList.get(i);
                        textView = (TextView) childView.findViewById(recyclerViewItem.getId());
                        textView.setTextColor(getResourceData(recyclerViewItem.getResId()));
                    }
                } catch (Exception e) {
                    KLog.e(e);
                }
            }

            if (views > 0) {
                try {
                    for (int x = 0; x < views; x++) {
                        viewId = mViews.get(x);
                        view = childView.findViewById(viewId.getId());
                        view.setBackgroundResource(getResourceId(viewId.getResId()));
                    }
                } catch (Exception e) {
                    KLog.e(e);
                }
            }
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
