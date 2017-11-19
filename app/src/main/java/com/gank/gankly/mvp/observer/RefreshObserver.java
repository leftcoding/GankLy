package com.gank.gankly.mvp.observer;

/**
 * Create by LingYan on 2017-11-19
 * Email:137387869@qq.com
 */

public abstract class RefreshObserver<T> extends BaseObserver<T> {
    public RefreshObserver(String tag) {
        super(tag);
    }

    @Override
    protected void onSuccessEmpty() {

    }

    @Override
    protected void refreshEmpty() {

    }

    @Override
    protected void appendEmpty() {

    }

    @Override
    protected void onErrorException() {

    }

    @Override
    protected void refreshError() {

    }

    @Override
    protected void appendError() {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onComplete() {

    }
}
