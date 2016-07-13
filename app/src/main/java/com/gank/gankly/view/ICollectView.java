package com.gank.gankly.view;

/**
 * Create by LingYan on 2016-05-12
 */
public interface ICollectView<T> extends ISwipeRefreshView {
    void refillDate(T list);

    void appendMoreDate(T list);

    void hasNoMoreDate();

    void fetchFinish();
}
