package com.gank.gankly.view;

/**
 * Create by LingYan on 2016-05-12
 */
public interface ISwipeRefreshView extends IBaseView {
    void hideRefresh();

    void showRefresh();

    void hasNoMoreDate();

    void clear();

    void showRefreshError(String errorStr);
}
