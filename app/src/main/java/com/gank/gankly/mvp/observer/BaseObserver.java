package com.gank.gankly.mvp.observer;

import android.lectcoding.ui.logcat.Logcat;

import com.leftcoding.rxbus.RxApiManager;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Create by LingYan on 2017-10-15
 */

public abstract class BaseObserver<T> implements Observer<T> {
    private String observerTag;

    BaseObserver(String requestTag) {
        this.observerTag = requestTag;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        RxApiManager.get().add(observerTag, d);
    }

    @Override
    public void onError(Throwable e) {
        Logcat.e(e);
    }
}
