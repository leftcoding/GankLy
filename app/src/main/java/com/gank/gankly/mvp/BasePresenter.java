package com.gank.gankly.mvp;

import rx.subscriptions.CompositeSubscription;

/**
 * Create by LingYan on 2016-10-21
 * Email:137387869@qq.com
 */

public abstract class BasePresenter {
    protected CompositeSubscription mRxManager = new CompositeSubscription();

    protected void onUnSubscribe() {
        mRxManager.clear();
    }
}
