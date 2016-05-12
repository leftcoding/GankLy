package com.gank.gankly.ui.view;

/**
 * Create by LingYan on 2016-05-12
 */
public interface IBaseView {
    void showEmpty();

    void showDisNetWork();

    void showView();

    enum ViewStatus {
        EMPTY, SHOW, DISNETWORK, LOADING
    }
}
