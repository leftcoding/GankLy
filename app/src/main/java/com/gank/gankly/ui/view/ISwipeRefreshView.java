package com.gank.gankly.ui.view;

/**
 * Create by LingYan on 2016-05-12
 */
public interface ISwipeRefreshView extends IBaseView {
    void onError(Throwable e, String errorString);

    void onCompleted();

    void hideRefresh();

    void showRefresh();

    void hasNoMoreDate();

    void clear();
}
