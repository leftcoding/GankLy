package com.gank.gankly.mvp.observer;

import com.leftcoding.http.bean.PageResult;

import io.reactivex.annotations.NonNull;

/**
 * Create by LingYan on 2017-10-12
 */

public abstract class PageObserver<T> extends BaseObserver<T> implements ILoadMoreException {
    private boolean isFirst;

    public PageObserver(String key, boolean isFirst) {
        super(key);
        this.isFirst = isFirst;
    }

    @Override
    public void onNext(@NonNull T t) {
        if (t == null) {
            onException();
            return;
        }

        if (t instanceof PageResult) {
            PageResult result = (PageResult) t;
            if (result.results == null) {
                onException();
            }
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        super.onError(e);
        onException();
    }

    @Override
    public void refreshError(String str) {

    }

    @Override
    public void appendError(String str) {

    }

    private void onException() {
        if (isFirst) {
            refreshError(REQUEST_ERROR);
        } else {
            appendError(REQUEST_ERROR);
        }
    }
}
