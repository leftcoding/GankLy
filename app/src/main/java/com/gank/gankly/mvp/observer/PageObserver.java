package com.gank.gankly.mvp.observer;

import com.leftcoding.network.domain.PageResult;

import io.reactivex.annotations.NonNull;

/**
 * Create by LingYan on 2017-10-12
 */

public abstract class PageObserver<T> extends RefreshObserver<T> {
    private boolean isFirst;

    public PageObserver(String tag, boolean isFirst) {
        super(tag);
        this.isFirst = isFirst;
    }

    @Override
    public void onNext(@NonNull T t) {
        if (t instanceof PageResult) {
            PageResult result = (PageResult) t;
            if (result.results == null || result.results.isEmpty()) {
                onSuccessEmpty();
            }
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        super.onError(e);
        onErrorException();
    }

    @Override
    protected void onErrorException() {
        if (isFirst) {
            refreshError();
        } else {
            appendError();
        }
    }

    @Override
    protected void onSuccessEmpty() {
        if (isFirst) {
            refreshEmpty();
        } else {
            appendEmpty();
        }
    }
}
