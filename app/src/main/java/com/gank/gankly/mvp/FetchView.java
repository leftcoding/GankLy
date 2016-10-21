package com.gank.gankly.mvp;

/**
 * Create by LingYan on 2016-10-20
 * Email:137387869@qq.com
 */

public interface FetchView extends BaseView{
    void showRefresh();

    void hideRefresh();

    void hasNoMoreDate();

    void showContent();

    void showEmpty();

    void showDisNetwork();

    void showError();

    void showLoading();
}
