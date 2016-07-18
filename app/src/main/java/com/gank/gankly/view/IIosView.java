package com.gank.gankly.view;

/**
 * Create by LingYan on 2016-05-24
 */
public interface IIosView<T> extends ISwipeRefreshView {
    void refillDate(T date);

    void appendMoreDate(T date);
}
