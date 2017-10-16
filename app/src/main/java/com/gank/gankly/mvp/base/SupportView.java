package com.gank.gankly.mvp.base;

/**
 * Create by LingYan on 2016-10-20
 * Email:137387869@qq.com
 */

public interface SupportView extends BaseView {
    void showProgress();

    void hideProgress();

    void hasNoMoreDate();

    void showContent();

    void showEmpty();

    void showDisNetWork();

    void showError();

    void showLoading();

    void showRefreshError(String errorStr);
}
