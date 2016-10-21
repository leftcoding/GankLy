package com.gank.gankly.mvp;


/**
 * Create by LingYan on 2016-10-21
 * Email:137387869@qq.com
 */

public abstract class FetchPresenter<M extends BaseModel, V extends BaseView> extends BasePresenter<M, V> {
    public abstract void fetchNew();

    public abstract void fetchMore();
}
