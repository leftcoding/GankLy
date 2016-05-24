package com.gank.gankly.ui.view;

import com.gank.gankly.config.ViewStatus;

/**
 * Create by LingYan on 2016-05-12
 */
public interface ISwipeRefreshView extends IBaseView {
    void onError(Throwable e);

    void onCompleted();

    void hideRefresh();

    void showRefresh();

    void hasNoMoreDate();

    ViewStatus getCurViewStatus();

    void clear();
}
