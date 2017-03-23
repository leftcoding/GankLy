package com.gank.gankly.utils;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.gank.gankly.App;
import com.gank.gankly.R;
import com.socks.library.KLog;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Create by LingYan on 2016-09-12
 * Email:137387869@qq.com
 */
public class StyleUtils {
    public static void changeSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout == null) {
            throw new NullPointerException("swipeRefreshLayout not be null");
        }

        int progressColor = R.color.white;
        int schemeColor = R.color.colorAccent;

        if (App.isNight()) {
            progressColor = R.color.baseSwipeRefreshLayoutProgressSchemeColor;
            schemeColor = R.color.baseSwipeRefreshLayoutSchemeColors;
        }

        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(App.getAppColor(progressColor));
        swipeRefreshLayout.setColorSchemeColors(App.getAppColor(schemeColor));
    }

    //让 RecyclerView 缓存在 Pool 中的 Item 失效
    //那么，如果是ListView，要怎么做呢？这里的思路是通过反射拿到 AbsListView 类中的 RecycleBin 对象，
    // 然后同样再用反射去调用 clear 方法
    public static void clearRecyclerViewItem(RecyclerView mRecyclerView) {
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear");
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(mRecyclerView));
            RecyclerView.RecycledViewPool recycledViewPool = mRecyclerView.getRecycledViewPool();
            recycledViewPool.clear();
        } catch (NoSuchFieldException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            KLog.e(e);
            CrashUtils.crashReport(e);
        }
    }
}
