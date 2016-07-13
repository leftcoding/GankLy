package com.gank.gankly.presenter;

/**
 * Create by LingYan on 2016-07-12
 * Email:137387869@qq.com
 */
public interface IBaseRefreshPresenter {
    void fetchNew(int page);

    void fetchMore(int page);
}
