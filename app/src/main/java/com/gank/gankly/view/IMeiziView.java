package com.gank.gankly.view;

/**
 * Create by LingYan on 2016-07-13
 * Email:137387869@qq.com
 */
public interface IMeiziView<T> extends ISwipeRefreshView {

    void refillDate(T list);

    void appendMoreDate(T list);

    void showRefreshError(String error);
}
