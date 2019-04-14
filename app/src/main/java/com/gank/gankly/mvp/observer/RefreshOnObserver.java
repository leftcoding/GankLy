package com.gank.gankly.mvp.observer;

import android.ly.business.domain.BaseEntity;

/**
 * Create by LingYan on 2017-11-19
 */

public abstract class RefreshOnObserver<T extends BaseEntity> extends BaseObserver<T> {

    public RefreshOnObserver(String requestTag) {
        super(requestTag);
    }

    protected abstract void onSuccess(T t);

    protected abstract void onFailure();

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFailure();
    }

    @Override
    public void onComplete() {
        // nothing
    }
}
