package com.gank.gankly.mvp;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by LingYan on 2016-10-24
 */

public class BaseModel {
    protected <T> Observable<T> toObservable(Observable<T> o) {
        return o.retry(3)
                .subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
