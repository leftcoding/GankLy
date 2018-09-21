package com.gank.gankly.mvp.observer;

import android.ly.business.domain.BaseEntity;

import android.lectcoding.ui.base.BaseView;

/**
 * Create by LingYan on 2017-11-19
 */

public abstract class RefreshOnObserver<T extends BaseEntity, V extends BaseView> extends BaseObserver<T> {
    protected V view;

    RefreshOnObserver(String requestTag, V v) {
        super(requestTag);
        this.view = v;
    }

    protected abstract void onRefreshSuccess(T t);

    protected abstract void onRefreshFailure();

    @Override
    public void onNext(T t) {
        if (t == null) {
            onRefreshFailure();
        }
    }

    @Override
    public void onError(Throwable e) {
        onErrorException(e);
        onRefreshFailure();
    }

    @Override
    public void onComplete() {
        // nothing
    }
}
