package com.gank.gankly.view;

/**
 * Create by LingYan on 2016-10-20
 * Email:137387869@qq.com
 */

public interface FetchView<T> extends BaseView<T> {
    void showRefresh();

    void hideRefresh();

    void showContent();

    void showEmpty();

    void showDisNetwork();

    void showError();

    void showLoading();
}
