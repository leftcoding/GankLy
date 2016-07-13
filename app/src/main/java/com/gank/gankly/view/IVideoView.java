package com.gank.gankly.view;

/**
 * Create by LingYan on 2016-05-23
 */
public interface IVideoView<T> extends ISwipeRefreshView {
    void refillDate(T list);

    void appendMoreDate(T list);

    void getNextPage(int page);
}
