package com.gank.gankly.mvp.observer;

import com.leftcoding.rxbus.RxManager;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2017-10-15
 */

public abstract class BaseObserver<T> implements Observer<T> {
    protected static final String REQUEST_ERROR = "网络请求错误.";
    private final String mKey;

    public BaseObserver(String key) {
        this.mKey = key;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        RxManager.get().add(mKey, d);
    }

    @Override
    public void onError(@NonNull Throwable e) {
    }
}
