package com.gank.gankly.mvp;

/**
 * Create by LingYan on 2016-10-20
 * Email:137387869@qq.com
 */

public interface IFetchView extends IBaseView {
    void showRefresh();

    void hideRefresh();

    void hasNoMoreDate();

    void showContent();

    void showEmpty();

    void showDisNetWork();

    void showError();

    void showLoading();

    void showRefreshError(String errorStr);
}
