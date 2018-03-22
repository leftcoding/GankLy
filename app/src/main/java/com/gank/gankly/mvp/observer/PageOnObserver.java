package com.gank.gankly.mvp.observer;

import android.ly.business.domain.PageEntity;

import com.gank.gankly.mvp.base.BaseView;

/**
 * Create by LingYan on 2017-10-12
 */

public abstract class PageOnObserver<T extends PageEntity, V extends BaseView> extends RefreshOnObserver<T, V> {
    protected PageOnObserver(String requestTag, V v) {
        super(requestTag, v);
    }

    @Override
    public final void onNext(T t) {
        if (t != null) {
            onSuccess(t);
        } else {
            onFailure();
        }
    }

    @Override
    public final void onError(Throwable e) {
        onErrorException(e);
        onFailure();
    }

    protected abstract void loadMoreSuccess(T t);

    protected abstract void loadMoreFailure();

    protected abstract boolean isFirst();

    private void onSuccess(T t) {
        if (isFirst()) {
            onRefreshSuccess(t);
        } else {
            loadMoreSuccess(t);
        }
    }

    private void onFailure() {
        if (isFirst()) {
            onRefreshFailure();
        } else {
            loadMoreFailure();
        }
    }
}
