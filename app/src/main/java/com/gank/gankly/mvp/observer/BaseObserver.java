package com.gank.gankly.mvp.observer;

import com.leftcoding.rxbus.RxManager;
import com.socks.library.KLog;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2017-10-15
 */

public abstract class BaseObserver<T> implements Observer<T> {
    private final String mTag;

    BaseObserver(String tag) {
        this.mTag = tag;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        RxManager.get().add(mTag, d);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        KLog.e(e);
    }

    protected abstract void onSuccessEmpty();

    protected abstract void refreshEmpty();

    protected abstract void appendEmpty();

    protected abstract void onErrorException();

    protected abstract void refreshError();

    protected abstract void appendError();


}
