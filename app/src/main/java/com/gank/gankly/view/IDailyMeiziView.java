package com.gank.gankly.view;

/**
 * Create by LingYan on 2016-07-05
 */
public interface IDailyMeiziView<T> extends ISwipeRefreshView {
    void refillDate(T list);

    void gotoBrowseActivity();

    void disDialog();

    void setProgressValue(int value);

    void setMax(int value);
}
